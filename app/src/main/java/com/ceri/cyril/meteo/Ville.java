package com.ceri.cyril.meteo;

/**
 * Created by cyril on 25/09/16.
 */
public class Ville {
    private String mNomVille, mPays;
    private int mVitesseVent, mDirectionVent, mPressionAtmos, mDateDernierReleve;
    private float mTemperature;

    public Ville()
    {

    }

    public Ville( String nomVille, String pays, int dateDernierReleve, int vitesseVent, int directionVent, int pressionAtmos, float temperature )
    {
        configVille( nomVille, mPays, dateDernierReleve, vitesseVent, directionVent, pressionAtmos, temperature );
    }

    public void configVille( String nomVille, String pays, int dateDernierReleve, int vitesseVent, int directionVent, int pressionAtmos, float temperature )
    {
        mNomVille = nomVille;
        mPays = pays;
        mVitesseVent = vitesseVent;
        mDirectionVent = directionVent;
        mPressionAtmos = pressionAtmos;
        mDateDernierReleve = dateDernierReleve;
        mTemperature = temperature;
    }
}
