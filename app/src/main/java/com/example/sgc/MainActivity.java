package com.example.sgc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //Declara el boton de registrar

    private View registrar;

    EditText CajaDNI;

    EditText CajaPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Poner el icono en el Action BAR
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.icindex);


        CajaDNI = (EditText) findViewById(R.id.edit_dni);
        CajaPass = (EditText) findViewById(R.id.edit_pass);


        //Boton REGISTRAR
        registrar = (Button) findViewById(R.id.btn_registrar);

        registrar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, Registrar_Usuario.class);
                startActivity(intent);

            }
        });

    }


    public void BotonLogin(View view) {
        AdminSQLite admin = new AdminSQLite(getBaseContext());
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String dni = CajaDNI.getText().toString();
        String pass = CajaPass.getText().toString();


        if (dni.trim()
                .length() < 5) {

            Toast.makeText(MainActivity.this, "DNI mayor a 5 a 8 caracateres", Toast.LENGTH_LONG).show();
        } else {

            if ((!dni.isEmpty()) || (!pass.isEmpty())) {


                Cursor fila = BaseDeDatos.rawQuery
                        ("Select dni, pass from USUARIOS where dni ='" + dni + "' and pass = '" + pass + "'", null);


                if (fila.moveToFirst()) {
                    CajaDNI.setText(fila.getString(0));
                    CajaPass.setText(fila.getString(1));

                    Toast.makeText(this, "Bienvenido!", Toast.LENGTH_SHORT).show();
                    GuardarUsuario();

                    Intent i = new Intent(MainActivity.this, Menu_Ppal.class);


                    startActivity(i);
                    BaseDeDatos.close();

                } else {
                    Toast.makeText(this, "Verificar usuario/contraseña.", Toast.LENGTH_SHORT).show();
                    BaseDeDatos.close();
                }


            } else {
                Toast.makeText(this, "Por favor completar todos los campos.", Toast.LENGTH_SHORT).show();
            }


        }


    }

    //Genera un archivo de preferencias compartidas un archivo xml
    public void GuardarUsuario() {

        // haceun archivo de preferencias de modo privado solo para acceso de la aplicacion

        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        String dni = CajaDNI.getText().toString();

        AdminSQLite Admin = new AdminSQLite(getBaseContext());
        SQLiteDatabase BaseDeDatos = Admin.getReadableDatabase();
        String query = "Select * from USUARIOS where dni='"+ dni+"'";
        Cursor cursor = BaseDeDatos.rawQuery(query, null);
        String usuario;

        if (cursor.moveToFirst()) {
            //con esto verificamos si hay un proximo registro
            do {
                usuario = cursor.getString(1);
            } while (cursor.moveToNext());


            cursor.close();


            //almacena los datos de mi formulario

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("dni", dni);
            editor.putString("usuario", usuario);
            editor.commit();


        }


    }

}

