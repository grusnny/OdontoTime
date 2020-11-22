package com.example.andres.OdontoTime.Controlador;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBCalendario extends SQLiteOpenHelper {

    private String sql="create table eventos("+
            "idEvento integer primary key autoincrement,"+
            "nombreEvento varchar(40),"+
            "ubicacion varchar(50),"+
            "fecha date,"+
            "horaDesde time,"+
            "horaHasta time,"+
            "descripcion varchar(60))";

    public DBCalendario(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
