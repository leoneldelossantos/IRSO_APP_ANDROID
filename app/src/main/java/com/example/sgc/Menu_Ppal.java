package com.example.sgc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



public class Menu_Ppal extends AppCompatActivity {

    private View agregar;
    private View reportes;
    private View mail;


    private TextView dni;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dni = (TextView)findViewById (R.id.dni);

        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String dni1 = preferences.getString("usuario","NO DISPONIBLE");


        dni.setText("Logueado:"+ dni1);


        agregar =(ImageButton) findViewById(R.id.botonadd);



        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                Intent intent= new Intent(Menu_Ppal.this, Agregar_Gasto.class);
                startActivity(intent);
            }
        });

        reportes =(ImageButton) findViewById(R.id.botonreportes);

        reportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                Intent intent= new Intent(Menu_Ppal.this, Reporte_Gasto.class);
                startActivity(intent);
            }
        });

        mail =(ImageButton) findViewById(R.id.botonmail);

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                mail();
            }
        });


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

    public void mail (){


        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"soporte_sia@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Solicitud de Pedido de Soporte - SIA - Gastos Coporate");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Detalle del Problema: ");

        startActivity(emailIntent);


    }
}