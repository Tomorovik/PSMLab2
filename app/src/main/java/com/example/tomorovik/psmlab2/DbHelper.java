package com.example.tomorovik.psmlab2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tomorovik on 06.04.2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public final static int DB_NUMBER = 1;
    public final static String ID = "_id";
    public final static String DB_NAME = "baza_telefonow";
    public final static String TABLE_NAME = "telefony";
    public final static String COL1 = "producent";
    public final static String COL2 = "model";
    public final static String COL3 = "wersja_androida";
    public final static String COL4 = "www";
    public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " integer primary key autoincrement, " +
            COL1 + " text not null, " +
            COL2 + " text not null, " +
            COL3 + " NUMERIC not null, " +
            COL4 + " text not null);";

    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
