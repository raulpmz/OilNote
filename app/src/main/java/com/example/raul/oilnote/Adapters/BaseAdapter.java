package com.example.raul.oilnote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

public class BaseAdapter extends ArrayAdapter<String> {

    protected Context context;
    protected LayoutInflater inflater;

    public BaseAdapter(Context paramContext, int paramInt) {
        super(paramContext, paramInt);
        this.context = paramContext;
    }

}