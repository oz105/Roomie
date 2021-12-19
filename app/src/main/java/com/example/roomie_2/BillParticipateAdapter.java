package com.example.roomie_2;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.ListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillParticipateAdapter  extends ArrayAdapter<String> {
        public CheckBox checkBox;
        private int layoutId;
        public BillParticipateAdapter(Context context, List<String> data,int layoutId) {
            super(context, 0, data);
            this.layoutId = layoutId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            String name = getItem(position);

            if (convertView == null) {

                convertView = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
            }

            TextView textViewItemName = (TextView)
                    convertView.findViewById(R.id.name);

            textViewItemName.setText(name);

            // Return the completed view to render on screen
            Log.i("Tamir check","get view adapter");
//        convertView.findViewById(R.id.editB).setVisibility(View.INVISIBLE);
            return convertView;
        }
    }


