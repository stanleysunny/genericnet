package com.temenos.dshubhamrajput.genericnet;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

public class ConfirmPage extends AppCompatActivity {
    public Intent test;
    public static String imp;
    public EditText e5;
    public TextView t1,t2,t3,t4,t5;
    public EditText e1,e2,e3,e4;
    final HashMap<String, String> obj = new HashMap<>();
    boolean success=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_page);
        e5=(EditText)findViewById(R.id.editText8);
        t1=(TextView) findViewById(R.id.textView);
        t2=(TextView) findViewById(R.id.textView3);
        t3=(TextView) findViewById(R.id.textView5);
        t4=(TextView) findViewById(R.id.textView6);
        t5=(TextView) findViewById(R.id.textView11);


        test=getIntent();
        Bundle extras = getIntent().getExtras();
        imp = extras.getString("getintent");
        if(imp.equals("internal"))
        {

            EditText Accno= (EditText)findViewById(R.id.editText);
            EditText Email = (EditText)findViewById(R.id.editText6);
            EditText nickname=(EditText)findViewById(R.id.editText7);
            EditText Customername=(EditText)findViewById(R.id.editText10);
           e5.setVisibility(View.GONE);
            t1.setText("Account Number");
            t2.setText("Email");
            t3.setText("Nick Name");
            t4.setText("Customer Name");
            Accno.setText(extras.getString("BenAcctNo"));
            Email.setText(extras.getString("Email"));
            nickname.setText(extras.getString("Nickname"));
            Customername.setText(extras.getString("Benname"));
            obj.put("BenAcctNo",extras.getString("BenAcctNo"));
            obj.put("BenCustomer",extras.getString("BenCustomer"));
            obj.put("BeneficiaryId",extras.getString("BeneficiaryId"));
            obj.put("Email",extras.getString("Email"));
            obj.put("Nickname",extras.getString("Nickname"));
            obj.put("OwningCustomer",extras.getString("OwningCustomer"));



        }
        else if(imp.equals("external"))
        {
            EditText Accno= (EditText)findViewById(R.id.editText);
            EditText Email = (EditText)findViewById(R.id.editText6);
            EditText nickname=(EditText)findViewById(R.id.editText7);
            EditText Customername=(EditText)findViewById(R.id.editText10);
            EditText Ifsc=(EditText)findViewById(R.id.editText8);


            t1.setText("Account Number");
            t2.setText("Email");
            t3.setText("Nick Name");
            t4.setText("IFSC code");
            t5.setText("Customer Name");
            Accno.setText(extras.getString("BenAcctNo"));
            Email.setText(extras.getString("Email"));
            nickname.setText(extras.getString("Nickname"));
            Customername.setText(extras.getString("Ifsc"));
            Ifsc.setText(extras.getString("Benname"));
            obj.put("BenAcctNo",extras.getString("BenAcctNo"));
            obj.put("BenCustomer",extras.getString("BenCustomer"));
            obj.put("BeneficiaryId",extras.getString("BeneficiaryId"));
            obj.put("Email",extras.getString("Email"));
            obj.put("Nickname",extras.getString("Nickname"));
            obj.put("Ifsc",extras.getString("Ifsc"));
            obj.put("OwningCustomer",extras.getString("OwningCustomer"));

        }

        Button viewStmt = (Button) findViewById(R.id.button2);
        viewStmt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new Commit(obj).execute();

            }
        });
        Button viewStmt1 = (Button) findViewById(R.id.button);
        viewStmt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


            }
        });

    }

private class Commit extends AsyncTask<Void, Void, Void> {

    HashMap<String, String> obj1= new HashMap<>();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

public Commit(HashMap<String, String> obj )
{
    obj1=obj;
}
    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();
        String url;
        try {
            PropertiesReader property = new PropertiesReader();
            if (imp.equals("external"))
                url = property.getProperty("url_beneficiary_Obnk_Input", getApplicationContext());
            else
                url = property.getProperty("url_beneficiary_Wbnk_Input", getApplicationContext());


            String BenAcctNo = obj1.get("BenAcctNo");
            String BencustomerNo = obj1.get("BenCustomer");
            String BenID = obj1.get("BeneficiaryId");
            String Email = obj1.get("Email");
            String Nickname = obj1.get("Nickname");
            String Ifsc = obj1.get("Ifsc");


            JSONObject jsonObjarray = new JSONObject();
            JSONObject postdata = new JSONObject();
            JSONArray array = new JSONArray();


            if (imp.equals("external")) {

                postdata.put("BenAcctNo", BenAcctNo);
                postdata.put("BenCustomer", BencustomerNo);
                postdata.put("BeneficiaryId", BenID);
                postdata.put("Email", Email);
                postdata.put("Ifsc", Ifsc);
                jsonObjarray.put("Nickname", Nickname);
                array.put(jsonObjarray);
                postdata.put("NicknameMvGroup", array);
                postdata.put("OwningCustomer", "190077");

            } else {
                postdata.put("BenAcctNo", BenAcctNo);
                postdata.put("BenCustomer", BencustomerNo);
                postdata.put("BeneficiaryId", BenID);
                postdata.put("Email", Email);
                jsonObjarray.put("Nickname", Nickname);
                array.put(jsonObjarray);
                postdata.put("NicknameMvGroup", array);
                postdata.put("OwningCustomer", "190077");
            }


            success = sh.jsonWrite(url, postdata);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if(success)
        Toast.makeText(ConfirmPage.this, "success", Toast.LENGTH_LONG).show();
    }


}
}