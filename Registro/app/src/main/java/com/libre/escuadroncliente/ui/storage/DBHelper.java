package com.libre.escuadroncliente.ui.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.libre.escuadroncliente.ui.pojos.Entidad;
import com.libre.escuadroncliente.ui.pojos.MProduct;
import com.libre.escuadroncliente.ui.pojos.Product;

import java.util.ArrayList;
import java.util.List;

public class DBHelper   extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "products_db";
    public SQLiteDatabase db;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS  Products(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tipo TEXT, " +
                "nombre_corto TEXT, " +
                "presentacion TEXT, " +
                "sabor INTEGER, " +
                "cantidad TEXT, " +
                "precio TEXT, " +
                "image TEXT, " +
                "calificacion TEXT, " +
                "ingredientes TEXT)");


        db.execSQL("CREATE TABLE IF NOT EXISTS entidades (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "estado TEXT ," +
                "municipio TEXT," +
                "ciudad TEXT, " +
                "cp INTEGER)");
    }




    public void insertPostalCodes(List<Entidad> entidad){


        db.beginTransaction();
        ContentValues vEntidad = new ContentValues();
        for (Entidad enti: entidad
             ) {

            vEntidad.put("id", 0);
            vEntidad.put("municipio", ""+enti.municipio);
            vEntidad.put("estado", ""+enti.estado);
            vEntidad.put("ciudad", ""+enti.ciudad);
            vEntidad.put("cp", ""+enti.cp);
            db.insert("Entidades", null, vEntidad);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    public void insertProduct(List<MProduct> products) {


        db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues vProduct = new ContentValues();
        for (MProduct product: products
                ) {

            vProduct.put("tipo", ""+product.tipo);
            vProduct.put("nombre_corto", ""+product.nombre_corto);
            vProduct.put("presentacion", ""+product.presentacion);
            vProduct.put("sabor", ""+product.sabor);
            vProduct.put("cantidad", ""+product.cantidad);
            vProduct.put("precio", ""+product.precio);
            vProduct.put("image", ""+product.image);
            vProduct.put("calificacion", ""+product.calificacion);
            vProduct.put("ingredientes", ""+product.ingredientes);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();


    }
    public void deleteProducts(){
        db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM Products");
        db.close();
    }
    public void deleteEntidades(){
        db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM Entidades");
        db.close();
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + "Products");
        // Create tables again
        onCreate(db);
    }

    public List<MProduct> getProductsByType(String tipo) {
        List<MProduct> memberList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM Products WHERE tipo="+"'"+tipo+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0){
            if (cursor.moveToFirst()) {
                do {

                    MProduct mProduct = new MProduct();
                    mProduct.tipo = cursor.getString(cursor.getColumnIndex("tipo"));
                    mProduct.nombre_corto = cursor.getString(cursor.getColumnIndex("nombre_corto"));
                    mProduct.presentacion = cursor.getString(cursor.getColumnIndex("presentacion"));
                    mProduct.sabor = cursor.getString(cursor.getColumnIndex("sabor"));
                    mProduct.cantidad = cursor.getString(cursor.getColumnIndex("cantidad"));
                    mProduct.precio = cursor.getString(cursor.getColumnIndex("precio"));
                    mProduct.image = cursor.getString(cursor.getColumnIndex("image"));
                    mProduct.calificacion = cursor.getInt(cursor.getColumnIndex("calificacion"));
                    mProduct.ingredientes = cursor.getString(cursor.getColumnIndex("ingredientes"));



                } while (cursor.moveToNext());
            }
        }
        db.close();
        return memberList;
    }



}