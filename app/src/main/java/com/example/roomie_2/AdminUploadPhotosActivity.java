package com.example.roomie_2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;

public class AdminUploadPhotosActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    private DatabaseReference reference = db.getReference();
    private String user, imageUrl;
    public int aptNum;
    private List<Upload> tempUploads = new ArrayList<>() ;

    private Button pickPhoto ,ButtonUpload;
    private EditText EditTextFileName;
    private ProgressBar ProgressBar;
    private StorageTask UploadTask;
    private ImageView image ;
    private Uri resultUri;
    private Upload upload;

    private StorageReference StorageRef;
    private DatabaseReference DatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload_photos);

        EditTextFileName = findViewById(R.id.file_name);
        image = findViewById(R.id.capturedImage);
        ButtonUpload = findViewById(R.id.upload);
        pickPhoto = findViewById(R.id.pick);

        ProgressBar = findViewById(R.id.progress_line_bar);

        StorageRef = FirebaseStorage.getInstance().getReference("uploads");
        DatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        pickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        ButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UploadTask != null && UploadTask.isInProgress()) {
                    Toast.makeText(AdminUploadPhotosActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
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


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (resultUri != null) {
            Log.i("upload", "upload type"+ resultUri.toString());
            StorageReference fileReference = StorageRef.child(System.currentTimeMillis()
                    + resultUri.toString().substring(resultUri.toString().lastIndexOf(".")));
//                    + "." + getFileExtension(resultUri));

            user = FirebaseAuth.getInstance().getCurrentUser().getUid();

            UploadTask = fileReference.putFile(resultUri)
                    .addOnSuccessListener(new OnSuccessListener<com.google.firebase.storage.UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(AdminUploadPhotosActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageUrl = uri.toString();
                                        Log.i("uploads","inside if the url is " + imageUrl);
                                        upload = new Upload(EditTextFileName.getText().toString().trim(), imageUrl);
                                        String uploadId = DatabaseRef.push().getKey();
                                        DatabaseRef.child(uploadId).setValue(upload);
                                        Log.i("uploads","before added the name is "+upload.getName() +" and URI " + upload.getImageUrl());
                                        updateImagesList();
                                    }
                                });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminUploadPhotosActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            ProgressBar.setProgress((int) progress);
                        }
                    });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }


    }

    private void updateImagesList() {
        reference.child("Users").child(user).child("apartmentId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("datasnapCheckAPT",dataSnapshot.getValue().toString());
                long x = (long) dataSnapshot.getValue();
                aptNum = Math.toIntExact(x);
                Log.i("datasnap","aptnum is :"+aptNum);

                reference.child("Apartments").child(""+aptNum).child("imagesUploads").addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i("uploads","enter onDataChange ");
                        if(dataSnapshot.exists()){
                            Log.i("uploads","enter if datasnapshot exsists ");
                            for(DataSnapshot dss :dataSnapshot.getChildren()){
                                Upload u = dss.getValue(Upload.class);
                                Log.i("uploads","the name is "+u.getName() +" and URI " + u.getImageUrl());
                                tempUploads.add(u);
                            }
                            Log.i("uploads","ths size is "+tempUploads.size());
                        }else{
                            tempUploads.add(upload);
                            Log.i("uploads","enter ELSE ");
                            reference.child("Apartments").child(""+aptNum).child("imagesUploads").setValue(tempUploads);
                        }

                        final Dialog LoadingDialog = new Dialog(AdminUploadPhotosActivity.this);
                        LoadingDialog.setContentView(R.layout.updating_data_dialog);
                        LoadingDialog.show();
                        tempUploads.add(upload);
                        Log.i("uploads","after add the name is "+upload.getName() +" and URI " + upload.getImageUrl());
                        Log.i("uploads","added upload ");
                        Log.i("uploads","the new size is  " + tempUploads.size());

                        reference.child("Apartments").child(""+aptNum).child("imagesUploads").setValue(tempUploads).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.i("uploads","Switch LIst ");
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(AdminUploadPhotosActivity.this, "Database Updated Successfully",
                                            Toast.LENGTH_SHORT).show();
                                    tempUploads.clear();
                                    LoadingDialog.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(AdminUploadPhotosActivity.this, "Update Failed! "+task.toString(),
                                            Toast.LENGTH_SHORT).show();
                                    LoadingDialog.dismiss();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Toast.makeText(AdminUploadPhotosActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Something went wrong! " + databaseError.getCode());

            }
        });

    }

    private void pickImage() {
        CropImage.activity().start(this);
    }

    private void requestStorgePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
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
        return res1 && res2 ;

    }

}