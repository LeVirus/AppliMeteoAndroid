package com.ceri.cyril.meteo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCityActivity extends AppCompatActivity {

    Button validBouton = null;
    EditText etVille = null, etPays = null;
    AddCityActivity act = this;
    static String ville , pays ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        initElementXML();
        ville = "";
        pays = "";
    }

    /**
     * Initialisation du bouton permettant la validation du formulaire.
     */
    void initElementXML()
    {
        ville = "";
        pays = "";
        etVille = ( EditText ) findViewById( R.id.editText );
        etPays = ( EditText ) findViewById( R.id.editText2 );
        validBouton = (Button) findViewById( R.id.button2 );
        validBouton.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //si les paramètres ne sont pas corrects
                if( "".equals( etVille.getText().toString() ) || "".equals( etPays.getText().toString() ) )
                {
                    Toast.makeText(getApplicationContext(), "Erreur veuillez compléter le formulaire", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ville = etVille.getText().toString();
                    pays = etPays.getText().toString();
                    Toast.makeText(getApplicationContext(), "Ok:" + etVille.getText() + ":   :" + etPays.getText()+":", Toast.LENGTH_SHORT).show();

                    act.finish();

                }
            }
        });
    }


}
