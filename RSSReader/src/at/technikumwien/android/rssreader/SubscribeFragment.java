package at.technikumwien.android.rssreader;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


@SuppressLint("NewApi")
public class SubscribeFragment extends Fragment {
	
	private Button mSubscribeButton;
	
	public static Fragment newInstance() {
		SubscribeFragment fragment = new SubscribeFragment();

		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_subscribe, container, false);
		
		final EditText editText = (EditText) v.findViewById(R.id.edit_subscribe);
		
		mSubscribeButton = (Button) v.findViewById(R.id.button_subscribe);
		mSubscribeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String input = editText.getText().toString();
				
				if(input != null){
					Toast.makeText(getActivity(), "Eingabe: " + input, Toast.LENGTH_SHORT).show();
					((RssActivity)getActivity()).addSubscriptions(input);
				}
			}
		});
		return v;
		
	}

}
