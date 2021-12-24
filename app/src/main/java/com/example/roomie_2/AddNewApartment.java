package com.example.roomie_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewApartment extends AppCompatActivity {
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference root = db.getReference().child("Apartments");
    EditText AppAddress,AppPassword,details,numOfRooms,price;
    Button updateInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        AppAddress = (EditText) findViewById(R.id.address);
        AppPassword = (EditText) findViewById(R.id.password);
        numOfRooms = (EditText) findViewById(R.id.rooms);
        details = (EditText) findViewById(R.id.details);
        price = (EditText) findViewById(R.id.priceRent);



        updateInfo = (Button) findViewById(R.id.updateData);
        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
                Map<String,Object> apartmentInfo = new HashMap<>();
                apartmentInfo.put("address",AppAddress.getText().toString());
                apartmentInfo.put("numOfRoom",numOfRooms.getText().toString());
                apartmentInfo.put("price",price.getText().toString());
                apartmentInfo.put("password",AppPassword.getText().toString());
                apartmentInfo.put("details",details.getText().toString());
                add_apartment(apartmentInfo);


            }
        });


    }


    public String get_name_by_id(String id) throws InterruptedException {
        final String[] name = new String[1];

        db.getReference().child("Users").child(id).child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name[0] = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });


        return name[0];

    }

    public void add_apartment(Map<String,Object> details)  {

        String adminId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        db.getReference().child("apartmentIds").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Long> apartmentIdwList = null;
                long newApartmentId;
                if (snapshot.exists()) {
                    apartmentIdwList = (List<Long>) (snapshot.getValue());
                    newApartmentId = find_available_appartment_id(apartmentIdwList);
                } else {
                    apartmentIdwList = new ArrayList<>();
                    newApartmentId = 0;
                    apartmentIdwList.add(newApartmentId);
                }

                Apartment myApp = new Apartment(details, newApartmentId);
                db.getReference().child("apartmentIds").setValue(apartmentIdwList);
                root.child(String.valueOf(newApartmentId)).setValue(myApp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("hananell_addNewApartment", "complate try set value");
                        if (task.isSuccessful()) {
                            Log.i("hananell_addNewApartment", "success");
                            Toast.makeText(AddNewApartment.this, "Apartment has created successsfuly :)", Toast.LENGTH_LONG).show();
                            db.getReference().child("Users").child(adminId).child("hasApartment").setValue(true);
                            db.getReference().child("Users").child(adminId).child("apartmentId").setValue(newApartmentId);
                            startActivity(new Intent(AddNewApartment.this, WelcomeAdminActivity.class));
                        } else {
                            Log.i("hananell_addNewApartment", "failed");
                            Toast.makeText(AddNewApartment.this, "Creating new apartment failed :( , Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public long find_available_appartment_id(List<Long> apartmentId){
        long lastId = apartmentId.get(0);
        for (int i = 1; i < apartmentId.size(); i++) {
            if(apartmentId.get(i)!=lastId+1){
                apartmentId.add(i-1,lastId+1);
                return lastId+1;
            }
            lastId = apartmentId.get(i);
        }
        apartmentId.add(lastId+1);
        return lastId+1;
    }

    public void checkValidation(){
        Log.i("roomie_addNewApartment","check validation");


        if(AppPassword.getText().toString().isEmpty() ){
            AppPassword.setError("join password is required!");
            AppPassword.requestFocus();
            return;
        }
        if(AppAddress.getText().toString().isEmpty() ){
            AppAddress.setError("apartment address is required!");
            AppAddress.requestFocus();
            return;
        }
        if(details.getText().toString().isEmpty() ){
            AppAddress.setError("details is required!");
            AppAddress.requestFocus();
            return;
        }
        if(price.getText().toString().isEmpty() ){
            AppAddress.setError("price is required!");
            AppAddress.requestFocus();
            return;
        }
        if(numOfRooms.getText().toString().isEmpty() ){
            AppAddress.setError("number of rooms is required!");
            AppAddress.requestFocus();
            return;
        }

    }
}