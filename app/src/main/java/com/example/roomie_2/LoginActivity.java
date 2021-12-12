package com.example.roomie_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register, forgetPassword;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.singIn);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        forgetPassword = (TextView) findViewById(R.id.forgotPassword);
        forgetPassword.setOnClickListener(this);

        FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && currentUser.isEmailVerified()){
            startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                Log.i("hananell Login","register");
                startActivity(new Intent(this,RegisterActivity.class));
                break;

            case R.id.forgotPassword:
                startActivity(new Intent(this,ForgetPasswordActivity.class));
                break;

            case R.id.singIn:
                Log.i("hananell Login","try log in");
                userLogin();
                break;

        }

    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter valid Email!");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Password should be at least 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("hananell Login","onComplete");


                if(task.isSuccessful()){
                    Log.i("hananell Login","success log in");

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference ref = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Users").child(user.getUid()).child("hasAppartment");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                                Log.i("hananell Login","find if has appartment");
                                boolean hasAppartment = Boolean.parseBoolean(dataSnapshot.getValue().toString());
                                if(hasAppartment){
                                    Log.i("hananell Login","has appartment");

                                    startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                                    progressBar.setVisibility(View.GONE);
                                }
                                else{
                                    Log.i("hananell Login","has not appartment");

                                    startActivity(new Intent(LoginActivity.this, FirstRegisteredEntry.class));
                                    progressBar.setVisibility(View.GONE);
                                }
                            }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




//                    if(user.isEmailVerified()){
//                        startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
//                    }else{
//                        user.sendEmailVerification();
//                        Toast.makeText(LoginActivity.this, "Check your Email, and verify your Email!",Toast.LENGTH_LONG).show();
//                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Failed to Login! Please check Your Email or Password", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}