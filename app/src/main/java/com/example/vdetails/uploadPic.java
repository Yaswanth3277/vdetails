package com.example.vdetails;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.util.Base64;

import com.example.vdetails.OcrManager;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.google.android.gms.internal.zzs.TAG;

public class uploadPic extends Activity {

    TextView textTargetUri;
    ImageView targetImage;
    Bitmap bitmap;
    TextView textview;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_pic);

        OcrManager manager = new OcrManager();
        manager.initAPI();
        Button buttonLoadImage = findViewById(R.id.loadimage);
        textTargetUri = findViewById(R.id.targeturi);
        targetImage = findViewById(R.id.targetimage);
        Button butt = findViewById(R.id.button);

        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }});

        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TestOpenALPR().execute();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            textTargetUri.setText(targetUri.toString());

            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }
        }
    }

    class TestOpenALPR extends AsyncTask<Void,Integer,String> {

        protected void onPreExecute (){
            super.onPreExecute();
            Log.d(TAG + " PreExceute","On pre Exceute......");
        }

        protected String doInBackground(Void...arg0) {
            String json_content = "";
            Log.d("Inside TestOpenALPR", "Alpr");
            textview = findViewById(R.id.textDisp);
            textview.setText("working");
            try {
                String secret_key = "sk_f7df03c694ef69992d41ec5f";

                // Read image file to byte array
                Path path = Paths.get("D://Yaswanth//Projects//Final Year Project//images//blackwhite.jpg");
                byte[] data = Files.readAllBytes(path);

                // Encode file bytes to base64
                byte[] encoded = Base64.getEncoder().encode(data);


                // Setup the HTTPS connection to api.openalpr.com
                URL url = new URL("https://api.openalpr.com/v2/recognize_bytes?secret_key=sk_f7df03c694ef69992d41ec5f&recognize_vehicle=0&country=in&return_image=0&topn=10");
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection) con;
                http.setRequestMethod("POST"); // PUT is another valid option
                http.setFixedLengthStreamingMode(encoded.length);
                http.setDoOutput(true);

                // Send our Base64 content over the stream
                try (OutputStream os = http.getOutputStream()) {
                    os.write(encoded);
                }

                int status_code = http.getResponseCode();
                if (status_code == 200) {
                    // Read the response
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            http.getInputStream()));

                    String inputLine;
                    while ((inputLine = in.readLine()) != null)
                        json_content += inputLine;
                    in.close();

                    Log.d("Json Content", json_content);
                } else {
                    Log.d("Got non-200 response: ", Integer.toString(status_code));
                }


            } catch (MalformedURLException e) {
                Log.d("Bad URL", e.toString());
            } catch (IOException e) {
                Log.d("Failed to open connection", e.toString());
            }
            return json_content;
        }

        protected void onProgressUpdate(Integer...a){
            super.onProgressUpdate(a);
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {
            //super.onPostExecute(result);
            Log.d(TAG + " onPostExecute", "" + result);

        }

    }
}


