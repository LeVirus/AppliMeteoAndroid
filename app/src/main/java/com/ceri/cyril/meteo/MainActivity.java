package com.ceri.cyril.meteo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList< Ville > mTabVille;
    ListView listeVille = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entrerValDefaut();
        initialiserVueListeVille();
    }

    /**
     * Fonction initialisant le tableau de villes à des valeurs par défaut.
     */
    void entrerValDefaut()
    {
        mTabVille.clear();
        //String nomVille, String pays, int dateDernierReleve, int vitesseVent, int directionVent, int pressionAtmos, float temperature
        mTabVille.add(new Ville( "Paris", "France", 0,0,0,0,0.0f ));
        mTabVille.add(new Ville( "Brest", "France", 0,0,0,0,0.0f ));
    }

    /**
     *  Initialisation des ClickListener sur chaque entré de la ListView.
     */
    void initClickListenerListeView()
    {
        listeVille.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Fonction récupérant la "ListView" présente dans le fichier XML et y ajoute les villes
     * du tableau.
     */
    void initialiserVueListeVille()
    {
        //Récupération de la ListView
        listeVille = (ListView) findViewById(R.id.listView);
        //Ajouter les villes graphiquement
        for( Ville a : mTabVille )
        {
            TextView tv = new TextView( this );
            tv.setText( a.getNomVille() + "\n" + a.getPays() );
            listeVille.addFooterView( tv );
        }
        initClickListenerListeView();
    }
}
