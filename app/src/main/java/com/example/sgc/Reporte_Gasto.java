package com.example.sgc;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.opencsv.CSVWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class Reporte_Gasto extends AppCompatActivity {

    /*public Button enviarmail;
    public Button mail;

*/


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int PICK_FILE_REQUEST = 1;
    Uri URI = null;




    /*String[] titulo = new String[]{
            "x",
            "x",
            "x",
    };

    int[] imagenes = {
            R.drawable.home,
            R.drawable.fotos,
            R.drawable.contacto
    };*/

    public EditText fecha_desde;

    public EditText fecha_hasta;


    private int año;
    private int mes;
    private int dia;

    private int año1;
    private int mes1;
    private int dia1;

    private Button boton_desde;
    private Button boton_hasta;

   /* public EditText FechaDesde;
    public EditText FechaHasta;
*/





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String dni1 = preferences.getString("dni","NO DISPONIBLE");


        //String QR = "SELECT * FROM gastos  where ( dni='"+dni1+"') and (fecha between  '"+fecha_desde1+"' and  '"+fecha_hasta2+"')";
        String QR = "SELECT * FROM gastos  where  dni='"+dni1+"'";

        AdminSQLite Admin = new AdminSQLite(getBaseContext());
        SQLiteDatabase BaseDeDatos = Admin.getReadableDatabase();


        if (BaseDeDatos != null) {
            //if (fecha != null) {

            // QR = QR + "Descripcion =" + fecha;
            QR = QR;
            //}
            Cursor registros = BaseDeDatos.rawQuery(QR, null);

            int cantidad = registros.getCount(); //obtengo la cantidad de registros.
            int i = 0;



            String[] arreglo_gastos = new String[cantidad];
            String[] arreglo_imagenes = new String[cantidad];


            if (registros.moveToFirst()) {
                //con esto verificamos si hay un proximo registro
                do {
                    String linea_gastos = registros.getString(0)+"-"+ registros.getString(1)+"- Importe: $"+ registros.getString(2);


                    String linea_imagenes = registros.getString(3);

                    if (linea_imagenes == null ){


                        Resources r = this.getResources();
                        Bitmap bm = BitmapFactory.decodeResource(r, R.drawable.home);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
                        //String encodedImage = Base64.encodeBytes(b);
                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        linea_imagenes = encodedImage ;
                    }


                    arreglo_gastos[i] = linea_gastos;

                    arreglo_imagenes[i] = linea_imagenes;

                    i++;
                } while (registros.moveToNext());


            }
            if (cantidad == 0) {

                Toast.makeText(this, "No se encontraron registros", Toast.LENGTH_SHORT).show();

            } else {
                ArrayAdapter<String> adapter_gastos = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo_gastos);
                ArrayAdapter<String> adapter_imagenes = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo_imagenes);

                ListView lista_gastos = (ListView) findViewById(R.id.lista_gastos);

                lista_gastos.setAdapter(adapter_gastos);
                lista_gastos.setAdapter(adapter_imagenes);



                final ListView lista = (ListView) findViewById(R.id.lista_gastos);
                ListaAdapter  adapter = new ListaAdapter(this, arreglo_gastos, arreglo_imagenes );
                lista.setAdapter(adapter);

            }
        }



      /*  fecha_desde = (EditText) findViewById(R.id.fecha_desde);
        boton_desde = (Button) findViewById(R.id.boton_desde);
        boton_desde.setOnClickListener(this);

        fecha_hasta = (EditText) findViewById(R.id.fecha_hasta);
        boton_hasta = (Button) findViewById(R.id.boton_hasta);
        boton_hasta.setOnClickListener(this);
*/





    }


/*
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClick(View v) {
        if (v == boton_desde) {
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            año = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    fecha_desde.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
            }
                    ,año, mes, dia);

            datePickerDialog.show();

        }
        if (v == boton_hasta) {
            final Calendar c = Calendar.getInstance();
            dia1 = c.get(Calendar.DAY_OF_MONTH);
            mes1 = c.get(Calendar.MONTH);
            año1 = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    fecha_hasta.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
            }
                    ,año1, mes1, dia1);

            datePickerDialog.show();

        }
    }

*/

    public void BucarLista(View view){
        /*String fecha_desde1 = fecha_desde.getText().toString();
        String fecha_hasta2 = fecha_hasta.getText().toString();*/

        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String dni1 = preferences.getString("dni","NO DISPONIBLE");


        //String QR = "SELECT * FROM gastos  where ( dni='"+dni1+"') and (fecha between  '"+fecha_desde1+"' and  '"+fecha_hasta2+"')";
        String QR = "SELECT * FROM gastos  where  dni='"+dni1+"'";

        AdminSQLite Admin = new AdminSQLite(getBaseContext());
        SQLiteDatabase BaseDeDatos = Admin.getReadableDatabase();


            if (BaseDeDatos != null) {
                //if (fecha != null) {

                // QR = QR + "Descripcion =" + fecha;
                QR = QR;
                //}
                Cursor registros = BaseDeDatos.rawQuery(QR, null);

                int cantidad = registros.getCount(); //obtengo la cantidad de registros.
                int i = 0;



                String[] arreglo_gastos = new String[cantidad];
                String[] arreglo_imagenes = new String[cantidad];


                if (registros.moveToFirst()) {
                    //con esto verificamos si hay un proximo registro
                    do {
                        String linea_gastos = registros.getString(0)+"-"+ registros.getString(1)+"- Importe: $"+ registros.getString(2);


                        String linea_imagenes = registros.getString(3);

                        if (linea_imagenes == null ){


                            Resources r = this.getResources();
                            Bitmap bm = BitmapFactory.decodeResource(r, R.drawable.home);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] b = baos.toByteArray();
                            //String encodedImage = Base64.encodeBytes(b);
                            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            linea_imagenes = encodedImage ;
                        }


                        arreglo_gastos[i] = linea_gastos;

                        arreglo_imagenes[i] = linea_imagenes;

                        i++;
                    } while (registros.moveToNext());


                }
                if (cantidad == 0) {

                    Toast.makeText(this, "No se encontraron registros", Toast.LENGTH_SHORT).show();

                } else {
                    ArrayAdapter<String> adapter_gastos = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo_gastos);
                    ArrayAdapter<String> adapter_imagenes = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo_imagenes);

                    ListView lista_gastos = (ListView) findViewById(R.id.lista_gastos);

                    lista_gastos.setAdapter(adapter_gastos);
                    lista_gastos.setAdapter(adapter_imagenes);



                    final ListView lista = (ListView) findViewById(R.id.lista_gastos);
                    ListaAdapter  adapter = new ListaAdapter(this, arreglo_gastos, arreglo_imagenes );
                    lista.setAdapter(adapter);

                }
            }



    }



    public void GenerarCSV(View view){

        /*String fecha_desde11 = fecha_desde.getText().toString();
        String fecha_hasta22 = fecha_hasta.getText().toString();*/

        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String dni11 = preferences.getString("dni","NO DISPONIBLE");

        String FILENAME = "gastos.csv";
        File directoryDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        File logDir = new File(directoryDownload, FILENAME);

            try {
                logDir.createNewFile();
                CSVWriter csvWriter = new CSVWriter(new FileWriter(logDir));

                AdminSQLite Admin = new AdminSQLite(getBaseContext());
                SQLiteDatabase BaseDeDatos = Admin.getWritableDatabase();

                Cursor curCSV = BaseDeDatos.rawQuery("SELECT * FROM gastos", null);
                //Cursor curCSV = BaseDeDatos.rawQuery("SELECT * FROM gastos  where ( dni='"+dni11+"') and (fecha between  '"+fecha_desde11+"' and  '"+fecha_hasta22+"')", null);


                csvWriter.writeNext(curCSV.getColumnNames());
                while (curCSV.moveToNext()) {
                    String arrStr[] = {curCSV.getString(0) + "," +curCSV.getString(1) + ","+ curCSV.getString(2) + ","+curCSV.getString(4) + ","};
                    csvWriter.writeNext(arrStr);
                }
                csvWriter.close();
                curCSV.close();

                Toast.makeText(this, "Archivo generado.", Toast.LENGTH_LONG).show();


                Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
                startActivity(intent);

                // return true;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                //   return false;
            }




    }



}






