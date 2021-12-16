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



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
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

                for(DataSnapshot ds:dataSnapshot.child("billsList").getChildren() ){
                    Bill temp = ds.getValue(Bill.class);
                    if(temp.from.equals(UserID)){
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

                names.addAll(apartmentUsers.values());
                names.remove(apartmentUsers.get(UserID));
                billAdapt = new BillParticipateAdapter(getApplicationContext(),names);
                TextView t = AddDialog.findViewById(R.id.paidBy);
                t.setText(apartmentUsers.get(UserID));
                listView = (ListView) AddDialog.findViewById(R.id.participateList);
                listView.setAdapter(billAdapt);
                AddDialog.show();

                done = (Button)AddDialog.findViewById(R.id.doneParticipate);
                done.setOnClickListener(this);
                break;







            case R.id.payment:



            case R.id.doneParticipate:
                Log.i("hananell","on click DONE participate");

                for (int i = 0; i < listView.getCount(); i++) {
                    View view =(View) listView.getItemAtPosition(i);
                    CheckBox cb = view.findViewById(R.id.check);
                    if(cb.isChecked()){

                        Log.i("haanell","itemSelect");
                    }

                }
                AddDialog.dismiss();

                //AddDialog.dismiss();




        }
    }



    public void calculate_bills(){




    }


}