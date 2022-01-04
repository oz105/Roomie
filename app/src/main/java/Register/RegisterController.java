package Register;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.core.content.ContextCompat;

import Login.LoginViewActivity;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

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


    public void choose_photo(){
        if(!checkCameraPermission()){
            requestCameraPermission();

        }else{if(!checkStorgePermission()){
            requestStorgePermission();

        }else{ pickImage(); } }


    }

    public void set_image(int requestCode, int resultCode,Intent data){

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == registerView.RESULT_OK) {
                registerView.resultUri = result.getUri();
                Picasso.with(registerView).load(registerView.resultUri).into(registerView.profile);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private void pickImage() {
        CropImage.activity().start(registerView);
    }
    private void requestStorgePermission() {
        registerView.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }
    private void requestCameraPermission() {
        registerView.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }
    private boolean checkStorgePermission() {
        boolean res = ContextCompat.checkSelfPermission(registerView, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res;
    }
    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(registerView, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(registerView, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res1 && res2 ;
    }
}
