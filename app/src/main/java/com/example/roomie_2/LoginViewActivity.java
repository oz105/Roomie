package com.example.roomie_2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class LoginViewActivity extends AppCompatActivity implements View.OnClickListener {

    public FirebaseDatabase db;


    public TextView register, forgetPassword;
    public EditText editTextEmail, editTextPassword;
    public Button signIn;
    public ProgressBar progressBar;



    public LoginController loginController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
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

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null && currentUser.isEmailVerified()){
//            startActivity(new Intent(LoginActivity.this, WelcomeUserActivity.class));
//        }
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;

            case R.id.forgotPassword:
                startActivity(new Intent(this,ForgetPasswordActivity.class));
                break;

            case R.id.signIn:
                loginController.getEmailAndPassword();
                break;
        }
    }

    public void Make_toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }
}



//
//public class LoginViewActivity extends AppCompatActivity implements View.OnClickListener {
//
//

//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);

//
//    }
//

//

//
//}