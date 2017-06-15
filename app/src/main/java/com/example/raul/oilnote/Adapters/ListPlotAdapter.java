package com.example.raul.oilnote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.raul.oilnote.Objects.Jornal;
import com.example.raul.oilnote.Objects.Plot;
import com.example.raul.oilnote.R;

import java.util.List;

/**
 * Created by Raul on 15/06/2017.
 */

public class ListPlotAdapter extends ArrayAdapter<Plot> {

    private TextView name, number;

    public ListPlotAdapter(Context context, List<Plot> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Obtener inflater:
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_list_plots, parent, false);
        }

        // Referencias UI:
        // TextView's:
        name    = (TextView) convertView.findViewById(R.id.name_plot);

        number  = (TextView) convertView.findViewById(R.id.number_plot);

        // Parcela actual:
        Plot plot = getItem(position);

        // Se editan los componentes del LinearLayout:
        // Nombre de la parcela:
        name.setText(plot.getName());

        // Número de plantas:
        number.setText(plot.getNumber_plant());

        // Retorna la vista:
        return convertView;
    }
}