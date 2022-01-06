package Login;

import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.example.roomie_2.User;
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

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private User profileUser;
    private String userID;


    LoginController loginController;

    public LoginModel(LoginController loginController) {
        this.loginController = loginController;
        this.mAuth = FirebaseAuth.getInstance();
    }


    public void userLogin(String email, String password){
        if(!verify_data(email, password)){
            loginController.goneProgressBar();
            return;
        }

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
                            loginController.goneProgressBar();
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
                    loginController.goneProgressBar();
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

    private boolean verify_data(String email, String password) {
        if (email.isEmpty()) {
            loginController.getLoginView().getEditTextEmail().setError("Email is required!");
            loginController.getLoginView().getEditTextEmail().requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginController.getLoginView().getEditTextEmail().setError("Please enter valid Email!");
            loginController.getLoginView().getEditTextEmail().requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            loginController.getLoginView().getEditTextPassword().setError("Password is required!");
            loginController.getLoginView().getEditTextPassword().requestFocus();
            return false;
        }
        if (password.length() < 6) {
            loginController.getLoginView().getEditTextPassword().setError("Password should be at least 6 characters");
            loginController.getLoginView().getEditTextPassword().requestFocus();
            return false;
        }
        return true;
    }
}
