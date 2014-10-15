package com.example.myapp;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.IllegalFormatCodePointException;


@SuppressLint("NewApi")
public class SubscribeFragment extends Fragment {
	
	private Button mSubscribeButton;
	
	public static Fragment newInstance() {
		SubscribeFragment fragment = new SubscribeFragment();

		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        }, new IntentFilter("test"));
		
		View v = inflater.inflate(R.layout.fragment_subscribe, container, false);
		
		final EditText editText = (EditText) v.findViewById(R.id.edit_subscribe);
		
		mSubscribeButton = (Button) v.findViewById(R.id.button_subscribe);
		mSubscribeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String input = editText.getText().toString();
				
				if(input != null){
					Toast.makeText(getActivity(), "Eingabe: " + input, Toast.LENGTH_SHORT).show();
                    try{
                        URL url = new URL(input);
                        url.toURI();
                        ((RssActivity)getActivity()).addSubscriptions(input);
                    }catch (MalformedURLException e){
                        Toast.makeText(getActivity(), "Keine Url eingegeben!", Toast.LENGTH_SHORT).show();
                    }catch (URISyntaxException e){
                        Toast.makeText(getActivity(), "Url nicht gültig!", Toast.LENGTH_SHORT).show();
                    }

				}
			}
		});
		return v;
		
	}
}
