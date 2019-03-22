package com.example.vdetails;

import android.graphics.Bitmap;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.example.vdetails.MainApplication;


public class OcrManager {

    TessBaseAPI baseAPI = null;
    public void initAPI(){


        baseAPI= new TessBaseAPI();
        String dataPath = MainApplication.instance.getTessDataParentDirectory();
        baseAPI.init(dataPath,"eng");

    }

    public String startRecognize(Bitmap bitmap)
    {
        if(baseAPI ==null)
            initAPI();
        baseAPI.setImage(bitmap);
        return baseAPI.getUTF8Text();
    }

}