package com.example.andres.OdontoTime.Modelo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

import com.example.andres.firebase.R;

public class Calendario extends AppCompatActivity implements CalendarView.OnDateChangeListener{
    private CalendarView calendarView;
    private int dia,mes,año;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        calendarView=(CalendarView)findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence []items= new CharSequence[3];
        items[0]="Seleccionar";
        items[1]="Ver citas";
        items[2]="Cancelar";
        dia=dayOfMonth;
        mes=month+1;
        año=year;

        builder.setTitle("¿Que desea hacer?")
        .setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    //tomar datos
                    finish();


                }else if(which==1){
                    Intent intent =new Intent(getApplication(),View_citas.class);
                    Bundle bundle =new Bundle();
                    bundle.putInt("dia",dia);
                    bundle.putInt("mes",mes);
                    bundle.putInt("año",año);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    return; //cerrar builder
                }
            }
        });
        AlertDialog dialog =builder.create();
        dialog.show();
    }

    @Override
    public void finish() {
        Intent data =new Intent();
        Bundle bundle =new Bundle();
        bundle.putInt("dia",dia);
        bundle.putInt("mes",mes);
        bundle.putInt("año",año);
        data.putExtras(bundle);
        setResult(RESULT_OK,data);
        super.finish();

    }

}


