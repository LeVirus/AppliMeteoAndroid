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
            "vnd.android.cursor.item/vnd." + AUTHORITY + "." + QSLManager.strNomTable;

    QSLManager Bdd = null;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    final static int URI_BDD = 0, URI_VILLE_BDD = 1;


    public static final Uri CONTENT_URI = new Uri.Builder().scheme( ContentResolver.SCHEME_CONTENT )
            .authority( AUTHORITY )
            .appendEncodedPath( QSLManager.strNomTable )
            .build();

    static
    {
        sUriMatcher.addURI("com.example.app.provider", "BDDMeteo", URI_BDD);
        sUriMatcher.addURI("com.example.app.provider", "BDDMeteo/", URI_VILLE_BDD);

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
        long id = Bdd.getWritableDatabase().insert( QSLManager.strNomTable,    QSLManager.CHAMP_TABLE[ QSLManager.INTEGER_PRIMARY_KEY ],    values );
        if ( id > -1 ) {
            Uri uri = ContentUris.withAppendedId( CONTENT_URI, id );
            getContext().getContentResolver().notifyChange( uri, null );
            return uri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        int count = Bdd.getWritableDatabase().delete(QSLManager.strNomTable, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri)
    {
        if (sUriMatcher.match(uri) == 0) {
            return(TYPE_DIR);
        }

        return(TYPE_ITEM);
    }

    @Override
    public Cursor query( Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder )
    {
        //int checkUri = sUriMatcher.match( uri );

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables( QSLManager.strNomTable );

        Cursor c = builder.query(Bdd.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        int count = Bdd.getWritableDatabase().update(QSLManager.strNomTable, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }



    }
