package com.example.roomie_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Login.LoginViewActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView banner, registerUser;
    private RadioGroup type;
    private CircleImageView profile;
    private EditText editTextFullName, editTextAge, editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private boolean isAdmin = false;
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference root = db.getReference().child("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        type = (RadioGroup) findViewById(R.id.type);
        banner = (TextView) findViewById(R.id.banner);
        profile = (CircleImageView)findViewById(R.id.profile_image);


        registerUser = (Button) findViewById(R.id.registerUser);


        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextAge = (EditText) findViewById(R.id.age);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        banner.setOnClickListener(this);
        registerUser.setOnClickListener(this);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });








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
                Log.i("roomie Register", "switch");
                //Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                //startActivity(myIntent);
                registerUser();
                break;
        }

    }

    private void registerUser() {
//        String email = editTextEmail.getText().toString().trim();
//        String password = editTextPassword.getText().toString().trim();
//        String fullName = editTextFullName.getText().toString().trim();
//        String age = editTextAge.getText().toString().trim();
//
//        if (fullName.isEmpty()) {
//            editTextFullName.setError("Full name is required!");
//            editTextFullName.requestFocus();
//            return;
//        }
//        if (age.isEmpty()) {
//            editTextAge.setError("Age is required!");
//            editTextAge.requestFocus();
//            return;
//        }
//        if (email.isEmpty()) {
//            editTextEmail.setError("Email is required!");
//            editTextEmail.requestFocus();
//            return;
//        }
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            editTextEmail.setError("Please enter valid Email!");
//            editTextEmail.requestFocus();
//            return;
//        }
//        if (password.isEmpty()) {
//            editTextPassword.setError("Password is required!");
//            editTextPassword.requestFocus();
//            return;
//        }
//        if (password.length() < 6) {
//            editTextPassword.setError("Password should be at least 6 characters");
//            editTextPassword.requestFocus();
//            return;
//        }
//
//
//        progressBar.setVisibility(View.VISIBLE);
//        mAuth.createUserWithEmailAndPassword(email,password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            String id = mAuth.getCurrentUser().getUid();
//                            //User user = new User(fullName, age, email,id,isAdmin);
//                            //db.getReference().child("Users").child(id).setValue(user)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//                                        Toast.makeText(RegisterActivity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
//                                        startActivity(new Intent(RegisterActivity.this, LoginViewActivity.class));
//                                    }else{
//                                        Toast.makeText(RegisterActivity.this, "Registered failed :( , Please try again", Toast.LENGTH_LONG).show();
//                                    }
//                                    progressBar.setVisibility(View.GONE);
//                                }
//                            });
//
//                        }else{
//                            Toast.makeText(RegisterActivity.this, "Registered failed :(", Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(RegisterActivity.this, LoginViewActivity.class));
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    }
//                });

    }
}







