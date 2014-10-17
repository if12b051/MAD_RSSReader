/*
 * UrlItem
 *
 * Capsle an rss feed URL into an object
 * @param String name Name of the feed
 * @param URL url Feedurl
 */

package at.technikumwien.android.rssreader.items;

import java.net.URL;

public class UrlItem {
    public String name;
    public URL url;

    public UrlItem(String name, URL url){
        this.name = name;
        this.url = url;
    }
}
