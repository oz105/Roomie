package AddApartment;

import android.content.Intent;
import android.widget.Toast;

import com.example.roomie_2.WelcomeAdminActivity;

import java.util.HashMap;
import java.util.Map;

public class AddApartmentController {


    AddApartmentView addApartmentView;
    AddApartmentModel addApartmentModel;

    public AddApartmentController(AddApartmentView addApartmentView){
        this.addApartmentView = addApartmentView;
        addApartmentModel = new AddApartmentModel(this);
    }

    public void save_apartment_details(){

        if(addApartmentView.AppPassword.getText().toString().isEmpty() ){
            addApartmentView.AppPassword.setError("join password is required!");
            addApartmentView.AppPassword.requestFocus();
            return;
        }
        if(addApartmentView.AppAddress.getText().toString().isEmpty() ){
            addApartmentView.AppAddress.setError("apartment address is required!");
            addApartmentView.AppAddress.requestFocus();
            return;
        }
        if(addApartmentView.details.getText().toString().isEmpty() ){
            addApartmentView.AppAddress.setError("details is required!");
            addApartmentView.AppAddress.requestFocus();
            return;
        }
        if(addApartmentView.price.getText().toString().isEmpty() ){
            addApartmentView.AppAddress.setError("price is required!");
            addApartmentView.AppAddress.requestFocus();
            return;
        }
        if(addApartmentView.numOfRooms.getText().toString().isEmpty() ){
            addApartmentView.AppAddress.setError("number of rooms is required!");
            addApartmentView.AppAddress.requestFocus();
            return;
        }
        Map<String,Object> apartmentInfo = new HashMap<>();
        apartmentInfo.put("address",addApartmentView.AppAddress.getText().toString());
        apartmentInfo.put("numOfRoom",addApartmentView.numOfRooms.getText().toString());
        apartmentInfo.put("price",addApartmentView.price.getText().toString());
        apartmentInfo.put("password",addApartmentView.AppPassword.getText().toString());
        apartmentInfo.put("details",addApartmentView.details.getText().toString());
       addApartmentModel.add_apartment(apartmentInfo,addApartmentView.Auth.getCurrentUser().getUid());

    }

    public void finish_create_apartment(boolean success,long apartmentId){
        if(success){
            make_toast("Congratulations, you have new apartment\n your apartment ID is"+apartmentId);
            addApartmentView.startActivity(new Intent(addApartmentView, WelcomeAdminActivity.class));
        }
        else {
            make_toast("Sorry, apartment create has been failed :( please try again"+apartmentId);
            addApartmentView.finish();
            addApartmentView.startActivity(addApartmentView.getIntent());
        }
    }
    public void make_toast(String messege){
        Toast.makeText(addApartmentView, messege, Toast.LENGTH_LONG).show();
    }


}
