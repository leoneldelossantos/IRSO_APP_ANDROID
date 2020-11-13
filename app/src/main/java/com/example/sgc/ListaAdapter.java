package com.example.sgc;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListaAdapter extends BaseAdapter {


    // Declare Variables
    Context context;
    String[] titulos;
    String[] imagenes;
    //int[] imagenes;
    LayoutInflater inflater;

    //public ListaAdapter(Context context, String[] titulos, int[] imagenes) {
    public ListaAdapter(Context context, String[] titulos, String[] imagenes) {
        this.context = context;
        this.titulos = titulos;
        this.imagenes = imagenes;




    }

    @Override
    public int getCount() {
        return titulos.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView txtTitle;
        ImageView imgImg;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.lista_formato, parent, false);

        // Locate the TextViews in listview_item.xml
        txtTitle = (TextView) itemView.findViewById(R.id.tituloLista);
        imgImg = (ImageView) itemView.findViewById(R.id.iconLista);


        // Capture position and set to the TextViews
        txtTitle.setText(titulos[position]);
        //imgImg.setImageResource(imagenes[position]);



        byte[] imageAsBytes = Base64.decode(imagenes[position], Base64.DEFAULT);

        imgImg.setImageBitmap(
                BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
        );







        return itemView;
    }
}