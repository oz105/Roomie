package com.example.roomie_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.List;

public class AddNewApartment extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference root = db.getReference().child("Apartments");
    EditText AppName,AppAddress,AppPassword;
    TextView create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_apartment);
        AppName = (EditText) findViewById(R.id.Name);
        AppAddress = (EditText) findViewById(R.id.Address);
        AppPassword = (EditText) findViewById(R.id.Password);
        create = (TextView) findViewById(R.id.create);
        create.setOnClickListener(this);
//        Submite = (Button) findViewById(R.id.create);
//        Submite.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create:
                Log.i("hananell_addNewApartment","after press create");
                checkValidation();
                try {
                    add_apartment(AppName.getText().toString(),AppAddress.getText().toString(),AppPassword.getText().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;

        }

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

    public void add_apartment(String AppName,String AppAddress,String AppPassword) throws InterruptedException {

        String adminId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.getReference().child("Users").child(adminId).child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db.getReference().child("apartmentIds").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Integer> apartmentIdwList=null;
                        int newApartmentId;
                        if(snapshot.exists()){
                            apartmentIdwList =(List<Integer>) (snapshot.getValue());
                            newApartmentId = find_available_appartment_id(apartmentIdwList);
                        }
                        else {
                            apartmentIdwList = new ArrayList<>();
                            newApartmentId = 0;
                            apartmentIdwList.add(newApartmentId);


                        }



                        String adminName = snapshot.getValue(String.class);
                        Apartment myApp = new Apartment(AppName,AppAddress,AppPassword,adminId,newApartmentId);
                        myApp.insert_name_and_id(adminId,adminName);
                        db.getReference().child("apartmentIds").setValue(apartmentIdwList);
                        root.child(String.valueOf(myApp.apartmentId)).setValue(myApp).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.i("hananell_addNewApartment","complate try set value");
                                if(task.isSuccessful()){
                                    Log.i("hananell_addNewApartment","success");
                                    Toast.makeText(AddNewApartment.this, "Apartment has created successsfuly :)", Toast.LENGTH_LONG).show();
                                    db.getReference().child("Users").child(adminId).child("hasApartment").setValue(true);
                                    db.getReference().child("Users").child(adminId).child("isAdmin").setValue(true);
                                    db.getReference().child("Users").child(adminId).child("apartmentId").setValue(myApp.apartmentId);
                                    startActivity(new Intent(AddNewApartment.this, WelcomeActivity.class));


                                }else{
                                    Log.i("hananell_addNewApartment","failed");

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public int find_available_appartment_id(List<Integer> apartmentId){
        int lastId = apartmentId.get(0);
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
        Log.i("hananell_addNewApartment","check validation");

        if(AppName.getText().toString().isEmpty() ){
            AppName.setError("Full name is required!");
            AppName.requestFocus();
            return;
        }
        if(AppPassword.getText().toString().isEmpty() ){
            AppPassword.setError("Full name is required!");
            AppPassword.requestFocus();
            return;
        }
        if(AppAddress.getText().toString().isEmpty() ){
            AppAddress.setError("Full name is required!");
            AppAddress.requestFocus();
            return;
        }
    }
}