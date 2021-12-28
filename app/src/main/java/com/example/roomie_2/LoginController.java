package com.example.roomie_2;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class LoginController {

    private LoginModel loginModel;
    public LoginViewActivity loginView;

    public LoginController (LoginViewActivity login){
        this.loginView = login;
        this.loginModel = new LoginModel(this);
    }


    public void getEmailAndPassword(){
        String email = loginView.editTextEmail.getText().toString().trim();
        String password = loginView.editTextPassword.getText().toString().trim();
        loginView.progressBar.setVisibility(View.VISIBLE);
        loginModel.userLogin(email,password);

    }


    public void decide_screen(int state) {
        loginView.progressBar.setVisibility(View.GONE);
        switch (state){
            case 0:
                break;
            case 1:
                Intent intent = new Intent(loginView, WelcomeAdminActivity.class);
                loginView.startActivity(intent);
                break;
            case 2:
                loginView.startActivity(new Intent(loginView, AddNewApartment.class));
                break;
            case 3:
                loginView.startActivity(new Intent(loginView, WelcomeUserActivity.class));
                break;
            case 4:
                loginView.startActivity(new Intent(loginView, JoinApartmentActivity.class));
        }
    }

    public void make_toast(String message){
        Toast.makeText(this.loginView, message, Toast.LENGTH_SHORT).show();
    }

}
