package com.example.andres.OdontoTime.Modelo;

import android.widget.Toast;

/**
 * Created by Andres on 30/04/2018.
 */

public class MyReceiver extends android.content.BroadcastReceiver {

    @Override
    public void onReceive(android.content.Context context, android.content.Intent intent) {
        Toast.makeText(context, "Es hora de tu medicina", Toast.LENGTH_LONG).show();
    }
}
