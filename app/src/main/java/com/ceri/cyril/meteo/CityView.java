package com.ceri.cyril.meteo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CityView extends AppCompatActivity {

    Ville ville;
    TextView tvVille, tvPays, tvVent, tvTemperature, tvPression, tvDate;
    //TextView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_view);
        recupRefTextView();
        recupBundle();
        ecrireInfoVille();

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

    }
}
