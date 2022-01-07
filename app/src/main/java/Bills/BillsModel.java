package Bills;

import android.util.Log;

import androidx.annotation.NonNull;

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

public class BillsModel {



    private BillsController billController;
    private long apartmentId = 0;
    private String userId;
    private List<Bill> owe,owed,allBills;
    private Map<String,String> apartmentUsers=null;

    private FirebaseDatabase db;
    private DatabaseReference rootUser,rootApartment;
    private FirebaseAuth auth;




    public BillsModel(BillsController controller){
        this.billController = controller;
        db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
        rootUser = db.getReference().child("Users");
        auth = FirebaseAuth.getInstance();
        rootApartment = db.getReference().child("Apartments");

        this.userId = auth.getCurrentUser().getUid();
        this.rootUser.child(userId).child("apartmentId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

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

        rootApartment.child(""+apartmentId).addListenerForSingleValueEvent(new ValueEventListener() {
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
                Map<String,String> balance = new HashMap<>();
                if(owe.size()==0 && owed.size()==0){
                    balance.put("total","you has no debt");
                }
                else{
                    balance = get_current_bills_details(balance);
                }
                billController.show_bills(balance);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public Map<String,String> get_current_bills_details(Map<String,String> balance){
        StringBuilder oweSB = new StringBuilder();
        StringBuilder owedSB = new StringBuilder();

        int total = 0;
        for(Bill bill:owe){
            String name = apartmentUsers.get(bill.debtor);
            oweSB.append("you owes "+name+" "+bill.getAmount()+" NIS\n");
            total-=bill.getAmount();
        }
        for(Bill bill:owed){
            String name = apartmentUsers.get(bill.owesMoney);
            owedSB.append(name+ " owes you "+bill.getAmount()+" NIS\n");
            total+=bill.getAmount();
        }
        if(oweSB.length()!=0){
            balance.put("owe",oweSB.toString());
        }
        if(owedSB.length()!=0){
            balance.put("owed",owedSB.toString());
        }
        if(total<0){
            balance.put("total","Total: you owe "+Math.abs(total));

        }
        else if(total>0){
            balance.put("total","Total: you owed "+total);
        }
        else{
            balance.put("total","you has no debt");
        }
        return balance;
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
        rootApartment.child(""+apartmentId).child("billsList").setValue(allBills).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                billController.load_bills();
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
        rootApartment.child(""+apartmentId).child("billsList").setValue(allBills).addOnCompleteListener(new OnCompleteListener<Void>() {
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
