package com.example.myapp;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.net.URL;
import java.util.List;

@SuppressLint("NewApi")
public class ShowFragment extends ListFragment {
    public static ShowFragment instance;
    public ArrayAdapter<String> adapter;

    public ShowFragment(){
        instance = this;
    }

    public static ShowFragment getInstance() {
        return instance;
    }
	/*public static ShowFragment newInstance(int position) {
		ShowFragment fragment = new ShowFragment();

        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
		
		return fragment;
	}*/

    public static ListFragment newInstance() {
        ShowFragment fragment = new ShowFragment();

        return fragment;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_show, container, false);
	}

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
    }

}
