package ShowInfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class ShowInfoModel {

    String apartmentId,userId;
    ShowInfoController showInfoController;
    List<String> photos;

    public ShowInfoModel(ShowInfoController showInfoController){
        this.showInfoController = showInfoController;
    }

    public void get_info(String userId){
        this.userId = userId;
        showInfoController.showInfoView.db.getReference().child("Users").child(userId).child("apartmentId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                apartmentId = String.valueOf(snapshot.getValue());
                showInfoController.showInfoView.db.getReference().child("Apartments").child(apartmentId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> photos;
                        Map<String,Object> details = (Map<String, Object>) snapshot.child("details").getValue();
                        if(snapshot.child("imagesUploads").exists()){
                            photos = (List<String>)snapshot.child("imagesUploads").getValue();
                            init_firs_photo(photos);
                        }
                        else {
                            showInfoController.init_photo(0,null);

                        }
                        showInfoController.show_details(details);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void init_firs_photo(List<String> photos){
        this.photos = photos;
        showInfoController.showInfoView.storageRef.child(photos.get(0)).getBytes(1024*1024*7).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap tempBitMap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                showInfoController.init_photo(photos.size(),tempBitMap);
            }
        });
    }


    public void get_photos(int index){
        showInfoController.showInfoView.storageRef.child(photos.get(index)).getBytes(1024*1024*7).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap tempBitMap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                showInfoController.show_photo(tempBitMap);
            }
        });
    }






}
