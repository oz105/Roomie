package com.example.roomie_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinApartmentActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText apartmentID,password;
    private Button join;
    private FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    private DatabaseReference Apartment = db.getReference().child("Apartments");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_apartment);
        apartmentID = (EditText) findViewById(R.id.appartmentId);
        password = (EditText) findViewById(R.id.password);
        join = (Button) findViewById(R.id.join);
        join.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        long currentAppId = Long.valueOf(apartmentID.getText().toString());
        String currentPassword = password.getText().toString();
        Apartment.child(String.valueOf(currentAppId)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot apartment) {
                Log.i("hananell new apartment","enter callback");
                if(apartment.exists()){
                    if(currentPassword.equals(apartment.child("details").child("password").getValue())) {
                        Log.i("hananell new apartment", "currect password");
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        db.getReference().child("Users").child(userId).child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String fullName = snapshot.getValue(String.class);
                                db.getReference().child("Users").child(userId).child("apartmentId").setValue(currentAppId);
                                db.getReference().child("Users").child(userId).child("hasApartment").setValue(true);
                                db.getReference().child("Apartments").child(String.valueOf(currentAppId)).child("apartmentUsers").child(userId).setValue(fullName);
                                startActivity(new Intent(JoinApartmentActivity.this, WelcomeUserActivity.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else{
                        Log.i("hananell new apartment","wrong password ");
                    }
                }
                else{
                    Log.i("hananell new apartment","apartment does not exist");
                    // incorrect apartment id

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }
}