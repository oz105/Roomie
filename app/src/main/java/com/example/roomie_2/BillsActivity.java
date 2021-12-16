package com.example.roomie_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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
    String apartmentID = "20";
    List<Bill> owe,owed;
    long totalAmount=0;
    TextView showBills;
    Button addBill,payment,done;
    Map<String,String> apartmentUsers=new HashMap<>();
    private ProgressBar progressBar;
    private ListView listView;
    BillParticipateAdapter billAdapt;
    List<String> names = new ArrayList<>();
    Dialog AddDialog;
    EditText editText;
    List<Bill> currentBils;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i("Bills activity","ON CREATE");

        db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
        setContentView(R.layout.activity_bills);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        root = db.getReference().child("Users").child(UserID);
        addBill = (Button) findViewById(R.id.newBill);
        payment = (Button) findViewById(R.id.payment);

        addBill.setOnClickListener(this);
        payment.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.VISIBLE);
        load_debt();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Bills activity","ON START");
    }

    public void load_debt(){
        DatabaseReference billsRef = db.getReference().child("Apartments").child(apartmentID);
        billsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(apartmentUsers.isEmpty()){
                    Log.i("hananell","show bills");
                    apartmentUsers = (Map<String,String>)dataSnapshot.child("apartmentUsers").getValue();
                }

                owe = new ArrayList<>();
                owed = new ArrayList<>();
                currentBils = new ArrayList<>();

                for(DataSnapshot ds:dataSnapshot.child("billsList").getChildren() ){
                    Bill temp = ds.getValue(Bill.class);
                    if(temp.from.equals(UserID)){
                        currentBils.add(temp);
                        owed.add(temp);
                    }
                    else if(temp.to.equals(UserID)){
                        owe.add(temp);
                    }
                }

                showBills= findViewById(R.id.showBills);
                StringBuilder sb = new StringBuilder();
                int total = 0;
                for(Bill bill:owe){
                    String name = apartmentUsers.get(bill.from);
                    sb.append("you owes "+name+" "+bill.amount+" NIS\n");
                    total-=bill.amount;
                }
                for(Bill bill:owed){
                    String name = apartmentUsers.get(bill.to);
                    sb.append(name+ " owes you "+bill.amount+" NIS\n");
                    total+=bill.amount;
                }
                if(total<0){
                    sb.append("Total: you owe "+Math.abs(total)+" NIS");
                }
                else if(total>0){
                    sb.append("Total: you owed "+total+" NIS");

                }
                else{
                    sb.append("you has no debt");
                }
                progressBar.setVisibility(View.GONE);
                showBills.setText(sb.toString());




            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newBill:
                Log.i("hananell","on click new bill before dialog");
                AddDialog = new Dialog(BillsActivity.this);

                AddDialog.setContentView(R.layout.add_bill);

                Log.i("hananell","on click new bill after dialog");
                names.clear();
                names.addAll(apartmentUsers.values());
                names.remove(apartmentUsers.get(UserID));
                billAdapt = new BillParticipateAdapter(getApplicationContext(),names);
                TextView t = AddDialog.findViewById(R.id.paidBy);
                editText = (EditText) AddDialog.findViewById(R.id.amount);

                t.setText("Paid by "+apartmentUsers.get(UserID)+" THE KONG!!!***");
                listView = (ListView) AddDialog.findViewById(R.id.participateList);
                listView.setAdapter(billAdapt);
                AddDialog.show();

                done = (Button)AddDialog.findViewById(R.id.doneParticipate);
                done.setOnClickListener(this);
                break;







            case R.id.payment:
            case R.id.doneParticipate:
                List<String> participateId = new ArrayList<>();

                for (int i = 0; i < listView.getCount(); i++) {
                   String str = listView.getItemAtPosition(i).toString();
                    CheckBox cb = (CheckBox)listView.getChildAt(i).findViewById(R.id.check);
                    if(cb.isChecked()){
                        for(Map.Entry<String, String> entry : apartmentUsers.entrySet()) {
                            if(str.equals(entry.getValue())){
                                participateId.add(entry.getKey());
                            }
                        }
                    }
                }
                double amountEach = Double.valueOf(editText.getText().toString())/participateId.size()+1;
                for(String uid:participateId){
                    boolean exist = false;
                    for(Bill b:currentBils){
                        if(b.to.equals(uid)){
                            exist=true;
                            b.amount+=amountEach;
                        }
                    }
                    if(!exist){
                        Bill newBill = new Bill(UserID,uid,amountEach);
                        currentBils.add(newBill);
                    }
                }
                db.getReference().child("Apartments").child(apartmentID).child("billsList").setValue(currentBils).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.i("","");
                            AddDialog.dismiss();
//                            finish();
//                            startActivity(getIntent());
                        }
                    }
                });



                //AddDialog.dismiss();




        }
    }



    public void calculate_bills(){




    }


}