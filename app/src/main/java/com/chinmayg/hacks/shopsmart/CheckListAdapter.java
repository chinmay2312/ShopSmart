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

public class CheckListAdapter extends ArrayAdapter {

    private ArrayList<String> checklist_arrl;

    public CheckListAdapter(@NonNull Context context, ArrayList<String> checklist_arrl) {
        super(context,0, checklist_arrl);

        this.checklist_arrl = checklist_arrl;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView==null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cheklist_item, parent, false);

        TextView tvChecklistItem = convertView.findViewById(R.id.tv_checklist_item);
        tvChecklistItem.setText(checklist_arrl.get(pos));

        return convertView;//super.getView(pos, convertView, parent);
    }
}
