package com.libre.escuadronpromotor.ui.storage.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.libre.escuadronpromotor.ui.pojos.Address;
import com.libre.escuadronpromotor.ui.pojos.Delivery;
import com.libre.escuadronpromotor.ui.pojos.Member;
import com.libre.escuadronpromotor.ui.pojos.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ProBook on 19/04/2016.
 */
public class DBHelper   extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL("CREATE TABLE IF NOT EXISTS  Miembros(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "imei TEXT, " +
                "name TEXT, " +
                "mail TEXT, " +
                "phone TEXT, " +
                "whats TEXT, " +
                "privacy TEXT, " +
                "gender TEXT, " +
                "birthday TEXT, " +
                "weigth TEXT, " +
                "suffering TEXT, " +
                "condition TEXT, " +
                "extra TEXT, " +
                "signature TEXT, " +
                "street TEXT, " +
                "numExt TEXT, " +
                "numInt TEXT, " +
                "postal TEXT, " +
                "col TEXT, " +
                "mun TEXT, " +
                "est TEXT, " +
                "idfront TEXT, " +
                "idback TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS  Orders(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_key TEXT, " +
                "user_name TEXT, " +
                "user_idb64 TEXT, " +
                "delivery_date TEXT, " +
                "products TEXT, " +
                "total TEXT)");
    }



    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + "Miembros");
        db.execSQL("DROP TABLE IF EXISTS " + "Orders");
        // Create tables again
        onCreate(db);
    }
    public long insertOrder(Delivery delivery) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vOrder = new ContentValues();
        vOrder.put("user_key", ""+delivery.user_key);
        vOrder.put("user_name", ""+delivery.user_name);
        vOrder.put("user_idb64", ""+delivery.user_idb64);
        vOrder.put("delivery_date", ""+delivery.delivery_date);
        vOrder.put("products", ""+delivery.products);
        vOrder.put("total", ""+delivery.total);
        long id = db.insert("Orders", null, vOrder);
        db.close();
        return id;
    }
    public long insertMember(Member member) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues vMember = new ContentValues();

        vMember.put("imei", member.imei);
        vMember.put("name", member.name);
        vMember.put("mail", member.mail);
        vMember.put("phone", member.phone);
        vMember.put("whats", member.whats);
        vMember.put("privacy", member.privacy);
        vMember.put("gender", member.gender);
        vMember.put("birthday", member.birthday);
        vMember.put("suffering", member.suffering);
        vMember.put("condition", member.condition);
        vMember.put("extra", member.extra);
        vMember.put("signature", member.signature);
        vMember.put("street", member.address.street);
        vMember.put("numExt", member.address.numExt);
        vMember.put("numInt", member.address.numInt);
        vMember.put("postal", member.address.postal);
        vMember.put("col", member.address.col);
        vMember.put("mun", member.address.mun);
        vMember.put("est", member.address.est);
        vMember.put("idfront", member.b64FrontId);
        vMember.put("idback", member.b64BackId);

        long id = db.insert("Miembros", null, vMember);
        db.close();
        return id;
    }
    public Delivery getOrder(String user_key) {
        String selectQuery = "SELECT  * FROM Orders WHERE user_key='"+user_key+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int c=cursor.getCount() ;
        Delivery delivery = new Delivery();
        if (cursor.getCount() > 0){
            if (cursor.moveToFirst()) {
                do {


                    delivery.user_key=cursor.getString(cursor.getColumnIndex("user_key"));
                    delivery.user_name=cursor.getString(cursor.getColumnIndex("user_name"));
                    delivery.user_idb64=cursor.getString(cursor.getColumnIndex("user_idb64"));
                    delivery.products=cursor.getString(cursor.getColumnIndex("products"));
                    delivery.delivery_date=cursor.getString(cursor.getColumnIndex("delivery_date"));
                    delivery.total=cursor.getString(cursor.getColumnIndex("total"));

                } while (cursor.moveToNext());
            }
        }
        db.close();
        return delivery;
    }
    public List<Member> getAllMembers() {
        List<Member> memberList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM Miembros ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        int c=cursor.getCount() ;
        if (cursor.getCount() > 0){
            if (cursor.moveToFirst()) {
                do {

                    Member member = new Member();
                    member.imei = cursor.getString(cursor.getColumnIndex("imei"));
                    member.name = cursor.getString(cursor.getColumnIndex("name"));
                    member.mail = cursor.getString(cursor.getColumnIndex("mail"));
                    member.phone = cursor.getString(cursor.getColumnIndex("phone"));
                    member.whats = Boolean.getBoolean(cursor.getString(cursor.getColumnIndex("whats")));
                    member.privacy = Boolean.getBoolean(cursor.getString(cursor.getColumnIndex("privacy")));
                    Address address = new Address();
                    address.street = cursor.getString(cursor.getColumnIndex("street"));
                    address.numExt = cursor.getString(cursor.getColumnIndex("numExt"));
                    address.numInt = cursor.getString(cursor.getColumnIndex("numInt"));
                    address.postal = cursor.getString(cursor.getColumnIndex("postal"));
                    address.col = cursor.getString(cursor.getColumnIndex("col"));
                    address.mun = cursor.getString(cursor.getColumnIndex("mun"));
                    address.est = cursor.getString(cursor.getColumnIndex("est"));
                    member.address = address;
                    member.gender = cursor.getInt(cursor.getColumnIndex("gender"));
                    member.birthday = cursor.getString(cursor.getColumnIndex("birthday"));
                    member.weigth = cursor.getInt(cursor.getColumnIndex("weigth"));
                    member.gender = cursor.getInt(cursor.getColumnIndex("gender"));
                    member.suffering = Boolean.getBoolean(cursor.getString(cursor.getColumnIndex("suffering")));
                    member.condition = cursor.getString(cursor.getColumnIndex("condition"));
                    member.extra = cursor.getString(cursor.getColumnIndex("extra"));
                    member.signature = cursor.getString(cursor.getColumnIndex("signature"));
                    member.b64FrontId = cursor.getString(cursor.getColumnIndex("idfront"));
                    member.b64BackId = cursor.getString(cursor.getColumnIndex("idback"));

                    memberList.add(member);
                } while (cursor.moveToNext());
            }
    }
        db.close();
        return memberList;
    }

    public void deleteMemberRegister(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Miembros","mail=?",new String[]{email});
        db.close();
    }
}