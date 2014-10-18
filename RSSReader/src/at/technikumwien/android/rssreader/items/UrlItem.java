/*
 * UrlItem
 *
 * Capsle an rss feed URL into an object
 * @param int id Primarykey of database
 * @param String name Name of the feed
 * @param URL url Feedurl
 */

package at.technikumwien.android.rssreader.items;

import java.net.URL;

public class UrlItem {
    public int id;
    public String name;
    public URL url;

    public UrlItem(){
        this.id = 0;
        this.name = null;
        this.url = null;
    }

    public UrlItem(int id, String name, URL url){
        this.id = id;
        this.name = name;
        this.url = url;
    }
}
