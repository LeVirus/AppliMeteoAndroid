package com.ceri.cyril.meteo;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import static javax.xml.transform.OutputKeys.VERSION;

/**
 * Created by cyril on 02/11/16.
 */

public class MeteoContentProvider extends ContentProvider
{
    public static final String AUTHORITY = "com.ceri.cyril.meteo.provider";

    public static final String TYPE_DIR =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + QSLManager.strNomTable;
    public static final String TYPE_ITEM =
            "vnd.android.cursor.item/vnd." + AUTHORITY + "." + QSLManager.strNomTable + "/*/*";

    QSLManager Bdd = null;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    final static int URI_BDD = 0,
            URI_VILLE_BDD = 1,
                    VILLE_SEGMENT = 1,
            PAYS_SEGMENT = 2;


    public static final Uri CONTENT_URI = new Uri.Builder().scheme( ContentResolver.SCHEME_CONTENT )
            .authority( AUTHORITY )
            .appendEncodedPath( QSLManager.strNomTable )
            .build();

    static
    {
        sUriMatcher.addURI( AUTHORITY, "BDDMeteo", URI_BDD);
        sUriMatcher.addURI( AUTHORITY, "BDDMeteo/*/*", URI_VILLE_BDD);

    }

    public static Uri getUriVille( String ville, String pays )
    {
        return CONTENT_URI.buildUpon().appendPath( pays ).appendPath( ville ).build();
    }

    @Override
    public boolean onCreate()
    {
        Bdd = new QSLManager( getContext() );

        return true;
    }

    @Override
    public Uri insert(Uri url, ContentValues values)
    {
        Log.d("sdfgfgdf", "xdddddddddddddddddddddddd");

        if ( sUriMatcher.match(url) != URI_VILLE_BDD )return null;
        //long id = Bdd.getWritableDatabase().insert( QSLManager.strNomTable,    QSLManager.CHAMP_TABLE[ QSLManager.INTEGER_PRIMARY_KEY ],    values );
        List< String > pathSegments = url.getPathSegments();
        String pays = pathSegments.get(PAYS_SEGMENT);
        String ville = pathSegments.get(VILLE_SEGMENT);
        boolean b = Bdd.ajoutVille( pays, ville );
        if ( b ) return getUriVille(  ville,  pays );
        else         Log.d("sdfgfgdf", "buuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuug");

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        if (sUriMatcher.match(uri) != URI_VILLE_BDD)return -1;

        List< String > pathSegments = uri.getPathSegments();
        String pays = pathSegments.get(PAYS_SEGMENT);
        String ville = pathSegments.get(VILLE_SEGMENT);
     Bdd.supprVilleS(pays, ville);
        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }

    @Override
    public String getType(Uri uri)
    {
        if (sUriMatcher.match(uri) == 0) {
            return TYPE_DIR;
        }

        return TYPE_ITEM;
    }

    @Override
    public Cursor query( Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder )
    {
        //int checkUri = sUriMatcher.match( uri );

/*        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables( QSLManager.strNomTable );



        Cursor c = builder.query(Bdd.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;*/

        int match = sUriMatcher.match(uri);

        Cursor result = null;

        switch (match)
        {
            case URI_BDD:
                result = Bdd.getAllCitiesCurs();
                break;
            case URI_VILLE_BDD:
                List<String> pathSegments = uri.getPathSegments();
                String country = pathSegments.get(PAYS_SEGMENT);
                String name = pathSegments.get(VILLE_SEGMENT);

                result = Bdd.getCityCurs(country, name);
                break;
            default:
                return  Bdd.getAllCitiesCurs();
        }

        result.setNotificationUri(getContext().getContentResolver(), uri);

        return result;

    }



    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        int count = Bdd.getWritableDatabase().update(QSLManager.strNomTable, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }



    }
