package com.ceri.cyril.meteo;

import java.io.Serializable;

/**
 * Created by cyril on 25/09/16.
 */
public class Ville implements Serializable{
    private String mNomVille, mPays, mDateDernierReleve, mDirectionVent,  strUnitTemp = "", strUnitDirVent = "", strUnitVitVent = "";
    private float mVitesseVent, mPressionAtmos, mTemperature;
    static JSONResponseHandler refJsonResp;

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

    public final String getDateDerniereMaj()
    {
        return mDateDernierReleve;
    }

    public final String getDirectionVent()
    {
        return mDirectionVent;
    }

    public final float getVitesseVent()
    {
        return mVitesseVent;
    }

    public final float getPressionAtmos()
    {
        return mPressionAtmos;
    }

    public final float getTemperature()
    {
        return mTemperature;
    }


    public Ville( String nomVille, String pays, String dateDernierReleve, float vitesseVent, String directionVent, float pressionAtmos, float temperature )
    {
        configVille( nomVille, pays, dateDernierReleve, vitesseVent, directionVent, pressionAtmos, temperature );
    }

    public void configVille( String nomVille, String pays, String dateDernierReleve, float vitesseVent, String directionVent, float pressionAtmos, float temperature )
    {
        mNomVille = nomVille;
        mPays = pays;
        mVitesseVent = vitesseVent;
        mDirectionVent = directionVent;
        mPressionAtmos = pressionAtmos;
        mDateDernierReleve = dateDernierReleve;
        mTemperature = temperature;
    }

    public void configUnitVille( String temp, String dirVent, String vitVent )
    {
        strUnitTemp = temp;
        strUnitDirVent = dirVent;
        strUnitVitVent = vitVent;
    }


    public String getTempUnit()
    {
        return strUnitTemp;
    }

    public String getDirVentUnit()
    {
        return strUnitDirVent;
    }

    public String getVitVentUnit()
    {
        return strUnitVitVent;
    }

    /**
     * Mémorisation de la référence de JSONResponseHandler(objet unique).
     * @param ref La référence en question.
     */
    public void memRefJsonResp( JSONResponseHandler ref )
    {
        refJsonResp = ref;
    }

    public JSONResponseHandler getJson()
    {
        return refJsonResp;
    }

    public final void afficherVille()
    {
        System.out.print( " AFFICHAGE VILLE \n" + mNomVille + "  \n" + mPays + "  \n" + mVitesseVent  +
                "  \n" + mDirectionVent  + "  \n" + mPressionAtmos  + "  \n" + mDateDernierReleve  + "  \n" + mTemperature  + "  \n FIN AFFICHAGE VILLE\n");
    }
}
