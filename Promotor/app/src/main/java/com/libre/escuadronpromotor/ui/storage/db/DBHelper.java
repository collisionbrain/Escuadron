package com.libre.escuadronpromotor.ui.storage.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.libre.escuadronpromotor.ui.pojos.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ProBook on 19/04/2016.
 */
public class DBHelper   extends SQLiteOpenHelper {
    public static final String nombreDB = "registroDB.db";

    public DBHelper(Context context)
    {
        super(context, nombreDB, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TablesManager tablesManager=new TablesManager(db);
        tablesManager.createTables();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }


    public long insertMiembros(Member member){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vCampus = new ContentValues();
        vCampus.put("IdCampus", member.name);

        long res=db.insert("Campus", null, vCampus);
        db.close();
        return res;
    }


    public int countItems(String tabla){
        SQLiteDatabase db = this.getWritableDatabase();
        String QRY="SELECT * FROM "+tabla;
        Cursor cursor= db.rawQuery(QRY,null);

        int val=cursor.getCount();
        db.close();
        return val;
    }

    public List<String> getAll(){
        List<String> listaEscuelas = new ArrayList<String>();
        listaEscuelas.add("Selecciona tu escuela");
        SQLiteDatabase db = this.getWritableDatabase();
        String QRY="SELECT  Escuela FROM Escuelas";
        Cursor crEscuelas= db.rawQuery(QRY, null);

        if (crEscuelas != null) {
            if (crEscuelas.moveToFirst()) {
                do{

                    listaEscuelas.add(crEscuelas.getString(0));

                }while(crEscuelas.moveToNext());
            }
        }
        crEscuelas.close();
        return listaEscuelas;
    }
}
