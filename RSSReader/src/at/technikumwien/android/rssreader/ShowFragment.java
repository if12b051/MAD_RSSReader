package at.technikumwien.android.rssreader;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class ShowFragment extends Fragment {
	
	public static ShowFragment newInstance() {
		ShowFragment fragment = new ShowFragment();
		
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_show, container, false);
		
	}

}
