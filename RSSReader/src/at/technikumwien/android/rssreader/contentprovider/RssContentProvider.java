package at.technikumwien.android.rssreader.contentprovider;

import android.content.UriMatcher;
import android.net.Uri;
import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.net.URI;
import java.net.URL;

public class RssContentProvider extends ContentProvider{
    private static final String DATABASE_NAME = "rssdatabase.db";
    private static final String CONTENTPROVIDER_AUTHORITY =
            "at.technikumwien.android.rssreader";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + CONTENTPROVIDER_AUTHORITY + ".provider/");

    // Table Names
    public static final String TABLE_RSS_FEEDS = "rss_feeds";
    public static final String TABLE_RSS_ITEMS = "rss_items";

    // UriMatcher
    private static final int FEEDS = 1;
    private static final int ITEMS = 2;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        URI_MATCHER.addURI("at.technikumwien.android.rssreader.provider", TABLE_RSS_FEEDS, FEEDS);
        URI_MATCHER.addURI("at.technikumwien.android.rssreader.provider", TABLE_RSS_ITEMS, ITEMS);
    }

    protected DatabaseHelper databaseHelper;

    public RssContentProvider(){
        //databaseHelper = new DatabaseHelper(getContext());
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projections, String selections, String[] selectionsArgs, String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        int table = URI_MATCHER.match(uri);
        switch (table){
            case FEEDS:
                cursor =  db.query(TABLE_RSS_FEEDS, projections, selections, selectionsArgs, null, null, sortOrder);
                break;
            case ITEMS:
                cursor = db.query(TABLE_RSS_ITEMS, projections, selections, selectionsArgs, null, null, sortOrder);
                break;
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = 0;
        String url = null;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int table = URI_MATCHER.match(uri);
        switch (table){
            case FEEDS:
                id = db.insertWithOnConflict(TABLE_RSS_FEEDS, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                url = CONTENT_URI + TABLE_RSS_FEEDS;
                break;
            case ITEMS:
                id = db.insertWithOnConflict(TABLE_RSS_ITEMS, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                url = CONTENT_URI + TABLE_RSS_ITEMS;
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(url + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = 0;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int table = URI_MATCHER.match(uri);
        switch(table){
            case FEEDS:
                rowsDeleted = db.delete(CONTENT_URI + TABLE_RSS_FEEDS, selection, selectionArgs);
            case ITEMS:
                rowsDeleted = db.delete(CONTENT_URI + TABLE_RSS_ITEMS, selection, selectionArgs);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int affectedRows = 0;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int table = URI_MATCHER.match(uri);
        switch (table){
            case FEEDS:
                affectedRows = db.update(TABLE_RSS_FEEDS, contentValues, selection, selectionArgs);
                break;
            case ITEMS:
                affectedRows = db.update(TABLE_RSS_ITEMS, contentValues, selection, selectionArgs);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return affectedRows;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 2;

        // Create Statement
        private static final String CREATE_TABLE_RSS_FEEDS =
                "CREATE TABLE " + TABLE_RSS_FEEDS + "("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name STRING NOT NULL,"
                    + "url STRING NOT NULL"
                + ");";
        private static final String CREATE_TABLE_RSS_ITEMS =
                "CREATE TABLE " + TABLE_RSS_ITEMS + "("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "feedid INTEGER NOT NULL,"
                    + "title STRING,"
                    + "date STRING,"
                    + "url STRING,"
                    + "read INT,"
                    + "FOREIGN KEY(feedid) REFERENCES " + TABLE_RSS_FEEDS + "(id)"
                + ");";

        public DatabaseHelper(Context context){
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