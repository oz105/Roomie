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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;

public class AddNewAppartment extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference root = db.getReference().child("Appartments");
    EditText AppName,AppAddress,AppPassword;
    TextView create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_appartment);
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
                Log.i("hananell_addNewAppartment","after press create");
                checkValidation();
                try {
                    add_appartment(AppName.getText().toString(),AppAddress.getText().toString(),AppPassword.getText().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;

        }

//        v.get
//        Appartment aprt = new Appartment(AppName.toString(),AppAddress.toString(),AppPassword.toString(),)

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

    public void add_appartment(String AppName,String AppAddress,String AppPassword) throws InterruptedException {

        String adminId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.getReference().child("Users").child(adminId).child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String adminName = snapshot.getValue(String.class);
                Bill tempBill = new Bill("Mhibatv7QMMrxAivhzsYoNKf5Ak2","vNtSV00uM6XyKi0JZrBxsdKwrnQ2",100);
                Appartment myApp = new Appartment(AppName,AppAddress,AppPassword,adminId);
                myApp.billsList.add(tempBill);
                myApp.insert_name_and_id(adminId,adminName);
                root.child(String.valueOf(myApp.appartmentId)).setValue(myApp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("hananell_addNewAppartment","complate try set value");
                        if(task.isSuccessful()){
                            Log.i("hananell_addNewAppartment","success");
                            Toast.makeText(AddNewAppartment.this, "Appartment has created successsfuly, Please try again", Toast.LENGTH_LONG).show();
                            db.getReference().child("Users").child(adminId).child("hasAppartment").setValue("True");
                            db.getReference().child("Users").child(adminId).child("isAdmin").setValue("True");
                            db.getReference().child("Users").child(adminId).child("appartmentId").setValue(myApp.appartmentId);
                            startActivity(new Intent(AddNewAppartment.this, WelcomeActivity.class));


                        }else{
                            Log.i("hananell_addNewAppartment","failed");

                            Toast.makeText(AddNewAppartment.this, "Creating new appartment failed :( , Please try again", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkValidation(){
        Log.i("hananell_addNewAppartment","check validation");

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