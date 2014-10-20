package at.technikumwien.android.rssreader.fragments;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.*;
import android.view.*;
import android.widget.AbsListView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import at.technikumwien.android.rssreader.R;
import at.technikumwien.android.rssreader.RssActivity;
import at.technikumwien.android.rssreader.RssService;
import at.technikumwien.android.rssreader.adapter.ItemArrayAdapter;
import at.technikumwien.android.rssreader.contentprovider.RssContentProvider;
import at.technikumwien.android.rssreader.items.RssItem;
import at.technikumwien.android.rssreader.items.UrlItem;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class ShowFragment extends ListFragment implements ServiceConnection, LoaderManager.LoaderCallbacks<Cursor> {
    private ItemArrayAdapter adapter;
    private Messenger serviceMessenger = null;
    private final Messenger messenger = new Messenger(new MessageHandler());
    private ServiceConnection connection = this;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_show, container, false);
	}

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(0, null, this);

        Context ctx = getActivity().getApplicationContext();
        String[] projection = { "id _id", "title", "date", "url", "read" };
        Cursor c = ctx.getContentResolver().query(Uri.parse(RssContentProvider.CONTENT_URI + RssContentProvider.TABLE_RSS_ITEMS), projection, null, null, null);

        adapter = new ItemArrayAdapter(ctx, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        setListAdapter(adapter);

        getListView().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int i, long l, boolean b) {
                int count = getListView().getCheckedItemCount();
                switch (count){
                    case 0:
                        mode.setSubtitle(null);
                        break;
                    case 1:
                        mode.setSubtitle("Ein Artikel ausgewählt!");
                        break;
                    default:
                        mode.setSubtitle(count + " Artikel ausgewählt");
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.cab_show, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_read:
                        SetStatus(1);
                        break;
                    case R.id.action_unread:
                        SetStatus(0);
                        break;
                    default:
                        return false;
                }

                return false;
            }

            private void SetStatus(int read){
                ContentValues values = new ContentValues();
                values.put("read", read);

                long[] items = getListView().getCheckedItemIds();
                for (int i = 0; i < items.length; i++){
                    getActivity().getContentResolver().update(
                            Uri.parse(RssContentProvider.CONTENT_URI + RssContentProvider.TABLE_RSS_ITEMS),
                            values, "id = ?", new String[] {String.valueOf(items[i])}
                    );
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        // bind service connection
        getActivity().bindService(new Intent(getActivity(), RssService.class), connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause(){
        super.onPause();
        //unbind service connection
        getActivity().unbindService(connection);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        WebViewFragment f = new WebViewFragment();

        // Get current item
        Cursor c = (Cursor) getListAdapter().getItem(position);
        c.moveToPosition(position);

        // New bundle with url of selected feed
        Bundle b = new Bundle();
        b.putInt("id", c.getInt(c.getColumnIndex("_id")));
        b.putString("url", c.getString(c.getColumnIndex("url")));

        // Set as argument for new fragment
        f.setArguments(b);

        // Switch fragment to ShowFragment
        ((RssActivity) getActivity()).switchContent(f);
    }

    @Override
    // On serviceConnection create Messenger and sendMessage()
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        serviceMessenger = new Messenger(iBinder);
        SendMessage();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    // Send message with url to service
    public void SendMessage(){
        try {
            Message msg = Message.obtain(null, RssService.MSG_URL);
            msg.setData(getArguments());
            msg.replyTo = messenger;
            serviceMessenger.send(msg);
        }
        catch (RemoteException e) {
            // In this case the service has crashed before we could even do anything with it
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = { "id _id", "title", "date", "url", "read" };
        return new CursorLoader(getActivity(), Uri.parse(RssContentProvider.CONTENT_URI + RssContentProvider.TABLE_RSS_ITEMS),
            projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }

    /*
    * Innerclass message handler for incoming messages from service
    */
    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RssService.MSG_ITEMS:
                    // Update ListView with data from service
                    //ArrayList<RssItem> items = msg.getData().getParcelableArrayList("items");
                    //ItemArrayAdapter adapter = new ItemArrayAdapter(getActivity(), R.layout.rss_list_items, items);
                    //setListAdapter(adapter);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

}
