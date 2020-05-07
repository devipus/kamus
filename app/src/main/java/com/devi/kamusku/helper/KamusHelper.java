package com.devi.kamusku.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.devi.kamusku.db.DatabaseHelper;
import com.devi.kamusku.model.KamusModel;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.devi.kamusku.db.DatabaseContract.KamusColumns.ARTI;
import static com.devi.kamusku.db.DatabaseContract.KamusColumns.KATA;
import static com.devi.kamusku.db.DatabaseContract.TABLE_ENG;
import static com.devi.kamusku.db.DatabaseContract.TABLE_INDO;

public class KamusHelper {

    private Context context;
    private DatabaseHelper databaseHelper;

    private SQLiteDatabase database;

    public KamusHelper(Context context) {
        this.context = context;
    }

    public KamusHelper open() throws SQLException{
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        databaseHelper.close();
    }

    public Cursor searchQueryByName(String query, String options){
        String table = null;
        if (options == "indo"){
            table = TABLE_INDO;
        } else {
            table = TABLE_ENG;
        }

        return database.rawQuery("SELECT * FROM " + table +
        " WHERE "+ KATA + " LIKE '%" + query.trim() + "%'", null);
    }

    public ArrayList<KamusModel> getDataByName(String cari, String options){


        ArrayList<KamusModel> arrayList = new ArrayList<>();
        KamusModel kamusModel;

        Cursor cursor = searchQueryByName(cari, options);

        cursor.moveToFirst();

        if (cursor.getCount()>0){
            do {
                kamusModel = new KamusModel();
                kamusModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                kamusModel.setKata(cursor.getString(cursor.getColumnIndexOrThrow(KATA)));
                kamusModel.setArti(cursor.getString(cursor.getColumnIndexOrThrow(ARTI)));

                arrayList.add(kamusModel);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public String getData(String cari, String options){
        String result = "";
        Cursor cursor = searchQueryByName(cari, options);
        cursor.moveToFirst();
        if (cursor.getCount() > 0){
            result = cursor.getString(2);
            for (; !cursor.isAfterLast(); cursor.moveToNext()){
                result = cursor.getString(2);
            }
        }
        cursor.close();
        return result;
    }

    public Cursor queryAllData(String options){
        String table = null;
        if (options == "indo"){
            table = TABLE_INDO;
        } else {
            table = TABLE_ENG;
        }

        return database.rawQuery("SELECT * FROM "+ table + " ORDER BY "+ _ID + " ASC", null);
    }

    public ArrayList<KamusModel> getAllData(String options){


        ArrayList<KamusModel> arrayList = new ArrayList<>();
        KamusModel kamusModel;

        Cursor cursor = queryAllData(options);
        cursor.moveToFirst();

        if (cursor.getCount()>0){
            do {
                kamusModel = new KamusModel();
                kamusModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                kamusModel.setKata(cursor.getString(cursor.getColumnIndexOrThrow(KATA)));
                kamusModel.setArti(cursor.getString(cursor.getColumnIndexOrThrow(ARTI)));

                arrayList.add(kamusModel);
                cursor.moveToNext();
            } while(!cursor.isAfterLast());
        }

        cursor.close();
        return arrayList;

    }


    public void beginTransaction(){
        database.beginTransaction();
    }

    public void setTransactionSuccess(){
        database.setTransactionSuccessful();
    }

    public void insertTransaction(KamusModel kamusModel, String options){

        String tb = null;
        if (options == "indo"){
             tb = TABLE_INDO;
        } else {
            tb = TABLE_ENG;
        }
        String sql = "INSERT INTO "+tb+" ("+KATA+", "+ARTI+") VALUES (?, ?)";
        SQLiteStatement stmt = database.compileStatement(sql);
        stmt.bindString(1, kamusModel.getKata());
        stmt.bindString(2, kamusModel.getArti());
        stmt.execute();
        stmt.clearBindings();
    }

    public void endTransaction() {
        database.endTransaction();
    }

}
