/*
 * ItemArrayAdapter
 *
 * Custom ArrayAdapter for RssItems with ViewHolder Pattern for performance
 */
package at.technikumwien.android.rssreader.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import at.technikumwien.android.rssreader.R;

public class ItemArrayAdapter extends CursorAdapter {
    Context context;

    public ItemArrayAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);

        this.context = context;
    }

    // Inflate view for article items
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.rss_list_items, parent, false);
    }

    // Set items text and typeface (bold = unread items)
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder;
        holder = new ViewHolder();
        holder.textViewTitle = (TextView)view.findViewById(R.id.textViewTitleLabel);
        holder.textViewDate = (TextView)view.findViewById(R.id.textViewDateLabel);
        view.setTag(holder);

        holder.textViewTitle.setText(cursor.getString(cursor.getColumnIndex("title")));
        holder.textViewDate.setText(cursor.getString(cursor.getColumnIndex("date")));

        if(cursor.getInt(cursor.getColumnIndex("read")) == 0){
            holder.textViewTitle.setTypeface(null, Typeface.BOLD);
            holder.textViewDate.setTypeface(null, Typeface.BOLD);
        }else{
            holder.textViewTitle.setTypeface(null, Typeface.NORMAL);
            holder.textViewDate.setTypeface(null, Typeface.NORMAL);
        }
    }

    static class ViewHolder {
        TextView textViewTitle;
        TextView textViewDate;
    }

}
