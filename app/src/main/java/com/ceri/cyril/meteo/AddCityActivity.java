package com.ceri.cyril.meteo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddCityActivity extends AppCompatActivity {

    Button validBouton = null;
    EditText etVille = null, etPays = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        initElementXML();
    }

    /**
     * Initialisation du bouton permettant la validation du formulaire.
     */
    void initElementXML()
    {
        etVille = ( EditText ) findViewById( R.id.editText );
        etPays = ( EditText ) findViewById( R.id.editText2 );
        etVille.setText("");
        etPays.setText("");
        validBouton = (Button) findViewById( R.id.button2 );
        validBouton.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if( etVille.getText().equals(new String("")) )Toast.makeText(getApplicationContext(),
                        "super:" + etVille.getText() + ":   :" + etPays.getText()+":", Toast.LENGTH_SHORT).show();
                else Toast.makeText(getApplicationContext(),
                        "tri:" + etVille.getText() + ":   :" + etPays.getText()+":", Toast.LENGTH_SHORT).show();
                if( etPays.getText().equals(new String("")) )Toast.makeText(getApplicationContext(),
                        "super:" + etVille.getText() + ":   :" + etPays.getText()+":", Toast.LENGTH_SHORT).show();
                else Toast.makeText(getApplicationContext(),
                        "tri:" + etVille.getText() + ":   :" + etPays.getText()+":", Toast.LENGTH_SHORT).show();

                boolean granted = ( ! etVille.getText().equals(new String(""))  && ! etPays.getText().equals(new String("")) );
                if(! granted) Toast.makeText(getApplicationContext(),
                        "Erreur veuillez compléter le formulaire", Toast.LENGTH_SHORT).show();
                else
                {
                    //Toast.makeText(getApplicationContext(),
                    //"super:" + etVille.getText() + ":   :" + etPays.getText()+":", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
