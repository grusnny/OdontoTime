package com.example.andres.OdontoTime.Modelo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.CalendarContract;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

/**
 * Created by Juan David on 22/03/2018.
 */

public class Utilidades {



     public static final String TABLA_CITA ="cita";
     public static final String CAMPO_NOMBRE="nombre";
    public static final String CAMPO_TIEMPO="serie";
    public static final String CAMPO_TIPOLOGIA="tipologia";

    public static final String CREAR_TABLA_CITA= "CREATE TABLE "+ TABLA_CITA +"("+CAMPO_NOMBRE+" TEXT, "+CAMPO_TIEMPO+" TEXT, "+CAMPO_TIPOLOGIA+" TEXT)";

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

}
