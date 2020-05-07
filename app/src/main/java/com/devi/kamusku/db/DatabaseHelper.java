package com.devi.kamusku.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.devi.kamusku.db.DatabaseContract.KamusColumns.ARTI;
import static com.devi.kamusku.db.DatabaseContract.KamusColumns.KATA;
import static com.devi.kamusku.db.DatabaseContract.TABLE_ENG;
import static com.devi.kamusku.db.DatabaseContract.TABLE_INDO;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static String DATABASE_NAME = "dbKamus";

    private static final int DATABASE_VERSION = 1;

    public static final String CREATE_TABLE_INDO = "create table "+
            TABLE_INDO + " ("+_ID+" integer primary key autoincrement, " +
            KATA + " text not null, " +
            ARTI + " text not null);";

    public static final String CREATE_TABLE_ENG = "create table "+
            TABLE_ENG + " ("+_ID+" integer primary key autoincrement, " +
            KATA + " text not null, " +
            ARTI + " text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_INDO);
        db.execSQL(CREATE_TABLE_ENG);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_INDO);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_ENG);
        onCreate(db);
    }
}
