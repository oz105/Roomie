package ShoppingList;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.example.roomie_2.R;


import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.roomie_2.WelcomeUserActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import Bills.BillsViewActivity;
import ShowInfo.ShowInfoView;

public class ShoppingListViewActivity extends AppCompatActivity implements View.OnClickListener {
    public FirebaseDatabase db;
    public DatabaseReference root,userRoot,shopRoot;
    public FirebaseAuth auth;

    public int aptNum;
    public int curPos;

    private BottomNavigationView bn;
    public Dialog AddDialog,editDialog;
    public Dialog LoadingDialog;
    EditText editQuantity,newN,newQ,editName;
    Button updateData;
    Button BackHome;
    Button addItem;

    public Button updateButton;
    public Button deleteButton;
    public Button cancelButton;
    public EditText editQ;
    LottieAnimationView animation2;
    LinearLayout shopContent;

    public ListView L;

    //public List<ShopItem> ListDB = new ArrayList<>();
    public ShopListAdapter sAdapter;
    public ShoppingListController ShopControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_view);

        db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
        root = db.getReference();
        userRoot = db.getReference().child("Users");
        shopRoot = root.child("Apartments").child(""+aptNum).child("shoppingList");
        auth = FirebaseAuth.getInstance();

        LoadingDialog = new Dialog(ShoppingListViewActivity.this);
        LoadingDialog.setContentView(R.layout.updating_data_dialog);

        editDialog = new Dialog(ShoppingListViewActivity.this);
        editDialog.setContentView(R.layout.editbox);

        AddDialog = new Dialog(ShoppingListViewActivity.this);
        AddDialog.setContentView(R.layout.add_item);

        bn = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bn.setSelectedItemId(R.id.nav_shoppong);

        //updateData = (Button)  findViewById(R.id.updateData);
        //updateData.setOnClickListener(this);

        BackHome = (Button)  findViewById(R.id.backButton);
        BackHome.setOnClickListener(this);

        addItem = (Button)  findViewById(R.id.addItem);
        addItem.setOnClickListener(this);

        updateButton = (Button) editDialog.findViewById(R.id.ChangeQ);
        deleteButton = (Button) editDialog.findViewById(R.id.itemDelete);
        cancelButton = (Button) editDialog.findViewById(R.id.cancelEdit);
        editQ = (EditText) editDialog.findViewById(R.id.inputQuantity);

        animation2 = AddDialog.findViewById(R.id.shopAnimation);

        shopContent = AddDialog.findViewById(R.id.shopContent);
        //Log.i("oncreate Shop","Shpinglis CREATE");
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ShopControl = new ShoppingListController(this); // now have apt
        ShopControl.load_ShopList(); // load from database and load listview.


        // finish. now listen to: add (after update), edit (after update), delete (after update)

        //loading list in controller.

        bn.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_info:
                        Log.i("roomie_welcome","starting bills");
                        startActivity(new Intent(ShoppingListViewActivity.this, ShowInfoView.class));
                    case R.id.nav_bills:
                        Log.i("roomie_welcome","starting bills");
                        startActivity(new Intent(ShoppingListViewActivity.this, BillsViewActivity.class));
                        break;
                    case R.id.nav_home:
                        startActivity(new Intent(ShoppingListViewActivity.this, WelcomeUserActivity.class));
                        break;
                }
                return false;
            }
        });

        Log.i("newRoot"," root contewnts are :"+root.child("Users").toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        bn.setSelectedItemId(R.id.nav_shoppong);

    }

    public void test(){
        shopContent.setVisibility(View.GONE);
        animation2.setVisibility(View.VISIBLE);
        animation2.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                AddDialog.dismiss();
                animation2.setVisibility(View.GONE);
                shopContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /*public void newItem(){
        AddDialog = new Dialog(ShoppingListViewActivity.this);
        AddDialog.setContentView(R.layout.add_item);
        AddDialog.show();
        Button doneAdd = (Button) AddDialog.findViewById(R.id.doneAdd);
        Button cancelButton = (Button) AddDialog.findViewById(R.id.cancelAdd);
        newN = (EditText) AddDialog.findViewById(R.id.newName);
        newQ = (EditText) AddDialog.findViewById(R.id.newQty);
        doneAdd.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancelAdd:
                Toast.makeText(ShoppingListViewActivity.this, "Dismissed.",
                        Toast.LENGTH_SHORT).show();
                AddDialog.dismiss();
                break;
            case R.id.doneAdd:
                if(Float.valueOf(newQ.getText().toString())<=0)
                {
                    Toast.makeText(ShoppingListViewActivity.this, "cant set this to a Value!",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    ShopItem cur = new ShopItem(newN.getText().toString(),Float.valueOf(newQ.getText().toString()));
                    ShopControl.ShopListModel.ListDB.add(cur);
                    Toast.makeText(ShoppingListViewActivity.this, "Item added Successfully :"+cur.name,
                            Toast.LENGTH_SHORT).show();
//                    AddDialog.dismiss();
                    test();
                    refresh_list();
                }
                break;
            case R.id.addItem:
                ShopControl.newItem();
                break;
            case R.id.backButton:
                refresh_list();
                finish();
                // ok
                break;
            case R.id.ChangeQ:
                //Button updateButton = (Button) editDialog.findViewById(R.id.ChangeQ);
                if(Float.valueOf(editQ.getText().toString())<=0)
                {
                    Toast.makeText(ShoppingListViewActivity.this, "cant set this to a Value!",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    ShopItem cur = new ShopItem(ShopControl.ShopListModel.ListDB.get(curPos).name,Float.valueOf(editQ.getText().toString()));
                    ShopControl.ShopListModel.ListDB.set(curPos,cur);
                    Toast.makeText(ShoppingListViewActivity.this, "Successful, "+cur.name+" Updated Quantity: "+cur.qty,
                            Toast.LENGTH_SHORT).show();
                    editDialog.dismiss();
                    refresh_list();
                }
                break;
            case R.id.itemDelete:
                String curname = ShopControl.ShopListModel.ListDB.get(curPos).name;
                ShopControl.ShopListModel.model_delete_item(curPos);
                sAdapter.notifyDataSetChanged();
                Make_toast(curname+ " deleted successfully");
                editDialog.dismiss();
                break;
            case R.id.cancelEdit:
                Make_toast("editing " + curPos +" Dismissed");
                editDialog.dismiss();
                break;

        }
    }

    public void Make_toast(String messege){
        Toast.makeText(this, messege, Toast.LENGTH_SHORT).show();

    }

    public void init_list(List<ShopItem> shopList){
        sAdapter = new ShopListAdapter(getApplicationContext(),shopList);
        L = (ListView) findViewById(R.id.list);
        L.setAdapter(sAdapter);
    }

    public void init_item_add(){
        Button doneAdd = (Button) AddDialog.findViewById(R.id.doneAdd);
        Button cancelButton = (Button) AddDialog.findViewById(R.id.cancelAdd);
        newN = (EditText) AddDialog.findViewById(R.id.newName);
        newQ = (EditText) AddDialog.findViewById(R.id.newQty);
        doneAdd.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    public void refresh_list(){
        sAdapter.notifyDataSetChanged();
        ShopControl.ShopListModel.UpdateFireBase();

    }

    public void init_edit_item(int index){
        curPos = index;
        Button updateButton = (Button) editDialog.findViewById(R.id.ChangeQ);
        Button deleteButton = (Button) editDialog.findViewById(R.id.itemDelete);
        Button cancelButton = (Button) editDialog.findViewById(R.id.cancelEdit);
        updateButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        final EditText editQ = (EditText) editDialog.findViewById(R.id.inputQuantity);
        editQ.setText(ShoppingListModel.ListDB.get(index).qty.toString());
    }

}