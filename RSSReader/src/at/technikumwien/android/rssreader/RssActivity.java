/*
 * RssActivity
 *
 * Main activity of the rss reader
 */

package at.technikumwien.android.rssreader;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.*;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import at.technikumwien.android.rssreader.fragments.*;

public class RssActivity extends ActionBarActivity implements AbsListView.MultiChoiceModeListener{
	public static RssActivity instance;

	public static RssActivity getInstance() {
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rss);
		instance = this;
	}

    @Override
    public void onResume() {
        super.onResume();
        //Switch to first fragment
        switchContent(new SubscriptionsFragment());
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
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.fragment_container, fragment)
        .addToBackStack(null)
		.commit();
	}

    /*
     * Overwrite onCreateOptions, inflates the ActionBarMenu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rss, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Overwrite onOptionsItemSelected to handle ActionBar menu item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_refresh:
                //SendMessage();
                return false;
            case R.id.action_add:
                switchContent(new SubscribeFragment());
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    //region Not implemented Interfacemethods
    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }
    //endregion
}
