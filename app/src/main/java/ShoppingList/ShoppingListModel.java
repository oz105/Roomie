package ShoppingList;


import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

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
import java.util.List;

public class ShoppingListModel {

    public FirebaseDatabase db;
    public DatabaseReference root,userRoot,shopRoot;
    public FirebaseAuth auth;



    public ShoppingListController controller;
    public long apartmentId;
    private String userId;
    public  List<ShopItem> ListDB;

    // get apt
    public ShoppingListModel(ShoppingListController controller){
        this.controller = controller;
        db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
        userRoot = db.getReference().child("Users");
        auth = FirebaseAuth.getInstance();
        this.userId = auth.getCurrentUser().getUid();

    }

    //update DB
    public void UpdateFireBase(){
//        controller.ShopListView.LoadingDialog.show();
        shopRoot.setValue(ListDB).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    controller.ShopListView.Make_toast("Database Updated Successfully");
                }
                else
                {
                    controller.ShopListView.Make_toast("Database update failed.");
                }
//                controller.ShopListView.LoadingDialog.dismiss();
            }
        });
    }


    public void init_sopping_list(){
        this.userRoot.child(userId).child("apartmentId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.i("ShopController","c'tor - apartment id exist");
                    apartmentId = (long) snapshot.getValue();
                    shopRoot = db.getReference().child("Apartments").child(""+apartmentId).child("shoppingList");
                    load_shop_list();
                }
                else{
                    Log.i("ShopController","c'tor - apartment id does not exist");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void load_shop_list(){
        ListDB = new ArrayList<>();
        shopRoot.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ShopItem c = ds.getValue(ShopItem.class);
                    ListDB.add(c);
                }
                controller. init_list_adapter(ListDB);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.i("Shoplifter", "cant load list", databaseError.toException());
            }
        });


    }

    public void model_delete_item(int index){
        String curname = ListDB.get(index).name;
        ListDB.remove(index);
        UpdateFireBase();
    }
}
