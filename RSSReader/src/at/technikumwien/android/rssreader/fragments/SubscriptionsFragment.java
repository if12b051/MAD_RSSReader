package at.technikumwien.android.rssreader.fragments;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ClipData;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.AvoidXfermode;
import android.net.Uri;
import android.os.*;
import android.text.AndroidCharacter;
import android.text.style.SuperscriptSpan;
import android.util.SparseBooleanArray;
import android.view.*;
import android.widget.AbsListView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import at.technikumwien.android.rssreader.*;
import at.technikumwien.android.rssreader.RssActivity;
import at.technikumwien.android.rssreader.adapter.UrlArrayAdapter;
import at.technikumwien.android.rssreader.contentprovider.RssContentProvider;
import at.technikumwien.android.rssreader.items.UrlItem;

import java.net.URI;
import java.util.ArrayList;

@SuppressLint("NewApi")
public class SubscriptionsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private UrlArrayAdapter adapter;
    private ArrayList<UrlItem> list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_subscriptions, container, false);
		
	}

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(0, null, this);

        Context ctx = getActivity().getApplicationContext();
        String[] projection = { "id _id", "name", "url" };
        Cursor c = ctx.getContentResolver().query(Uri.parse(RssContentProvider.CONTENT_URI + RssContentProvider.TABLE_RSS_FEEDS), projection, null, null, null);

        adapter = new UrlArrayAdapter(ctx, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
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
                        mode.setSubtitle("Ein Rssfeed ausgewählt!");
                        break;
                    default:
                        mode.setSubtitle(count + " Rssfeeds ausgewählt");
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.cab_subscriptions, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_edit:
                        break;
                    case R.id.action_delete:
                        deleteSelectedItems();
                        break;
                    default:
                        return false;
                }

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });
    }

    private void deleteSelectedItems(){
        long[] items = getListView().getCheckedItemIds();
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
        v.setBackgroundResource(0);
        v.setBackgroundResource(android.R.color.holo_blue_light);
        ShowFragment f = new ShowFragment();

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
