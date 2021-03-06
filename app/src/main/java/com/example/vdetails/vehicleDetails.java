package com.example.vdetails;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class vehicleDetails extends AppCompatActivity {

    String Vehicle_number;
    String Vehicle_numb;
    public TextView Registration_Number;
    public EditText Vehicle_num;
    public Button get_details;
    public EditText Owner_Name;
    public EditText Insurance;
    public EditText Vid;
    public EditText RegDate;
    public EditText Vfit;
    public EditText Regloc;
    public EditText FuelType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_details);
        Bundle b = this.getIntent().getExtras();
        Vehicle_numb = b.getString("Vehicle_Number");
        Vehicle_number = Vehicle_numb.replaceAll("\\s+","");
        Registration_Number = findViewById(R.id.Regno);
        Registration_Number.setText(Vehicle_number);
        Vehicle_num = findViewById(R.id.Vnum);
        Owner_Name = findViewById(R.id.owner_name);
        Insurance = findViewById(R.id.insurance);
        Vid = findViewById(R.id.vid);
        RegDate = findViewById(R.id.Regdate);
        Vfit = findViewById(R.id.Vfit);
        Regloc = findViewById(R.id.RegLoc);
        FuelType = findViewById(R.id.fuelType);
        Log.d("Test", "Testing Debug");

        new TestAsync().execute();


    }

    class TestAsync extends AsyncTask<Void, Integer, String>
    {
        String Desc = "";
        String Owner = "";
        String Insur = "";
        String VehicleId = "";
        String RegDat = "";
        String VehicleFit = "";
        String RegLoc = "";
        String fueltype = "";
        String fueltypename = "";

        String TAG = getClass().getSimpleName();

        protected void onPreExecute (){
            super.onPreExecute();
            Log.d(TAG + " PreExceute","On pre Exceute......");
        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround","On doInBackground...");

            try {

                String host = "www.regcheck.org.uk";
                Socket socket = new Socket(host, 80);

                String request = "GET http://www.regcheck.org.uk/api/reg.asmx/CheckIndia?RegistrationNumber="+Vehicle_number+"&username=shashi HTTP/1.0\r\n\r\n";
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
                if(reader.has("Description")) {
                    Desc = reader.getString("Description");
                }else{
                    Desc = "Not Available";
                }

                if(reader.has("Insurance")) {
                    Insur = reader.getString("Insurance");
                }else{
                    Insur = "Not Available";
                }

                if(reader.has("VechileIdentificationNumber")) {
                    VehicleId = reader.getString("VechileIdentificationNumber");
                }else{
                    VehicleId = "Not Available";
                }

                if(reader.has("RegistrationDate")) {
                    RegDat = reader.getString("RegistrationDate");
                }else{
                    RegDat = "Not Available";
                }

                if(reader.has("Fitness")) {
                    VehicleFit = reader.getString("Fitness");
                }else{
                    VehicleFit = "Not Available";
                }

                if(reader.has("Location")) {
                    RegLoc = reader.getString("Location");
                }else{
                    RegLoc = "Not Available";
                }

                if(reader.has("Owner")) {
                    Owner = reader.getString("Owner");
                }else{
                    Owner = "Not Available";
                }

                if(reader.has("FuelType")) {
                    fueltype = reader.getString("FuelType");
                    JSONObject fuelreader = new JSONObject(fueltype);
                    fueltypename = fuelreader.getString("CurrentTextValue");
                }
                else{
                    fueltypename = "Not Available";
                }



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

            if(!Desc.equals("")) {
                Vehicle_num.setText(Desc);
            }else{
                Vehicle_num.setText("Not Available");
            }

            if(!Owner.equals("")) {
                Owner_Name.setText(Owner);
            }else{
                Owner_Name.setText("Not Available");
            }

            if(!Insur.equals("")) {
                Insurance.setText(Insur);
            }else{
                Insurance.setText("Not Available");
            }

            if(!VehicleId.equals("")) {
                Vid.setText(VehicleId);
            }else{
                Vid.setText("Not Available");
            }

            if(!RegDat.equals("")) {
                RegDate.setText(RegDat);
            }else{
                RegDate.setText("Not Available");
            }

            if(!VehicleFit.equals("")) {
                Vfit.setText(VehicleFit);
            }else{
                Vfit.setText("Not Available");
            }

            if(!RegLoc.equals("")) {
                Regloc.setText(RegLoc);
            }else{
                Regloc.setText("Not Available");
            }

            if(!fueltypename.equals("")){

                FuelType.setText(fueltypename);
            }
            else{

                FuelType.setText("Not Available");
            }

        }
    }
}

