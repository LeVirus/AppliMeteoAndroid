package com.ceri.cyril.meteo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class PreferenceActivity extends AppCompatActivity {

    private static Spinner spinDirVent, spinVitVent, spinTemp;
    Button button;
    static SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        initPrefConfig();
        loadPref();
    }

    void initPrefConfig()
    {
        button = (Button)findViewById( R.id.button );
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePref();
                //if (getIntent().getBooleanExtra("EXIT", false))
                //{
                    finish();
                //}
            }
        });
        spinDirVent = (Spinner) findViewById( R.id.spinnerDir );
        spinVitVent = (Spinner) findViewById( R.id.spinnerVit );
        spinTemp = (Spinner) findViewById( R.id.spinnerTemp );
        earnConf();
        sharedPrefEditor = sharedPref.edit();
    }

    void savePref()
    {
        sharedPrefEditor.putInt( "spinDirVent", spinDirVent.getSelectedItemPosition() );        // Saving integer
        sharedPrefEditor.putInt( "spinVitVent", spinVitVent.getSelectedItemPosition() );        // Saving integer
        sharedPrefEditor.putInt( "spinTemp", spinTemp.getSelectedItemPosition() );        // Saving integer


        // Save the changes in SharedPreferences
        sharedPrefEditor.commit(); // commit changes
    }

    void earnConf()
    {
        sharedPref = getApplicationContext().getSharedPreferences("Preferences", MODE_PRIVATE);
    }

    void loadPref()
    {

        int spinA = sharedPref.getInt( "spinDirVent", 0 ),
         spinB = sharedPref.getInt( "spinVitVent", 0 ),
         spinC = sharedPref.getInt( "spinTemp", 0 );

            if( spinDirVent.getCount() > spinA )spinDirVent.setSelection( spinA );
            if( spinVitVent.getCount() > spinB )spinVitVent.setSelection( spinB );
            if( spinTemp.getCount() > spinC )spinTemp.setSelection( spinC );

    }

    public static int getConfDirVent()
    {
        return sharedPref.getInt( "spinDirVent", 0 );
    }

    public static int getConfVitVent()
    {
        return sharedPref.getInt( "spinVitVent", 0 );

    }

    public static int getConftemp()
    {
        return sharedPref.getInt( "spinTemp", 0 );

    }

}
