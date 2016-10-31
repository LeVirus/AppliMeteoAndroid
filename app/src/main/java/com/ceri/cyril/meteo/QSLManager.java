package com.ceri.cyril.meteo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by cyril on 31/10/16.
 */



public class QSLManager extends SQLiteOpenHelper
{
    int muiNombreElementTable = 0;
    SQLiteDatabase mMemTable = null;
    final int NOM_VILLE = 0,
            PAYS = 1,
            DATE_DERNIER_RELEVE = 2,
            TEMPERATURE = 3,
            VITESSE_VENT = 4,
            PRESSION_ATMOS = 5,
            CLE_PRIMAIRE = 6,
            TEXT = 0,
            INTEGER_PRIMARY_KEY = 1       ;

    String CHAMP_TABLE[] = { "nomVille", "pays", "dateDernierReleve", "temperature", "vitesseVent", "pressionAtmos", "clePrimaire" };
    String TYPE_CHAMP[] = { "TEXT", "INTEGER_PRIMARY_KEY"};

    String strNomTable = "BDDMeteo";
    List< Ville > listVille;
    public QSLManager( Context context )
    {
        super(context, "BDDMeteo", null, 1 );
    }



    @Override
    public void onCreate( SQLiteDatabase sqLiteDatabase )
    {
        mMemTable = sqLiteDatabase;
        createTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        if ( oldVersion != newVersion ) {
            //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
            onCreate( sqLiteDatabase );
        }
    }

    public List< Ville > getAllCities()
    {
        return listVille;
    }

    /**
     * Création de la table avec tous les champs et les contraintes nécessaires.
     */
    void createTable()
    {
        mMemTable.execSQL( "CREATE TABLE IF NOT EXISTS " + strNomTable + "(" +
                CHAMP_TABLE[ NOM_VILLE ] + TYPE_CHAMP[ TEXT ] +
                CHAMP_TABLE[ PAYS ] + TYPE_CHAMP[ TEXT ] +
                CHAMP_TABLE[ DATE_DERNIER_RELEVE ] + TYPE_CHAMP[ TEXT ] +
                CHAMP_TABLE[ TEMPERATURE ] + TYPE_CHAMP[ TEXT ] +
                CHAMP_TABLE[ VITESSE_VENT ] + TYPE_CHAMP[ TEXT ] +
                CHAMP_TABLE[ PRESSION_ATMOS ] + TYPE_CHAMP[ TEXT ] +
                CHAMP_TABLE[ CLE_PRIMAIRE ] + TYPE_CHAMP[ INTEGER_PRIMARY_KEY ] + //cle primaire
                "UNIQUE (" +
                CHAMP_TABLE[ NOM_VILLE ] + "," +
                CHAMP_TABLE[ PAYS ] +
                ")" +
                ")"
        );
    }

    /**
     * Ajout d'une ligne représentant une ligne dans la table.
     * @param nomVille Le nom de la ville à ajouter.
     * @param nomPays Le nom du pays à ajouter.
     * @return true si la ligne a été ajoutée avec succès, false sinon.
     */
    public boolean ajoutLigneTable( String nomVille, String nomPays )
    {
        int mem = muiNombreElementTable;

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put( CHAMP_TABLE[ NOM_VILLE ] , nomVille);
        values.put( CHAMP_TABLE[ PAYS ] , nomPays);


        long current = mMemTable.insert(strNomTable, null, values);
        return mem < ( int )current;
    }

    public boolean supprLigneTable( String nomVille, String nomPays )
    {
        String where = CHAMP_TABLE[ NOM_VILLE ] + "=" + nomVille + " and " + CHAMP_TABLE[ PAYS ] + "=" + nomPays;
        mMemTable.delete( strNomTable, where, null);
        return true;
    }
}
