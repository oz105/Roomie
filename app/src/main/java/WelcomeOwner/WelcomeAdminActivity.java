package WelcomeOwner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.roomie_2.R;
import com.google.firebase.auth.FirebaseAuth;

import Info.EditInfoView;
import Login.LoginViewActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class WelcomeAdminActivity extends AppCompatActivity implements View.OnClickListener {
    private Button info,logout;

    WelcomeAdminController controller;
    public GridView photos;
    public Dialog profileDialog;
    public TextView name,mail,phone,age;
    public CircleImageView photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_admin);
        controller = new WelcomeAdminController(this);
        photos = findViewById(R.id.photos);
        info = (Button) findViewById(R.id.info);
        logout = (Button) findViewById(R.id.logout);
        info.setOnClickListener(this);

        logout.setOnClickListener(this);


        photos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                profileDialog = new Dialog(controller.welcomeAdminActivity);
                profileDialog.setContentView(R.layout.profile);
                name = profileDialog.findViewById(R.id.profileName);
                mail = profileDialog.findViewById(R.id.profileMail);
                phone = profileDialog.findViewById(R.id.profilePhone);
                age = profileDialog.findViewById(R.id.profileAge);
                photo = profileDialog.findViewById(R.id.photo);
                controller.click_profile(view,position);
            }
        });


    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.info):
                Intent intent = new Intent(WelcomeAdminActivity.this, EditInfoView.class);
                startActivity(intent);
                break;
            case (R.id.logout):
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(WelcomeAdminActivity.this, LoginViewActivity.class));
                break;
        }


    }
}