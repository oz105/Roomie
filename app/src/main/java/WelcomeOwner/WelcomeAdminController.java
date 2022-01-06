package WelcomeOwner;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.view.View;

import com.example.roomie_2.User;

import java.util.List;

public class WelcomeAdminController {




    WelcomeAdminActivity welcomeAdminActivity;
    WelcomwAdminModel welcomwAdminModel;

    public WelcomeAdminController(WelcomeAdminActivity welcomeAdminActivity){
        this.welcomeAdminActivity = welcomeAdminActivity;
        welcomwAdminModel = new WelcomwAdminModel(this);
    }



    public void init_photo(Bitmap[] photos){
        ProfileAdapter adapter = new ProfileAdapter(welcomeAdminActivity,photos);
        welcomeAdminActivity.photos.setAdapter(adapter);
    }

    public void click_profile(View view, int position){
        welcomwAdminModel.get_user_details(position);
    }

    public void open_profile(User user,Bitmap profilePic){
        welcomeAdminActivity.photo.setImageBitmap(profilePic);
        welcomeAdminActivity.age.setText(user.age);
        welcomeAdminActivity.phone.setText(user.phone);
        welcomeAdminActivity.mail.setText(user.email);
        welcomeAdminActivity.name.setText(user.fullName);
        welcomeAdminActivity.profileDialog.show();

        // dialog profilePicture
    }

}
