package com.example.roomie_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterViewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView banner, registerUser;
    private RadioGroup type;
    private EditText editTextFullName, editTextAge, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    private boolean isAdmin = false;


    public RegisterController registerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view);

        type = (RadioGroup) findViewById(R.id.type);
        banner = (TextView) findViewById(R.id.banner);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextPassword = (EditText) findViewById(R.id.password);
        registerUser = (Button) findViewById(R.id.registerUser);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextAge = (EditText) findViewById(R.id.age);


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
        }

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