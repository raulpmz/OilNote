package com.example.raul.oilnote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.raul.oilnote.Objects.Jornal;
import com.example.raul.oilnote.Objects.Missing;
import com.example.raul.oilnote.R;

import java.util.List;

/**
 * Created by Raul on 16/06/2017.
 */

public class ListMissingAdapter extends ArrayAdapter<Missing> {

    private TextView name, date;

    public ListMissingAdapter(Context context, List<Missing> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Obtener inflater:
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_list_missings, parent, false);
        }

        // Referencias UI:
        // TextView's:
        name = (TextView) convertView.findViewById(R.id.name_worker);

        date = (TextView) convertView.findViewById(R.id.date_jornal);

        // Jornal actual:
        Missing missing = getItem(position);

        // Se editan los componentes del LinearLayout:
        // Fecha del jornal:
        date.setText(missing.getMissing_date());

        // Nombre del trabajador:
        name.setText(missing.getWorker_name());

        // Retorna la vista:
        return convertView;
    }
}