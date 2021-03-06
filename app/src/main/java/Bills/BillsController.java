package Bills;

import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.roomie_2.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BillsController {

    private BillsModel model;
    public BillsViewActivity billsView;
    List<NameAndUid> names;
    private int payToIndex;


    public BillsController(BillsViewActivity bills){
        this.billsView = bills;
        this.model = new BillsModel(this);

    }

    public void load_bills(){
        billsView.progressBar.setVisibility(View.VISIBLE);
        model.load_debt_and_names();
    }

    public void show_bills(Map<String,String> balance){
        if(balance.get("owe")==null){
            billsView.owe.setVisibility(View.GONE);
        }
        else {
            billsView.owe.setText(balance.get("owe"));
        }
        if(balance.get("owed")==null){
            billsView.owed.setVisibility(View.GONE);
        }
        else {
            billsView.owed.setText(balance.get("owed"));
        }
        billsView.total.setText(balance.get("total"));



        if(billsView.addBillDialog!=null){
            billsView.addBillDialog.dismiss();
        }
        if(billsView.paymentDialog!=null){
            billsView.paymentDialog.dismiss();
        }
        billsView.progressBar.setVisibility(View.GONE);

    }

    public void init_names(){
        names =new ArrayList<>();
        for (Map.Entry<String, String> entry : model.getApartmentUsers().entrySet()) {
            if(entry.getKey().equals(model.getUserId())) continue;
            NameAndUid temp = new NameAndUid(entry.getValue(), entry.getKey());
            names.add(temp);
        }

    }

    public void open_new_bill(){
        if(names==null){
            init_names();
        }
        BillParticipateAdapter participateAdapter = new BillParticipateAdapter(billsView.getApplicationContext(),names, R.layout.add_bill_participate);
        billsView.newBillListView.setAdapter(participateAdapter);
        billsView.paidBy.setText(model.getApartmentUsers().get(model.getUserId()));
        billsView.addBillDialog.show();
    }
    public void finish_adding_new_bill(){
        billsView.progressBar.setVisibility(View.VISIBLE);
        List<String> participateUid = new ArrayList<>();
        for (int i = 0; i < billsView.newBillListView.getCount(); i++) {
            CheckBox cb = (CheckBox) (billsView.newBillListView.getChildAt(i).findViewById(R.id.check));
            if(cb.isChecked()){
                participateUid.add(names.get(i).uid);
            }
        }
        double amount = Double.valueOf(billsView.newBillAmount.getText().toString());
        model.calculate_new_bill(participateUid,amount);
    }

    public void open_payment(){
        payToIndex = -1;
        if(names==null){
            init_names();
        }
        BillParticipateAdapter participateAdapter = new BillParticipateAdapter(billsView.getApplicationContext(),names,R.layout.add_payment_participate);
        billsView.paymentListView.setAdapter(participateAdapter);
        billsView.paymentPayBy.setText(model.getApartmentUsers().get(model.getUserId()));
        billsView.paymentDialog.show();
    }

    public void choose_pay_to(int position){
        payToIndex = position;
        billsView.payFor.setText(names.get(payToIndex).name);
    }

    public void finish_payment(){
        if(payToIndex==-1){
           billsView.Make_toast("You need to choose who to pay");
        }
        else{
            double amount = Double.valueOf(billsView.paymentAmount.getText().toString());
            model.update_payment(names.get(payToIndex).uid,amount);

        }

    }
    public void make_toast(String messege){
        Toast.makeText(this.billsView, messege, Toast.LENGTH_SHORT).show();
    }
}
