package com.example.roomie_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeUserActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;
    private long currentApartmetId;

    private Button logout,bills, ShoppingBut, info;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);
        bills = (Button) findViewById(R.id.bills);
        bills.setOnClickListener(this);

        ShoppingBut = (Button) findViewById(R.id.shoppingList);
        ShoppingBut.setOnClickListener(this);

        info = (Button) findViewById(R.id.info);
        info.setOnClickListener(this);

        logout = (Button) findViewById(R.id.signOut);
        logout.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");
        userID = user.getUid();
        reference.child(userID).child("apartmentId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentApartmetId = (long)snapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bills:
                Log.i("roomie_welcome","starting bills");
                startActivity(new Intent(WelcomeUserActivity.this, BillsActivity.class));
                break;

            case R.id.shoppingList:
                Log.i("roomie_welcome","starting shoplist");
                startActivity(new Intent(WelcomeUserActivity.this, ShoppingListActivity.class));
                break;

            case R.id.info:
                Log.i("roomie_welcome","starting info");
                Intent intent = new Intent(WelcomeUserActivity.this, ShowInfoActivity.class);
                intent.putExtra("currentApartmentId", String.valueOf(currentApartmetId));
                startActivity(intent);

                break;

            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(WelcomeUserActivity.this, LoginActivity.class));
                break;
        }


    }
}