package com.example.vdetails;

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

    String Desc = "";
    public TextView Registration_Number;
    public EditText Vehicle_num;
    public Button get_details;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_details);
        Bundle b = this.getIntent().getExtras();
        String Vehicle_number = b.getString("Vehicle_Number");
        Registration_Number = findViewById(R.id.Regno);
        Registration_Number.setText(Vehicle_number);
        Vehicle_num = findViewById(R.id.Vnum);
        get_details = findViewById(R.id.button5);
        Log.d("Test", "Testing Debug");

        new TestAsync().execute();

    }



}

class Vdetails extends AsyncTask<String, Void, Void> {

    private Exception exception;

    protected Void doInBackground(String... urls) {

        try {

            String host = "www.regcheck.org.uk";
            Socket socket = new Socket(host, 80);

            String request = "GET http://www.regcheck.org.uk/api/reg.asmx/CheckIndia?RegistrationNumber=KA05KG2990&username=sauravbv HTTP/1.0\r\n\r\n";
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
            //Desc = reader.getString("Description");
            Desc = nNode.getTextContent();
            Vehicle_num.setText(Desc);
        } catch (Exception ex) {
            Log.d("Error", ex.toString());
        }
        Log.d("output", Desc);


        protected void onPostExecute (RSSFeed feed){

        }
    }
}

class TestAsync extends AsyncTask<Void, Integer, String>
{
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

            String request = "GET http://www.regcheck.org.uk/api/reg.asmx/CheckIndia?RegistrationNumber=KA05KG2990&username=sauravbv HTTP/1.0\r\n\r\n";
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
            //Desc = reader.getString("Description");
            Desc = nNode.getTextContent();
            Vehicle_num.setText(Desc);
        } catch (Exception ex) {
            Log.d("Error", ex.toString());
        }
        Log.d("output", Desc);

        return "You are at PostExecute";
    }

    protected void onProgressUpdate(Integer...a){
        super.onProgressUpdate(a);
        Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d(TAG + " onPostExecute", "" + result);
    }
}