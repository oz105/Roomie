package AddApartment;

import androidx.annotation.NonNull;

import com.example.roomie_2.Apartment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import AddApartment.AddApartmentController;

public class AddApartmentModel {


    private AddApartmentController addApartController;
    public AddApartmentModel(AddApartmentController controller){
        this.addApartController = controller;
    }



    public void add_apartment(Map<String,Object> details,String adminID){
        final Apartment[] newApartment = {null};
        addApartController.addApartmentView.db.getReference().child("apartmentIds").addListenerForSingleValueEvent(new ValueEventListener() {
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
                finish_addApartment(newApartment[0]);
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

    public void finish_addApartment(Apartment apartment){
        addApartController.addApartmentView.rootApartmrnt.child(String.valueOf(apartment.getApartmentId())).setValue(apartment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    addApartController.addApartmentView.rootUser.child(apartment.getAdminId()).child("hasApartment").setValue(true);
                    addApartController.addApartmentView.rootUser.child(apartment.getAdminId()).child("apartmentId").setValue(apartment.getApartmentId());
                    addApartController.finish_create_apartment(true,apartment.getApartmentId());
                }
                else{
                    addApartController.finish_create_apartment(false,apartment.getApartmentId());
                }
            }
        });



    }

}





