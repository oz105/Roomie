package Info;

import android.content.Intent;
import android.widget.Toast;

import WelcomeOwner.WelcomeAdminActivity;

import java.util.HashMap;
import java.util.Map;

public class EditInfoController {

    private EditInfoView editInfoView;
    private EditInfoModel editInfoModel;


    public EditInfoController(EditInfoView editInfoView){
        this.editInfoView = editInfoView;
        this.editInfoModel = new EditInfoModel(this);
        this.editInfoModel.get_apartment_details();
    }


    public void init_info_details(Map<String,Object> details,long apartmentId){
        editInfoView.id.setText("Appartment ID: "+apartmentId);
        editInfoView.AppAddress.setText(details.get("address").toString());
        editInfoView.AppPassword.setText(details.get("password").toString());
        editInfoView.numOfRooms.setText(details.get("numOfRoom").toString());
        editInfoView.details.setText(details.get("details").toString());
        editInfoView.price.setText(details.get("price").toString());
    }

    public void update_data(){

        if(editInfoView.AppPassword.getText().toString().isEmpty() ){
            editInfoView.AppPassword.setError("join password is required!");
            editInfoView.AppPassword.requestFocus();
            return;
        }
        if(editInfoView.AppAddress.getText().toString().isEmpty() ){
            editInfoView.AppAddress.setError("apartment address is required!");
            editInfoView.AppAddress.requestFocus();
            return;
        }
        if(editInfoView.details.getText().toString().isEmpty() ){
            editInfoView.AppAddress.setError("details is required!");
            editInfoView.AppAddress.requestFocus();
            return;
        }
        if(editInfoView.price.getText().toString().isEmpty() ){
            editInfoView.AppAddress.setError("price is required!");
            editInfoView.AppAddress.requestFocus();
            return;
        }
        if(editInfoView.numOfRooms.getText().toString().isEmpty() ){
            editInfoView.AppAddress.setError("number of rooms is required!");
            editInfoView.AppAddress.requestFocus();
            return;
        }
        Map<String,Object> apartmentInfo = new HashMap<>();
        apartmentInfo.put("address",editInfoView.AppAddress.getText().toString());
        apartmentInfo.put("numOfRoom",editInfoView.numOfRooms.getText().toString());
        apartmentInfo.put("price",editInfoView.price.getText().toString());
        apartmentInfo.put("password",editInfoView.AppPassword.getText().toString());
        apartmentInfo.put("details",editInfoView.details.getText().toString());
        editInfoModel.update_data_at_db(apartmentInfo);
    }

    public void finish(boolean isSeccess){
        if(isSeccess){
            Toast.makeText(editInfoView, "Update success :)", Toast.LENGTH_LONG).show();
            editInfoView.startActivity(new Intent(editInfoView, WelcomeAdminActivity.class));
        }
        else {
            Toast.makeText(editInfoView, "Update Faild :(", Toast.LENGTH_LONG).show();
            editInfoView.startActivity(new Intent(editInfoView, WelcomeAdminActivity.class));
        }
    }


}
