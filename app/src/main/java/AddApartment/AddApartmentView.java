package AddApartment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.roomie_2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import AddApartment.AddApartmentController;

public class AddApartmentView extends AppCompatActivity implements View.OnClickListener {


    public EditText AppAddress,AppPassword,details,numOfRooms,price;
    public Button updateInfo;
    public AddApartmentController addApartmentController;
    public ImageView image1,image2,image3,image4;

    public int curImg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apartment_view);

        AppAddress = (EditText) findViewById(R.id.address);
        AppPassword = (EditText) findViewById(R.id.password);
        numOfRooms = (EditText) findViewById(R.id.rooms);
        details = (EditText) findViewById(R.id.details);
        price = (EditText) findViewById(R.id.rent);

        updateInfo = (Button) findViewById(R.id.updateData);

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);

        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);

        updateInfo.setOnClickListener(this);





        addApartmentController = new AddApartmentController(this);
//        updateInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addApartmentController.save_apartment_details();
//
//            }
//        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.updateData:
                addApartmentController.prepareUploads();
                break;
            case R.id.image1:
                addApartmentController.choose_photo(0);
                break;
            case R.id.image2:
                addApartmentController.choose_photo(1);
                break;
            case R.id.image3:
                addApartmentController.choose_photo(2);
                break;
            case R.id.image4:
                addApartmentController.choose_photo(3);
                break;
        }

    }

    public void check_permission(){


        if(!checkPermission()){
            requestPermission();
        }else{
            CropImage.activity().start(this);
        }

    }

    private boolean checkPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }

    private void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                addApartmentController.finish_take_photo(result.getUri());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                addApartmentController.make_toast("bad image");
            }
        }
    }


}
