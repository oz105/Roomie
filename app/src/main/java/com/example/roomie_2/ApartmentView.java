package com.example.roomie_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ApartmentView extends AppCompatActivity {
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    private DatabaseReference ImgRef = db.getReference();
    ViewPager mViewPager;
    DatabaseReference root = db.getReference();
    private List<Upload> mUploadList = new ArrayList<>();
    public int aptNum;
    ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_view);
        //
        Log.i("oncreate AptView","aptview CREATE");
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        root.child("Users").child(user).child("apartmentId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("datasnapCheckAPT",dataSnapshot.getValue().toString());
                long x = (long) dataSnapshot.getValue();
                aptNum = Math.toIntExact(x);
                Log.i("new aptNumber: ","aptnum is :"+aptNum);
                root.child("Apartments").child(""+aptNum).child("imagesUploads").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i("new aptNumber: ","aptnum is :"+aptNum);

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Upload c = ds.getValue(Upload.class);
                            mUploadList.add(c);
                        }

                        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerMain);
                        Log.i("aptView","set adapter with list size: "+mUploadList.size());
                        Log.i("aptView","also aptnum is :"+aptNum+"");
                        viewPager.setAdapter(new ViewPagerAdapter(getApplicationContext(),mUploadList));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("cancel apt view","aptnum is :"+aptNum);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("cancel","aptnum is :"+aptNum);
            }
        });


        //

    }
}
