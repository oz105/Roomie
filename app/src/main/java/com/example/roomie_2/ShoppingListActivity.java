package com.example.roomie_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference root = db.getReference();
    ListView L;
    EditText editQuantity;
    EditText editName;
    Button updateData;
    Button BackHome;
    Button addItem;
    public int aptNum;
    public List<ShopItem> ListDB = new ArrayList<>();
    public ShopListAdapter sAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("oncreate Shop","Shpinglis CREATE");
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        root.child("Users").child(user).child("apartmentId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("datasnapCheckAPT",dataSnapshot.getValue().toString());
                long x = (long) dataSnapshot.getValue();
                aptNum = Math.toIntExact(x);
                Log.i("datasnap","aptnum is :"+aptNum);

                root.child("Apartments").child(""+aptNum).child("shoppingList").addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            ShopItem c = ds.getValue(ShopItem.class);
                            ListDB.add(c);
                        }

//                Log.i("datasnap","data is : "+ListDB.isEmpty());
                        Log.i("datasnap","listDb [0] is : "+ListDB.get(0));
                        //refreshAdapter(ListDB);

                        Log.i("!!!ShoplistAdapter!!!","list size is "+ListDB.size());
                        sAdapter = new ShopListAdapter(getApplicationContext(),ListDB);
                        ListView listView = (ListView) findViewById(R.id.list);

                        listView.setAdapter(sAdapter);
                        AdapterView.OnItemClickListener tamir = new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(ShoppingListActivity.this, "Pressed item pos: "+position,
                                        Toast.LENGTH_SHORT).show();
                                Log.i("Tamir shopping","ONintemCLICK"+position);
                                Log.i("Tamir shopping","afterClick"+position);
                                editItem(ListDB.get(position).name,position);
                            }
                        };
                        listView.setOnItemClickListener(tamir);
                        Log.i("warning","dataChanged of Shoplist");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.i("Shoplifter", "cant load list", databaseError.toException());
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read from shoplist failed: " + databaseError.getCode());

            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        updateData = (Button)  findViewById(R.id.updateData);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateFireBase();
            }
        });
        BackHome = (Button)  findViewById(R.id.backButton);
        BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addItem = (Button)  findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newItem();
            }
        });
        Log.i("newRoot"," root contewnts are :"+root.child("Users").toString());

    }



    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(ShoppingListActivity.this,"ON START", Toast.LENGTH_SHORT).show();
        Log.i("Tamir shopping","ONSTART");

    }

    public void click(ListView lv, AdapterView.OnItemClickListener ot){
        lv.setOnItemClickListener(ot);
    }

    public void editItem(String oldItem, final int index){
        final Dialog editDialog = new Dialog(ShoppingListActivity.this);
        editDialog.setTitle("editing "+oldItem);
        editDialog.setContentView(R.layout.editbox);
        TextView msg = (TextView) editDialog.findViewById(R.id.updateDialog);
        final EditText editQ = (EditText) editDialog.findViewById(R.id.inputQuantity);
        editQ.setText(ListDB.get(index).qty.toString());
        editDialog.show();
        Log.i("Show","Show dialog");
        Button updateButton = (Button) editDialog.findViewById(R.id.ChangeQ);
        Button deleteButton = (Button) editDialog.findViewById(R.id.itemDelete);
        Button cancelButton = (Button) editDialog.findViewById(R.id.cancelEdit);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Float.valueOf(editQ.getText().toString())<=0)
                {
                    Toast.makeText(ShoppingListActivity.this, "cant set this to a Value!",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    ShopItem cur = new ShopItem(ListDB.get(index).name,Float.valueOf(editQ.getText().toString()));
                    ListDB.set(index,cur);
                    Toast.makeText(ShoppingListActivity.this, "Successful, "+cur.name+" Updated Quantity: "+cur.qty,
                            Toast.LENGTH_SHORT).show();
                    editDialog.dismiss();
                    sAdapter.notifyDataSetChanged();
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curname = ListDB.get(index).name;
                ListDB.remove(index);
                sAdapter.notifyDataSetChanged();
                Toast.makeText(ShoppingListActivity.this, curname+" deleted Successfuly! ",
                        Toast.LENGTH_SHORT).show();
                editDialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ShoppingListActivity.this, "Dismissed.",
                        Toast.LENGTH_SHORT).show();
                editDialog.dismiss();
            }
        });
    }

    public void UpdateFireBase(){
        final Dialog LoadingDialog = new Dialog(ShoppingListActivity.this);
        LoadingDialog.setContentView(R.layout.updating_data_dialog);
        LoadingDialog.show();
        root.child("Appartments").child(""+aptNum).child("shoppingList").setValue(ListDB).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ShoppingListActivity.this, "Database Updated Successfully",
                            Toast.LENGTH_SHORT).show();
                    LoadingDialog.dismiss();
                }
                else
                {
                    Toast.makeText(ShoppingListActivity.this, "Update Failed! "+task.toString(),
                            Toast.LENGTH_SHORT).show();
                    LoadingDialog.dismiss();
                }
            }
        });
    }

    public void newItem(){
        final Dialog AddDialog = new Dialog(ShoppingListActivity.this);
        AddDialog.setContentView(R.layout.add_item);
        AddDialog.show();

        Button doneAdd = (Button) AddDialog.findViewById(R.id.doneAdd);
        Button cancelButton = (Button) AddDialog.findViewById(R.id.cancelAdd);
        final EditText newN = (EditText) AddDialog.findViewById(R.id.newName);
        final EditText newQ = (EditText) AddDialog.findViewById(R.id.newQty);


        doneAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Float.valueOf(newQ.getText().toString())<=0)
                {
                    Toast.makeText(ShoppingListActivity.this, "cant set this to a Value!",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    ShopItem cur = new ShopItem(newN.getText().toString(),Float.valueOf(newQ.getText().toString()));
                    ListDB.add(cur);
                    Toast.makeText(ShoppingListActivity.this, "Item added Successfully :"+cur.name,
                            Toast.LENGTH_SHORT).show();
                    AddDialog.dismiss();
                    sAdapter.notifyDataSetChanged();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ShoppingListActivity.this, "Dismissed.",
                        Toast.LENGTH_SHORT).show();
                AddDialog.dismiss();
            }
        });

    }


}