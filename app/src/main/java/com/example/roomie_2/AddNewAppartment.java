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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                Log.i("hananell","after press create");
                checkValidation();
                add_appartment(AppName.toString(),AppAddress.toString(),AppPassword.toString());

                break;

        }

//        v.get
//        Appartment aprt = new Appartment(AppName.toString(),AppAddress.toString(),AppPassword.toString(),)

    }

    public void add_appartment(String AppName,String AppAddress,String AppPassword){

        String adminId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Appartment myApp = new Appartment(AppName,AppAddress,AppPassword,adminId);
        Log.i("hananell","befor save in data base");

        root.setValue(myApp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i("hananell","success");

                    Toast.makeText(AddNewAppartment.this, "Creating new appartment failed :( , Please try again", Toast.LENGTH_LONG).show();



                }else{
                    Log.i("hananell","failed");

                    Toast.makeText(AddNewAppartment.this, "Creating new appartment failed :( , Please try again", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void checkValidation(){
        Log.i("hananell","check validation");

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