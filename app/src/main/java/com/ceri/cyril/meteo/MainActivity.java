package com.ceri.cyril.meteo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * protected void onCreate(Bundle savedInstanceState)
 * public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
 * public boolean onContextItemSelected(MenuItem item)
 * void entrerValDefaut()
 * void initialiserVueListeVille()
 * void initClickListenerListeView()
 * void initLongClickListenerListeView()
 * void setItemToDelete( int i )
 * final Ville getConstVille( int positionVille )
 */

public class MainActivity extends AppCompatActivity implements Serializable
{
    int villeSelect = -1, itemToDelete = -1;
    ArrayList< Ville > mTabVille = null;
    ListView listeVille = null;
    MainActivity mRefMainAct = null;
    ArrayAdapter<String> listAdapter ;
    Button bouton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entrerValDefaut();
        initialiserVueListeVille();
        initBouton();
        mRefMainAct = this;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Meteo");
        menu.add(0, v.getId(), 0, "Supprimer élément");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if (item.getTitle() == "Supprimer élément") {
            listAdapter.remove( listAdapter.getItem( itemToDelete ) );
            listAdapter.notifyDataSetChanged();
            mTabVille.remove( itemToDelete );
            Toast.makeText(this, "Elémént supprimé" , Toast.LENGTH_SHORT).show();
        }
        else {
            return false;
        }
        return true;
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
        //Récupération de la ListView
        listeVille = (ListView) findViewById(R.id.listView);
        registerForContextMenu( listeVille );


        ArrayList< String > strTab = new ArrayList< String >();
        //Ajouter les villes graphiquement
        for( Ville a : mTabVille )
        {
            strTab.add(a.getNomVille() + "\n" + a.getPays());
            System.out.println(a.getNomVille() + a.getPays());
        }
            try
            {
                listAdapter = new ArrayAdapter<String>( this, R.layout.activity_main, R.id.debug , strTab );
                // Set the ArrayAdapter as the ListView's adapter.
                listeVille.setAdapter( listAdapter );

            }catch (Exception e)
            {
                System.out.print( e.toString() );
            }

        initClickListenerListeView();
        initLongClickListenerListeView();
    }

    /**
     * Initialisation du bouton permettant l'ouverture du formulaire d'ajout de villes.
     */
    void initBouton()
    {
        bouton = (Button) findViewById(R.id.button);
        bouton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity( new Intent( mRefMainAct, AddCityActivity.class ) );
            }
        });
    }

    /**
     *  Initialisation des ClickListener sur chaque entrée de la ListView.
     *  Un long click fera apparaitre un menu contextuel qui permettra de supprimer un élément.
     */
    void initClickListenerListeView()
    {
        listeVille.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text

                Intent myIntent = new Intent(mRefMainAct, CityView.class);
                myIntent.putExtra("ville", mTabVille.get(position));
                //myIntent.putExtra("int", position);
                try
                {
                    villeSelect = position;
                    System.out.print("startAct\n");
                    startActivity( myIntent );


                //startActivity(new Intent(MainActivity.this, CityView.class));

                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();

                }catch (Exception e)
                {
                    System.out.print( e.toString() );
                }
            }
        });

    }


    /**
     *  Initialisation des LongClickListener sur chaque entrée de la ListView.
     */
    void initLongClickListenerListeView()
    {
        listeVille.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id)
            {
                setItemToDelete( pos );
                mRefMainAct.openContextMenu( arg0 );

                return true;
            }
        });
    }

    /**
     * Fonction appelée aprés un long click sur un élément de la ListView.
     * Mémorisation de la position de l'objet à supprimer dans la ListView.
     * @param i Position de l'élément.
     */
    void setItemToDelete( int i )
    {
        itemToDelete = i;
    }

    /**
     * Fonction envoyant la ville dont la position est envoyé en paramètre.
     * @param positionVille La position(dans le tableau) de la ville à récupérer.
     * @return Une référence de la ville ou NULL en cas de paramètre éroné.
     */
    final Ville getConstVille( int positionVille )
    {
        if( positionVille < 0 || positionVille > mTabVille.size() )
        {
            return null;
        }
        return mTabVille.get( positionVille );
    }

}
