package WelcomeOwner;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.roomie_2.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAdapter extends ArrayAdapter<Bitmap> {


    int layoutId;
    public ProfileAdapter(@NonNull Context context, Bitmap[]photos) {
        super(context, 0,photos);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Bitmap tempPhoto =  getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.profile_photo, parent, false);
        }

        CircleImageView imageView = convertView.findViewById(R.id.photo);
        imageView.setImageBitmap(tempPhoto);
        return convertView;
    }
}
