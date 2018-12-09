package com.chinmayg.hacks.shopsmart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DailyRecomAdapter extends ArrayAdapter {

    private ArrayList<String> daily_recom_arrl;

    public DailyRecomAdapter(@NonNull Context context, ArrayList<String> daily_recom_arrl) {
        super(context,0, daily_recom_arrl);

        this.daily_recom_arrl = daily_recom_arrl;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView==null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.daily_recom_item, parent, false);

        TextView tvDailyRecomItem = convertView.findViewById(R.id.tv_daily_recom_item);
        tvDailyRecomItem.setText(daily_recom_arrl.get(pos));



        return convertView;//super.getView(pos, convertView, parent);
    }
}
