package com.example.roomie_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BillsViewActivity extends AppCompatActivity implements View.OnClickListener {
    public FirebaseDatabase db;
    public DatabaseReference rootUser,rootApartment;
    public FirebaseAuth auth;

    public TextView showBills,paidBy;
    public Button addBill,payment, doneAddBill,donePayment;
    public EditText newBillAmount,paymentAmount;
    public TextView paymentPayBy;

    public ProgressBar progressBar;
    public BottomNavigationView bn;
    public Dialog addBillDialog,paymentDialog;

    public BillsController billController;
    public BillParticipateAdapter participateAdapter,paymentAdapt;
    public ListView newBillListView,paymentListView;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_view);
        db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
        rootUser = db.getReference().child("Users");
        auth = FirebaseAuth.getInstance();
        rootApartment = db.getReference().child("Apartments");

        addBill = (Button) findViewById(R.id.newBill);
        payment = (Button) findViewById(R.id.payment);
        bn = (BottomNavigationView) findViewById(R.id.bottom_nav);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        showBills = (TextView) findViewById(R.id.showBills);

        addBill.setOnClickListener(this);
        payment.setOnClickListener(this);
        billController = new BillsController(this);
        billController.load_bills();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newBill:
                addBillDialog = new Dialog(this);
                addBillDialog.setContentView(R.layout.add_bill);
                paidBy = (TextView) addBillDialog.findViewById(R.id.paidBy);
                newBillListView = (ListView)addBillDialog.findViewById(R.id.participateList);
                doneAddBill = (Button)addBillDialog.findViewById(R.id.doneParticipate);
                doneAddBill.setOnClickListener(this);
                billController.open_new_bill();
                break;

            case R.id.doneParticipate:
                newBillAmount = (EditText) addBillDialog.findViewById(R.id.amount);
                billController.finish_adding_new_bill();
                break;

            case R.id.payment :
                paymentDialog = new Dialog(this);
                paymentDialog.setContentView(R.layout.add_payment);
                paymentPayBy = (TextView) paymentDialog.findViewById(R.id.PaimentPaidBy);
                paymentAmount = (EditText)paymentDialog.findViewById(R.id.paymentAmount);
                paymentListView = (ListView) paymentDialog.findViewById(R.id.payParticipateList);
                donePayment = (Button) paymentDialog.findViewById(R.id.donePayment);
                donePayment.setOnClickListener(this);
                billController.open_payment();
                paymentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        billController.choose_pay_to(position);
                    }

                });
                break;

            case R.id.donePayment:
                billController.finish_payment();
                break;
        }
    }
    public void Make_toast(String messege){
        Toast.makeText(this, messege, Toast.LENGTH_SHORT).show();

    }
}