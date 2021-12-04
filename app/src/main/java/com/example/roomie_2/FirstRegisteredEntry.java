package com.example.roomie_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

public class FirstRegisteredEntry extends AppCompatActivity implements View.OnClickListener {
    TextView newAppartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("hananell","create first registerentry");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_registered_entry);
        newAppartment = (TextView) findViewById(R.id.Create);
//        joinAppartment = (TextView) findViewById(R.id.join);
        newAppartment.setOnClickListener(this);
//        joinAppartment.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("hananell","on start");

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Create:
                startActivity(new Intent(this,AddNewAppartment.class));
                break;
//            case R.id.join:
//                //startActivity(new Intent(this,RegisterActivity.class));
        }

    }
}