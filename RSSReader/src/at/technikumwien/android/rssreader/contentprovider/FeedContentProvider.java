package at.technikumwien.android.rssreader.contentprovider;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import at.technikumwien.android.rssreader.items.UrlItem;

import java.util.ArrayList;
import java.util.List;

public class FeedContentProvider extends RssContentProvider {
    public List<UrlItem> GetAllFeeds(){
        List<UrlItem> rslt = new ArrayList<UrlItem>();



        return rslt;
    }

    public void InsertFeed(String name, String url){
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("url", url);

        insert(Uri.parse(CONTENT_URI + TABLE_RSS_FEEDS), values);
    }

    public void UpdateFeed(){

    }

    public void DeleteFeed(){

    }
}
