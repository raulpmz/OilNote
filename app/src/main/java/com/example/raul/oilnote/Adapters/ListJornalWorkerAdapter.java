package com.example.raul.oilnote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.raul.oilnote.Objects.Jornal;
import com.example.raul.oilnote.R;

import java.util.List;

/**
 * Created by Raul on 15/06/2017.
 */

public class ListJornalWorkerAdapter extends ArrayAdapter<Jornal> {

    private TextView date, salary;

    public ListJornalWorkerAdapter(Context context, List<Jornal> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Obtener inflater:
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_list_jornals_workers, parent, false);
        }

        // Referencias UI:
        // TextView's:
        date    = (TextView) convertView.findViewById(R.id.date_jornal);

        salary  = (TextView) convertView.findViewById(R.id.salary);
        // Jornal actual:
        Jornal jornal = getItem(position);

        // Se editan los componentes del LinearLayout:
        // Fecha del jornal:
        date.setText(jornal.getJornal_date());
        salary.setText(jornal.getJornal_salary() + " €");

        // Retorna la vista:
        return convertView;
    }
}