package at.technikumwien.android.rssreader.fragments;

import android.app.Fragment;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import at.technikumwien.android.rssreader.R;
import at.technikumwien.android.rssreader.contentprovider.RssContentProvider;

public class WebViewFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);

        //get url from arguments
        Bundle bundle = getArguments();

        // Open Webview to display rss content
        WebView webView = (WebView) getView().findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(bundle.getString("url"));

        // Save read status in databasse
        ContentValues values = new ContentValues();
        values.put("read", 1);
        getActivity().getContentResolver().update(
            Uri.parse(RssContentProvider.CONTENT_URI + RssContentProvider.TABLE_RSS_ITEMS),
            values, "id = ?", new String[] {String.valueOf(getArguments().getInt("id"))}
        );
    }
}