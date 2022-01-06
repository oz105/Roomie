package Login;

import android.content.Intent;
import android.view.View;

import com.example.roomie_2.JoinApartmentActivity;
import WelcomeOwner.WelcomeAdminActivity;
import com.example.roomie_2.WelcomeUserActivity;

import AddApartment.AddApartmentView;

public class LoginController {

    private LoginModel loginModel;
    private LoginViewActivity loginView;

    public LoginController (LoginViewActivity login){
        this.loginView = login;
        this.loginModel = new LoginModel(this);
    }


    public void getEmailAndPassword(){
        String email = loginView.getEditTextEmail().getText().toString().trim();
        String password = loginView.getEditTextPassword().getText().toString().trim();
        loginView.getProgressBar().setVisibility(View.VISIBLE);
        loginModel.userLogin(email,password);

    }


    public void decide_screen(int state) {
        loginView.getProgressBar().setVisibility(View.GONE);
        switch (state){
            case 0:
                break;
            case 1:
                Intent intent = new Intent(loginView, WelcomeAdminActivity.class);
                //Intent intent = new Intent(loginView, AdminPhotosViewActivity.class);
                loginView.startActivity(intent);
                break;
            case 2:
                loginView.startActivity(new Intent(loginView, AddApartmentView.class));
                break;
            case 3:
                loginView.startActivity(new Intent(loginView, WelcomeUserActivity.class));
                break;
            case 4:
                loginView.startActivity(new Intent(loginView, JoinApartmentActivity.class));
        }
    }

    public void make_toast(String message){
        this.loginView.make_toast(message);
    }

    public LoginViewActivity getLoginView() {
        return loginView;
    }

    public void goneProgressBar() {
        loginView.getProgressBar().setVisibility(View.GONE);
    }
}
