/*
 * UrlArrayAdapter
 *
 * Custom ArrayAdapter for UrlItems with ViewHolder Pattern for performance
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


public class UrlArrayAdapter extends CursorAdapter {
    Context context;
    ViewHolder viewHolder;

   @SuppressLint("NewApi")
   public UrlArrayAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);

        this.context = context;
    }

    /*public UrlArrayAdapter(Context context, int resourceId, ArrayList<UrlItem> items){
        super(context, resourceId, items);
        super(context,)

        this.context = context;
        this.resourceId = resourceId;
        this.items = items;
    }*/

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.url_list_items, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder;
        //if (view == null){
            holder = new ViewHolder();
            holder.textViewName = (TextView)view.findViewById(R.id.textViewNameLabel);
            holder.textViewUrl = (TextView)view.findViewById(R.id.textViewUrlLabel);
            view.setTag(holder);
        //}else {
        //    holder = (ViewHolder)view.getTag();
        //}

        holder.textViewName.setText(cursor.getString(cursor.getColumnIndex("name")));
        holder.textViewUrl.setText(cursor.getString(cursor.getColumnIndex("url")));

        //TextView listItem = (TextView) view.findViewById(R.id.textViewNameLabel);
        //listItem.setText(cursor.getString(cursor.getColumnIndex("name")));
    }

    /*public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            //LayoutInflater inflater = context. .getLayoutInflater();
            //convertView = inflater.inflate(resourceId, parent, false);
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_subscriptions, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView)convertView.findViewById(R.id.textViewNameLabel);
            viewHolder.textViewUrl = (TextView)convertView.findViewById(R.id.textViewUrlLabel);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        UrlItem item = items.get(position);
        if (item != null){
            viewHolder.textViewName.setText(item.name);
            viewHolder.textViewUrl.setText(item.url.toString());
        }

        return convertView;

    }*/

    static class ViewHolder {
        TextView textViewName;
        TextView textViewUrl;
    }

}
