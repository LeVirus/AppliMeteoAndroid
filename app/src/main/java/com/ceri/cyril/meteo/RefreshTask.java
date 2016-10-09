package com.ceri.cyril.meteo;


import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import java.net.URL;
import java.util.ArrayList;

import static com.ceri.cyril.meteo.CityView.errReq;
import static com.ceri.cyril.meteo.CityView.queue;
import static com.ceri.cyril.meteo.CityView.req;
import static com.ceri.cyril.meteo.CityView.respList;


/**
 * Created by cyril on 07/10/16.
 */

 public class RefreshTask extends AsyncTask<URL, Integer, Long>
{
    long a = 0;
    ArrayList<Ville> tabVille = null;
    protected Long doInBackground(URL... urls)
    {
        Log.d("-------------------", "getWeather debut-------------------------------------------------------------------\n");
        String url = null;
        if( null == errReq )errReq = new ErrResp();
        if( null == respList )respList = new ResponseListener();

        if( tabVille == null )
           return a;

        for( Ville v : tabVille )
        {
            url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" +
                    v.getNomVille() + "%2C%20fr%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

            req = new StringRequest(Request.Method.GET, url, respList, errReq );
            queue.add( req );
        }
        return a;
    }

public void memTabVille( ArrayList<Ville> memTab )
{
    tabVille = memTab;
}


    //URL urlYahoo = new URL("http://weather.yahooapis.com/forecastrss?w=woeid&u=unit");

}
