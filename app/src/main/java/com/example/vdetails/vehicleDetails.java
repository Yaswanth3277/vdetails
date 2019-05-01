package com.example.vdetails;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class vehicleDetails extends AppCompatActivity {

    public TextView Registration_Number;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_details);
        Bundle b = this.getIntent().getExtras();
        String Vehicle_number = b.getString("Vehicle_Number");
        Registration_Number = findViewById(R.id.Regno);
        Registration_Number.setText(Vehicle_number);

    }
}
