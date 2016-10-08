package com.ceri.cyril.meteo;

import android.accounts.Account;
import android.app.DownloadManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;





/**
 * Created by cyril on 07/10/16.
 */

 public class RefreshTask extends AsyncTask<URL, Integer, Long>
{
//http://weather.yahooapis.com/forecastrss?w=woeid&u=unit
    //select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+ city +"%22)&format=json
    protected Long doInBackground(URL... urls) {
        int count = urls.length;
        long totalSize = 0;

        return totalSize;
    }




    //URL urlYahoo = new URL("http://weather.yahooapis.com/forecastrss?w=woeid&u=unit");

}
