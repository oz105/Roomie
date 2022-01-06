package Info;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.roomie_2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class EditInfoView extends AppCompatActivity {
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://roomie-f420f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference apartmentRef = db.getReference().child("Apartments");
    DatabaseReference userRef = db.getReference().child("Users");

    EditText AppAddress,AppPassword,details,numOfRooms,price;
    TextView id;
    Button updateInfo;
    FirebaseAuth Auth;
    private String apartmentId="";
    private Map<String,Object> apartmentInfo;
    EditInfoController editInfoController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info_view);
        Auth = FirebaseAuth.getInstance();
        id = (TextView)findViewById(R.id.appartmentId) ;
        AppAddress = (EditText) findViewById(R.id.address);
        AppPassword = (EditText) findViewById(R.id.password);
        numOfRooms = (EditText) findViewById(R.id.rooms);
        details = (EditText) findViewById(R.id.details);
        price = (EditText) findViewById(R.id.rent);
        updateInfo = (Button) findViewById(R.id.updateData);
        editInfoController = new EditInfoController(this);
        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfoController.update_data();

            }
        });
    }
}