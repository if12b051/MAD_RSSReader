/*
 * RssActivity
 *
 * Main activity of the rss reader
 */

package at.technikumwien.android.rssreader;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.*;
import android.support.v7.app.ActionBarActivity;

import java.net.URL;
import java.util.ArrayList;

import at.technikumwien.android.rssreader.items.*;
import at.technikumwien.android.rssreader.fragments.*;

@SuppressLint("NewApi")
public class RssActivity extends ActionBarActivity{
    // Current fragment
	private Fragment mContent;
	public static RssActivity instance;
    // Saved subscriptions
	private ArrayList<UrlItem> subscriptions;

	public static RssActivity getInstance() {
		return instance;
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rss);

        // Initialize ArrayList
		subscriptions = new ArrayList<UrlItem>();
		instance = this;
	}

    @Override
    public void onResume() {
        super.onResume();
        //Switch to first fragment
        switchContent(new MenuFragment());
    }

    @Override
    public void onBackPressed(){
        // Handle backbutton and navigate through fragment backstack
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() != 0){
            fm.popBackStack();
        }else{
            super.onBackPressed();
        }
    }

    /*
     * Function to switch the current fragment in the activity. Use FragmentManager to replace
     * fragment and add it to backstack.
     *
     * @param Fragment fragment The new fragment
     */
	public void switchContent(Fragment fragment) { 
		if(mContent != null && mContent.getClass().equals(fragment.getClass())) {
	            return;
	    }
		mContent = fragment;
		
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.fragment_container, fragment)
        .addToBackStack(null)
		.commit();
	}

    /*
     * Get saved subscriptions
     *
     * @return ArrayList<UrlItem>
     */
	public ArrayList<UrlItem> getSubscriptions() {
		return subscriptions;
	}

    /*
     * Add a new UrlItem to the subscriptions list
     *
     * @param String name Feedname
     * @param URL url Feedurl
     */
	public void addSubscriptions(String name, URL url) {
        subscriptions.add(new UrlItem(name, url));
	}

}
