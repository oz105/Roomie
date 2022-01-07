package WelcomeOwner;

import android.graphics.Bitmap;
import android.view.View;

import com.example.roomie_2.User;

public class WelcomeAdminController {




    WelcomeAdminView welcomeAdminView;
    WelcomwAdminModel welcomwAdminModel;

    public WelcomeAdminController(WelcomeAdminView welcomeAdminView){
        this.welcomeAdminView = welcomeAdminView;
        welcomwAdminModel = new WelcomwAdminModel(this);

    }



    public void init_photo(Bitmap[] photos){
        welcomeAdminView.admin_wait.setVisibility(View.GONE);
        ProfileAdapter adapter = new ProfileAdapter(welcomeAdminView,photos);
        welcomeAdminView.photos.setAdapter(adapter);
    }

    public void click_profile(View view, int position){
        welcomwAdminModel.get_user_details(position);
    }

    public void open_profile(User user,Bitmap profilePic){
        welcomeAdminView.photo.setImageBitmap(profilePic);
        welcomeAdminView.age.setText(user.age);
        welcomeAdminView.phone.setText(user.phone);
        welcomeAdminView.mail.setText(user.email);
        welcomeAdminView.name.setText(user.fullName);
        welcomeAdminView.profileDialog.show();

        // dialog profilePicture
    }

}
