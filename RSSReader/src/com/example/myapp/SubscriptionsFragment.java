package com.example.myapp;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.TypeVariable;
import java.util.List;


@SuppressLint("NewApi")
public class SubscriptionsFragment extends ListFragment {
    private ArrayAdapter<String> adapter;

	public static ListFragment newInstance() {
		SubscriptionsFragment fragment = new SubscriptionsFragment();
		
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_subscriptions, container, false);
	}
	
	public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        List<String> list = ((RssActivity) getActivity()).getSubscriptions();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //getListView().setItemChecked(position, true);
        String selectedValue = (String) getListAdapter().getItem(position);
        ((RssActivity) getActivity()).SendMessage(selectedValue);
        ((RssActivity) getActivity()).switchContent(new ShowFragment());
    }

}
