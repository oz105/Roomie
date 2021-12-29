package com.example.roomie_2;

import android.content.Intent;
import android.view.View;

public class RegisterController {

    private RegisterModel registerModel ;
    private RegisterViewActivity registerView;

    public RegisterController(RegisterViewActivity registerView) {
        this.registerView = registerView;
        this.registerModel = new RegisterModel(this);
    }

    public void getDate(boolean isAdmin){
        String email = registerView.getEditTextEmail().getText().toString().trim();
        String password = registerView.getEditTextPassword().getText().toString().trim();
        String fullName = registerView.getEditTextFullName().getText().toString().trim();
        String age = registerView.getEditTextAge().getText().toString().trim();
        registerView.getProgressBar().setVisibility(View.VISIBLE);
        registerModel.registerUser(email, password, fullName, age ,isAdmin);

    }

    public void make_toast(String message){
        this.registerView.make_toast(message);
    }

    public RegisterViewActivity getRegisterView() {
        return registerView;
    }

    public void goneProgressBar() {
        registerView.getProgressBar().setVisibility(View.GONE);
        registerView.startActivity(new Intent(registerView, LoginViewActivity.class));

    }
    public void onlyGone() {
        registerView.getProgressBar().setVisibility(View.GONE);
    }
}
