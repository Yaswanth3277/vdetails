package com.example.vdetails;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.example.vdetails.OcrManager;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class takePic extends Activity {
    private int STORAGE_PERMISSION_CODE = 1;

    private static final int CAMERA_PIC_REQUEST = 2500;

    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_pic);

        Button b = findViewById(R.id.Button01);
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

        class TestAsync extends AsyncTask<Void, Integer, String>
        {
            String Desc = "";
            String Owner = "";
            String Insur = "";
            String VehicleId = "";
            String RegDat = "";
            String VehicleFit = "";
            String RegLoc = "";

            String TAG = getClass().getSimpleName();

            protected void onPreExecute (){
                super.onPreExecute();
                Log.d(TAG + " PreExceute","On pre Exceute......");
            }

            protected String doInBackground(Void...arg0) {
                Log.d(TAG + " DoINBackGround","On doInBackground...");

                try {

                    String host = "doc.openalpr.com";
                    Socket socket = new Socket(host, 80);

                    String request = "GET http://doc.openalpr.com/api/?api=cloudapi#/default/recognizeFile HTTP/1.0\r\n\r\n";
                    OutputStream os = socket.getOutputStream();
                    os.write(request.getBytes());
                    os.flush();

                    InputStream in = socket.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String read;

                    while ((read = br.readLine()) != null) {

                        sb.append(read);
                    }

                    br.close();


                    String strXml = sb.toString();
                    Log.d("str", strXml);
                    int intStart = strXml.indexOf("<?xml");
                    strXml = strXml.substring(intStart);

                    socket.close();

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    ByteArrayInputStream input = new ByteArrayInputStream(strXml.getBytes("UTF-8"));
                    Document doc = builder.parse(input);
                    NodeList nList = doc.getElementsByTagName("vehicleJson");
                    Node nNode = nList.item(0);
                    Log.d("Output", nNode.getTextContent());
                    JSONObject reader = new JSONObject(nNode.getTextContent());

                    //JSONObject sys  = reader.getString("Description");
                    Desc = reader.getString("Description");

                    //Desc = nNode.getTextContent();

                } catch (Exception ex) {
                    Log.d("Error", ex.toString());
                }
                Log.d("output", Desc);



                return Desc;
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            image = (Bitmap) data.getExtras().get("data");
            ImageView imageview =  findViewById(R.id.ImageView01);
            imageview.setImageBitmap(image);
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
                                    new String[] {Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
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
                    new String[] {Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
        }
    }

}

