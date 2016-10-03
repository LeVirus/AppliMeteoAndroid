package com.ceri.cyril.meteo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CityView extends AppCompatActivity {

    Ville ville;
    TextView tvVille;
    TextView tvPays;

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
    }

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
        tvVille.setText( ville.getNomVille() );
        tvPays.setText( ville.getPays() );
    }
}
