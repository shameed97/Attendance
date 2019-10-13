package com.example.attendance;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListviewAdapter extends ArrayAdapter<String> {


    private ArrayList<String> clName = new ArrayList<>();
    private Context context;

    public ListviewAdapter(ArrayList<String> clName, Context context) {
        super(context, R.layout.class_list, clName);
        this.clName = clName;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View row = layoutInflater.inflate(R.layout.class_list, parent, false);
        TextView textView = row.findViewById(R.id.clList);
        textView.setText(clName.get(position));

        return row;
    }
}
