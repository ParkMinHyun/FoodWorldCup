package com.example.parkminhyun.foodworldcup.JSoup;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by ParkMinHyun on 2017-11-28.
 */

public class JSoup_AsnynTask extends AsyncTask<Void, Void, String> {

    public JsoupAsyncResponse delegate = null;

    private String homepageUrl;
    private String storeName;

    public interface JsoupAsyncResponse {
        void processOfJsoupAsyncFinish(String response);

    }

    public JSoup_AsnynTask(JsoupAsyncResponse delegate, String homepageUrl, String storeName) {
        this.delegate = delegate;
        this.homepageUrl = homepageUrl;
        this.storeName = storeName;
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {
            Document doc = Jsoup.connect(homepageUrl).get();
            Elements link = doc.select(".biz_name");
            if(link == null){
                link = doc.select(".item_info");
            }

            String relHref = link.attr("href");

            StringBuilder relHrefStringBuilder = new StringBuilder(relHref);
            String baseURL = relHrefStringBuilder.substring(0, relHrefStringBuilder.indexOf("query=") + "query=".length());

            return baseURL + storeName;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        delegate.processOfJsoupAsyncFinish(result);
    }

}

