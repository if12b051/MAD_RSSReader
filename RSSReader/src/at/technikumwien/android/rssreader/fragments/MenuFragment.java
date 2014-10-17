package at.technikumwien.android.rssreader.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import at.technikumwien.android.rssreader.R;
import at.technikumwien.android.rssreader.RssActivity;

@SuppressLint("NewApi")
public class MenuFragment extends Fragment {
	private static Button mSubscriptionsButton;
	private static Button mSubscribeButton;

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
                // Open subscription fragment to list subscribed feeds
				RssActivity.getInstance().switchContent(new SubscriptionsFragment());
				
			}
		});
		
		mSubscribeButton = (Button)getView().findViewById(R.id.subscribe_button);
		mSubscribeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                // Open fragment to subscribe to rss feed
				RssActivity.getInstance().switchContent(new SubscribeFragment());
			}
		});
	}
	
	
}
