package com.example.roomie_2;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AdminPhotosModel {

    public int aptNum;
    private Upload upload;
    private FirebaseDatabase db ;
    private String user, imageUrl;
    private List<Upload> tempUploads;
    private DatabaseReference reference ;

    private final StorageReference StorageRef;
    private final DatabaseReference DatabaseRef;

    private final AdminPhotosController adminController;


    public AdminPhotosModel(AdminPhotosController adminController) {
        db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
        reference =db.getReference();
        this.adminController = adminController;
        this.StorageRef = FirebaseStorage.getInstance().getReference("uploads");
        this.DatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        this.tempUploads = new ArrayList<>();
    }

    public void uploadFile(String fileName, Uri resultUri) {
        StorageReference fileReference = StorageRef.child(System.currentTimeMillis()
                + resultUri.toString().substring(resultUri.toString().lastIndexOf(".")));

        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        adminController.getAdminView().setUploadTask(
                fileReference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<com.google.firebase.storage.UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() { adminController.getAdminView().getProgressBar().setProgress(0); }
                            }, 500);
                            adminController.make_toast("Upload successful");
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageUrl = uri.toString();
                                        upload = new Upload(fileName, imageUrl);
                                        String uploadId = DatabaseRef.push().getKey();
                                        DatabaseRef.child(uploadId).setValue(upload);
                                        updateImagesList();
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    adminController.make_toast(e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    adminController.getAdminView().getProgressBar().setProgress((int) progress);
                }
            }));
    }

    private void updateImagesList() {
        reference.child("Users").child(user).child("apartmentId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long x = (long) dataSnapshot.getValue();
                aptNum = Math.toIntExact(x);
                reference.child("Apartments").child(""+aptNum).child("imagesUploads").addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot dss :dataSnapshot.getChildren()){
                                Upload u = dss.getValue(Upload.class);
                                tempUploads.add(u);
                            }
                        }else{
                            tempUploads.add(upload);
                            reference.child("Apartments").child(""+aptNum).child("imagesUploads").setValue(tempUploads);
                        }

                        final Dialog LoadingDialog = new Dialog(adminController.getAdminView());
                        LoadingDialog.setContentView(R.layout.updating_data_dialog);
                        LoadingDialog.show();
                        tempUploads.add(upload);
                        reference.child("Apartments").child(""+aptNum).child("imagesUploads").setValue(tempUploads).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    adminController.make_toast("Database Updated Successfully");
                                    tempUploads.clear();
                                }
                                else
                                {
                                    adminController.make_toast("Update Failed! ");
                                }
                                LoadingDialog.dismiss();
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        adminController.make_toast("Something went wrong!");
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Something went wrong! " + databaseError.getCode());

            }
        });

    }


}
