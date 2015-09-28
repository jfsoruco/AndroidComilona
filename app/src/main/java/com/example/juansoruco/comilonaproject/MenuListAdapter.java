package com.example.juansoruco.comilonaproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juansoruco.comilonaproject.menu.MenuDia;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milton.torrico on 08/09/2015.
 */
public class MenuListAdapter extends ArrayAdapter {

    //private Context mContext;
    //private ArrayList<MenuDia> mMenuDias;
    //private int mSelectedMenuDia;


    public MenuListAdapter(Context context, ArrayList<MenuDia> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_list_item, null);
        }
        TextView fullName = (TextView) convertView.findViewById(R.id.fullName);
        TextView nroVotos = (TextView) convertView.findViewById(R.id.nroVotos);
        RadioButton radioVoto = (RadioButton) convertView.findViewById(R.id.rb_option);

        MenuDia menuDia =  (MenuDia)getItem(position);
        fullName.setText(menuDia.getDescription());
        nroVotos.setText(""+menuDia.getNro_voto());
        radioVoto.setChecked(menuDia.getChecked());

        //Log.i(null,"--> "+ fullName.getText()+" "+nroVotos.getText());
        return convertView;
    }
}

