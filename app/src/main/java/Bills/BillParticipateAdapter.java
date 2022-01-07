package Bills;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.roomie_2.R;

import java.util.List;

public class BillParticipateAdapter  extends ArrayAdapter<NameAndUid> {
        public CheckBox checkBox;
        private int layoutId;
        public BillParticipateAdapter(Context context, List<NameAndUid> data,int layoutId) {
            super(context, 0, data);
            this.layoutId = layoutId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            NameAndUid name = getItem(position);

            if (convertView == null) {

                convertView = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
            }
            TextView textViewItemName = (TextView) convertView.findViewById(R.id.name);
            textViewItemName.setText(name.name);

            // Return the completed view to render on screen

//        convertView.findViewById(R.id.editB).setVisibility(View.INVISIBLE);
            return convertView;
        }
    }


