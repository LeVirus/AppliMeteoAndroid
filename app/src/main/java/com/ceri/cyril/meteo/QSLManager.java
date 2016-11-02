package com.ceri.cyril.meteo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by cyril on 31/10/16.
 */



public class QSLManager extends SQLiteOpenHelper
{
    int muiNombreElementTable;
     static int NOM_VILLE = 0,
            PAYS = 1,
            DATE_DERNIER_RELEVE = 2,
            TEMPERATURE = 3,
            VITESSE_VENT = 4,
            DIRECTION_VENT = 5,
            PRESSION_ATMOS = 6,
            CLE_PRIMAIRE = 7,
            TEXT = 0,
            INTEGER_PRIMARY_KEY = 1       ;

    public static final String CHAMP_TABLE[] = { "nomVille", "pays", "dateDernierReleve", "temperature", "vitesseVent", "pressionAtmos", "clePrimaire" };
    String TYPE_CHAMP[] = { " TEXT", " INTEGER_PRIMARY_KEY"};


    private static SQLiteDatabase sqliteDb;
    //private static DatabaseHelper instance;
    private static final int DATABASE_VERSION = 1;

    public final static String strNomTable = "BDDMeteo";
    static ArrayList<Ville> listVille = new ArrayList<>();

    public QSLManager( Context context )
    {
        super(context, strNomTable, null, DATABASE_VERSION );
        synchroSQLTab();
    }



    @Override
    public void onCreate( SQLiteDatabase sqLiteDatabase )
    {
        sqliteDb = sqLiteDatabase;
        createTable( sqLiteDatabase );
        synchroSQLTab();
    }

    void createTable( SQLiteDatabase sqLiteDatabase )
    {
        sqLiteDatabase.execSQL( "CREATE TABLE IF NOT EXISTS " + strNomTable +
                "(" +
                CHAMP_TABLE[ NOM_VILLE ] + TYPE_CHAMP[ TEXT ] + "," +
                CHAMP_TABLE[ PAYS ] + TYPE_CHAMP[ TEXT ] + "," +
                CHAMP_TABLE[ DATE_DERNIER_RELEVE ] + TYPE_CHAMP[ TEXT ] + "," +
                CHAMP_TABLE[ TEMPERATURE ] + TYPE_CHAMP[ TEXT ] + "," +
                CHAMP_TABLE[ VITESSE_VENT ] + TYPE_CHAMP[ TEXT ] + "," +
                CHAMP_TABLE[ DIRECTION_VENT ] + TYPE_CHAMP[ TEXT ] + "," +
                CHAMP_TABLE[ PRESSION_ATMOS ] + TYPE_CHAMP[ TEXT ] + "," +
                CHAMP_TABLE[ CLE_PRIMAIRE ] + TYPE_CHAMP[ INTEGER_PRIMARY_KEY ] + "," + //cle primaire
                    "UNIQUE " +
                    "(" +
                    CHAMP_TABLE[ NOM_VILLE ] + "," +
                    CHAMP_TABLE[ PAYS ] +
                    ")" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        if ( oldVersion != newVersion ) {
            sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + strNomTable );
            onCreate( sqLiteDatabase );

        }
    }

    public ArrayList<Ville> getAllCities()
    {
        return listVille;
    }

    /**
     * Ajout d'une ligne représentant une ligne dans la table.
     * @param nomVille Le nom de la ville à ajouter.
     * @param nomPays Le nom du pays à ajouter.
     * @return true si la ligne a été ajoutée avec succès, false sinon.
     */
    public boolean ajoutVille( String nomVille, String nomPays )
    {
        boolean granted ;
        synchroSQLTab();

        SQLiteDatabase table = getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put( CHAMP_TABLE[ NOM_VILLE ] , nomVille);
        values.put( CHAMP_TABLE[ PAYS ] , nomPays);

        long current = table.insert( strNomTable, null, values);

        granted = ( muiNombreElementTable < ( int )current );
        if( granted )
        {
            listVille.add( new Ville( nomVille, nomPays, "dd",0.0f,"dd",0.0f,0.0f ) );
            muiNombreElementTable++;
        }

        return granted;

    }

    /**
     * Récupération des données concernant les villes stockées dans la table SQL.
     */
    void synchroSQLTab()
    {
        float temp = 100000, vit = 100000, atmos = 100000;
        SQLiteDatabase table = getWritableDatabase();


            if (null == listVille) listVille = new ArrayList<>();
            else listVille.clear();

            Cursor curs = table.rawQuery("SELECT * FROM " + strNomTable, null);

            muiNombreElementTable = curs.getCount();

            //startManagingCursor(curs);
            while ( curs.moveToNext() )
            {
                Log.d( "Synchro ::------------ ", curs.getString( NOM_VILLE ) + curs.getString( PAYS ) + "-------------------------------------");

                try
                {
                     temp = Float.parseFloat( curs.getString( TEMPERATURE ) );
                     vit = Float.parseFloat( curs.getString( VITESSE_VENT ) );
                     atmos = Float.parseFloat( curs.getString( PRESSION_ATMOS ) );
                }catch (Exception e)
                {

                }


                Ville v = new Ville(curs.getString( NOM_VILLE ), curs.getString( PAYS ), curs.getString( DATE_DERNIER_RELEVE ),
                        vit, curs.getString( DATE_DERNIER_RELEVE ), atmos, temp );

                Log.d( "Synchro ::------------ ", curs.getString( NOM_VILLE ) + curs.getString( PAYS ) + "-------------------------------------");

                //Ville( String nomVille, String pays, String dateDernierReleve,        float vitesseVent, String directionVent, float pressionAtmos, float temperature )
                listVille.add( v );

            }

    }

    public boolean supprVille( int indexVille )
    {
        SQLiteDatabase mMemTable = getWritableDatabase();

        boolean granted ;
        mMemTable = getWritableDatabase();

        //String where = CHAMP_TABLE[ INTEGER_PRIMARY_KEY ] + "=" + indexVille ;
        String where = CHAMP_TABLE[ NOM_VILLE ] + "=? AND " + CHAMP_TABLE[ PAYS ] + "=?";


        if( listVille.size() < indexVille )return false;
        //long current = mMemTable.delete( strNomTable, where, null);
        long current = mMemTable.delete(strNomTable, where, new String[]
                {
                listVille.get( indexVille ).getNomVille(),
                listVille.get( indexVille ).getPays() } );

        granted = ( muiNombreElementTable > ( int )current );
        if( granted )
        {
            //int i = getIndexTabVille( nomVille, nomPays );
            if( current <= 0/*indexVille > -1*/ )listVille.remove( indexVille );
            else {
                Log.d("---------------ERROR", "incohérence tableau Ville Table SQL\n");
                return false;
            }
            muiNombreElementTable--;
        }
        return granted;
    }

    /**
     * Renvoie l'index de la ville dont les paramètres correspondent au nom de la ville et du pays.
     * @return le numéro de l'index de la ville si cette dernière existe, -1 sinon.
     */
    int getIndexTabVille( String nomVille, String nomPays )
    {
        Ville v;
        for(int i = 0; i < listVille.size(); ++i)
        {
            v = listVille.get( i );
            if( v.getNomVille().equals( nomVille ) &&
                v.getPays().equals( nomPays ) )return i;
        }
        return -1;
    }


    public boolean isTableExists(String tableName, boolean openDb)
    {
        if(openDb) {
            if(sqliteDb == null || !sqliteDb.isOpen()) {
                sqliteDb = getReadableDatabase();
            }

            if(!sqliteDb.isReadOnly()) {
                sqliteDb.close();
                sqliteDb = getReadableDatabase();
            }
        }

        Cursor cursor = sqliteDb.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

}
