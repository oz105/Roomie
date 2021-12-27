package com.example.roomie_2;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillsModel {



    BillsController billController;
    long apartmentId;
    private String userId;
    private List<Bill> owe,owed,allBills;

    private Map<String,String> apartmentUsers;




    public BillsModel(BillsController controller){
        this.billController = controller;

        this.userId = billController.billsView.auth.getCurrentUser().getUid();
        this.billController.billsView.rootUser.child(userId).child("apartmentId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.i("BillsControler","c'tor - apartment id exist");
                    apartmentId = (long) snapshot.getValue();
                }
                else{
                    Log.i("BillsControler","c'tor - apartment id does not exist");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void load_debt_and_names(){
        allBills = new ArrayList<>();
        owe = new ArrayList<>();
        owed = new ArrayList<>();
        billController.billsView.rootApartment.child(""+apartmentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(apartmentUsers==null){
                    apartmentUsers= (Map<String,String>)snapshot.child("apartmentUsers").getValue();
                }
                DataSnapshot billsSnap = snapshot.child("billsList");
                if(billsSnap.exists()){
                    for(DataSnapshot bills: billsSnap.getChildren()){
                        Bill tempBill = bills.getValue(Bill.class);
                        if(tempBill.debtor.equals(userId)){
                            owed.add(tempBill);
                        }
                        else if(tempBill.owesMoney.equals(userId)){
                            owe.add(tempBill);
//                            allBills.add(tempBill);
                        }
                        else {
                            allBills.add(tempBill);
                        }

                    }
                }
                else{
                    //

                }
                String totalBalance="";
                if(owe.size()==0 && owed.size()==0){
                    totalBalance = "you has no debt";
                }
                else{
                    totalBalance = get_current_bills_details();
                }
                billController.show_bills(totalBalance);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public String get_current_bills_details(){
        StringBuilder sb = new StringBuilder();
        int total = 0;
        for(Bill bill:owe){
            String name = apartmentUsers.get(bill.debtor);
            sb.append("you owes "+name+" "+bill.getAmount()+" NIS\n");
            total-=bill.getAmount();
        }
        for(Bill bill:owed){
            String name = apartmentUsers.get(bill.owesMoney);
            sb.append(name+ " owes you "+bill.getAmount()+" NIS\n");
            total+=bill.getAmount();
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
        return sb.toString();
    }

    public void calculate_new_bill(List<String> participateUid,double amount){
        double amountForEach = amount/(participateUid.size()+1);
        for (String name:participateUid){
            boolean exist = false;
            for(Bill bill:owed){
                if(bill.owesMoney.equals(name)){
                    exist = true;
                    bill.add_to_amount(amountForEach);
                }
            }
            if(!exist){
                Bill newBill = new Bill(userId,name,amountForEach);
                allBills.add(newBill);
            }
        }
        allBills.addAll(owed);
        allBills.addAll(owe);
        update_bills();
    }




    public void update_bills(){
        List<Bill> billToRemove = new ArrayList<>();
        for(Bill bill:allBills){
            for(Bill otherBill:allBills){
                if(bill.owesMoney.equals(otherBill.debtor) && bill.debtor.equals(otherBill.owesMoney)&& bill.getAmount()!=0 && otherBill.getAmount()!=0){
                    if(bill.getAmount()>otherBill.getAmount()){
                        bill.reduce_from_amount(otherBill.getAmount());
                        otherBill.init_amount();
                        billToRemove.add(otherBill);
                    }
                    else if(bill.getAmount()<otherBill.getAmount()){
                        otherBill.reduce_from_amount(bill.getAmount());
                        bill.init_amount();
                        billToRemove.add(bill);
                    }
                    else {
                        bill.init_amount();
                        otherBill.init_amount();
                        billToRemove.add(bill);
                        billToRemove.add(otherBill);
                    }
                    break;
                }
            }
        }
        allBills.removeAll(billToRemove);
        billController.billsView.rootApartment.child(""+apartmentId).child("billsList").setValue(allBills).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                load_debt_and_names();
            }
        });


    }

    public void update_payment(String payForUid,double amount){
        for(Bill bill:owe){
            if(bill.debtor.equals(payForUid)){
                if(Math.abs(bill.getAmount()-amount)<0.0999){
                    owe.remove(bill);
                    break;
                }
                else if(bill.getAmount()<amount){
                    billController.make_toast("Cannot pay more then the debt!");
                    return;
                }
                else{
                    bill.reduce_from_amount(amount);
                    break;
                }
            }
        }
        allBills.addAll(owe);
        allBills.addAll(owed);
        billController.billsView.rootApartment.child(""+apartmentId).child("billsList").setValue(allBills).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                load_debt_and_names();
            }
        });


    }






    public Map<String, String> getApartmentUsers() {
        return apartmentUsers;
    }

    public String getUserId() {
        return userId;
    }
}
