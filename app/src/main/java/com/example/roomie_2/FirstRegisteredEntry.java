package com.example.roomie_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

public class FirstRegisteredEntry extends AppCompatActivity implements View.OnClickListener {
    TextView newAppartment,join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("hananell first entry","create first registerentry");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_registered_entry);
        newAppartment = (TextView) findViewById(R.id.Create);
        join = (TextView) findViewById(R.id.join);
        newAppartment.setOnClickListener(this);
        join.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();


    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Create:
                Log.i("hananell first entry","create first appartment");
                startActivity(new Intent(this,AddNewAppartment.class));
                break;
            case R.id.join:
                startActivity(new Intent(this,JoinAppartmentActivity.class));
        }

    }
}