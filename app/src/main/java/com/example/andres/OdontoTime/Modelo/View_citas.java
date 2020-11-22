package com.example.andres.OdontoTime.Modelo;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.andres.OdontoTime.Controlador.DBCalendario;
import com.example.andres.firebase.R;

public class View_citas extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    private SQLiteDatabase db;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_citas);

        listView=(ListView)findViewById(R.id.listaEventos);
        listView.setOnItemLongClickListener(this);

        Bundle bundle=getIntent().getExtras();
        int dia,mes,año;
        dia=bundle.getInt("dia");
        mes=bundle.getInt("mes");
        año=bundle.getInt("año");
        String cadena= dia+" - "+ mes +" - "+año;
        DBCalendario bd =new DBCalendario(getApplicationContext(),"Agenda",null,1);
        db=bd.getReadableDatabase();


        String sql="select * from eventos where fecha='"+cadena+"'";
        Cursor c;
        String nombre,fecha,descripcion,ubicacion;
        try {
            c=db.rawQuery(sql,null);
            arrayAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
            if(c.moveToFirst()){
                do{
                    nombre=c.getString(1);
                    ubicacion=c.getString(2);
                    fecha=c.getString(3);
                    descripcion=c.getString(6);
                    arrayAdapter.add(nombre+", "+ubicacion+", "+fecha+", "+descripcion);
                    listView.setAdapter(arrayAdapter);
                }while(c.moveToNext());
            }else{
                this.finish();
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplication(),"Error: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
            this.finish();
        }


    }

    private void eliminar(String dato){
        String[]datos=dato.split(", ");
        String sql="delete from eventos where nombreEvento='"+datos[0]+"' and"+
                "  ubicacion='"+datos[1]+"' and fecha='"+datos[2]+"' and " +
                "descripcion='"+datos[3]+"'";
        try {
            arrayAdapter.remove(dato);
            listView.setAdapter(arrayAdapter);
            db.execSQL(sql);
            Toast.makeText(getApplication(),"Cita eliminada",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplication(),"Error: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        CharSequence[]items= new CharSequence[2];
        items[0]="Eliminar cita";
        items[1]="cancelar";
        builder.setTitle("Eliminar cita").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    //eliminar cita
                    eliminar(parent.getItemAtPosition(position).toString());
                }
                else{
                    return;
                }
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
        return false;
    }
}
