package com.example.andres.OdontoTime.Vista;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andres.OdontoTime.Controlador.FirebaseReferences;
import com.example.andres.OdontoTime.Modelo.Usuario;
import com.example.andres.firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class Registro extends AppCompatActivity implements View.OnClickListener{
    Button boton_Registro;
    EditText email_Registro,contraseña_Registro,nombre,documento,contraseña_Registro2;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        boton_Registro = (Button) findViewById(R.id.boton_Registro);
        email_Registro = (EditText) findViewById(R.id.email_Registro);
        contraseña_Registro = (EditText) findViewById(R.id.contraseña_Registro);
        contraseña_Registro2 = (EditText) findViewById(R.id.contraseña_Registro2);
        nombre = (EditText) findViewById(R.id.nombre_Registro);
        documento = (EditText) findViewById(R.id.documento_Registro);
        boton_Registro.setOnClickListener(this);

    }
    private void registrar(String email, String pass, final View view){
        try {
            final int doc=Integer.parseInt(documento.getText().toString());
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.i("sesion", "usuario creado correctamente");
                        email_Registro.setHint("email");
                        contraseña_Registro.setHint("password");
                            FirebaseDatabase bd = FirebaseDatabase.getInstance();
                            DatabaseReference usuarioRef = bd.getReference(FirebaseReferences.USUARIO_REFERENCES);
                            Date fecha = new Date(0, 0, 0, 0, 0, 0);
                            Usuario user = new Usuario(nombre.getText().toString(), email_Registro.getText().toString(),doc, FirebaseAuth.getInstance().getCurrentUser().getUid(),"-", fecha);
                            usuarioRef.child(FirebaseReferences.USUARIO_REFERENCES).push().setValue(user);
                            contraseña_Registro.setText("");
                            contraseña_Registro2.setText("");
                            nombre.setText("");
                            documento.setText("");
                            email_Registro.setText("");

                        Toast.makeText(context, "¡Usuario creado correctamente!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("sesion", task.getException().getMessage() + "");
                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch(Exception e){
            Toast.makeText(context, "¡Datos Erroneos!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(R.id.boton_Registro==view.getId()) {
            if(contraseña_Registro.getText().toString().compareTo(contraseña_Registro2.getText().toString())==0){
                String emailRegistro = email_Registro.getText().toString();
                String passRegistro = contraseña_Registro.getText().toString();
                registrar(emailRegistro, passRegistro, boton_Registro);
            }
            else{
                Toast.makeText(this, "¡Contraseñas incorrectas!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
