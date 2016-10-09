package com.ceri.cyril.meteo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList;


/**
 * protected void onCreate(Bundle savedInstanceState)
 * public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
 * public boolean onContextItemSelected(MenuItem item)
 * void entrerValDefaut()
 * void rafraichirVueListeVille()
 * void initClickListenerListeView()
 * void initLongClickListenerListeView()
 * void setItemToDelete( int i )
 * final Ville getConstVille( int positionVille )
 */

public class MainActivity extends AppCompatActivity implements Serializable
{
    int villeSelect = -1, itemToDelete = -1;
    ArrayList<Ville> mTabVille = null;
    ListView listeVille = null;
    MainActivity mRefMainAct = null;
    ArrayAdapter<String> listAdapter;
    Button bouton = null, boutonRaf = null;
    String ville = "", pays = "";
    RefreshTask refreshTaskk = null;
    JSONResponseHandler jsonResp = null;
    RefreshTask refreshTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listeVille = (ListView) findViewById(R.id.listView);
        refreshTask = new RefreshTask();
        if( Ville.refJsonResp == null)
            Ville.refJsonResp = new JSONResponseHandler();
        if( refreshTaskk == null )
        {
            refreshTaskk = new RefreshTask();
            refreshTaskk.memTabVille( mTabVille );
        }

        entrerValDefaut();
        initBouton();
        rafraichirVueListeVille();
        mRefMainAct = this;

        Log.d("-------------------","Application -------------------------------------------------------------------\n");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if( ajoutVille( AddCityActivity.ville, AddCityActivity.pays ) )
        {
            rafraichirVueListeVille();
        }
        AddCityActivity.ville = "";
        AddCityActivity.pays = "";
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
        ajoutVille( "Paris", "France" );
        ajoutVille( "Brest", "France" );
    }

    /**
     * Fonction d'ajout d'une ville dans le tableau.
     * @param nomVille La ville à ajouter
     * @param nomPays La ville à ajouter
     * @return true si les paramètres sont corrects, false sinon.
     */
    public boolean ajoutVille( String nomVille, String nomPays )
    {
        if( null == nomVille || null == nomVille ||
                "".equals( nomVille ) || "".equals( nomPays ) )
        {
            return false;
        }
        Ville v = new Ville( nomVille, nomPays, "dd",0.0f,"dd",0.0f,0.0f );
        v.memRefJsonResp( jsonResp );
        mTabVille.add( v );
        return true;
    }


    /**
     * Fonction récupérant la "ListView" présente dans le fichier XML et y ajoute les villes
     * du tableau.
     */
    public void rafraichirVueListeVille()
    {

        registerForContextMenu( listeVille );


        ArrayList< String > strTab = new ArrayList<  >();
        //Ajouter les villes graphiquement
        for( Ville a : mTabVille )
        {
            strTab.add(a.getNomVille() + "\n" + a.getPays());
        }
            try
            {
                listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strTab);
                // Set the ArrayAdapter as the ListView's adapter.
                listeVille.setAdapter( listAdapter );

            }catch (Exception e)
            {
                Log.d("-------------------",e.toString() + " -------------------------------------------------------------------\n");
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
                try {
                    Intent intent = new Intent( mRefMainAct, AddCityActivity.class );
                    intent.putExtra("ville",  ville);
                    intent.putExtra("pays",  pays);
                    startActivity( intent );
                    //ville = ""; pays = "";
                }catch (Exception e)
                {
                    Log.d("-------------------",e.toString() + " initBouton\n");
                }
            }
        });

        boutonRaf = (Button) findViewById(R.id.button3);
        boutonRaf.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    refreshTask.doInBackground();
                }catch (Exception e)
                {
                    Log.d("-------------------",e.toString() + " initBouton\n");
                }
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
                    startActivity( myIntent );


                //startActivity(new Intent(MainActivity.this, CityView.class));

                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();

                }catch (Exception e)
                {
                    Log.d("-------------------",e.toString() + " setOnItemClickListener\n");
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

    final void afficherTabVille()
    {
        for( Ville a : mTabVille )
        {
            Log.d("-------------------",a.getNomVille() + " afficherTabVille\n");

        }
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
