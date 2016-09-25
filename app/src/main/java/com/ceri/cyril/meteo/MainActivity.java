package com.ceri.cyril.meteo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList< Ville > mTabVille;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entrerValDefaut();
    }

    void entrerValDefaut()
    {
        mTabVille.clear();
        //String nomVille, String pays, int dateDernierReleve, int vitesseVent, int directionVent, int pressionAtmos, float temperature
        mTabVille.add(new Ville( "Paris", "France", 0,0,0,0,0.0f ));
        mTabVille.add(new Ville( "Brest", "France", 0,0,0,0,0.0f ));
    }

    void initialiserVueListeVille()
    {
        ListView listeVille = (ListView) findViewById(R.id.listView);
        //Ajouter les villes graphiquement
    }
}
