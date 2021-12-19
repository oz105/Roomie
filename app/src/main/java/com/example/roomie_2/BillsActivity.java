package com.example.roomie_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;

public class BillsActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase db;
    private DatabaseReference root;
    private FirebaseAuth auth;
    private String UserID,currentUserToPay="";
    private long apartmentID ;
    private  List<Bill> owe,owed,currentBils;
    private long totalAmount=0;
    private TextView showBills;
    private Button addBill,payment, doneAddBill,donePayment;
    private Map<String,String> apartmentUsers=new HashMap<>();
    private ProgressBar progressBar;
    private ListView listView;
    private BillParticipateAdapter billAdapt;
    private List<String> names = null;
    private Dialog addBillDialog,paymentDialog;
    private EditText editText;



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

        root.child("apartmentId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                apartmentID = (long) snapshot.getValue();
                load_debt();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Bills activity","ON START");
    }

    public void load_debt(){
        DatabaseReference billsRef = db.getReference().child("Apartments").child(String.valueOf(apartmentID));
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
                        currentBils.add(temp);
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

    public void init_names(){
        names = new ArrayList<>();
        names.addAll(apartmentUsers.values());
        names.remove(apartmentUsers.get(UserID));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newBill:
                Log.i("hananell","on click new bill before dialog");
                addBillDialog = new Dialog(BillsActivity.this);

                addBillDialog.setContentView(R.layout.add_bill);

                Log.i("hananell","on click new bill after dialog");
                if(names==null){
                    init_names();
                }
                billAdapt = new BillParticipateAdapter(getApplicationContext(),names,R.layout.add_bill_participate);
                TextView t = addBillDialog.findViewById(R.id.paidBy);
                editText = (EditText) addBillDialog.findViewById(R.id.amount);

                t.setText("Paid by "+apartmentUsers.get(UserID)+" THE KONG!!!***");
                listView = (ListView) addBillDialog.findViewById(R.id.participateList);
                listView.setAdapter(billAdapt);
                addBillDialog.show();

                doneAddBill = (Button) addBillDialog.findViewById(R.id.doneParticipate);
                doneAddBill.setOnClickListener(this);
                break;


            case R.id.payment:
                paymentDialog = new Dialog(BillsActivity.this);

                paymentDialog.setContentView(R.layout.add_payment);
                if(names==null){
                    init_names();
                }



                BillParticipateAdapter paymentAdapt = new BillParticipateAdapter(getApplicationContext(),names,R.layout.add_payment_participate);
                TextView paymentPaidBy = paymentDialog.findViewById(R.id.PaimentPaidBy);
                editText = (EditText) paymentDialog.findViewById(R.id.amount);
                paymentPaidBy.setText("Paid by "+apartmentUsers.get(UserID));
                ListView payListView = (ListView) paymentDialog.findViewById(R.id.payParticipateList);
                payListView.setAdapter(paymentAdapt);

                payListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        currentUserToPay = payListView.getItemAtPosition(position).toString();
                        String messege = "Paid by: "+apartmentUsers.get(UserID)+" TO "+currentUserToPay;
                        paymentPaidBy.setText(messege);
                    }

                });

                paymentDialog.show();

                donePayment = (Button)paymentDialog.findViewById(R.id.donePayment);
                donePayment.setOnClickListener(this);
                break;



            case R.id.donePayment:
                if(currentUserToPay==null){
                    Toast.makeText(BillsActivity.this, "didn't choose user for payment",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                double amount = Double.valueOf(((EditText)(paymentDialog.findViewById(R.id.paymentAmount))).getText().toString());
                String payToUid="";
                for(Map.Entry<String, String> entry : apartmentUsers.entrySet()) {
                    if(currentUserToPay.equals(entry.getValue())){
                        payToUid=entry.getKey();
                    }
                }
                Boolean hasPay=false;
                for(Bill b:currentBils){
                    if(b.from.equals(payToUid) && b.to.equals(UserID)){
                        if(amount>b.amount){
                            Toast.makeText(BillsActivity.this, "The debt amount is: "+b.amount+", payment cannot be more then this amount!",
                                    Toast.LENGTH_SHORT).show();
                            break;

                        }
                        else if(amount<b.amount){
                            b.amount-=amount;
                            hasPay=true;
                        }
                        else{
                            currentBils.remove(b);
                            hasPay=true;
                        }
                    }
                }
                if(hasPay){
                    db.getReference().child("Apartments").child(String.valueOf(apartmentID)).child("billsList").setValue(currentBils).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(BillsActivity.this, "payment success :)",
                                        Toast.LENGTH_SHORT).show();

                                paymentDialog.dismiss();
//                            finish();
//                            startActivity(getIntent());
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(BillsActivity.this, "payment faild, you dont have debt for "+currentUserToPay,
                            Toast.LENGTH_SHORT).show();
                }
                break;


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
                    double amountEach = Double.valueOf(editText.getText().toString())/(participateId.size()+1);

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
                    db.getReference().child("Apartments").child(String.valueOf(apartmentID)).child("billsList").setValue(currentBils).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.i("","");
                                addBillDialog.dismiss();
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