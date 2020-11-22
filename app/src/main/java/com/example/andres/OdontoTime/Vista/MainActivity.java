package com.example.andres.OdontoTime.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.andres.OdontoTime.Controlador.DBCitas;
import com.example.andres.firebase.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBCitas conex = new DBCitas(this, "dbMedicamentos", null, 1);

    }

    public void registro(View vista){
        Intent ir =null;
        switch (vista.getId()){
            case R.id.button_medico:
                ir = new Intent(this,loginAdministrador.class);
                break;

            case R.id.button_paciente:
                ir = new Intent(this,loginUsuario.class);
                break;
        }
            if (ir != null) {
                startActivity(ir);
            }
    }
}
