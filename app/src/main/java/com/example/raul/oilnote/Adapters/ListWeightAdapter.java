package com.example.raul.oilnote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.raul.oilnote.Objects.Jornal;
import com.example.raul.oilnote.Objects.Weight;
import com.example.raul.oilnote.R;

import java.util.List;

/**
 * Created by Raul on 15/06/2017.
 */

public class ListWeightAdapter extends ArrayAdapter<Weight> {

    private TextView date, name, number;

    public ListWeightAdapter(Context context, List<Weight> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Obtener inflater:
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_list_weight, parent, false);
        }

        // Referencias UI:
        // TextView's:
        date    = (TextView) convertView.findViewById(R.id.date_weight);

        name    = (TextView) convertView.findViewById(R.id.name_plot);

        number  = (TextView) convertView.findViewById(R.id.weight_number);

        // Pesado actual:
        Weight weight = getItem(position);

        // Se editan los componentes del LinearLayout:
        // Fecha del pesado:
        date.setText(weight.getWeight_date());

        // Nombre de la parcela:
        name.setText(weight.getPlot_name());

        // Número de kilogamos pesados:
        number.setText(weight.getWeight_number());

        // Retorna la vista:
        return convertView;
    }
}