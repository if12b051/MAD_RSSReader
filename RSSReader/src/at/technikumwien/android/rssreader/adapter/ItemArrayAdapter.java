/*
 * ItemArrayAdapter
 *
 * Custom ArrayAdapter for RssItems with ViewHolder Pattern for performance
 */
package at.technikumwien.android.rssreader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import at.technikumwien.android.rssreader.R;
import at.technikumwien.android.rssreader.RssActivity;
import at.technikumwien.android.rssreader.items.RssItem;

import java.util.ArrayList;

public class ItemArrayAdapter extends ArrayAdapter<RssItem> {
    Context context;
    int resourceId;
    ArrayList<RssItem> items = null;
    ViewHolder viewHolder;

    public ItemArrayAdapter(Context context, int resourceId){
        super(context, resourceId);

        this.items = new ArrayList<RssItem>();
        this.context = context;
        this.resourceId = resourceId;
    }

    public ItemArrayAdapter(Context context, int resourceId, ArrayList<RssItem> items){
        super(context, resourceId, items);

        this.context = context;
        this.resourceId = resourceId;
        this.items = items;
    }

    public View getView(int position, View convertView, ViewGroup parent){
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
        }

        return convertView;

    }

    static class ViewHolder {
        TextView textViewTitle;
        TextView textViewDate;
    }

}
