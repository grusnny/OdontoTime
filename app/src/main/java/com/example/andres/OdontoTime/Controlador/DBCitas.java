package com.example.andres.OdontoTime.Controlador;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.andres.OdontoTime.Modelo.Utilidades;
//import com.example.juandavid.farmatempo.utilidades.Utilidades;

/**
 * Created by Juan David on 11/03/2018.
 */

public class DBCitas extends SQLiteOpenHelper {

    //metodo constructor de la base de datos
    public DBCitas(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //metodo que crea la tabla en la base de datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREAR_TABLA_CITA);
    }

    //metodo que actualiza la base de datos, cada que hay un cambio en la tabla
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterios, int versionReciente) {
        db.execSQL("DROP TABLE IF EXISTS citas");
        onCreate(db);
    }
}
