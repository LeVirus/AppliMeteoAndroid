package com.ceri.cyril.meteo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.lang.System.in;
import static java.lang.Thread.sleep;

public class CityView extends AppCompatActivity {

    Ville ville;
    TextView tvVille, tvPays, tvVent, tvTemperature, tvPression, tvDate;
    String url = null;
    static StringRequest req;
    RequestQueue queue = null;
    private static ErrResp errReq = null;
    private static ResponseListener respList = null;
    CityView refThis = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_view);
        refThis = this;
        queue = Volley.newRequestQueue( this );
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
        tvVent.setText( "Direction Vent     "  + ville.getDirectionVent() + "\nVitesse vent   " + ville.getVitesseVent() );
        tvTemperature.setText( "Temperature     "  + ville.getTemperature() );
        tvPression.setText( "Pression atmosphérique    "  + ville.getPressionAtmos() );
        tvDate.setText( "Date     "  + ville.getDateDerniereMaj() );
        url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" +
                ville.getNomVille() + "%2C%20fr%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

        Log.d( "-------------------", url + "ecrireInfoVille -------------------------------------------------------------------\n" );


    }

    public void sendMajVille(List<String> infoVille)
    {
        try
        {
            //List<String> recupInfo = respList.getInfo();


            int cmpt = 0;
            float temp = 0, vent = 0, pression = 0;
            String date = "", dirVent = "";
            for(String a  : infoVille)
            {
                Log.d("-------------------", a + "  add info to Ville -------------------------------------------------------------------\n");
                switch( cmpt ){
                    case 0://vent vitesse + direction
                        String[] splited = a.split("\\s+");
                        vent = Float.parseFloat( splited[0] );
                        dirVent = splited[1];
                        break;//temp
                    case 1:
                        temp = Float.parseFloat(a);

                        break;//pression
                    case 2:
                        pression = Float.parseFloat(a);
                        break;
                    case 3://date
                        date = a;
                        break;
                }
                cmpt++;
            }

            ville.configVille( ville.getNomVille(), ville.getPays(), date, vent, dirVent, pression, temp);
        }catch (Exception e)
        {
            Log.d("-------------------", e.toString() + "  add info to Ville -------------------------------------------------------------------\n");
        }
        ville.afficherVille();
        ecrireInfoVille();
    }


    public void getWeather()
    {
        Log.d("-------------------", "getWeather debut-------------------------------------------------------------------\n");
        if(null == errReq)errReq = new ErrResp();
        if(null == respList)respList = new ResponseListener();
        respList.giveRef( refThis );
        req = new StringRequest(Request.Method.GET, url, respList, errReq );
        queue.add( req );

    }
}

class ResponseListener implements Response.Listener<String>
{
    CityView ref = null;
    List<String> lstStr = null;
        @Override
        public void onResponse(String s)
        {
            Log.d("reponse::::::::", s + "getWeather -------------------------------------------------------------------\n");

            InputStream stream = new ByteArrayInputStream(s.getBytes());
            if( Ville.refJsonResp == null )
                Ville.refJsonResp = new JSONResponseHandler();
            try
            {
                lstStr = Ville.refJsonResp.handleResponse(stream, "");
                ref.sendMajVille( lstStr );
            }catch (Exception e)
            {
                System.out.println(e.toString() + "getweather");
            }
        }

    public List<String> getInfo()
    {
        return lstStr;
    }

    public void giveRef(CityView refCityView)
    {
        ref = refCityView;
    }


};

class ErrResp implements Response.ErrorListener
{
        @Override
public void onErrorResponse(VolleyError volleyError)
        {

    //Log.d("ERROR-------", "getWeather -------------------------------------------------------------------\n");

}
};
