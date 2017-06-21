package com.example.raul.oilnote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.raul.oilnote.Objects.Expense;
import com.example.raul.oilnote.Objects.Jornal;
import com.example.raul.oilnote.R;

import java.util.List;

/**
 * Created by Raul on 15/06/2017.
 */

public class ListExpenseAdapter extends ArrayAdapter<Expense> {

    private TextView date, type, money;

    public ListExpenseAdapter(Context context, List<Expense> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Obtener inflater:
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_list_expense, parent, false);
        }

        // Referencias UI:
        // TextView's:
        date  = (TextView) convertView.findViewById(R.id.date_expense);
        type  = (TextView) convertView.findViewById(R.id.type_expense);
        money = (TextView) convertView.findViewById(R.id.money_expense);
        // Jornal actual:
        Expense expense = getItem(position);

        // Se editan los componentes del LinearLayout:
        // Fecha del gasto:
        date.setText(expense.getDate());

        // Tipo de gasto:
        type.setText(expense.getType());

        // Cuantia de dinero:
        money.setText(expense.getMoney()+" €");

        // Retorna la vista:
        return convertView;
    }
}