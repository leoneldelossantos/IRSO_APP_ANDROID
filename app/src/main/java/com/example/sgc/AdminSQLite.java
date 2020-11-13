package com.example.sgc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


public class AdminSQLite extends SQLiteOpenHelper {


    //TABLA USUARIOS
    public static String TABLA_USUARIOS="usuarios";
    public static String CAMPO_DNI="dni";
    public static String CAMPO_NYA="nya";
    public static String CAMPO_PASSWORD="pass";
    public static String CAMPO_MAIL="mail";

    public static final String USUARIOS="CREATE TABLE "+TABLA_USUARIOS+" ("+CAMPO_DNI+" String, "+CAMPO_NYA+" String, "+CAMPO_PASSWORD+" String, "+CAMPO_MAIL+" String)";

    //TABLA GASTOS
    public static String TABLA_GASTOS="gastos";
    public static String CAMPO_FECHA="fecha";
    public static String CAMPO_TIPO_GASTO="tipogasto";
    public static String CAMPO_IMPORTE="importe";
    public static String CAMPO_IMAGEN="imagen";
    public static String CAMPO_DNI_TBLGASTOS="dni";


    public static final String GASTOS="CREATE TABLE "+TABLA_GASTOS+" ("+CAMPO_FECHA+" String, "+CAMPO_TIPO_GASTO+" String,"+CAMPO_IMPORTE+" String, "+CAMPO_IMAGEN+" String, "+CAMPO_DNI_TBLGASTOS+" String)";


    public AdminSQLite(@Nullable Context context) {
        super(context,"BasedeDatos.db",null,3);
    }

    @Override
    public void onCreate(SQLiteDatabase BaseDeDatos) {
        BaseDeDatos.execSQL("Drop table if exists gastos");
        BaseDeDatos.execSQL(USUARIOS);
        BaseDeDatos.execSQL(GASTOS);

        Log.e("oncreate","oncreate");
    }

    //Verifica si exsiste una version antigua de la BD
    @Override
          public void onUpgrade(SQLiteDatabase BaseDeDatos, int oldVersion, int newVersion) {
            BaseDeDatos.execSQL("Drop table if exists USUARIOS");
             BaseDeDatos.execSQL("Drop table if exists gastos");
            //BaseDeDatos.execSQL(USUARIOS);
             onCreate(BaseDeDatos);
            Log.e("onupgrade","onupgrade");

    }


}