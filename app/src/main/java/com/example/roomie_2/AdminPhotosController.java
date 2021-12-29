package com.example.roomie_2;

import android.net.Uri;

public class AdminPhotosController {

    private AdminPhotosModel adminModel ;



    private AdminPhotosViewActivity adminView;

    public AdminPhotosController(AdminPhotosViewActivity adminView) {
        this.adminView = adminView;
        this.adminModel = new AdminPhotosModel(this);
    }


    public void uploadFile(String fileName, Uri resultUri) {
        if (resultUri != null){
            adminModel.uploadFile(fileName,resultUri);
        }else{
            make_toast("No file selected");
        }

    }

    public void make_toast(String message){
        this.adminView.make_toast(message);
    }

    public AdminPhotosViewActivity getAdminView() {
        return adminView;
    }

}
