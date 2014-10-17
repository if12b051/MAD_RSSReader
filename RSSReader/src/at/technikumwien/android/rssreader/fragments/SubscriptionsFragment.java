package at.technikumwien.android.rssreader.fragments;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.os.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import at.technikumwien.android.rssreader.*;
import at.technikumwien.android.rssreader.RssActivity;
import at.technikumwien.android.rssreader.adapter.UrlArrayAdapter;
import at.technikumwien.android.rssreader.items.UrlItem;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class SubscriptionsFragment extends ListFragment{
    private ArrayList<UrlItem> list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_subscriptions, container, false);
		
	}

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        list = ((RssActivity) getActivity()).getSubscriptions();
        setListAdapter(new UrlArrayAdapter(getActivity(), R.layout.url_list_items, list));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        getListView().setItemChecked(position, true);
        ShowFragment f = new ShowFragment();

        // Get current item
        UrlItem item = (UrlItem) getListAdapter().getItem(position);

        // New bundle with url of selected feed
        Bundle b = new Bundle();
        b.putString("url", item.url.toString());

        // Set as argument for new fragment
        f.setArguments(b);

        // Switch fragment to ShowFragment
        ((RssActivity) getActivity()).switchContent(f);
    }
}
