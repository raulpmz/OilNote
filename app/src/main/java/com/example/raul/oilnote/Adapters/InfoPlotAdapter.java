package com.example.raul.oilnote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.raul.oilnote.Objects.Weight;
import com.example.raul.oilnote.R;

import java.util.List;

/**
 * Created by Raul on 18/06/2017.
 */

public class InfoPlotAdapter extends ArrayAdapter<Weight> {

    private TextView date, number, efficiency;

    public InfoPlotAdapter(Context context, List<Weight> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Obtener inflater:
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_list_plot_weight, parent, false);
        }

        // Referencias UI:
        // TextView's:
        date        = (TextView) convertView.findViewById(R.id.date_weight);

        number      = (TextView) convertView.findViewById(R.id.weight_number);

        efficiency  = (TextView) convertView.findViewById(R.id.efficiency);

        // Pesado actual:
        Weight weight = getItem(position);

        // Se editan los componentes del LinearLayout:
        // Fecha del pesado:
        date.setText(weight.getWeight_date());

        // Número de kilogamos pesados:
        number.setText(weight.getWeight_number() + " Kg");

        if(!weight.getWeight_efficiency().equals("") && !weight.getWeight_efficiency().equals(null)){
            efficiency.setText(weight.getWeight_efficiency() + " %");
        }

        // Retorna la vista:
        return convertView;
    }
}