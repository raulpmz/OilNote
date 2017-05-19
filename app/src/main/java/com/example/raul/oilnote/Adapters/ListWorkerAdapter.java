package com.example.raul.oilnote.Adapters;

import android.content.Context;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.raul.oilnote.Objects.Worker;
import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.ImageHelper;

import java.util.List;

import static com.example.raul.oilnote.Utils.Codification.decodeBase64;

/**
 * Created by ptmarketing02 on 18/05/2017.
 */

public class ListWorkerAdapter extends ArrayAdapter<Worker> {

    ImageView avatar;
    TextView name;

    public ListWorkerAdapter(Context context, List<Worker> objects) {
        super(context, 0, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_workers_list, parent, false);
        }

        // Referencias UI.
        // TextView:
        name   = (TextView) convertView.findViewById(R.id.name_worker);

        // ImagenView:
        avatar = (ImageView) convertView.findViewById(R.id.image_worker);

        // Trabajador actual.
        Worker worker = getItem(position);

        // Se editan los componentes del LinearLayout:
        // Nombre del trabajador:
        name.setText(worker.getWorkerName());

        // Foto del trabajador:
        ImageHelper.rounderImage(decodeBase64(worker.getWorkerPhoto()),avatar);

        // Retorna la vista:
        return convertView;
    }
}
