package at.technikumwien.android.rssreader;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


@SuppressLint("NewApi")
public class SubscriptionsFragment extends Fragment {


	public static Fragment newInstance() {
		SubscriptionsFragment fragment = new SubscriptionsFragment();
		
		
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		
		return inflater.inflate(R.layout.fragment_subscriptions, container, false);
		
	}
	
	
	

}
