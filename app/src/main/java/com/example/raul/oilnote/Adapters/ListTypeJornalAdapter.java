package com.example.raul.oilnote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.raul.oilnote.Objects.Missing;
import com.example.raul.oilnote.Objects.TypeJornal;
import com.example.raul.oilnote.R;

import java.util.List;

/**
 * Created by Raul on 16/06/2017.
 */

public class ListTypeJornalAdapter extends ArrayAdapter<TypeJornal> {

    private TextView name, money;

    public ListTypeJornalAdapter(Context context, List<TypeJornal> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Obtener inflater:
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_list_type_jornal, parent, false);
        }

        // Referencias UI:
        // TextView's:
        name = (TextView) convertView.findViewById(R.id.type_name);

        money = (TextView) convertView.findViewById(R.id.type_value);

        // Tipo de jornal actual:
        TypeJornal typeJornal = getItem(position);

        // Se editan los componentes del LinearLayout:
        // Nombre del jornal:
        name.setText(typeJornal.getName());

        // Precio del jornal:
        money.setText(typeJornal.getMoney() + " €");

        // Retorna la vista:
        return convertView;
    }
}