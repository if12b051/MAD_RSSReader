/*
 * UrlArrayAdapter
 *
 * Custom ArrayAdapter for UrlItems with ViewHolder Pattern for performance
 */
package at.technikumwien.android.rssreader.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import at.technikumwien.android.rssreader.R;

public class UrlArrayAdapter extends CursorAdapter {
    Context context;

   public UrlArrayAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
        this.context = context;
    }

    // Inflate view of list items for subscriptions
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.url_list_items, parent, false);
    }

    // Set item text
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder;

        holder = new ViewHolder();
        holder.textViewName = (TextView)view.findViewById(R.id.textViewNameLabel);
        holder.textViewUrl = (TextView)view.findViewById(R.id.textViewUrlLabel);
        view.setTag(holder);

        holder.textViewName.setText(cursor.getString(cursor.getColumnIndex("name")));
        holder.textViewUrl.setText(cursor.getString(cursor.getColumnIndex("url")));
    }

    static class ViewHolder {
        TextView textViewName;
        TextView textViewUrl;
    }

}
