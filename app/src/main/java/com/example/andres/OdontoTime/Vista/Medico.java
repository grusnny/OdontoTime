package com.example.andres.OdontoTime.Vista;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.andres.OdontoTime.Modelo.Cita;
import com.example.andres.OdontoTime.Controlador.DBCitas;
import com.example.andres.OdontoTime.Controlador.FirebaseReferences;
import com.example.andres.OdontoTime.Modelo.Calendario;
import com.example.andres.firebase.R;
import com.example.andres.OdontoTime.Modelo.Utilidades;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Medico extends AppCompatActivity implements View.OnClickListener{


    Spinner comboMedicamentos;
    ArrayList<String>listaMedicamentos;
    ArrayList<Cita> citaLista;
    TextView nombre;
    EditText identificacion;
    TextView correo,dia,mes,año,hora,time_1,time,dia_1,mes_1,año_1,fecha1;
    Spinner mySpin;
    String nom,ide,spin,cor,id;
    Button buscar,guardar,fecha,horab;
    DBCitas conex;
    Context context=this;
    String identificador;
    String userId;
    Boolean ehora=false,efecha=false, espin=false, euser=false;
    public static final int REQUEST_CODE=10;

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    private String tiempo;


    public final Calendar c = Calendar.getInstance();
    final int hora2 = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    String vHora;
    String vMinuto;
    String vTiempo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico);
        comboMedicamentos = (Spinner)findViewById(R.id.spinner_medicamentos);
        nombre = (TextView)findViewById(R.id.nombre);
        dia_1=(TextView)findViewById(R.id.textViewDia1);
        dia=(TextView)findViewById(R.id.textViewDia);
        mes_1=(TextView)findViewById(R.id.textViewMes1);
        mes=(TextView)findViewById(R.id.textViewMes);
        año_1=(TextView)findViewById(R.id.textViewAño1);
        año=(TextView)findViewById(R.id.textViewAño);
        time_1=(TextView)findViewById(R.id.textViewHora1);
        time=(TextView)findViewById(R.id.textViewHora);
        fecha1=(TextView)findViewById(R.id.textViewFecha);
        identificacion = (EditText)findViewById(R.id.editText_documento);
        correo = (TextView)findViewById(R.id.correo);
        mySpin=(Spinner) findViewById(R.id.spinner_medicamentos);
        guardar=(Button) findViewById(R.id.button_enviarPres);
        horab=(Button) findViewById(R.id.hora);
        buscar=(Button) findViewById(R.id.buscar_usuario);
        fecha=(Button) findViewById(R.id.fecha);
        hora=(TextView)findViewById(R.id.textViewHora);
        buscar.setOnClickListener(this);
        fecha.setOnClickListener(this);
        horab.setOnClickListener(this);
        mySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                if(adapterView.getItemAtPosition(pos).toString().compareTo("seleccionar")!=0){
                    espin=true;
                    if(ehora==true && efecha==true && espin==true && euser==true){
                        guardar.setEnabled(true);
                    }
                }
                else{
                    guardar.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {    }
        });
        conex = new DBCitas(getApplicationContext(),"DBCitas",null,1);
        consultarListaMedicamento();
        ArrayAdapter<CharSequence> adaptador=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,listaMedicamentos);
        comboMedicamentos.setAdapter(adaptador);

    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.buscar_usuario){
            FirebaseDatabase bd = FirebaseDatabase.getInstance();
            final DatabaseReference usuarioRef = bd.getReference(FirebaseReferences.USUARIO_REFERENCES);
            usuarioRef.child("USUARIOS").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final List<String> usuarios = new ArrayList<String>();
                    try {
                        int contador=0;
                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                            contador=contador+1;
                            userId=areaSnapshot.getKey();
                            int areaName = areaSnapshot.child("documento").getValue(int.class);
                            if (areaName == Integer.parseInt(identificacion.getText().toString())) {
                                nombre.setText(areaSnapshot.child("nombre").getValue(String.class));
                                correo.setText(areaSnapshot.child("email").getValue(String.class));
                                identificador=areaSnapshot.child("identificador").getValue(String.class);
                                buscar.setEnabled(false);
                                Toast.makeText(context, "¡Usuario encontrado!", Toast.LENGTH_SHORT).show();
                                euser=true;
                                if(ehora==true && efecha==true && espin==true && euser==true){
                                    guardar.setEnabled(true);
                                }
                                break;
                                }
                            if(dataSnapshot.getChildrenCount()==contador) {
                                Toast.makeText(context, "¡Usuario no registrado!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    catch(Exception e){
                        Toast.makeText(context, "¡Algo va mal!", Toast.LENGTH_SHORT).show();
                   }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context, "¡Usuario no registrado!", Toast.LENGTH_SHORT).show();
                }
            });
        }else if(view.getId()==R.id.hora){
           obtenerHora();
           ehora=true;
            if(ehora==true && efecha==true && espin==true && euser==true){
                guardar.setEnabled(true);
            }
        }

        else{
            Intent intent= new Intent(getApplication(),Calendario.class);
            startActivityForResult(intent,REQUEST_CODE);
            efecha=true;
            if(ehora==true && efecha==true && espin==true && euser==true){
                guardar.setEnabled(true);
            }
        }
    }

    private void obtenerHora(){
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String horaFormateada =  (hourOfDay < 9)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 9)? String.valueOf(CERO + minute):String.valueOf(minute);

                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                horab.setVisibility(View.INVISIBLE);
                time.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                time.setVisibility(View.VISIBLE);
                time_1.setVisibility(View.VISIBLE);
                vHora=horaFormateada;
                vMinuto=minutoFormateado;
                vTiempo=AM_PM;



            }

        }, hora2, minuto, false);

        recogerHora.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK && requestCode==REQUEST_CODE){

            if(data.hasExtra("dia")){
                Log.d("VALORD", String.valueOf(data.getExtras().getInt("mes")));
                fecha1.setVisibility(View.VISIBLE);
                dia_1.setVisibility(View.VISIBLE);
                dia.setText(String.valueOf(data.getExtras().getInt("dia")));
                dia.setVisibility(View.VISIBLE);
                mes_1.setVisibility(View.VISIBLE);
                mes.setText(String.valueOf(data.getExtras().getInt("mes")));
                mes.setVisibility(View.VISIBLE);
                año_1.setVisibility(View.VISIBLE);
                año.setText(String.valueOf(data.getExtras().getInt("año")));
                año.setVisibility(View.VISIBLE);
                fecha.setVisibility(View.INVISIBLE);
            }

        }
    }

    private void consultarListaMedicamento() {
        SQLiteDatabase db=conex.getReadableDatabase();
        Cita farmaco=null;
        citaLista = new ArrayList<Cita>();
        Cursor curso = db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_CITA,null);

        while(curso.moveToNext()){
            farmaco= new Cita();
            farmaco.setNombre(curso.getString(0));
            farmaco.setSerie(curso.getString(1));
            farmaco.setTipologia(curso.getString(2));


            citaLista.add(farmaco);
        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaMedicamentos= new ArrayList<String>();
        listaMedicamentos.add("seleccionar");

        for (int i = 0; i< citaLista.size(); i++){
            listaMedicamentos.add(citaLista.get(i).getNombre()+" - "+ citaLista.get(i).getSerie()+" - "+ citaLista.get(i).getTipologia());

        }
    }

    // Método que pasa de activity y evia las variables cargadas de informacion
    public void envio(View vista1){
        FirebaseDatabase bd = FirebaseDatabase.getInstance();
        DatabaseReference usuarioRef = bd.getReference(FirebaseReferences.USUARIO_REFERENCES);

        Intent go =null;
        switch (vista1.getId()){
            case R.id.button_enviarPres:
                go = new Intent(this, EnvioPrescipcion.class);
                nom = nombre.getText().toString();
                ide = identificacion.getText().toString();
                Date fecha = new Date(Integer.parseInt(año.getText().toString()),
                        Integer.parseInt(mes.getText().toString()),
                        Integer.parseInt(dia.getText().toString()),Integer.parseInt(vHora),Integer.parseInt(vMinuto),0);
                spin = mySpin.getSelectedItem().toString();
                cor = correo.getText().toString();
                id=identificador;
                go.putExtra("first",nom);
                go.putExtra("second",ide);
                go.putExtra("third",fecha);
                go.putExtra("fifth",spin);
                go.putExtra("sixth",cor);
                go.putExtra("seventh",id);
                go.putExtra("hora",vHora);
                go.putExtra("minuto",vMinuto);
                go.putExtra("vtiempo",vTiempo);
                break;
        }
        if (go != null) {
            startActivity(go);
        }
    }

}