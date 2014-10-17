/*
 * UrlArrayAdapter
 *
 * Custom ArrayAdapter for UrlItems with ViewHolder Pattern for performance
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
import at.technikumwien.android.rssreader.items.UrlItem;

import java.util.ArrayList;

public class UrlArrayAdapter extends ArrayAdapter<UrlItem> {
    Context context;
    int resourceId;
    ArrayList<UrlItem> items = null;
    ViewHolder viewHolder;

    public UrlArrayAdapter(Context context, int resourceId){
        super(context, resourceId);

        this.context = context;
        this.resourceId = resourceId;
        this.items = new ArrayList<UrlItem>();
    }

    public UrlArrayAdapter(Context context, int resourceId, ArrayList<UrlItem> items){
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

    }

    static class ViewHolder {
        TextView textViewName;
        TextView textViewUrl;
    }

}
