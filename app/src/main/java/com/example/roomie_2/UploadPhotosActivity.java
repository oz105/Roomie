package com.example.roomie_2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.widget.Toast;

public class UploadPhotosActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button ButtonChooseImage;
    private Button ButtonUpload;
    private TextView TextViewShowUploads;
    private EditText EditTextFileName;
    private ImageView ImageView;
    private ProgressBar ProgressBar;

    private Uri ImageUri;

    private StorageReference StorageRef;
    private DatabaseReference DatabaseRef;

    private StorageTask UploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photos);

        ButtonChooseImage = findViewById(R.id.choose_image);
        ButtonUpload = findViewById(R.id.upload);
        TextViewShowUploads = findViewById(R.id.show_uploads);
        EditTextFileName = findViewById(R.id.file_name);
        ImageView = findViewById(R.id.image_view);
        ProgressBar = findViewById(R.id.progress_bar);

        StorageRef = FirebaseStorage.getInstance().getReference("uploads");
        DatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");


        ButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        ButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UploadTask != null && UploadTask.isInProgress()) {
                    Toast.makeText(UploadPhotosActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
        TextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadPhotosActivity.this, ImagesActivity.class);
                startActivity(intent);

            }
        });


    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            ImageUri = data.getData();

            Picasso.with(this).load(ImageUri).into(ImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (ImageUri != null) {
            StorageReference fileReference = StorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(ImageUri));

            UploadTask = fileReference.putFile(ImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(UploadPhotosActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload(EditTextFileName.getText().toString().trim(),
                                    fileReference.getDownloadUrl().toString());
                            String uploadId = DatabaseRef.push().getKey();
                            DatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadPhotosActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
}