package com.ceri.cyril.meteo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PreferenceActivity extends AppCompatActivity {

    String directionVent, vitesseVent, temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
    }
}
