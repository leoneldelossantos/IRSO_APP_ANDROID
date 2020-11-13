package com.example.sgc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class Agregar_Gasto extends AppCompatActivity implements  AdapterView.OnItemSelectedListener , View.OnClickListener{

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;

    private int año;
    private int mes;
    private int dia;


    private EditText edit_fecha;
    private Button botoncalendario;


    Spinner combogastos;
    String gasto;
    String gasto11;

    String encodedImage;


    ImageView picture;
    ImageButton openCamera;

    // BD
    public EditText CajaFecha;
    public EditText CajaImporte;

    private String[] gastos1 = {"Seleccionar gasto...", "Comida", "Viaticos", "Transporte publico - SUBE", "Otros"};


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit_fecha = (EditText) findViewById(R.id.edit_fecha);
        botoncalendario = (Button) findViewById(R.id.button_hoy);

        botoncalendario.setOnClickListener(this);


        combogastos = (Spinner) findViewById(R.id.idspinnercombogastos);
        combogastos.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, gastos1);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        combogastos.setAdapter(aa);
        gasto = combogastos.getSelectedItem().toString();


        openCamera = (ImageButton) findViewById(R.id.idopencamara);
        picture = (ImageView) findViewById(R.id.idimagenfactura);


        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();

            }
        });

        //Inserta datos en la BD
        CajaFecha = (EditText) findViewById(R.id.edit_fecha);
        CajaImporte = (EditText) findViewById(R.id.edit_importe);


    }

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            openCamera();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                //Toast.makeText(this, "Camara necesita permisos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {

            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            picture.setImageBitmap(bitmap);

            //Combierte el bitmpap en Array

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
             encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);





        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClick(View v) {
        if (v == botoncalendario) {
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            año = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    edit_fecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
            }
                    ,año, mes, dia);

            datePickerDialog.show();

        }
    }




    public void BotonRegistrarGastos (View view){
        AdminSQLite Admin = new AdminSQLite(getBaseContext());
        //Abrir bd de lectura y escritura
        SQLiteDatabase BaseDeDatos = Admin.getWritableDatabase();

        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String dni1 = preferences.getString("dni","NO DISPONIBLE");

        String fecha = CajaFecha.getText().toString();
        String importe = CajaImporte.getText().toString();
        String imagen = encodedImage;


        if (!fecha.isEmpty() && !importe.isEmpty() && !gasto11.equals("Seleccionar gasto...") ) {

            ContentValues insertar = new ContentValues();

            insertar.put("fecha", fecha);
            insertar.put("tipogasto", gasto11);
            insertar.put("importe", importe);
            insertar.put("imagen", imagen);
            insertar.put("dni", dni1);



            BaseDeDatos.insert("GASTOS", null, insertar);

            BaseDeDatos.close();



            Toast.makeText(this, "Registro exitoso!", Toast.LENGTH_LONG).show();


            Intent i = new Intent(this, Agregar_Gasto.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Por favor completar los campos.", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        gasto11= gastos1[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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