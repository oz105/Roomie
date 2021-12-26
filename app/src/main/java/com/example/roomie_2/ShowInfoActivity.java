package com.example.roomie_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ShowInfoActivity extends AppCompatActivity {

    FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference root = db.getReference().child("Apartments");
    TextView AppAddress,AppPassword,details,numOfRooms,price,id;

    private BottomNavigationView bn;
    private String apartmentId="";
    private Map<String,Object> apartmentInfo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);
        bn = (BottomNavigationView) findViewById(R.id.bottom_nav);

        apartmentId = getIntent().getStringExtra("currentApartmentId");
        id = (TextView)findViewById(R.id.appartmentId) ;
        id.setText("Appartment ID: "+apartmentId);
        AppAddress = (TextView) findViewById(R.id.address);
        AppPassword = (TextView) findViewById(R.id.password);
        numOfRooms = (TextView) findViewById(R.id.rooms);
        details = (TextView) findViewById(R.id.details);
        price = (TextView) findViewById(R.id.priceRent);
//        bn.setSelectedItemId(R.id.nav_info);
        bn.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_info:
                        Log.i("roomie_welcome","starting bills");
                        startActivity(new Intent(ShowInfoActivity.this, ShowInfoActivity.class));
                        break;
                    case R.id.nav_bills:
                        break;
                    case R.id.nav_shoppong:
                        Log.i("roomie_welcome","starting bills");
                        startActivity(new Intent(ShowInfoActivity.this, ShoppingListActivity.class));
                        break;
                    case R.id.nav_home:
                        startActivity(new Intent(ShowInfoActivity.this, WelcomeUserActivity.class));
                        break;


                }

                return false;
            }
        });

        root.child(apartmentId).child("details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                apartmentInfo = (Map<String, Object>) snapshot.getValue();
                AppAddress.setText("Address: "+apartmentInfo.get("address").toString());
                AppPassword.setText("Password: "+apartmentInfo.get("password").toString());
                numOfRooms.setText("Number of rooms: "+apartmentInfo.get("numOfRoom").toString());
                details.setText(apartmentInfo.get("details").toString());
                price.setText("Price: "+apartmentInfo.get("price").toString());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}