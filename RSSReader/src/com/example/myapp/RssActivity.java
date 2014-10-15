package com.example.myapp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.*;
import android.os.Messenger;
import android.support.v7.app.ActionBarActivity;

@SuppressLint("NewApi")
public class RssActivity extends ActionBarActivity implements ServiceConnection {
    private Messenger serviceMessenger = null;
    private final Messenger messenger = new Messenger(new MessageHandler());
    private ServiceConnection connection = this;
	private Fragment mContent;
	public static RssActivity instance;
	private ArrayList<String> subscriptions;
    public ArrayList<String> items;
	
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
        bindService(new Intent(this, RssService.class), connection, Context.BIND_AUTO_CREATE);
		switchContent(new MenuFragment());
	}

    @Override
    public void onPause(){
        super.onPause();
        unbindService(connection);
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() != 0){
            fm.popBackStack();
        }else{
            super.onBackPressed();
        }
    }

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

	public ArrayList<String> getSubscriptions() {
		return subscriptions;
	}

	public void addSubscriptions(String sub) {
		subscriptions.add(sub);
	}

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        serviceMessenger = new Messenger(iBinder);
    }

    public void SendMessage(String url){
        try {
            Message msg = Message.obtain(null, RssService.MSG_URL);
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            msg.setData(bundle);
            msg.replyTo = messenger;
            serviceMessenger.send(msg);
        }
        catch (RemoteException e) {
            // In this case the service has crashed before we could even do anything with it
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RssService.MSG_ITEMS:
                    items = msg.getData().getStringArrayList("items");
                    if (items != null) {
                        ShowFragment.getInstance().adapter.addAll(items);
                        ShowFragment.getInstance().adapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

}
