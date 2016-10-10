package com.ceri.cyril.meteo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.lang.System.in;
import static java.lang.Thread.sleep;

public class CityView extends AppCompatActivity {

    Ville ville;
    int index = -1;
    TextView tvVille, tvPays, tvVent, tvTemperature, tvPression, tvDate;
    Button buttonRefresh;
    String url = null;
    static StringRequest req;
    static RequestQueue queue = null;
    static ErrResp errReq = null;
    static ResponseListener respList = null;
    CityView refThis = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_view);
        refThis = this;
        if( queue == null)  queue = Volley.newRequestQueue( this );
        recupRefTextView();
        recupBundle();
        ecrireInfoVilleUI();
        majButton();

        //getWeather();

    }

    void recupRefTextView()
    {
        tvVille = (TextView) findViewById( R.id.ville );
        tvPays = (TextView) findViewById( R.id.pays );
        tvVent = (TextView) findViewById( R.id.Vent );
        tvTemperature = (TextView) findViewById( R.id.Temperature );
        tvPression = (TextView) findViewById( R.id.Pression );
        tvDate = (TextView) findViewById( R.id.Date );
        buttonRefresh = (Button) findViewById( R.id.button4 );

    }

    void majButton()
    {
        buttonRefresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                try {
                    getWeather();
                }catch (Exception e)
                {
                    Log.d("-------------------",e.toString() + " refreshTask initBouton\n");
                }
            }
        });
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
                index = (int)extras.get("positionn");
            }catch (Exception e)
            {
                System.out.println(e.toString() + "recupBundle");
            }
        }
    }

    void ecrireInfoVilleUI()
    {
        if( ville == null )return;
        tvVille.setText( "Ville     " + ville.getNomVille() );
        tvPays.setText( "Pays     " + ville.getPays()+"                        " );
        tvVent.setText( "Direction Vent     "  + ville.getDirectionVent() + "\nVitesse vent   " + ville.getVitesseVent() );
        tvTemperature.setText( "Temperature     "  + ville.getTemperature() );
        tvPression.setText( "Pression atmosphérique    "  + ville.getPressionAtmos() );
        tvDate.setText( "Date     "  + ville.getDateDerniereMaj() );
        url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" +
                ville.getNomVille() + "%2C%20fr%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    }




    public void getWeather()
    {
        if(null == errReq)errReq = new ErrResp();
        if(null == respList)respList = new ResponseListener();
        respList.giveRefVille( ville );
        respList.giveRefView( refThis );
        //req = new StringRequest(Request.Method.GET, url, respList, errReq );
        MainActivity.refreshTaskk.doInBackground( index );

        //queue.add( req );

    }

    public void majVille( Ville a )
    {
        ville = a;
    }
}

class ResponseListener implements Response.Listener<String>
{
    CityView ref = null;
    List<String> lstStr = null;
    Ville ville = null;
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
                sendMajVille(  );
                if( ref != null )
                {
                    Log.d("-------------------","  Ecrire ville-------------------------------------------------------------------\n");

                    ref.ecrireInfoVilleUI();
                    Log.d("-------------------","  Ecrire ville-------------------------------------------------------------------\n");

                    ref = null;
                    MainActivity.refreshTaskk.writeToast("Mise à jour Terminée");
                }
            }catch (Exception e)
            {
                System.out.println(e.toString() + "sendMaj");
            }

        }

    public List<String> getInfo()
    {
        return lstStr;
    }


    public void giveRefVille(Ville v)
    {
        ville = v;
    }

    public void giveRefView( CityView reff )
    {
        ref = reff;
    }

    public void sendMajVille()
    {
        if( ville == null )return;
        try
        {
            //List<String> recupInfo = respList.getInfo();


            int cmpt = 0;
            float temp = 0, vent = 0, pression = 0;
            String date = "", dirVent = "";
            for(String a  : lstStr)
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
            ville.configVille( ville.getNomVille(), ville.getPays(), date, vent, dirVent, pression, temp );
            ref.majVille( ville );

        }catch (Exception e)
        {
            Log.d("-------------------", e.toString() + "  Error-------------------------------------------------------------------\n");
        }

    }

};

class ErrResp implements Response.ErrorListener
{
        @Override
public void onErrorResponse(VolleyError volleyError)
        {

            MainActivity.refreshTaskk.writeToast("Problème requête");

}
};
