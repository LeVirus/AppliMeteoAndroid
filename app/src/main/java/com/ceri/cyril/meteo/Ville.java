package com.ceri.cyril.meteo;

import java.io.Serializable;

/**
 * Created by cyril on 25/09/16.
 */
public class Ville implements Serializable{
    private String mNomVille, mPays;
    private int mVitesseVent, mDirectionVent, mPressionAtmos, mDateDernierReleve;
    private float mTemperature;

    public Ville()
    {

    }

    public final String getNomVille()
    {
        return mNomVille;
    }

    public final String getPays()
    {
        return mPays;
    }

    public Ville( String nomVille, String pays, int dateDernierReleve, int vitesseVent, int directionVent, int pressionAtmos, float temperature )
    {
        configVille( nomVille, pays, dateDernierReleve, vitesseVent, directionVent, pressionAtmos, temperature );
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

    public final void afficherVille()
    {
        System.out.print( "  \n" + mNomVille + "  \n" + mPays + "  \n");
    }
}
