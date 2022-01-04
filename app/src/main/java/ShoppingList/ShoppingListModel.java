package ShoppingList;


import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListModel {

    public ShoppingListController controller;
    public long apartmentId;
    private String userId;
    public static List<ShopItem> ListDB;

    // get apt
    public ShoppingListModel(ShoppingListController controller){
        this.controller = controller;

        this.userId = controller.ShopListView.auth.getCurrentUser().getUid();
        this.controller.ShopListView.userRoot.child(userId).child("apartmentId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.i("ShopController","c'tor - apartment id exist");
                    apartmentId = (long) snapshot.getValue();
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

    //update DB
    public void UpdateFireBase(){
//        controller.ShopListView.LoadingDialog.show();
        controller.ShopListView.shopRoot.setValue(ListDB).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public void load_shop_list(){
        ListDB = new ArrayList<>();
        controller.ShopListView.shopRoot.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ShopItem c = ds.getValue(ShopItem.class);
                    ListDB.add(c);
                }
                controller.ShopListView.init_list(ListDB);
                AdapterView.OnItemClickListener tamir = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        controller.ShopListView.Make_toast("Pressed item pos: "+position);
                        Log.i("Tamir shopping","ONintemCLICK"+position);
                        Log.i("Tamir shopping","afterClick"+position);
                        controller.editItem(ListDB.get(position).name,position);
                    }
                };
                controller.ShopListView.L.setOnItemClickListener(tamir);
                Log.i("warning","dataChanged of Shoplist");
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
    }
}
