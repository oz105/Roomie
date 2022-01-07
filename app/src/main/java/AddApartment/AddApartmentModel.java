package AddApartment;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.roomie_2.Apartment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

import AddApartment.AddApartmentController;

public class AddApartmentModel {

    private FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    private DatabaseReference rootApartmrnt = db.getReference().child("Apartments");
    private DatabaseReference rootUser = db.getReference().child("Users");
    private FirebaseAuth Auth;
    private AddApartmentController addApartController;

    private int currentIndex;
    private List<String> nameList;
    private String [] imageNames;
    private Uri[] imageUris;
    private int imgIndex;
    private int numpics;
    private int sumUpload;
    private StorageReference StorageRef;
    private StorageTask UploadTask;



    public AddApartmentModel(AddApartmentController controller){
        this.addApartController = controller;
        Auth = FirebaseAuth.getInstance();
        this.StorageRef = FirebaseStorage.getInstance().getReference("uploads");
        imageNames = new String[4];
        imageUris = new Uri[4];
    }



    public void add_apartment(Map<String,Object> details){
        String adminID = Auth.getCurrentUser().getUid();
        final Apartment[] newApartment = {null};
        db.getReference().child("apartmentIds").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Long> apartmentIdwList = null;
                long newApartmentId;
                if(snapshot.exists()){
                    apartmentIdwList = (List<Long>) (snapshot.getValue());
                    newApartmentId = find_available_appartment_id(apartmentIdwList);
                }
                else {
                    apartmentIdwList = new ArrayList<>();
                    newApartmentId = 0;
                    apartmentIdwList.add(newApartmentId);

                }
                newApartment[0] = new Apartment(details,newApartmentId);
                newApartment[0].setAdminId(adminID);
                finish_add_apartment(newApartment[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public long find_available_appartment_id( List<Long> apartmentIdwList){
        long lastId = apartmentIdwList.get(0);
        for (int i = 1; i < apartmentIdwList.size(); i++) {
            if(apartmentIdwList.get(i)!=lastId+1){
                apartmentIdwList.add(i-1,lastId+1);
                return lastId+1;
            }
            lastId = apartmentIdwList.get(i);
        }
        apartmentIdwList.add(lastId+1);
        return lastId+1;
    }

    public void finish_add_apartment(Apartment apartment){
        apartment.setPhotos(Arrays.asList(this.imageNames.clone()));
        rootApartmrnt.child(String.valueOf(apartment.getApartmentId())).setValue(apartment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    rootUser.child(apartment.getAdminId()).child("hasApartment").setValue(true);
                    rootUser.child(apartment.getAdminId()).child("apartmentId").setValue(apartment.getApartmentId());
                    addApartController.finish_create_apartment(true,apartment.getApartmentId());
                }
                else{
                    addApartController.finish_create_apartment(false,apartment.getApartmentId());
                }
            }
        });
    }


    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void add_new_photo(Uri result){


        imageUris [currentIndex] = result;
        imageNames [currentIndex] = System.currentTimeMillis()
                + result.toString().substring(result.toString().lastIndexOf("."));
        addApartController.set_image(currentIndex, result);
    }

    public void finish_edit_info(){

        numpics=0;
        sumUpload=0;
        List <Uri> ImgList = new LinkedList<>();
        List <String> ImgNamesList = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            if(imageNames[i]!=null)
            {
                numpics++;
                ImgList.add(imageUris[i]);
                ImgNamesList.add(imageNames[i]);
            }
        }
        nameList = ImgNamesList;
        for (int i =0; i< ImgNamesList.size();i++){
            uploadFile(ImgNamesList.get(i),ImgList.get(i));
        }

    }

    public void uploadFile(String fileName, Uri resultUri) {
        StorageReference fileReference = StorageRef.child(fileName);
        //String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        setUploadTask(
                fileReference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<com.google.firebase.storage.UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Handler handler = new Handler();
//
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() { addApartmentView.progressBar.setProgress(0); }
//                        }, 500);
//                        make_toast("Upload successful "+ sumUpload);
//                        if (taskSnapshot.getMetadata().getReference() != null) {
//                            //Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
//                        }
                        sumUpload++;
                        if(sumUpload==numpics)
                        {
                            addApartController.save_apartment_details();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                        addApartmentView.progressBar.setProgress((int) progress);
                    }
                }));
    }


    public void setUploadTask(StorageTask uploadTask) {
        UploadTask = uploadTask;
    }

}





