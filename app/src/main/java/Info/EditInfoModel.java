package Info;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class EditInfoModel {

    private FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    private DatabaseReference apartmentRef = db.getReference().child("Apartments");
    private DatabaseReference userRef = db.getReference().child("Users");
    private FirebaseAuth Auth;
    private EditInfoController editInfoController;
    private String currentUserId;
    private long apartmentId;
    private Map<String,Object> details;

    public EditInfoModel(EditInfoController editInfoController){
        Auth = FirebaseAuth.getInstance();

        this.editInfoController = editInfoController;
        this.currentUserId = Auth.getCurrentUser().getUid();
        apartmentId = -1;
    }

    public void get_apartment_details(){
        if(apartmentId!=-1){
            send_init_data();
        }
        else {
            userRef.child(currentUserId).child("apartmentId").addListenerForSingleValueEvent(new ValueEventListener() {
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
        apartmentRef.child(""+apartmentId).addListenerForSingleValueEvent(new ValueEventListener() {
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
            apartmentRef.child(""+apartmentId).child("details").setValue(apartmentInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
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
