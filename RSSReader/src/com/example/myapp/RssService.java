package com.example.myapp;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xris on 14.10.2014.
 */
public class RssService extends Service {
    private final Messenger messenger = new Messenger(new MessageHandler());
    private Messenger client;
    private MessageHandler handler;
    public static final int MSG_ITEMS = 1;
    public static final int MSG_URL = 2;

    @Override
    public IBinder onBind(Intent intent) {
        handler = new MessageHandler();
        return messenger.getBinder();
    }

    /**
     * Handle incoming messages from MainActivity
     */
    private class MessageHandler extends Handler { // Handler of incoming messages from clients.
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_URL:
                    client = msg.replyTo;
                    String url = msg.getData().getString("url");
                    RssThread myThread = new RssThread(url);
                    myThread.start();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private class RssThread extends Thread{
        String url;
        public RssThread(String parameter){
            this.url = parameter;
        }
        @Override
        public void run(){
            Parse(url);
        }
    }

    private void Parse(String urlParam) {
        try {
            URL url = new URL(urlParam);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = input.read(buffer)) != -1; ) {
                output.write(buffer, 0, count);
            }
            byte[] response = output.toByteArray();
            String rssFeed = new String(response, "UTF-8");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(rssFeed));
            parser.nextTag();

            ArrayList<String> rss = readRss(parser);
            Message msg = Message.obtain(null, MSG_ITEMS);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("items", rss);
            msg.setData(bundle);
            client.send(msg);
            //handler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }


    private ArrayList<String> readRss(XmlPullParser parser) throws XmlPullParserException, IOException{
        ArrayList<String> items = new ArrayList<String>();
        parser.require(XmlPullParser.START_TAG, null, "rss");
        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = parser.getName();
            if (name.equals("channel")){
                items.addAll(readChannel(parser));
            }else{
                skip(parser);
            }
        }
        return items;
    }

    private ArrayList<String> readChannel(XmlPullParser parser) throws XmlPullParserException, IOException{
        ArrayList<String> items = new ArrayList<String>();
        parser.require(XmlPullParser.START_TAG, null, "channel");
        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = parser.getName();
            if (name.equals("item")){
                items.add(readItem(parser));
            }else{
                skip(parser);
            }
        }
        return items;
    }

    private String readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = null;
        parser.require(XmlPullParser.START_TAG, null, "item");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                parser.require(XmlPullParser.START_TAG, null, "title");
                String title = "";
                if (parser.next() == XmlPullParser.TEXT) {
                    title = parser.getText();
                    parser.nextTag();
                }
                parser.require(XmlPullParser.END_TAG, null, "title");
                result = title;
            } else {
                skip(parser);
            }
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
