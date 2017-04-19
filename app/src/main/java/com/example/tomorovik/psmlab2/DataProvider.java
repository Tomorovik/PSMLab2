package com.example.tomorovik.psmlab2;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Tomorovik on 06.04.2017.
 */

public class DataProvider extends ContentProvider{

    public DbHelper dbHelper;
    private static final String IDENTIFICATOR = "com.example.tomorovik.psmlab2.DataProvider";
    public static final Uri URI_CONTENT = Uri.parse("content://" + IDENTIFICATOR + "/" + DbHelper.TABLE_NAME);
    private static final int FULL_TABLE = 1;
    private static final int SELECTED_ROW = 2;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(IDENTIFICATOR, DbHelper.TABLE_NAME, FULL_TABLE);
        URI_MATCHER.addURI(IDENTIFICATOR, DbHelper.TABLE_NAME + "/#", SELECTED_ROW);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        switch (uriType) {
            case FULL_TABLE:
                cursor = db.query(false,DbHelper.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder,null,null);
                break;
            case SELECTED_ROW:
                cursor = db.query(false,DbHelper.TABLE_NAME,projection,SelectID(selection,uri),selectionArgs,null,null,sortOrder,null,null);
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private String SelectID(String selection, Uri uri) {
        if(selection != null && !selection.equals(""))
            selection = selection + " and " + DbHelper.ID + "=" + uri.getLastPathSegment();
        else
            selection = DbHelper.ID + "=" + uri.getLastPathSegment();
        return  selection;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long addedAmount = 0;
        switch (uriType) {
            case FULL_TABLE:
                addedAmount = db.insert(DbHelper.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(DbHelper.TABLE_NAME + "/" + addedAmount);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedAmount = 0;
        switch (uriType) {
            case FULL_TABLE:
                deletedAmount = db.delete(DbHelper.TABLE_NAME, selection, selectionArgs);
                break;
            case SELECTED_ROW:
                deletedAmount = db.delete(DbHelper.TABLE_NAME, SelectID(selection, uri), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedAmount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updatedAmount = 0;
        switch (uriType) {
            case FULL_TABLE:
                updatedAmount = db.update(DbHelper.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SELECTED_ROW:
                updatedAmount = db.update(DbHelper.TABLE_NAME, values, SelectID(selection, uri), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updatedAmount;
    }
}
