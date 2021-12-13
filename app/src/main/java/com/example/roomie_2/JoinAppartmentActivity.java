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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinAppartmentActivity extends AppCompatActivity implements View.OnClickListener {

    EditText appartmentID,password;
    Button join;
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference Appartment = db.getReference().child("Appartments");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_appartment);
        appartmentID = (EditText) findViewById(R.id.appartmentId);
        password = (EditText) findViewById(R.id.password);
        join = (Button) findViewById(R.id.join);
        join.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String currentAppId = appartmentID.getText().toString();
        String currentPassword = password.getText().toString();
        Appartment.child(currentAppId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot appartment) {
                Log.i("hananell join new appartment","enter callback");
                if(appartment.exists()){

                    Appartment.child(currentAppId).child("Password").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.getValue(String.class).equals(currentPassword)){
                                Log.i("hananell join new appartment","currect password");
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                db.getReference().child("Users").child(userId).child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String fullName = snapshot.getValue(String.class);
                                        db.getReference().child("Users").child(userId).child("appartmentId").setValue(currentAppId);
                                        db.getReference().child("Users").child(userId).child("hasAppartment").setValue("True");
                                        db.getReference().child("Appartments").child(currentAppId).child("appartmentUsers").child(userId).setValue(fullName);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });








                                startActivity(new Intent(JoinAppartmentActivity.this,WelcomeActivity.class));
                            }
                            else{
                                Log.i("hananell join new appartment","wrong password "+snapshot.getValue().toString());

                            }



                            // add user id to appartment

                            //add appartment id to user

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });





                }
                else{
                    Log.i("hananell join new appartment","appartment does not exist");
                    // incorrect appartment id

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }
}