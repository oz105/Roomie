package com.example.roomie_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import WelcomeOwner.WelcomeAdminActivity;

public class AdminUpdateInfo extends AppCompatActivity {
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference root = db.getReference().child("Apartments");
    EditText AppAddress,AppPassword,details,numOfRooms,price;
    TextView id;
    Button updateInfo;
    private String apartmentId="";
    private Map<String,Object> apartmentInfo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        apartmentId = getIntent().getStringExtra("currentApartmentId");
        id = (TextView)findViewById(R.id.appartmentId) ;
        id.setText("Appartment ID: "+apartmentId);
        AppAddress = (EditText) findViewById(R.id.address);
        AppPassword = (EditText) findViewById(R.id.password);
        numOfRooms = (EditText) findViewById(R.id.rooms);
        details = (EditText) findViewById(R.id.details);
        price = (EditText) findViewById(R.id.priceRent);
        updateInfo = (Button) findViewById(R.id.updateData);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.getReference().child("Apartments").child(apartmentId).child("details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                apartmentInfo = (Map<String, Object>) snapshot.getValue();
                AppAddress.setText(apartmentInfo.get("address").toString());
                AppPassword.setText(apartmentInfo.get("password").toString());
                numOfRooms.setText(apartmentInfo.get("numOfRoom").toString());
                details.setText(apartmentInfo.get("details").toString());
                price.setText(apartmentInfo.get("price").toString());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!AppPassword.getText().toString().isEmpty() ){
                    apartmentInfo.put("password",AppPassword.getText().toString());
                }
                if(!AppAddress.getText().toString().isEmpty() ){
                    apartmentInfo.put("address",AppAddress.getText().toString());
                }
                if(!details.getText().toString().isEmpty() ){
                    apartmentInfo.put("details",details.getText().toString());
                }
                if(!price.getText().toString().isEmpty() ){
                    apartmentInfo.put("price",price.getText().toString());
                }
                if(!numOfRooms.getText().toString().isEmpty() ){
                    apartmentInfo.put("numOfRoom",numOfRooms.getText().toString());
                }
                root.child(apartmentId).child("details").setValue(apartmentInfo);
                Intent intent = new Intent(AdminUpdateInfo.this, WelcomeAdminActivity.class);
                intent.putExtra("currentApartmentId", apartmentId);
                startActivity(intent);

            }
        });


    }



}