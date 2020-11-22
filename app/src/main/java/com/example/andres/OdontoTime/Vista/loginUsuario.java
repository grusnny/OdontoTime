package com.example.andres.OdontoTime.Vista;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andres.firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginUsuario extends AppCompatActivity implements View.OnClickListener{
    Button boton_InicioUser;
    EditText email_User,contraseña_User;
    Context context=this;
    String identificador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuario);

        boton_InicioUser = (Button) findViewById(R.id.boton_InicioUser);
        boton_InicioUser.setOnClickListener(this);
        email_User = (EditText) findViewById(R.id.email_User);
        contraseña_User = (EditText) findViewById(R.id.contraseña_User);
        FirebaseAuth.AuthStateListener mAuthListener;

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario= firebaseAuth.getCurrentUser();
                if (usuario != null){
                    Toast.makeText(context, "¡Sesion iniciada!", Toast.LENGTH_SHORT).show();
                    Log.i("sesion","sesion iniciada con email: "+usuario.getEmail());

                }else{
                    Toast.makeText(context, "¡Sesion cerrada!", Toast.LENGTH_SHORT).show();
                    Log.i("sesion","sesion cerrada");

                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.boton_InicioUser){
            String emailInicio =email_User.getText().toString();
            String passInicio =contraseña_User.getText().toString();
            if(emailInicio.compareToIgnoreCase("")!=0 && passInicio.compareTo("")!=0) {
                iniciarsesion(emailInicio,passInicio,boton_InicioUser);
                boton_InicioUser.setEnabled(false);
            }else{
                Toast.makeText(context, "¡Completa los campos requeridos!", Toast.LENGTH_LONG).show();
            }


        }
    }
    private void iniciarsesion(String email,String pass,final View view){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "¡Sesion iniciada!", Toast.LENGTH_SHORT).show();
                    Log.i("sesion","sesion iniciada");
                    if(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString().compareTo("admin1@admin.staff")==0){
                        identificador=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                        envio();
                        //lanzarUsuario(view);
                    }
                    else{
                        identificador=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                        envio();
                        //lanzarUsuario(view);
                    }
                }
                else{
                    Toast.makeText(context, "¡Sesion "+task.getException().getMessage()+" !", Toast.LENGTH_LONG).show();
                    Log.i("sesion",task.getException().getMessage()+"");
                    boton_InicioUser.setEnabled(true);
                }
            }
        });

    }
    public void lanzarUsuario(View view) {
        Intent i = new Intent(this, Paciente.class );
        startActivity(i);
    }
    public void envio(){
        Intent go =null;
        go = new Intent(this, Paciente.class);
        String id=identificador;
        go.putExtra("first",id);
        startActivity(go);
    }
}

