package Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import Login.LoginViewActivity;
import com.example.roomie_2.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;


public class RegisterViewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView banner, registerUser;
    private RadioGroup type;
    private EditText editTextFullName, editTextAge, editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    public CircleImageView profile;
    public StorageReference StorageRef;
    public Uri resultUri;

    private boolean isAdmin = false;


    private RegisterController registerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view);

        type = (RadioGroup) findViewById(R.id.type);
        banner = (TextView) findViewById(R.id.banner);
        profile = (CircleImageView)findViewById(R.id.profile_image);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextPassword = (EditText) findViewById(R.id.password);
        registerUser = (Button) findViewById(R.id.registerUser);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextAge = (EditText) findViewById(R.id.age);
        StorageRef = FirebaseStorage.getInstance().getReference("Profile");

        profile.setOnClickListener(this);
        registerController = new RegisterController(this);
        registerUser.setOnClickListener(this);
        banner.setOnClickListener(this);

        type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checked = (RadioButton) group.findViewById(checkedId);
                if(checked.getId()==R.id.admin){
                    isAdmin = true;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, LoginViewActivity.class));
                break;
            case R.id.registerUser:
                registerController.getDate(isAdmin);
                break;
            case R.id.profile_image:
                registerController.choose_photo();
                break;


        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        registerController.set_image(requestCode,resultCode,data);

    }

    public void make_toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    public EditText getEditTextFullName() {
        return editTextFullName;
    }

    public EditText getEditTextAge() {
        return editTextAge;
    }

    public EditText getEditTextEmail() {
        return editTextEmail;
    }

    public EditText getEditTextPassword() {
        return editTextPassword;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }



}