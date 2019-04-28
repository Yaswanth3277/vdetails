package com.example.vdetails;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.vdetails.OcrManager;

public class uploadPic extends AppCompatActivity {

    public void onGetText(View view){

        TextView text = findViewById(R.id.urlDisp);

        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.textfile);
        OcrManager ocr = new OcrManager();
        String imageText = ocr.startRecognize(bm);
        text.setText(imageText);




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_pic);

        OcrManager manager = new OcrManager();
        manager.initAPI();

    }
}
