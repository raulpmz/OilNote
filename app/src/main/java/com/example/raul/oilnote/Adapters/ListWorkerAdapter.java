package com.example.raul.oilnote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raul.oilnote.Objects.Worker;
import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.RoundedImageView;

import java.util.List;

import static com.example.raul.oilnote.Utils.Codification.decodeBase64;

/**
 * Created by ptmarketing02 on 18/05/2017.
 */

public class ListWorkerAdapter extends ArrayAdapter<Worker> {

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
            convertView = inflater.inflate(R.layout.workers_list_adapter, parent, false);
        }

        // Referencias UI.
        ImageView avatar            = (ImageView) convertView.findViewById(R.id.image_worker);
        TextView name               = (TextView) convertView.findViewById(R.id.name_worker);

        // Trabajador actual.
        Worker worker = getItem(position);

        // La librería Glide permite cargar imágenes de forma asíncrona para no entorpecer el hilo principal de la UI.
        // Glide.with(getContext()).load(worker.getWorkerPhoto()).into(avatar);
        // avatar.setImageBitmap(decodeBase64("data:image/jpeg;base64,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"));

        //avatar.setImageBitmap(decodeBase64(worker.getWorkerPhoto()));
        name.setText(worker.getWorkerName());

        return convertView;
    }
}
