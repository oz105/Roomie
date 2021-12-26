package com.example.roomie_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeAdminActivity extends AppCompatActivity implements View.OnClickListener {
    private Button info,notfications,logout;
    private String apartmentId="";
    private FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    private DatabaseReference root = db.getReference().child("Apartments");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_admin);
        apartmentId = getIntent().getStringExtra("currentApartmentId");
        info = (Button) findViewById(R.id.info);
        notfications = (Button) findViewById(R.id.notfication);
        logout = (Button) findViewById(R.id.logout);
        info.setOnClickListener(this);
        notfications.setOnClickListener(this);
        logout.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.info):
                Intent intent = new Intent(WelcomeAdminActivity.this, AdminUpdateInfo.class);
                intent.putExtra("currentApartmentId", apartmentId);
                startActivity(intent);
            case (R.id.notfication):
                break;

            case (R.id.logout):
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(WelcomeAdminActivity.this, LoginActivity.class));
                break;



        }


    }
}