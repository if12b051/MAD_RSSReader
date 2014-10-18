package at.technikumwien.android.rssreader.contentprovider;

import android.net.Uri;
import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class RssContentProvider extends ContentProvider{
    private static final String DATABASE_NAME = "rssdatabase";
    private static final String CONTENTPROVIDER_AUTHORITY =
            "at.technikumwien.android.rssreader";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + CONTENTPROVIDER_AUTHORITY + ".provider/");

    // Table Names
    protected static final String TABLE_RSS_FEEDS = "rss_feeds";
    protected static final String TABLE_RSS_ITEMS = "rss_items";

    protected DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projections, String selections, String[] selectionsArgs, String sortOrder) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        return db.query(uri.toString(), projections, selections, selectionsArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        return db.in;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;

        // Create Statement
        private static final String CREATE_TABLE_RSS_FEEDS =
                "CREATE TABLE " + TABLE_RSS_FEEDS + "("
                    + "id INTEGER PRIMARY KEY,"
                    + "name STRING NOT NULL,"
                    + "url STRING NOT NULL"
                + ");";
        private static final String CREATE_TABLE_RSS_ITEMS =
                "CREATE TABLE " + TABLE_RSS_ITEMS + "("
                    + "id INTEGER PRIMARY KEY,"
                    + "feedid INTEGER,"
                    + "title STRING,"
                    + "date STRING,"
                    + "url STRING,"
                    + "read INT"
                    + "FOREIGN KEY(feedid) REFERENCES" + TABLE_RSS_FEEDS + "(id)"
                + ");";

        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_RSS_FEEDS);
            db.execSQL(CREATE_TABLE_RSS_ITEMS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RSS_FEEDS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RSS_ITEMS);
            onCreate(db);
        }
    }
}