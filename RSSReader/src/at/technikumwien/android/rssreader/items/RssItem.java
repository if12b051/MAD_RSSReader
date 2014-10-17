/*
 * RssItem
 *
 * Capsle an rss item into an parcelable object
 *
 * @param String title The title of the item
 * @param String date Time of the item
 * @param URL url Article url
 * @param boolean read Status whetever the item was already read or not
 */

package at.technikumwien.android.rssreader.items;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.MalformedURLException;
import java.net.URL;

public class RssItem implements Parcelable {
    public String title;
    public String date;
    public URL url;
    public boolean read;

    // Standardconstructor
    public RssItem(){
        this.title = null;
        this.date = null;
        this.url = null;
        this.read = false;
    }

    // Overloaded constructor
    public RssItem(String title, String date, URL url, boolean read){
        this.title = title;
        this.date = date;
        this.url = url;
        this.read = read;
    }

    // Parceable overloaded constructor
    public RssItem(Parcel parcel){
        super();
        readFromParcel(parcel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Convert Parcel to Object
    public void readFromParcel(Parcel in) {
        title = in.readString();
        date = in.readString();
        try {
            url = new URL(in.readString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        read = in.readByte() != 0;
    }

    @Override
    // Convert object to Parcel
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(date);
        parcel.writeString(url.toString());
        parcel.writeByte((byte)(read ? 1 : 0));
    }

    // Parcelable Creator
    public static final Creator<RssItem> CREATOR = new Creator<RssItem>() {
        @Override
        public RssItem createFromParcel(Parcel parcel) {
            return new RssItem(parcel);
        }

        @Override
        public RssItem[] newArray(int i) {
            return new RssItem[i];
        }
    };
}