package com.example.andres.OdontoTime.Vista;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.andres.firebase.R;

public class MenuMedico extends AppCompatActivity implements View.OnClickListener {
    Button admmedicamento,admusuario,prescripcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_medico);
        admmedicamento=(Button) findViewById(R.id.adm_medicamento);
        admusuario=(Button) findViewById(R.id.adm_usuario);
        prescripcion=(Button) findViewById(R.id.nueva_prescripcion);
        admmedicamento.setOnClickListener(this);
        admusuario.setOnClickListener(this);
        prescripcion.setOnClickListener(this);
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.adm_medicamento:
                lanzarMedicamento(view);
                break;
            case R.id.adm_usuario:
                lanzarUsuario(view);
                break;
            case R.id.nueva_prescripcion:
                lanzarPrescripcion(view);
                break;

        }

    }
    public void lanzarMedicamento(View view) {
        Intent i = new Intent(this, PrescripcionMedica.class );
        startActivity(i);
    }
    public void lanzarUsuario(View view) {
        Intent i = new Intent(this, Registro.class );
        startActivity(i);
    }
    public void lanzarPrescripcion(View view) {
        Intent i = new Intent(this, Medico.class );
        startActivity(i);
    }
}
