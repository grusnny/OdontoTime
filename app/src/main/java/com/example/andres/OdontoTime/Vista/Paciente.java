package com.example.andres.OdontoTime.Vista;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andres.OdontoTime.Modelo.MyReceiver;
import com.example.andres.OdontoTime.Controlador.Save;
import com.example.andres.OdontoTime.Controlador.FirebaseReferences;
import com.example.andres.OdontoTime.Modelo.Utilidades;
import com.example.andres.firebase.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import java.util.Calendar;
import java.util.Date;

public class Paciente extends AppCompatActivity implements View.OnClickListener {
    String id;
    TextView prescripcionEs;
    ImageView ivQRCode2;
    Button guardar, alarma;
    Bitmap bitmap;
    Object objeto;
    Date fecha=new Date();
    private static final int ALARM_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);
        prescripcionEs = (TextView) findViewById(R.id.textView_prescipcionEscrita2);
        ivQRCode2 = (ImageView) findViewById(R.id.ivQRCode2);
        guardar = (Button) findViewById(R.id.DescargarQR);
        alarma = (Button) findViewById(R.id.alarma);
        guardar.setOnClickListener(this);
        alarma.setOnClickListener(this);
        recibePrescripcion();
        cargarUserInfo();
    }

    private void recibePrescripcion() {
        Bundle datos = getIntent().getExtras();
        id = datos.getString("first");
    }

    private void cargarUserInfo() {
        FirebaseDatabase bd = FirebaseDatabase.getInstance();
        DatabaseReference usuarioRef = bd.getReference(FirebaseReferences.USUARIO_REFERENCES);
        usuarioRef.child("USUARIOS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("APUNTADOR", "ENTRE AL METODO");
                try {
                    int contador = 0;
                    for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                        String userId = areaSnapshot.getKey();
                        contador = contador + 1;
                        String areaName = areaSnapshot.child("identificador").getValue().toString();
                        if (areaName.compareTo(id) == 0) {
                            String valorQR = areaSnapshot.child("qr").getValue().toString();
                            if (valorQR.compareTo("-") != 0) {
                                String QR = areaSnapshot.child("qr").getValue().toString();
                                fecha =  areaSnapshot.child("fecha").getValue(Date.class);
                                Log.i("APUNTADOR", String.valueOf(fecha.getHours()));
                                Utilidades util = new Utilidades();
                                bitmap = util.StringToBitMap(QR);
                                ivQRCode2.setImageBitmap(bitmap);
                                guardar.setEnabled(true);
                                alarma.setEnabled(true);
                                String prescripcion=decodeBitmap(bitmap);
                                prescripcionEs.setText(prescripcion);
                                Log.i("APUNTADOR", prescripcion);
                            } else {
                                prescripcionEs.setText("El usuario no tiene prescripcion");
                            }
                            break;
                        }
                        if (dataSnapshot.getChildrenCount() == contador) {
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.DescargarQR:
                descargarQR();
                break;
            case R.id.alarma:
                addEventToCalendar(this);
                //activarAlarma(lapso);
                break;
        }
    }
    private void descargarQR() {
        Save guardar= new Save();
        guardar.SaveImage(this,bitmap);
    }
    private void activarAlarma(int when){
            AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);


            Intent intent  = new Intent(this, MyReceiver.class);
            PendingIntent pIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, intent,  PendingIntent.FLAG_CANCEL_CURRENT);
            manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + when * 1000*3600, pIntent);
            Toast.makeText(this, "Recordatorio de medicamento en :"+when+" horas", Toast.LENGTH_LONG).show();
        }
    private String decodeBitmap(Bitmap bitmap){
        int width = bitmap.getWidth(), height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        bitmap = null;
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
        MultiFormatReader reader = new MultiFormatReader();
        try
        {
            Result result = reader.decode(bBitmap);
            Log.i("BITMP",result.getText());
            return result.getText();
        }
        catch (NotFoundException e)
        {
            return null;
        }
    }
    private void addEventToCalendar(Activity activity){
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, fecha.getDate());
        cal.set(Calendar.MONTH, fecha.getMonth()-1);
        cal.set(Calendar.YEAR, fecha.getDay());

        cal.set(Calendar.HOUR_OF_DAY, fecha.getHours());
        cal.set(Calendar.MINUTE, fecha.getMinutes());

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis()+60*60*1000);

        intent.putExtra(CalendarContract.Events.ALL_DAY, false);
        intent.putExtra(CalendarContract.Events.RRULE , "FREQ=DAILY");
        intent.putExtra(CalendarContract.Events.TITLE, "Recordatorio de cita");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Recordatorio de cita");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION,"");

        activity.startActivity(intent);
    }

}


