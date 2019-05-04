package com.example.vdetails;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.annotation.NonNull;
import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.util.Base64;
import java.util.Random;

import com.example.vdetails.OcrManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.google.android.gms.internal.zzs.TAG;


public class takePic extends Activity {
    private int STORAGE_PERMISSION_CODE = 1;

    private static final int CAMERA_PIC_REQUEST = 2500;

    Bitmap image;
    String fname;
    String platenumber;
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_pic);

        Button b = findViewById(R.id.Button01);
        Button butt = findViewById(R.id.button2);
        Button getDetails = findViewById(R.id.button6);
        textview = findViewById(R.id.textView);
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(takePic.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(takePic.this, "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    requestStoragePermission();
                }

                if (ContextCompat.checkSelfPermission(takePic.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(takePic.this, "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    requestCameraPermission();
                }

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new takePic.TestOpenALPR().execute();
            }
        });

        getDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vehicledetails = new Intent(takePic.this,vehicleDetails.class);
                vehicledetails.putExtra("Vehicle_Number",platenumber);
                startActivity(vehicledetails);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            image = (Bitmap) data.getExtras().get("data");
            ImageView imageview =  findViewById(R.id.ImageView01);
            imageview.setImageBitmap(image);
        }
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Vdetails/images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        Log.i(TAG, "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.d("Images","Stored Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(takePic.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(takePic.this,
                                    new String[]{Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
        }
    }

    class TestOpenALPR extends AsyncTask<Void,Integer,String> {

        protected void onPreExecute (){
            super.onPreExecute();
            Log.d(TAG + " PreExceute","On pre Exceute......");
        }
        @TargetApi(Build.VERSION_CODES.O)

        protected String doInBackground(Void...arg0) {
            String json_content = "";
            Log.d("Inside TestOpenALPR", "Alpr");
            try {
                String secret_key = "sk_f7df03c694ef69992d41ec5f";

                // Read image file to byte array
                //   Path path = Paths.get("E:").resolve("sample.jpg");

                // Path path = Paths.get("android.resource://"+ R.drawable.blackwhite);

                byte[] data = Files.readAllBytes(Paths.get("/storage/emulated/0/Vdetails/images/"+fname));


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
                    JSONObject resultreader = new JSONObject(json_content);

                    Log.d("JSON object", resultreader.toString());
                    JSONArray platearray = resultreader.getJSONArray("results");

                    Log.d("JSON Array",platearray.toString());
                    JSONObject platedetails = platearray.getJSONObject(0);

                    Log.d("Inner JSON Object", platedetails.toString());
                    platenumber = platedetails.getString("plate");

                    Log.d("Plate numbers",platenumber);



                } else {
                    Log.d("Got non-200 response: ", Integer.toString(status_code));
                }


            } catch (MalformedURLException e) {
                Log.d("Bad URL", e.toString());
            } catch (IOException e) {
                Log.d("Failed to open connect", e.toString());
            }
            catch(Exception e){
                Log.d("Others",e.toString());
            }
            return platenumber;
        }

        protected void onProgressUpdate(Integer...a){
            super.onProgressUpdate(a);
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {
            //super.onPostExecute(result);
            Log.d(TAG + " onPostExecute", "" + result);
            textview.setText(platenumber);

        }

    }

}

