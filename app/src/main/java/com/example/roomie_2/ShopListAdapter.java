package com.example.roomie_2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.ListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopListAdapter extends ArrayAdapter<ShopItem> {
    private Button myButton;
    public ShopListAdapter(Context context, List<ShopItem> data) {
        super(context, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ShopItem curItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_shop_item, parent, false);
        }

        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.curName);
        TextView textViewItemDescription = (TextView)
                convertView.findViewById(R.id.curQty);

        textViewItemName.setText(curItem.name);
        String stringdouble = Float.toString(curItem.qty);
        textViewItemDescription.setText(stringdouble);
        // Return the completed view to render on screen
        Log.i("Tamir check","get view adapter");
//        convertView.findViewById(R.id.editB).setVisibility(View.INVISIBLE);
        return convertView;
    }
}
