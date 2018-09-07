package com.libre.escuadronpromotor.ui.storage.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ProBook on 19/04/2016.
 */
public class TablesManager {

    private SQLiteDatabase db;
    TablesManager(SQLiteDatabase db){
        this.db=db;
    }

    public void createTables() {

        db.execSQL("CREATE TABLE IF NOT EXISTS  Usuarios(" +
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
                "est TEXT)");


    }


}
