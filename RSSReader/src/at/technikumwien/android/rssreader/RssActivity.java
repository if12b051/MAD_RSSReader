package at.technikumwien.android.rssreader;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;

@SuppressLint("NewApi")
public class RssActivity extends ActionBarActivity {
	
	private Button mSubscriptionsButton;
	private Button mSubscribeButton;
	private Button mShowButton;
	private Fragment mContent;
	public static RssActivity instance;
	
	public static RssActivity getInstance() {
		return instance;
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rss);
		
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

}
