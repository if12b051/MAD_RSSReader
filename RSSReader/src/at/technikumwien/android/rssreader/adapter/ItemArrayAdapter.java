/*
 * ItemArrayAdapter
 *
 * Custom ArrayAdapter for RssItems with ViewHolder Pattern for performance
 */
package at.technikumwien.android.rssreader.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import at.technikumwien.android.rssreader.R;

public class ItemArrayAdapter extends CursorAdapter {
    Context context;
    ViewHolder viewHolder;

    @SuppressLint("NewApi")
    public ItemArrayAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);

        this.context = context;
    }

    /*public ItemArrayAdapter(Context context, int resourceId, ArrayList<RssItem> items){
        super(context, resourceId, items);

        this.context = context;
        this.resourceId = resourceId;
        this.items = items;
    }*/

    /*public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            LayoutInflater inflater = ((RssActivity)context).getLayoutInflater();
            convertView = inflater.inflate(resourceId, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textViewTitle = (TextView)convertView.findViewById(R.id.textViewTitleLabel);
            viewHolder.textViewDate = (TextView)convertView.findViewById(R.id.textViewDateLabel);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RssItem item = items.get(position);
        if (item != null){
            viewHolder.textViewTitle.setText(item.title);
            viewHolder.textViewDate.setText(item.date);

            if (!item.read) {
                viewHolder.textViewTitle.setTypeface(null, Typeface.BOLD);
                viewHolder.textViewDate.setTypeface(null, Typeface.BOLD);
            }
        }

        return convertView;

    }*/

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.rss_list_items, parent, false);
    }

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
        }
    }

    static class ViewHolder {
        TextView textViewTitle;
        TextView textViewDate;
    }

}
