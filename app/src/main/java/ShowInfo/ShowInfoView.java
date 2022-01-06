package ShowInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.roomie_2.R;
import com.example.roomie_2.WelcomeUserActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import Bills.BillsViewActivity;
import ShoppingList.ShoppingListViewActivity;


public class ShowInfoView extends AppCompatActivity implements View.OnClickListener {

    String apartmentId = "0";
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    List<String> photos ;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference("uploads");
    List<Uri> links = new ArrayList<>();
    ImageButton pre,next;
    int currentIndex = 0;
    Bitmap [] bm;
    FrameLayout fl;
    LinearLayout wait;
    ProgressBar progressBar;
    TextView details,rooms,price,address,noPhoto;
    ImageView image;
    ShowInfoController showInfoController;
    String userId;
    BottomNavigationView bn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showInfoController = new ShowInfoController(this);
        setContentView(R.layout.activity_show_info_view);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        showInfoController.init_info(userId);
        bn = (BottomNavigationView) findViewById(R.id.bottom_nav);
        details = findViewById(R.id.details);
        wait = findViewById(R.id.waiting);
        rooms = findViewById(R.id.rooms);
        price = findViewById(R.id.rent);
        address = findViewById(R.id.address);
        image = findViewById(R.id.image);
        fl = findViewById(R.id.photoBack);
        noPhoto = findViewById(R.id.noPhoto);
        progressBar = findViewById(R.id.progressBar);


        pre = findViewById(R.id.bt_pre);
        next = findViewById(R.id.bt_next);
        bn.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()){
                    case R.id.nav_info:
                        return true;
                    case R.id.nav_bills:
                        Log.i("roomie_welcome","starting bills");
                        startActivity(new Intent(ShowInfoView.this, BillsViewActivity.class));
                        return true;
                    case R.id.nav_shoppong:
                        Log.i("roomie_welcome","starting bills");
                        startActivity(new Intent(ShowInfoView.this, ShoppingListViewActivity.class));
                        return true;
                    case R.id.nav_home:
                        startActivity(new Intent(ShowInfoView.this, WelcomeUserActivity.class));
                        return true;
                    default:
                        return false;
                }


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        bn.setSelectedItemId(R.id.nav_info);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_pre:
                showInfoController.change_photo(0);
                break;

            case R.id.bt_next:
                showInfoController.change_photo(1);
                break;

        }

    }
}