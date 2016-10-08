package com.ceri.cyril.meteo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class CityView extends AppCompatActivity {

    Ville ville;
    TextView tvVille, tvPays, tvVent, tvTemperature, tvPression, tvDate;
    String url = null;
    static StringRequest req;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_view);
        recupRefTextView();
        recupBundle();
        ecrireInfoVille();
        getWeather();

    }

    void recupRefTextView()
    {
        tvVille = (TextView) findViewById( R.id.ville );
        tvPays = (TextView) findViewById( R.id.pays );
        tvVent = (TextView) findViewById( R.id.Vent );
        tvTemperature = (TextView) findViewById( R.id.Temperature );
        tvPression = (TextView) findViewById( R.id.Pression );
        tvDate = (TextView) findViewById( R.id.Date );
    }

    /**
     * Récupération des données émises à la création de l'activité.
     */
    void recupBundle()
    {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try
            {
                ville = (Ville)extras.get("ville");
            }catch (Exception e)
            {
                System.out.println(e.toString() + "recupBundle");
            }
        }
    }

    void ecrireInfoVille()
    {
        if( ville == null )return;
        tvVille.setText( "Ville     " + ville.getNomVille() );
        tvPays.setText( "Pays     " + ville.getPays() );
        tvVent.setText( "Vent     "  );//à compléter
        tvTemperature.setText( "Temperature     "  );//à compléter
        tvPression.setText( "Pression     "  );//à compléter
        tvDate.setText( "Date     "  );//à compléter
        url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" +
                ville.getNomVille() + "%2C%20fr%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    }


    public void getWeather()
    {
        //RequestQueue queue = Volley.newRequestQueue(this);
        req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String s)
            {
                System.out.print("+++++++++++++++++++++++++++++++++++++++++++++++++++++"+s+"++++++++++++++++++++++++++++++++++++++++++\n");
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.print("+++++++++++++++++++++++++++++++++++++++++++++++++++++"+"++++++++++++++++++++++++++++++++++++++++++\n");

            }
        });
    }
}
