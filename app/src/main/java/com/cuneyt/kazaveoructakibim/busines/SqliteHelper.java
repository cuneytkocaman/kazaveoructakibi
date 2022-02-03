package com.cuneyt.kazaveoructakibim.busines;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.cuneyt.kazaveoructakibim.dataAccess.SqliteInfo;

public class SqliteHelper extends SQLiteOpenHelper {

    private Context context;

    public SqliteHelper(@Nullable Context context) {
        super(context, SqliteInfo.DB_NAME, null, SqliteInfo.VERSION);
        this.context = context;
    }

    //  TABLO OLUŞTURMA
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SqliteInfo.CREATE_SABAH);
        sqLiteDatabase.execSQL(SqliteInfo.CREATE_OGLE);
        sqLiteDatabase.execSQL(SqliteInfo.CREATE_IKINDI);
        sqLiteDatabase.execSQL(SqliteInfo.CREATE_AKSAM);
        sqLiteDatabase.execSQL(SqliteInfo.CREATE_YATSI);
        sqLiteDatabase.execSQL(SqliteInfo.CREATE_ORUC);
    }

    //  YAPISAL DEĞİŞİKLİK OLDUĞUNDA ESKİYİ SİL YENİSİNİ OLUŞTUR.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SqliteInfo.DROP_SABAH);
        sqLiteDatabase.execSQL(SqliteInfo.DROP_OGLE);
        sqLiteDatabase.execSQL(SqliteInfo.DROP_IKINDI);
        sqLiteDatabase.execSQL(SqliteInfo.DROP_AKSAM);
        sqLiteDatabase.execSQL(SqliteInfo.DROP_YATSI);
        sqLiteDatabase.execSQL(SqliteInfo.DROP_ORUC);

        onCreate(sqLiteDatabase);
    }

    // CRUD İŞLEMLERİ ----------------------------------------------------------------------------------------------
    // EKLEME (INSERT)
    public void addData(String id, String date, String vakit, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SqliteInfo.ID, id);
        values.put(SqliteInfo.DATE, date);
        values.put(SqliteInfo.VAKIT, vakit);

        long result = db.insert(tableName, null, values);
    }

    //  GÜNCELLEME (UPDATE)
    public void updateData(String row_id, String date, String vakit, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqliteInfo.DATE, date);
        values.put(SqliteInfo.VAKIT, vakit);

        long result = db.update(tableName, values, SqliteInfo.ID + " =? ", new String[]{row_id});
    }

    //  LİSTELEME (SELECT)
    public Cursor readData(String querySelect) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(querySelect, null);
        return c;
    }

    // SİLME
    public void deleteData(String row_id, String tableName){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(tableName, SqliteInfo.ID + " =? ", new String[]{row_id});

        /*if (result == -1){
            Toast.makeText(context, "Veriler silinemedi", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Bütün veri silindi.", Toast.LENGTH_SHORT).show();
        }*/
    }
}