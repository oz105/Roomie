package Info;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class EditInfoModel {

    EditInfoController editInfoController;
    String currentUserId;
    long apartmentId;
    Map<String,Object> details;

    public EditInfoModel(EditInfoController editInfoController,String currentUserId){
        this.editInfoController = editInfoController;
        this.currentUserId = currentUserId;
        apartmentId = -1;
    }

    public void get_apartment_details(){
        if(apartmentId!=-1){
            send_init_data();
        }
        else {
            editInfoController.editInfoView.userRef.child(currentUserId).child("apartmentId").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        apartmentId = (long) snapshot.getValue();
                        send_init_data();
                    }
                    else {

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void send_init_data(){
        editInfoController.editInfoView.apartmentRef.child(""+apartmentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    details = ( Map<String,Object>)snapshot.child("details").getValue();
                    editInfoController.init_info_details(details,apartmentId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void update_data_at_db( Map<String,Object> apartmentInfo){
        if(apartmentInfo.equals(details)){
            editInfoController.finish(true);
        }
        else {
            editInfoController.editInfoView.apartmentRef.child(""+apartmentId).child("details").setValue(apartmentInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        editInfoController.finish(true);
                    }
                    else {
                        editInfoController.finish(false);

                    }
                }
            });
        }


    }
}
