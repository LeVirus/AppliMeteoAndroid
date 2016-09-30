package com.ceri.cyril.meteo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
{
    ArrayList< Ville > mTabVille = null;
    ListView listeVille = null;
    MainActivity mRefMainAct = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entrerValDefaut();
        initialiserVueListeVille();
        mRefMainAct = this;
    }

    /**
     * Fonction initialisant le tableau de villes à des valeurs par défaut.
     */
    void entrerValDefaut()
    {
        if( mTabVille == null )
            mTabVille = new ArrayList< Ville >();
        else
            mTabVille.clear();
        //String nomVille, String pays, int dateDernierReleve, int vitesseVent, int directionVent, int pressionAtmos, float temperature
        mTabVille.add(new Ville( "Paris", "France", 0,0,0,0,0.0f ));
        mTabVille.add(new Ville( "Brest", "France", 0,0,0,0,0.0f ));
    }


    /**
     * Fonction récupérant la "ListView" présente dans le fichier XML et y ajoute les villes
     * du tableau.
     */
    void initialiserVueListeVille()
    {
        ArrayAdapter<String> listAdapter ;
        //Récupération de la ListView
        listeVille = (ListView) findViewById(R.id.listView);
        ArrayList< String > strTab = new ArrayList< String >();
        //Ajouter les villes graphiquement
        for( Ville a : mTabVille ) {
            strTab.add(a.getNomVille() + "\n" + a.getPays());
            System.out.println(a.getNomVille() + a.getPays());
            //TextView tv = new TextView(this);
            //tv.setText(a.getNomVille() + "\n" + a.getPays());
        }
            try{

                // Create ArrayAdapter using the planet list.
                listAdapter = new ArrayAdapter<String>(this, R.layout.activity_main, R.id.debug , strTab);

                // Set the ArrayAdapter as the ListView's adapter.
                listeVille.setAdapter( listAdapter );

                //listeVille.addView( tv );

            }catch (Exception e)
            {
System.out.print( e.toString() );
            }

        initClickListenerListeView();
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

                Intent myIntent = new Intent(mRefMainAct, CityView.class);
                myIntent.putExtra("Ville", mTabVille);
                myIntent.putExtra("int", position);
                try
                {
                    startActivity( myIntent );
                }catch (Exception e)
                {
                    System.out.print( e.toString() );
                }

                //startActivity(new Intent(MainActivity.this, CityView.class));

                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
