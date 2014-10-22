package at.technikumwien.android.rssreader.fragments;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.*;
import android.view.*;
import android.widget.AbsListView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import at.technikumwien.android.rssreader.*;
import at.technikumwien.android.rssreader.RssActivity;
import at.technikumwien.android.rssreader.adapter.UrlArrayAdapter;
import at.technikumwien.android.rssreader.contentprovider.RssContentProvider;

public class SubscriptionsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private UrlArrayAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_subscriptions, container, false);
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        // Initialize Cursor Loader, reset if needed to prevent errors that loader doesn't get initialized
        Loader loader = getLoaderManager().getLoader(0);
        if ( loader != null && loader.isReset() ) {
            getLoaderManager().restartLoader(0, getArguments(), this);
        } else {
            getLoaderManager().initLoader(0, getArguments(), this);
        }

        // Load database cursor with subscriptions
        Context ctx = getActivity().getApplicationContext();
        String[] projection = { "id _id", "name", "url" };
        Cursor c = ctx.getContentResolver().query(Uri.parse(RssContentProvider.CONTENT_URI + RssContentProvider.TABLE_RSS_FEEDS), projection, null, null, null);

        // Set adapter
        adapter = new UrlArrayAdapter(ctx, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        setListAdapter(adapter);

        // Initialize Multichoicelistening
        SetMultiChoice();
    }

    /*
     * Enable contextual action bar and multichoice for listview
     */
    private void SetMultiChoice(){
        getListView().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            /*
             * Override onItemCheckedStateChanged to handle whenever an item is selected or deselected.
             * Display how many items are currently selected.
             */
            public void onItemCheckedStateChanged(ActionMode mode, int i, long l, boolean b) {
                int count = getListView().getCheckedItemCount();
                switch (count){
                    case 0:
                        mode.setTitle(null);
                        mode.setSubtitle(null);
                        break;
                    case 1:
                        mode.setTitle("Auswahl");
                        mode.setSubtitle("Ein Rssfeed ausgewählt!");
                        break;
                    default:
                        mode.setTitle("Auswahl");
                        mode.setSubtitle(count + " Rssfeeds ausgewählt");
                }
            }

            @Override
            /*
             * Override onCreateActionMode to inflate contextual menu for this fragment
             */
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.cab_subscriptions, menu);
                return true;
            }

            @Override
            /*
             * Override onActionItemClicked to handle menu item click to delete selected elements
             */
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_delete:
                        deleteSelectedItems();
                        break;
                    default:
                        return false;
                }

                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });
    }

    /*
     * Function to delete selected Elements
     */
    private void deleteSelectedItems(){
        // Get checked items
        long[] items = getListView().getCheckedItemIds();

        // For each selected item, delete feed and it's items
        for (int i = 0; i < items.length; i++){
            getActivity().getContentResolver().delete(
                    Uri.parse(RssContentProvider.CONTENT_URI + RssContentProvider.TABLE_RSS_ITEMS),
                    "feedid = ?", new String[] {String.valueOf(items[i])}
            );
            getActivity().getContentResolver().delete(
                    Uri.parse(RssContentProvider.CONTENT_URI + RssContentProvider.TABLE_RSS_FEEDS),
                    "id = ?", new String[] {String.valueOf(items[i])}
            );
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ShowFragment f = new ShowFragment();

        // Get current item
        Cursor c = (Cursor) getListAdapter().getItem(position);
        c.moveToPosition(position);

        // New bundle with url and id of selected feed
        Bundle b = new Bundle();
        b.putInt("id", c.getInt(c.getColumnIndex("_id")));
        b.putString("url", c.getString(c.getColumnIndex("url")));

        // Set as argument for new fragment
        f.setArguments(b);

        // Switch fragment to ShowFragment
        ((RssActivity) getActivity()).switchContent(f);
    }

    // Load new cursor to retrieve feeds with id, name and url
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = { "id _id", "name", "url" };
        return new CursorLoader(getActivity(), Uri.parse(RssContentProvider.CONTENT_URI + RssContentProvider.TABLE_RSS_FEEDS),
            projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }
}
