package com.example.roomie_2;

import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginModel {

    public DatabaseReference reference;
    public FirebaseAuth mAuth;
    public FirebaseUser user;
    public User profileUser;
    public String userID;


    LoginController loginController;

    public LoginModel(LoginController loginController) {
        this.loginController = loginController;
        this.mAuth = FirebaseAuth.getInstance();
    }


    public void userLogin(String email, String password){
        verify_data(email, password);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("hananell Login","onComplete");
                if(task.isSuccessful()){
                    Log.i("hananell Login","success log in");

                    user = FirebaseAuth.getInstance().getCurrentUser();
                    reference = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");
                    userID = user.getUid();

                    reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int state = 0 ;
                            if(dataSnapshot.exists()) {
                                Log.i("roomie Login", "find if has apartment");
                                profileUser = dataSnapshot.getValue(User.class);
                                state = check_state(profileUser);
                            }
                            else{
                                loginController.make_toast("Something went wrong!");
                            }
                            loginController.decide_screen(state);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            loginController.make_toast("Something went wrong!");
                        }
                    });

//                    if(user.isEmailVerified()){
//                        startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
//                    }else{
//                        user.sendEmailVerification();
//                        Toast.makeText(LoginActivity.this, "Check your Email, and verify your Email!",Toast.LENGTH_LONG).show();
//                    }
                }else{
                    loginController.make_toast("Failed to Login! Please check Your Email or Password");
                }
            }
        });
    }

    private int check_state (User profileUser){
        if (profileUser.isAdmin) {
            Log.i("roomie Login", "Admin" );
            if (profileUser.hasApartment) {
                Log.i("roomie Login", "has apartment");
                // admin welcome here...
                return 1 ;
            } else {

                Log.i("roomie Login", "has no apartment");
                // admin add new apartment
                return 2;
            }
        } else {
            Log.i("roomie Login", "User");

            if (profileUser.hasApartment) {
                Log.i("hananell Login", "has apartment");
                // user welcome here...
                return 3;
            } else {
                Log.i("hananell Login", "has not apartment");
                // user join apartment
                return 4;
            }

        }

    }

    private void verify_data(String email, String password) {
        if (email.isEmpty()) {
            loginController.loginView.editTextEmail.setError("Email is required!");
            loginController.loginView.editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginController.loginView.editTextEmail.setError("Please enter valid Email!");
            loginController.loginView.editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            loginController.loginView.editTextPassword.setError("Password is required!");
            loginController.loginView.editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            loginController.loginView.editTextPassword.setError("Password should be at least 6 characters");
            loginController.loginView.editTextPassword.requestFocus();
            return;
        }
    }
}
