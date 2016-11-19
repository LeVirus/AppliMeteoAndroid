package com.ceri.cyril.meteo;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.FloatProperty;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.util.List;


public class CityView extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{

    Ville ville;
    int index = -1;
    TextView tvVille, tvPays, tvVent, tvTemperature, tvPression, tvDate;
    Button buttonRefresh;
    String url = null;
    static StringRequest req;
    static RequestQueue queue = null;
    static ErrResp errReq = null;
    static ResponseListener respList = null;
    static MainActivity refMainAct;
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
        getWeather();

        majButton();


    }

    void launchLoader()
    {
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(MeteoContentProvider.AUTHORITY);
        //account = intent.getParcelableExtra(CityListActivity.ACCOUNT);

        Bundle bundle = new Bundle();
        bundle.putParcelable(MeteoContentProvider.AUTHORITY, uri);
        getLoaderManager().initLoader(1, bundle, this);

    }

    /**
     * Récupération des objets UI en provenance du fichier XML associé.
     */
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

    static void giveRef( MainActivity main )
    {
        refMainAct = main;
    }

    /**
     * Traitement de l'appui du bouton Raffraichir.
     */
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

    /**
     * Initialisation de la présentation graphique de la ville affichée et préparation de l'URL à envoyer au serveur Yahoo.
     */
    void ecrireInfoVilleUI()
    {
        String[] str = {QSLManager.CHAMP_TABLE[ QSLManager.NOM_VILLE], QSLManager.CHAMP_TABLE[ QSLManager.PAYS],
                QSLManager.CHAMP_TABLE[ QSLManager.VITESSE_VENT], QSLManager.CHAMP_TABLE[ QSLManager.DIRECTION_VENT], QSLManager.CHAMP_TABLE[ QSLManager.TEMPERATURE]
        , QSLManager.CHAMP_TABLE[ QSLManager.PRESSION_ATMOS], QSLManager.CHAMP_TABLE[ QSLManager.DATE_DERNIER_RELEVE] };

        String unitDirectVent = "", unitTemp = " °c", unitvitVent = " mph";


        SharedPreferences sharedPref = CityView.refMainAct.getApplicationContext().getSharedPreferences("Preferences", 0);
        int spinA = sharedPref.getInt( "spinDirVent", 0 ),
                spinB = sharedPref.getInt( "spinVitVent", 0 ),
                spinC = sharedPref.getInt( "spinTemp", 0 );
        //convertir cardinaux en degré
        if(spinA == 1)
        {
            unitDirectVent = " °";
        }
        //vit vent
        if(spinB == 1)
        {
            unitvitVent = " km/h";
        }
        //température
        if(spinC == 1)
        {
            unitTemp = " f";
        }


        /*Cursor cursor = refMainAct.getContentResolver().query( MeteoContentProvider.getUriVille( ville.getNomVille() , ville.getPays() ),
                str, null, null, null);

        if( cursor == null )return;
        cursor.moveToFirst();

        try {
            tvVille.setText( "Ville     " + cursor.getString(cursor.getColumnIndex(QSLManager.CHAMP_TABLE[QSLManager.NOM_VILLE])) );
            tvPays.setText( "Pays     " + cursor.getString(cursor.getColumnIndex(QSLManager.CHAMP_TABLE[QSLManager.PAYS])));
            tvVent.setText("Vitesse vent     "+cursor.getString(cursor.getColumnIndex(QSLManager.CHAMP_TABLE[QSLManager.VITESSE_VENT])) +
                    "\nDirection Vent     "  + cursor.getString(cursor.getColumnIndex(QSLManager.CHAMP_TABLE[QSLManager.DIRECTION_VENT])));
            tvTemperature.setText( "Temperature       "  + cursor.getString(cursor.getColumnIndex(QSLManager.CHAMP_TABLE[QSLManager.TEMPERATURE])));
            tvPression.setText( "Pression atmosphérique    "  + cursor.getString(cursor.getColumnIndex(QSLManager.CHAMP_TABLE[QSLManager.PRESSION_ATMOS])));
            tvDate.setText( "Date     "  + cursor.getString(cursor.getColumnIndex(QSLManager.CHAMP_TABLE[QSLManager.DATE_DERNIER_RELEVE])));
        }catch (Exception e)
        {
         Log.d("---------------------", e.toString() );
        }*/



        if( ville == null )return;
        tvVille.setText( "Ville     " + ville.getNomVille() );
        tvPays.setText( "Pays     " + ville.getPays()+"                        " );
        tvVent.setText( "Direction Vent     "  + ville.getDirectionVent() + unitDirectVent + "\nVitesse vent   " + ville.getVitesseVent() + unitvitVent);
        tvTemperature.setText( "Temperature     "  + ville.getTemperature() + unitTemp );
        tvPression.setText( "Pression atmosphérique    "  + ville.getPressionAtmos() );
        tvDate.setText( "Date     "  + ville.getDateDerniereMaj() );
        url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" +
                ville.getNomVille() + "%2C%20fr%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    }




    /**
     * Envoi de la requète au serveur météo, aprés instanciation évantuelles des objets nécessaires
     * et envoie des références au Listener.
     */
    public void getWeather()
    {
        if(null == errReq)errReq = new ErrResp();
        if(null == respList)respList = new ResponseListener();
        respList.giveRefVille( ville );
        respList.giveRefView( refThis );
        //req = new StringRequest(Request.Method.GET, url, respList, errReq );
        MainActivity.refreshTaskk.doInBackground( index );
        //launchLoader();//a corriger

        //queue.add( req );

    }

    public void majVille( Ville a )
    {
        ville = a;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle)
    {
        return new CursorLoader(this, (Uri)bundle.getParcelable(MeteoContentProvider.AUTHORITY), null,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        /**
         * tvVille.setText( "Ville     " + ville.getNomVille() );
         tvPays.setText( "Pays     " + ville.getPays()+"                        " );
         tvVent.setText( "Direction Vent     "  + ville.getDirectionVent() + "\nVitesse vent   " + ville.getVitesseVent() );
         tvTemperature.setText( "Temperature     "  + ville.getTemperature() );
         tvPression.setText( "Pression atmosphérique    "  + ville.getPressionAtmos() );
         tvDate.setT
         */
        if (cursor != null) {
            cursor.moveToFirst();

            tvVille.setText( cursor.getString( cursor.getColumnIndex( QSLManager.CHAMP_TABLE[ QSLManager.NOM_VILLE] ) ) );

            tvPays.setText(cursor.getString(cursor.getColumnIndex( QSLManager.CHAMP_TABLE[ QSLManager.PAYS ] )));

            tvVent.setText( cursor.getString(cursor.getColumnIndex( QSLManager.CHAMP_TABLE[ QSLManager.VITESSE_VENT] )) +
            "  " + cursor.getString(cursor.getColumnIndex( QSLManager.CHAMP_TABLE[ QSLManager.DIRECTION_VENT] )) );

            tvTemperature.setText(cursor.getString(cursor.getColumnIndex( QSLManager.CHAMP_TABLE[ QSLManager.TEMPERATURE] )));

            tvPression.setText(cursor.getString(cursor.getColumnIndex( QSLManager.CHAMP_TABLE[ QSLManager.PRESSION_ATMOS] )));

            tvDate.setText(cursor.getString(cursor.getColumnIndex( QSLManager.CHAMP_TABLE[ QSLManager.DATE_DERNIER_RELEVE] )));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
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


    /**
     * Récupération de la référence de la ville à traiter.
     * @param v La référence vers la ville.
     */
    public void giveRefVille(Ville v)
    {
        ville = v;
    }

    /**
     * Récupération de la référence de la vue représentant la ville.
     * @param reff La référence vers la vue.
     */
    public void giveRefView( CityView reff )
    {
        ref = reff;
    }

    /**
     * Application de la mise à jour aprés extraction des données du callback.
     * Mise à jour de l'objet Ville et de la Vue.
     */
    public void sendMajVille()
    {
        if( ville == null )return;
        try
        {
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

            applyPref();

            majBdd();


            ref.majVille( ville );

        }catch (Exception e)
        {
            Log.d("-------------------", e.toString() + "  Error-------------------------------------------------------------------\n");
        }

    }

    void applyPref()
    {
        SharedPreferences sharedPref = CityView.refMainAct.getApplicationContext().getSharedPreferences("Preferences", 0);

        int spinA = sharedPref.getInt( "spinDirVent", 0 ),
                spinB = sharedPref.getInt( "spinVitVent", 0 ),
                spinC = sharedPref.getInt( "spinTemp", 0 );

        //valeur par défaut
        String dirVent = ville.getDirectionVent();
        float vitVent = ville.getVitesseVent(), temp = ville.getTemperature();

        //convertir cardinaux en degré
        if(spinA == 1)
        {
            String[] arrComp = {"(N)", "(NNE)", "(NE)", "(ENE)", "(E)", "(ESE)", "(SE)", "(SSE)", "(S)", "(SSW)", "(SW)", "(WSW)", "(W)", "(WNW)", "(NW)", "(NNW)"};
            int i;
            for( i = 0;i<arrComp.length;++i )
            {
                if(arrComp[i].equals(ville.getDirectionVent()))
                {
                    float a = 360.0f / 16.0f * i;
                    dirVent = String.valueOf((float)a);
                    break;
                }
            }
        }

        //vit vent
        if(spinB == 1)
        {
            vitVent = ville.getVitesseVent() * 1.609344f;
        }

        //température
        if(spinC == 1)
        {
            temp = ville.getTemperature() + 32.0f/(5.0f / 9.0f);
        }
        ville.configVille( ville.getNomVille(), ville.getPays(), ville.getDateDerniereMaj(), vitVent, dirVent, ville.getPressionAtmos(), temp );
    }

    void majBdd()
    {
        ContentValues values = new ContentValues();
        values.put( QSLManager.CHAMP_TABLE[ QSLManager.NOM_VILLE ] , ville.getNomVille() );
        values.put( QSLManager.CHAMP_TABLE[ QSLManager.PAYS ] , ville.getPays() );
        values.put( QSLManager.CHAMP_TABLE[ QSLManager.TEMPERATURE ] , "" + ville.getTemperature() );
        values.put( QSLManager.CHAMP_TABLE[ QSLManager.DIRECTION_VENT ] , ville.getDirectionVent() );
        values.put( QSLManager.CHAMP_TABLE[ QSLManager.VITESSE_VENT ] , "" + ville.getDirectionVent() );
        values.put( QSLManager.CHAMP_TABLE[ QSLManager.PRESSION_ATMOS ] , ville.getPressionAtmos() );
        CityView.refMainAct.uapdateBdd( values, ville.getNomVille(), ville.getPays() );


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
