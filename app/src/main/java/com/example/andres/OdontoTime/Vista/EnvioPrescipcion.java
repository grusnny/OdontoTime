package com.example.andres.OdontoTime.Vista;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.andres.OdontoTime.Controlador.DBCalendario;
import com.example.andres.OdontoTime.Controlador.Save;
import com.example.andres.OdontoTime.Controlador.FirebaseReferences;
import com.example.andres.OdontoTime.Modelo.Utilidades;
import com.example.andres.firebase.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.io.File;
import java.util.Date;

public class EnvioPrescipcion extends AppCompatActivity {

    TextView prescripcionEs;
    Button enviar;
    ImageView ivQRCode;
    String NombreQR;
    String bitmapQR;
    String id;
    Date fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envio_prescipcion);
        recibePrescripcion();


        prescripcionEs=(TextView)findViewById(R.id.textView_prescipcionEscrita);
        enviar=(Button)findViewById(R.id.button_enviar);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        iniciliarComponentes();
        clickButton();

    }
    private void clickButton() {
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gerarQRCode();
                guardar();
                saveFecha();
                enviarCorreo(prescripcionEs.getText().toString());

            }
        });
    }

    public void saveFecha(){
        DBCalendario bd= new DBCalendario(getApplication(),"Agenda",null,1);
        SQLiteDatabase db =bd.getWritableDatabase();
        Bundle datos  = getIntent().getExtras();
        String nom1 = datos.getString("first");
        String ide1 = datos.getString("second");
        Date fecha = (Date)datos.get("third");
        String spin1 = datos.getString("fifth");
        id= datos.getString("seventh");
        String hora= datos.getString("hora");
        String minuto= datos.getString("minuto");
        String vTiempo= datos.getString("vtiempo");
        String sql="insert into eventos"+
                "(nombreEvento, ubicacion, fecha, horaDesde, horaHasta, descripcion)" +
                "values('" +
                spin1+
                "','" +"Centro de asistencia"+
                "','"+fecha.getDate()+" - "+ fecha.getMonth() + " - " + fecha.getYear()+
                "','"+hora+":"+minuto+":"+"00"+
                "','"+"00:00:00"+
                "','"+"Recordatorio de cita de "+nom1+" indentificad(a) con documento: "+ide1+
                "')";
        try {
            db.execSQL(sql);
        }
        catch (Exception e) {

            Toast.makeText(getApplication(),"Error"+e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }
    private void gerarQRCode() {
        String texto = prescripcionEs.getText().toString();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(texto, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            Utilidades util= new Utilidades();
            bitmapQR=util.BitMapToString(bitmap);
            ivQRCode.setImageBitmap(bitmap);
            Save guardar= new Save();
            guardar.SaveImage(this,bitmap);
            NombreQR=guardar.NombreQr();
        }catch (WriterException e){
            e.printStackTrace();
        }
    }

    private void iniciliarComponentes() {
        ivQRCode = (ImageView) findViewById(R.id.ivQRCode);
    }
    public void  enviarCorreo(String mensaje){

        Bundle dato1  = getIntent().getExtras();
        String[] cor1 = new String[]{dato1.getString("sixth")};
        String nom2 = dato1.getString("first");
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/directorioQR",NombreQR));
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("image/pjpeg");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        emailIntent.putExtra(Intent.EXTRA_EMAIL,cor1);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Prescripción medica de: " +nom2);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email."));
            Log.i("EMAIL", "Enviando email...");
        }
        catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(this, "NO existe ningún cliente de email instalado!.", Toast.LENGTH_SHORT).show();
        }

    }

    private void recibePrescripcion() {
        Bundle datos  = getIntent().getExtras();
        String nom1 = datos.getString("first");
        String ide1 = datos.getString("second");
        fecha = (Date)datos.get("third");
        String spin1 = datos.getString("fifth");
        String hora= datos.getString("hora");
        String minuto= datos.getString("minuto");
        String vTiempo= datos.getString("vtiempo");
        id= datos.getString("seventh");
        prescripcionEs = (TextView)findViewById(R.id.textView_prescipcionEscrita);
        prescripcionEs.setText("Sr(a) "+nom1+" Identificado con número de cedula "+ide1+
                " le recordamos su cita de "+spin1+
                " es el dia "+ fecha.getDate()+
                "/"+fecha.getMonth()+
                "/"+fecha.getYear()+
                " a las "+hora+":"+minuto+" "+vTiempo+
                "\n Recuerde cancelar su cita con anticipacion si es el caso..");
    }

    private void guardar(){
        FirebaseDatabase bd = FirebaseDatabase.getInstance();
        DatabaseReference usuarioRef = bd.getReference(FirebaseReferences.USUARIO_REFERENCES);
        usuarioRef.child("USUARIOS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseDatabase bd = FirebaseDatabase.getInstance();
                DatabaseReference usuarioRef = bd.getReference(FirebaseReferences.USUARIO_REFERENCES);
                try {
                    int contador=0;
                    for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {

                        String userId=areaSnapshot.getKey();
                        contador=contador+1;
                        String areaName = areaSnapshot.child("identificador").getValue().toString();
                        if (areaName.compareTo(id)==0) {

                            usuarioRef.child("USUARIOS").child(userId).child("qr").setValue(bitmapQR);
                            usuarioRef.child("USUARIOS").child(userId).child("fecha").setValue(fecha);
                            break;
                        }
                        if(dataSnapshot.getChildrenCount()==contador) {
                        }
                    }
                }
                catch(Exception e){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}


