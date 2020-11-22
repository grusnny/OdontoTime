package com.example.andres.OdontoTime.Vista;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.andres.OdontoTime.Controlador.Save;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import com.example.andres.firebase.R;

public class CodigoQR extends AppCompatActivity {

Button btnGerar;
EditText edtTexto;
ImageView ivQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_qr);

        iniciliarComponentes();
        clickButton();
    }


    private void clickButton() {
        btnGerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gerarQRCode();
            }
        });
    }

    private void gerarQRCode() {
        String texto = edtTexto.getText().toString();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(texto, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ivQRCode.setImageBitmap(bitmap);
            Save guardar= new Save();
            guardar.SaveImage(this,bitmap);
        }catch (WriterException e){
            e.printStackTrace();
        }
    }

    private void iniciliarComponentes() {
        edtTexto = (EditText) findViewById(R.id.texto);
        btnGerar = (Button) findViewById(R.id.boton_generar);
        ivQRCode = (ImageView) findViewById(R.id.ivQRCode);
    }

}
