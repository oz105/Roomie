package com.example.roomie_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewAppartment extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference root = db.getReference().child("Appartments");
    EditText AppName,AppAddress,AppPassword,Submite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_appartment);
        AppName = (EditText) findViewById(R.id.Name);
        AppAddress = (EditText) findViewById(R.id.Address);
        AppPassword = (EditText) findViewById(R.id.Password);
        Submite.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
//        checkValidation();
//        v.get
//        Appartment aprt = new Appartment(AppName.toString(),AppAddress.toString(),AppPassword.toString(),)





    }

    public void checkValidation(){
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