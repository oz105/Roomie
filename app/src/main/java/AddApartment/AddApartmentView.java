package AddApartment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.roomie_2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import AddApartment.AddApartmentController;

public class AddApartmentView extends AppCompatActivity {


    public EditText AppAddress,AppPassword,details,numOfRooms,price;
    public Button updateInfo;
    public AddApartmentController addApartmentController;

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
        addApartmentController = new AddApartmentController(this);
        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addApartmentController.save_apartment_details();

            }
        });


    }
}