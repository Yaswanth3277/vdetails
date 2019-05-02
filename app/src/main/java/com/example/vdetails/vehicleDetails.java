package com.example.vdetails;

import android.os.Bundle;
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

        try{

            String host = "www.regcheck.org.uk";
            Socket socket = new Socket(host, 80);
// change yourusernamehere
            String request = "GET http://www.regcheck.org.uk/api/reg.asmx/CheckIndia?RegistrationNumber=" + Registration_Number + "&username=sauravbv HTTP/1.0\r\n\r\n";
            OutputStream os = socket.getOutputStream();
            os.write(request.getBytes());
            os.flush();

            InputStream in = socket.getInputStream();
            StringBuilder sb=new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String read;

            while((read=br.readLine()) != null) {
//System.out.println(read);
                sb.append(read);
            }

            br.close();
            String strXml = sb.toString();
            int intStart = strXml.indexOf("<?xml");
            strXml = strXml.substring(intStart);
//System.out.print(strXml);
            socket.close();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(strXml.getBytes("UTF-8"));
            Document doc = builder.parse(input);
            NodeList nList = doc.getElementsByTagName("vehicleJson");
            Node nNode = nList.item(0);
            //System.out.print(nNode.getTextContent());
            JSONObject reader = new JSONObject(nNode.getTextContent());

            //JSONObject sys  = reader.getJSONObject("Description");
            Description = sys.getString("Description");

        }
        catch(Exception ex)
        {
            System.out.print("Error");
        }

    }
}
