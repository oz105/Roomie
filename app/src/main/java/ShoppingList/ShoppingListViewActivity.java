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
import android.widget.AdapterView;
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

//        BackHome = (Button)  findViewById(R.id.backButton);
//        BackHome.setOnClickListener(this);

        addItem = (Button)  findViewById(R.id.addItem);
        addItem.setOnClickListener(this);

//        pudateButton = (Button) editDialog.findViewById(R.id.ChangeQ);
        deleteButton = (Button) editDialog.findViewById(R.id.itemDelete);
        cancelButton = (Button) editDialog.findViewById(R.id.cancelEdit);
        editQ = (EditText) editDialog.findViewById(R.id.inputQuantity);

        animation2 = AddDialog.findViewById(R.id.shopAnimation);

        shopContent = AddDialog.findViewById(R.id.shopContent);

        ShopControl = new ShoppingListController(this); // now have apt
        ShopControl.load_ShopList(); // load from database and load listview.




        bn.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_info:
                        Log.i("roomie_welcome","starting bills");
                        startActivity(new Intent(ShoppingListViewActivity.this, ShowInfoView.class));
                        break;
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

    }

    @Override
    protected void onStart() {
        super.onStart();
        bn.setSelectedItemId(R.id.nav_shoppong);

    }

    public void end_shop_animation(){
        shopContent.setVisibility(View.GONE);
        animation2.setVisibility(View.VISIBLE);
        animation2.playAnimation();
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
                    newN.setText("");
                    newQ.setText("");
                    ShopControl.ShopListModel.ListDB.add(cur);
                    Toast.makeText(ShoppingListViewActivity.this, "Item added Successfully :"+cur.name,
                            Toast.LENGTH_SHORT).show();
//                    AddDialog.dismiss();
                    end_shop_animation();
                    refresh_list();
                }
                break;
            case R.id.addItem:
                ShopControl.newItem();
                break;
            case R.id.backButton:
                refresh_list();
                finish();
                break;
            case R.id.ChangeQ:
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
        AdapterView.OnItemClickListener tamir = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ShopControl.click_item(position);

            }
        };
        L.setOnItemClickListener(tamir);
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
        EditText editQ = (EditText) editDialog.findViewById(R.id.inputQuantity);
        String quantity = ShopControl.get_item_at_index(index);
        editQ.setText(quantity);
    }

}