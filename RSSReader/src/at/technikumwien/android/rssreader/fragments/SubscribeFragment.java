package at.technikumwien.android.rssreader.fragments;

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
import at.technikumwien.android.rssreader.R;
import at.technikumwien.android.rssreader.RssActivity;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;


@SuppressLint("NewApi")
public class SubscribeFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_subscribe, container, false);

        final EditText nameText = (EditText) v.findViewById(R.id.edit_subscribe_name);
		final EditText urlText = (EditText) v.findViewById(R.id.edit_subscribe_url);
		
		Button mSubscribeButton = (Button) v.findViewById(R.id.button_subscribe);
		mSubscribeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                // Name of the feed
                String nameString = nameText.getText().toString();

                // Url to the feed
				String urlString = urlText.getText().toString();

                // Both need to be set
                if(!urlString.isEmpty() && !nameString.isEmpty()){
                    try{
                        // Quick 'n' Dirty check if url is ok
                        URL url = new URL(urlString);
                        url.toURI();
                        // Save subscription in array
                        ((RssActivity)getActivity()).addSubscriptions(nameString, url);
                        Toast.makeText(getActivity(), "Rss Feed gespeichert", Toast.LENGTH_SHORT).show();
                    }catch (MalformedURLException e){
                        Toast.makeText(getActivity(), "Keine Url eingegeben!", Toast.LENGTH_SHORT).show();
                    }catch (URISyntaxException e){
                        Toast.makeText(getActivity(), "Url nicht gültig!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Bitte Namen und URL eingeben!", Toast.LENGTH_SHORT).show();
                }
            }
		});
		return v;
		
	}

}
