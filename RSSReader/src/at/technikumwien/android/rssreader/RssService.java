/*
 * RssService
 *
 * Service of the application to download an rss feed and parse the XML file
 */

package at.technikumwien.android.rssreader;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import at.technikumwien.android.rssreader.contentprovider.RssContentProvider;
import at.technikumwien.android.rssreader.items.RssItem;
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

public class RssService extends Service {
    private final Messenger messenger = new Messenger(new MessageHandler());
    private Messenger client;
    private MessageHandler handler;
    public static final int MSG_ITEMS = 1;
    public static final int MSG_URL = 2;

    private String url = null;
    private int id = 0;

    @Override
    public IBinder onBind(Intent intent) {
        handler = new MessageHandler();
        return messenger.getBinder();
    }

    /*
     * Innerclass message handler for incoming messages
     */
    private class MessageHandler extends Handler { // Handler of incoming messages from clients.
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_URL:
                    client = msg.replyTo;
                    url = msg.getData().getString("url");
                    id = msg.getData().getInt("id");
                    RssThread myThread = new RssThread(url);
                    myThread.start();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Innerclass threading class to download and parse rss feed
     */
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
            //try to download feed
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

            // Initialize XML-Parser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(rssFeed));
            parser.nextTag();

            // Parse RSS and send data to client (showFragment)
            ArrayList<RssItem> rss = readRss(parser);
            Message msg = Message.obtain(null, MSG_ITEMS);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("items", rss);
            msg.setData(bundle);
            client.send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    /*
    * Entry point of xml parsing, returning all rss items
    *
    * @param XMLPullParser parser Instance of the xml parser
    * @return ArrayList<RssItem> items All items of the rss feed
    */
    private ArrayList<RssItem> readRss(XmlPullParser parser) throws XmlPullParserException, IOException{
        ArrayList<RssItem> items = new ArrayList<RssItem>();
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

    /*
    * Read the channel tag of the rss feed
    *
    * @param XMLPullParser parser Instance of the xml parser
    * @return ArrayList<RssItem> items All items of the rss feed
    */
    private ArrayList<RssItem> readChannel(XmlPullParser parser) throws XmlPullParserException, IOException{
        ArrayList<RssItem> items = new ArrayList<RssItem>();
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

    /*
    * Read the current item of the rss feed
    *
    * @param XMLPullParser parser Instance of the xml parser
    * @return RssItem result The current parsed rss item
    */
    private RssItem readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        RssItem result = new RssItem();
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
                result.title = title;
            } else if (name.equals("link")){
                parser.require(XmlPullParser.START_TAG, null, "link");
                String link = "";
                if (parser.next() == XmlPullParser.TEXT) {
                    link = parser.getText();
                    parser.nextTag();
                }
                parser.require(XmlPullParser.END_TAG, null, "link");
                result.url = new URL(link);
            }else if (name.equals("pubDate")) {
                parser.require(XmlPullParser.START_TAG, null, "pubDate");
                String date = "";
                if (parser.next() == XmlPullParser.TEXT) {
                    date = parser.getText();
                    parser.nextTag();
                }
                parser.require(XmlPullParser.END_TAG, null, "pubDate");
                result.date = date;
            }else{
                skip(parser);
            }
        }

        ContentValues values = new ContentValues();
        values.put("feedid", id);
        values.put("title", result.title);
        values.put("date", result.date);
        values.put("url", result.url.toString());
        values.put("read", 0);

        getContentResolver().insert(Uri.parse(RssContentProvider.CONTENT_URI + RssContentProvider.TABLE_RSS_ITEMS), values);
        return result;
    }

    /*
    * Skip all unneeded tags
    *
    * @param XMLPullParser parser Instance of the xml parser
    */
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