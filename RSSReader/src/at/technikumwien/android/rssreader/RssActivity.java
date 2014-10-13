package at.technikumwien.android.rssreader;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

@SuppressLint("NewApi")
public class RssActivity extends ActionBarActivity {
	
	private Fragment mContent;
	public static RssActivity instance;
	private ArrayList<String> subscriptions;
	
	public static RssActivity getInstance() {
		return instance;
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rss);
		
		subscriptions = new ArrayList<String>();
		
		instance = this;
		
		switchContent(new MenuFragment());

	}
	
	public void switchContent(Fragment fragment) { 
		if(mContent != null && mContent.getClass().equals(fragment.getClass())) {
	            
	            return;
	    }
		mContent = fragment;
		
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.fragment_container, fragment)
		.commit();
	
	}

	public List<String> getSubscriptions() {
		return subscriptions;
	}

	public void addSubscriptions(String sub) {
		subscriptions.add(sub);
	}

}
