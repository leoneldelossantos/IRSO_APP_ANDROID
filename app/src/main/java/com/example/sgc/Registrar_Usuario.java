package com.example.sgc;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registrar_Usuario extends AppCompatActivity {

    public EditText CajaDni, CajaNyA, CajaPass, CajaMail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);



        CajaDni = (EditText) findViewById(R.id.dni);
        CajaNyA = (EditText) findViewById(R.id.nya);
        CajaPass = (EditText) findViewById(R.id.password);
        CajaMail = (EditText) findViewById(R.id.mail);

    }

    public void BotonRegistrar(View view) {
        AdminSQLite Admin = new AdminSQLite(getBaseContext());
        SQLiteDatabase BaseDeDatos = Admin.getWritableDatabase();

        String dni = CajaDni.getText().toString();
        String nya = CajaNyA.getText().toString();
        String pass = CajaPass.getText().toString();
        String mail = CajaMail.getText().toString();

        if (dni.trim()
                .length() < 5) {

            Toast.makeText(Registrar_Usuario.this, "DNI mayor a 5 a 8 caracateres", Toast.LENGTH_LONG).show();
        } else {
            if (!esEmailValido(mail)) {
                Toast.makeText(Registrar_Usuario.this, "Por favor, revisar correo electronico.", Toast.LENGTH_LONG).show();
            } else {

                if (nya.isEmpty() && pass.isEmpty()) {
                    Toast.makeText(Registrar_Usuario.this, "Por favor, completar los campos.", Toast.LENGTH_LONG).show();


                } else {
                    Cursor fila = BaseDeDatos.rawQuery("select dni from USUARIOS where dni='" + dni + "'", null);


                    if (fila.moveToFirst()) {
                        Toast.makeText(this, "DNI ya registrado" + dni, Toast.LENGTH_SHORT).show();
                    } else {
                        ContentValues insertar = new ContentValues();

                        insertar.put("dni", dni);
                        insertar.put("nya", nya);
                        insertar.put("pass", pass);
                        insertar.put("mail", mail);

                        BaseDeDatos.insert("USUARIOS", null, insertar);

                        BaseDeDatos.close();
                        CajaDni.setText("");
                        CajaPass.setText("");

                        Toast.makeText(this, "Registro exitoso!", Toast.LENGTH_LONG).show();

                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                    }


                }


            }
        }


    }

    public static boolean esEmailValido(String email) {
        String expresiones = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        //"^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expresiones, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_salir:
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                return true;



        }
        return super.onOptionsItemSelected(item);
    }


}