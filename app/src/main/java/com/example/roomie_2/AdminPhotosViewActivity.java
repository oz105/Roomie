package com.example.roomie_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

public class AdminPhotosViewActivity extends AppCompatActivity implements View.OnClickListener {


    private Uri resultUri;
    private ImageView image ;
    private StorageTask UploadTask;
    private EditText EditTextFileName;
    private Button pickPhoto ,ButtonUpload;
    private android.widget.ProgressBar ProgressBar;

    private AdminPhotosController adminPhotosController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_photos_view);

        EditTextFileName = findViewById(R.id.file_name);
        image = findViewById(R.id.capturedImage);

        pickPhoto = findViewById(R.id.pick);
        ButtonUpload = findViewById(R.id.upload);

        pickPhoto.setOnClickListener(this);
        ButtonUpload.setOnClickListener(this);

        ProgressBar = findViewById(R.id.progress_line_bar);
        adminPhotosController = new AdminPhotosController(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pick:
                Log.i("uploadView Login","on pick");
                boolean pickType = true ;
                if(pickType){
                    if(!checkCameraPermission()){
                        requestCameraPermission();

                    }else{ pickImage(); }
                }else{
                    if(!checkStorgePermission()){
                        requestStorgePermission();

                    }else{ pickImage(); }
                }
                break;
            case R.id.upload:
                if (UploadTask != null && UploadTask.isInProgress()) {
                    make_toast("Upload in progress");
                }else {
                    adminPhotosController.uploadFile(EditTextFileName.getText().toString().trim(), resultUri);
                }break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                Picasso.with(this).load(resultUri).into(image);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void setUploadTask(StorageTask uploadTask) {
        UploadTask = uploadTask;
    }
    public void make_toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public android.widget.ProgressBar getProgressBar() {
        return ProgressBar;
    }

    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }

    private boolean checkStorgePermission() {
        boolean res = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res;
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }

    private void requestStorgePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }

    private void pickImage() {
        CropImage.activity().start(this);
    }
}