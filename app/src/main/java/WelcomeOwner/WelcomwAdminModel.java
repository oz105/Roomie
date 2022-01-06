package WelcomeOwner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.roomie_2.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WelcomwAdminModel {


    private FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    private DatabaseReference apartmentRef = db.getReference().child("Apartments");
    private DatabaseReference usersRef = db.getReference().child("Users");
    StorageReference storageRef = FirebaseStorage.getInstance().getReference("Profile");

    WelcomeAdminController welcomeAdminController;
    String ownerId ="",apartmentId="";
    List<String>usersId;
    Bitmap [] photosBit;

    public WelcomwAdminModel(WelcomeAdminController welcomeAdminController){
        this.welcomeAdminController = welcomeAdminController;
        ownerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersRef.child(ownerId).child("apartmentId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                apartmentId = String.valueOf((Long)snapshot.getValue());
                get_apartment_users();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void get_apartment_users(){
        apartmentRef.child(apartmentId).child("apartmentUsers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Map<String,String> users = (Map<String, String>) snapshot.getValue();

                    usersId = new ArrayList<>();
                    usersId.addAll(users.keySet());

                    get_user_photo_link();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void get_user_photo_link(){
        photosBit = new Bitmap[usersId.size()];
        List<String> photos = new ArrayList<>();
        for(int index = 0;index<usersId.size();index++){
            String tempId = usersId.get(index);
            int finalIndex = index;
            usersRef.child(tempId).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String tempPhoto = (String) snapshot.getValue();
                    add_photo(tempPhoto, finalIndex);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
     public void add_photo(String photoName,int index){
         storageRef.child(photoName).getBytes(1024*1024*7).addOnSuccessListener(new OnSuccessListener<byte[]>() {
             @Override
             public void onSuccess(byte[] bytes) {
                 Bitmap tempBitMap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                 photosBit[index]= tempBitMap;
                 if(index==usersId.size()-1){
                     welcomeAdminController.init_photo(photosBit);
                 }
             }
         });
     }


    public void get_user_details(int position){

        usersRef.child(usersId.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User current =  snapshot.getValue(User.class);
                welcomeAdminController.open_profile(current,photosBit[position]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }






}
