package com.temenos.dshubhamrajput.genericnet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by upriya on 06-03-2017.
 */

public class Addbeneficiary extends AppCompatActivity {

    public static String BenID;
    private String status = "no";

    // private String TAG = AddBeneficiary.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addbeneficiary);
        getSupportActionBar().setTitle("Add beneficiary");
        final HashMap<String, String> obj = new HashMap<>();
        final EditText BAccNo = (EditText) findViewById(R.id.BeneAccNo);
        final EditText Email = (EditText) findViewById(R.id.Email);
        final EditText Nickname = (EditText) findViewById(R.id.Nickname);
        final EditText IFSC = (EditText) findViewById(R.id.IFSC);
        final CheckBox withinbank1 = (CheckBox) findViewById(R.id.withinbank);
        final CheckBox neft1 = (CheckBox) findViewById(R.id.neft);
        final TextView t = (TextView) findViewById(R.id.textView5);
        final EditText e = (EditText) findViewById(R.id.IFSC);
        new NewDeal().execute();

        withinbank1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                neft1.setChecked(false);
                t.setVisibility(View.INVISIBLE);
                e.setVisibility(View.INVISIBLE);


            }
        });


        neft1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                withinbank1.setChecked(false);
                t.setVisibility(View.VISIBLE);
                e.setVisibility(View.VISIBLE);
                status = "yes";


            }
        });
        Button mSubmitButton = (Button) findViewById(R.id.button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (status.equals("yes")) {
                    String AccNo = BAccNo.getText().toString();
                    String Emailstr = Email.getText().toString();
                    String Nick = Nickname.getText().toString();
                    String IFSCstr = IFSC.getText().toString();
                    obj.put("BenAcctNo", AccNo);
                    obj.put("Email", Emailstr);
                    obj.put("Nickname", Nick);
                    obj.put("Ifsc", IFSCstr);
                } else {
                    String AccNo = BAccNo.getText().toString();
                    String Emailstr = Email.getText().toString();
                    String Nick = Nickname.getText().toString();
                    obj.put("BenAcctNo", AccNo);
                    obj.put("Email", Emailstr);
                    obj.put("Nickname", Nick);
                }
                new PostDetails(status, obj).execute();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    private class PostDetails extends AsyncTask<Void, Void, Void> {

        public String localStatus;
        public HashMap<String, String> localobj;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        public PostDetails(String status, HashMap<String, String> obj) {
            localStatus = status;
            localobj = obj;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh1= new HttpHandler();
            String url;
            String urlStr = "";
            HttpHandler sh = new HttpHandler();
            JSONObject postdata = new JSONObject();
            JSONArray array = new JSONArray();
            String response = "";
            PropertiesReader property = new PropertiesReader();
            JSONObject jsonObjarray = new JSONObject();
            String BenAcctNo = localobj.get("BenAcctNo");
            System.out.println(BenAcctNo);
            String Email = localobj.get("Email");
            String Nickname = localobj.get("Nickname");
            String Ifsc = "";



            // Log.e(TAG,"Response from url: " + jsonStr);
            String cusurl;
            String BencustomerNo="";
            String Benname ="";
            String jsonStr="";

            try {
                if (localStatus.equals("yes")) {

                    Ifsc = localobj.get("Ifsc");
                    postdata.put("BenAcctNo", BenAcctNo);
                    PropertiesReader property1 = new PropertiesReader();
                     cusurl= property1.getProperty("url_BenCustomer", getApplicationContext());
                    cusurl=cusurl+BenAcctNo;
                    jsonStr=sh.makeServiceCallGet(cusurl);
                    if ( jsonStr != null) {
                        try {
                            JSONObject cus1 = new JSONObject(jsonStr );
                            JSONObject  cus2  = cus1.getJSONObject("_embedded");
                             JSONArray cusarray1 =cus2.getJSONArray("item");
                            for(int k=0;k<cusarray1.length();k++)
                            {
                                    JSONObject cus3= cusarray1.getJSONObject(k);
                                    BencustomerNo=cus3.getString("CustomerNo");
                                    Benname= cus3.getString("Name");
                            }
                        } catch (final JSONException e) {
                            //  Log.e(TAG, "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    postdata.put("BenCustomer", BencustomerNo);
                    postdata.put("BeneficiaryId", BenID);
                    postdata.put("Email", Email);
                    postdata.put("Ifsc", Ifsc);
                    jsonObjarray.put("Nickname", Nickname);
                    array.put(jsonObjarray);
                    postdata.put("NicknameMvGroup", array);
                    postdata.put("OwningCustomer", "190077");
                    // urlStr= property.getProperty("url_beneficiary_Obnk_validate", getApplicationContext());
                    urlStr="http://10.93.22.116:9089/Test-iris/Test.svc/GB0010001/verBeneficiary_Obnks(\'"+ BenID +"\')/validate";
                    System.out.println(urlStr);
                } else {
                    postdata.put("BenAcctNo", BenAcctNo);
                    postdata.put("BenCustomer", "100100");
                    postdata.put("BeneficiaryId", BenID);
                    postdata.put("Email", Email);
                    jsonObjarray.put("Nickname", Nickname);
                    array.put(jsonObjarray);
                    postdata.put("NicknameMvGroup", array);
                    postdata.put("OwningCustomer", "190077");
                    //urlStr= property.getProperty("url_beneficiary_Wbnk_validate", getApplicationContext());
                    urlStr="http://10.93.22.116:9089/Test-iris/Test.svc/GB0010001/verBeneficiary_Wbnks(\'"+ BenID +"\')/validate";

                }
            } catch (final JSONException e) {
                //  Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            String text,info;
            try {

                    URL u = new URL(urlStr);
                    String basicAuth = "";
                    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                    String userPass = "PAYUSER1" + ":" + "123456";
                    basicAuth = "Basic " + new String((new Base64()).encode(userPass.getBytes()));
                    conn.setRequestProperty("Authorization", basicAuth);
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);


                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(postdata.toString());

                    writer.flush();
                    writer.close();
                    os.close();
                int responseCode = conn.getResponseCode();
                    System.out.println(responseCode);
                    BufferedInputStream in  ;
                    BufferedReader reader ;
                    StringBuilder sb ;

                if (responseCode >= 200 && responseCode < 400) {
                   in = new BufferedInputStream(conn.getInputStream());
                      reader = new BufferedReader(new InputStreamReader(in));
                    sb = new StringBuilder();

                    try {
                        String line;
                        try {
                            while((line = reader.readLine()) != null) {
                                sb.append(line).append('\n');
                            }
                        } catch (IOException var14) {
                            var14.printStackTrace();
                        }
                    } finally {
                        try {
                            in.close();
                        } catch (IOException var13) {
                            var13.printStackTrace();
                        }

                    }

                } else {
                    // READING THE ERROR
                    in = new BufferedInputStream(conn.getErrorStream());
                    reader = new BufferedReader(new InputStreamReader(in));
                    sb = new StringBuilder();

                    try {
                        String line;
                        try {
                            while((line = reader.readLine()) != null) {
                                sb.append(line).append('\n');
                            }
                        } catch (IOException var14) {
                            var14.printStackTrace();
                        }
                    } finally {
                        try {
                            in.close();
                        } catch (IOException var13) {
                            var13.printStackTrace();
                        }

                    }
                    String jsonstr=sb.toString();

                    JSONObject jsonErrorObj= new JSONObject(jsonstr);
                    try {
                        JSONObject jsonEmbedObj = jsonErrorObj.getJSONObject("_embedded");
                        JSONArray jsonErrorArrObj= jsonEmbedObj.getJSONArray( "http://temenostech.temenos.com/rels/errors");
                        for(int i=0; i<jsonErrorArrObj.length();i++)
                        {
                            JSONObject item = jsonErrorArrObj.getJSONObject(i);
                            JSONArray errorlist=item.getJSONArray("ErrorsMvGroup");

                            for(int j=0;j<errorlist.length();j++)
                            {
                                JSONObject error = errorlist.getJSONObject(j);
                              text = error.getString("Text");
                                info =error.getString("Info");

                                System.out.println(text);
                                System.out.println(info);
                            }
                        }
                    }catch(Exception exception){}

                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    private class NewDeal extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url;
            try {
                PropertiesReader property = new PropertiesReader();


                url = property.getProperty("url_beneficiary_Wbnk_new", getApplicationContext());

                String jsonStr = sh.makeServiceCall(url);

                if (jsonStr != null) {
                    try {

                        JSONObject jsonObj = new JSONObject(jsonStr);
                          BenID = jsonObj.getString("BeneficiaryId");
                    } catch (final JSONException e) {
                        //  Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }


    }
}

