package com.example.roomie_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BillsActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference root;
    private FirebaseAuth auth;
    String UserID;
    List<String> debt;
    long totalAmount=0;
    TextView showBills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
        root = db.getReference().child("Bills");
        UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        auth = FirebaseAuth.getInstance();
        debt = load_debt();
        setContentView(R.layout.activity_bills);
        showBills= findViewById(R.id.showBills);
        StringBuilder sb = new StringBuilder();
        for(String str:debt){
            sb.append(str).append("\n");
        }
        showBills.setText(sb.toString());

    }

    public ArrayList<String> load_debt(){
        ArrayList<String> ans = new ArrayList<>();
        DatabaseReference usersdRef = db.getReference().child("Users");
        DatabaseReference billsOwe = db.getReference().child("Bills");
        DatabaseReference billsOwed = db.getReference().child("Bills");

        Map<String,String> idToName = new HashMap<>();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String id = (String) ds.child("id").getValue();
                    String name = (String) ds.child("fullName").getValue();
                    idToName.put(id,name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);
        // i owe
        ValueEventListener billsOweListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String id = (String) ds.child("id").getValue();
                    long amount = (long) ds.child("amount").getValue();
                    totalAmount-=amount;
                    String messege = "You owe "+idToName.get(id)+" "+amount+" NIS";
                    ans.add(messege);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        billsOwe.addListenerForSingleValueEvent(billsOweListener);

        ValueEventListener billsOwedListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String id = (String) ds.child("id").getValue();
                    long amount = (long) ds.child("amount").getValue();
                    totalAmount+=amount;
                    String messege = idToName.get(id)+" owes you "+amount+" NIS";
                    ans.add(messege);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        billsOwed.addListenerForSingleValueEvent(billsOwedListener);

        return ans;
    }


}