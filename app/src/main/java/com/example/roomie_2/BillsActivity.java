package com.example.roomie_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.concurrent.CompletableFuture;

public class BillsActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase db;
    private DatabaseReference root;
    private FirebaseAuth auth;
    String UserID;
    String appartmentID = "0";
    List<Bill> owe,owed;
    long totalAmount=0;
    TextView showBills;
    Button addBill,payment;
    Map<String,String> appartmentUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
        setContentView(R.layout.activity_bills);

        UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        root = db.getReference().child("Users").child(UserID);
        addBill = (Button) findViewById(R.id.newBill);
        payment = (Button) findViewById(R.id.payment);
        addBill.setOnClickListener(this);
        payment.setOnClickListener(this);
//        root.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                appartmentID =  snapshot.child("appartmentId").getValue(String.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        auth = FirebaseAuth.getInstance();
        owe = new ArrayList<>();
        owed = new ArrayList<>();
        load_debt(owe,owed);



        showBills= findViewById(R.id.showBills);
        StringBuilder sb = new StringBuilder();
        int total = 0;
        for(Bill bill:owe){
            String name = appartmentUsers.get(bill.from);
            sb.append("you owes "+name+" "+bill.amount+" NIS\n");
            total-=bill.amount;
        }
        for(Bill bill:owed){
            String name = appartmentUsers.get(bill.from);
            sb.append(name+ " owes you "+bill.amount+" NIS\n");
            total+=bill.amount;
        }
        if(total<0){
            sb.append("you owe "+total+" NIS");
        }
        else if(total>0){
            sb.append("you owed "+total+" NIS");

        }
        else{
            sb.append("you has no debt");
        }

        showBills.setText(sb.toString());

    }









    public void load_debt(List owe,List owed){
        DatabaseReference billsRef = db.getReference().child("Appartments").child(appartmentID).child("billsList");
        billsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Bill> list = dataSnapshot.getValue(List.class);
                for(Bill bill:list){
                    String x = bill.from;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.newBill):


            case R.id.payment:


        }


    }
}