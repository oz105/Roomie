package Login;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomie_2.ForgetPasswordActivity;
import com.example.roomie_2.R;
import com.example.roomie_2.WelcomeUserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import Register.RegisterViewActivity;

public class LoginViewActivity extends AppCompatActivity implements View.OnClickListener {

    public FirebaseDatabase db;
    private FirebaseAuth mAuth;


    private TextView register, forgetPassword;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;
    private ProgressBar progressBar;



    public LoginController loginController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
        mAuth = FirebaseAuth.getInstance();
        signIn = (Button) findViewById(R.id.signIn);
        register = (TextView) findViewById(R.id.register);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        forgetPassword = (TextView) findViewById(R.id.forgotPassword);

        signIn.setOnClickListener(this);
        register.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);

        loginController = new LoginController(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && currentUser.isEmailVerified()){
            startActivity(new Intent(LoginViewActivity.this, WelcomeUserActivity.class));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterViewActivity.class));
                //startActivity(new Intent(this,AdminPhotosViewActivity.class));
                break;

            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;

            case R.id.signIn:
                loginController.getEmailAndPassword();
                break;
        }
    }

    public void make_toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

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
