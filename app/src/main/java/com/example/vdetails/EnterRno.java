package com.example.vdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnterRno extends AppCompatActivity {

    EditText RegistrationNo;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterno);

        RegistrationNo = findViewById(R.id.editText);
        button = findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RegistrationNo.getText().toString().matches("")){
                    Toast.makeText(EnterRno.this,"No Registration Number to get Details",Toast.LENGTH_SHORT).show();
                }
                else{

                    Intent vehicledetails = new Intent(EnterRno.this, vehicleDetails.class);
                    vehicledetails.putExtra("Vehicle_Number", RegistrationNo.getText().toString());
                    startActivity(vehicledetails);
                }
            }
        });
    }
}
