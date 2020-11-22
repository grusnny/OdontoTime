package com.example.andres.OdontoTime.Vista;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andres.OdontoTime.Controlador.DBCitas;
import com.example.andres.firebase.R;
import com.example.andres.OdontoTime.Modelo.Utilidades;
//import com.example.juandavid.farmatempo.utilidades.Utilidades;

public class PrescripcionMedica extends AppCompatActivity {

    EditText cpoNombre,cpoSerie,cpoTipo;

    DBCitas conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescripcion_medica);

        conexion =new DBCitas(getApplicationContext(),"DBCitas",null,1);

        cpoNombre=(EditText)findViewById(R.id.campoNombre);
        cpoSerie=(EditText)findViewById(R.id.campoSerie);
        cpoTipo=(EditText)findViewById(R.id.campoTipo);
    }

        public void onClick(View inolvidable){

            switch (inolvidable.getId()) {
                case R.id.button:
                    almacenarMedicamento();
                    break;
                case R.id.button2:
                    eliminar();
                    break;
                case R.id.button3:
                    consultar();
                    break;

            }
        }

    private void eliminar() {
        SQLiteDatabase db=conexion.getWritableDatabase();
        String[] parametros={cpoNombre.getText().toString()};

        db.delete(Utilidades.TABLA_CITA,Utilidades.CAMPO_NOMBRE+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Cita eliminado",Toast.LENGTH_LONG).show();
        cpoNombre.setText("");
        limpiar();
        db.close();

    }

    private void consultar() {
                SQLiteDatabase db=conexion.getReadableDatabase();
                String[] parametros={cpoNombre.getText().toString()};

                try {
                    //select nombre,telefono from usuario where codigo=?
                    Cursor cursor=db.rawQuery("SELECT "+Utilidades.CAMPO_TIEMPO+","+Utilidades.CAMPO_TIPOLOGIA+
                            " FROM "+Utilidades.TABLA_CITA +" WHERE "+Utilidades.CAMPO_NOMBRE+"=? ",parametros);

                    cursor.moveToFirst();
                    cpoSerie.setText(cursor.getString(0));
                    cpoTipo.setText(cursor.getString(1));

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Cita no existe",Toast.LENGTH_SHORT).show();
                    limpiar();
                }

            }
    private void limpiar() {
        cpoSerie.setText("");
        cpoTipo.setText("");
    }

    private void almacenarMedicamento() {
        DBCitas conex = new DBCitas(this, "DBCitas", null, 1);
        
        //abro la base de datos para poder editarla
        SQLiteDatabase datam = conex.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_NOMBRE,cpoNombre.getText().toString());
        values.put(Utilidades.CAMPO_TIEMPO,cpoSerie.getText().toString());
        values.put(Utilidades.CAMPO_TIPOLOGIA,cpoTipo.getText().toString());

        Long resultado = datam.insert(Utilidades.TABLA_CITA,Utilidades.CAMPO_NOMBRE,values);
        Toast.makeText(getApplicationContext(),"Cita Registrada: "+ resultado,Toast.LENGTH_SHORT).show();
        datam.close();

        Medico a=new Medico();

    }

}
