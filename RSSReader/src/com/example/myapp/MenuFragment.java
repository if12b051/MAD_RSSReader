package com.example.myapp;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("NewApi")
public class MenuFragment extends Fragment {
	
	private static Button mSubscriptionsButton;
	private static Button mSubscribeButton;
	private static Button mShowButton;
	
	public MenuFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_menu, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mSubscriptionsButton = (Button) getView().findViewById(R.id.subscriptions_button);
		mSubscriptionsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RssActivity.getInstance().switchContent(new SubscriptionsFragment());
			}
		});
		
		mSubscribeButton = (Button)getView().findViewById(R.id.subscribe_button);
		mSubscribeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RssActivity.getInstance().switchContent(new SubscribeFragment());
			}
		});
		
		mShowButton = (Button)getView().findViewById(R.id.show_button);
		mShowButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//RssActivity.getInstance().switchContent(new ShowFragment());
			}
		});
	}
	
	
}
