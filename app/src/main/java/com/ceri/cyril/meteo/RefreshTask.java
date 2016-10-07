package com.ceri.cyril.meteo;

import android.os.AsyncTask;

import java.net.URL;


/**
 * Created by cyril on 07/10/16.
 */

 public class RefreshTask extends AsyncTask<URL, Integer, Long>
{
    //URL urlYahoo = new URL("http://weather.yahooapis.com/forecastrss?w=woeid&u=unit");
    protected Long doInBackground(URL... urls) {
        int count = urls.length;
        long totalSize = 0;
        for (int i = 0; i < count; i++) {
            //totalSize += Downloader.downloadFile(urls[i]);
            publishProgress((int) ((i / (float) count) * 100));
            // Escape early if cancel() is called
            if (isCancelled()) break;
        }
        return totalSize;
    }
}
