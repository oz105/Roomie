package Register;


import android.net.Uri;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.example.roomie_2.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterModel {


    private FirebaseAuth mAuth;
    private RegisterController registerController;
    private FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");

    public RegisterModel(RegisterController registerController) {
        this.registerController = registerController;
        this.mAuth = FirebaseAuth.getInstance();

    }

    public void registerUser(String email, String password, String fullName, String age,String phone, boolean isAdmin) {
        if (!verifyDate(email, password, fullName, age)) {
            registerController.onlyGone();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = mAuth.getCurrentUser().getUid();
                    User user = new User(fullName, age, email, id,phone, isAdmin);

                    db.getReference().child("Users").child(id).setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        save_image_to_db(id);
                                        registerController.make_toast("User has been registered successfully");
                                    } else {
                                        registerController.make_toast("Registered failed :( , Please try again");
                                    }
                                }
                            });
                } else {
                    task.getException();
                    registerController.make_toast("Registered failed :(");
                }
                registerController.goneProgressBar();
            }
        });
    }

    private boolean verifyDate(String email, String password, String fullName, String age) {
        if (fullName.isEmpty()) {
            registerController.getRegisterView().getEditTextFullName().setError("Full name is required!");
            registerController.getRegisterView().getEditTextFullName().requestFocus();
            return false;
        }
        if (age.isEmpty()) {
            registerController.getRegisterView().getEditTextAge().setError("Age is required!");
            registerController.getRegisterView().getEditTextAge().requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            registerController.getRegisterView().getEditTextEmail().setError("Email is required!");
            registerController.getRegisterView().getEditTextEmail().requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registerController.getRegisterView().getEditTextEmail().setError("Please enter valid Email!");
            registerController.getRegisterView().getEditTextEmail().requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            registerController.getRegisterView().getEditTextPassword().setError("Password is required!");
            registerController.getRegisterView().getEditTextPassword().requestFocus();
            return false;
        }
        if (password.length() < 6) {
            registerController.getRegisterView().getEditTextPassword().setError("Password should be at least 6 characters");
            registerController.getRegisterView().getEditTextPassword().requestFocus();
            return false;
        }
        return true;
    }

    public void save_image_to_db(String id) {
        Uri uri = registerController.getRegisterView().resultUri;
        if (uri != null) {
            String photoName = System.currentTimeMillis()
                    + uri.toString().substring(uri.toString().lastIndexOf("."));
            StorageReference fileReference = registerController.getRegisterView().StorageRef.child(photoName);
            fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            db.getReference().child("Users").child(id).child("profile").setValue(photoName);
                        }
                    });

                }
            });
        }
    }
}





