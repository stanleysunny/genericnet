package com.temenos.dshubhamrajput.genericnet;

import android.os.AsyncTask;

import android.util.Log;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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



/**
 * Created by upriya on 06-03-2017.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Addbeneficiary extends AppCompatActivity {

    public String intentData = "internal";
    public Intent commit;

    public static String BenID;
    private static String status = "no";
    public static boolean success= true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addbeneficiary);
        getSupportActionBar().setTitle("Add beneficiary");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     //   final HashMap<String, String> obj = new HashMap<>();
        final CheckBox withinbank1 = (CheckBox) findViewById(R.id.withinbank);
        final CheckBox neft1 = (CheckBox) findViewById(R.id.neft);
        final TextView ifscTextview = (TextView) findViewById(R.id.textView5);
        final EditText ifscEtext = (EditText) findViewById(R.id.Ifsc);
        final EditText benAccNo = (EditText) findViewById(R.id.BenAccNo);
        final EditText accNoCheck = (EditText) findViewById(R.id.ReenterAccNo);
        final EditText emailUser = (EditText) findViewById(R.id.Email);
        final EditText Nickname = (EditText) findViewById(R.id.NickName);
        final ImageView helpicon = (ImageView) findViewById(R.id.help_icon);

        new NewDeal().execute();


        withinbank1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (withinbank1.isChecked()) {
                    neft1.setChecked(false);
                    ifscTextview.setVisibility(View.INVISIBLE);
                    ifscEtext.setVisibility(View.INVISIBLE);
                    helpicon.setVisibility(View.INVISIBLE);
                    intentData = "internal";

                }
            }
        });

        neft1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (neft1.isChecked()) {
                    withinbank1.setChecked(false);
                    ifscTextview.setVisibility(View.VISIBLE);
                    ifscEtext.setVisibility(View.VISIBLE);
                    helpicon.setVisibility(View.VISIBLE);
                    intentData = "external";
                    status="yes";


                }
            }
        });
         Button viewStmt = (Button) findViewById(R.id.BenButton);
        viewStmt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(intentData.equals("external")) {
                    if ((ifscEtext.getText().toString()).matches(""))
                        ifscEtext.setError("This field cannot be left blank");
                }
                if ((benAccNo.getText().toString()).matches(""))
                    benAccNo.setError("This field cannot be left blank");
                if ((accNoCheck.getText().toString()).matches(""))
                    accNoCheck.setError("This field cannot be left blank");
                if ((Nickname.getText().toString()).matches(""))
                     Nickname.setError("This field cannot be left blank");
                if (((benAccNo.getError() == null) && (accNoCheck.getError() == null) && (emailUser.getError() == null) && ( Nickname.getError() == null) && (ifscEtext.getError() == null)))
                {
                    if (status.equals("yes")) {
                        String AccNo = benAccNo.getText().toString();
                        String Emailstr = emailUser.getText().toString();
                        String Nick = Nickname.getText().toString();
                        String IFSCstr = ifscEtext.getText().toString();
                        new PostDetails().execute(status,AccNo, Emailstr, Nick,IFSCstr);
                    } else {
                        String AccNo = benAccNo.getText().toString();
                        String Emailstr = emailUser.getText().toString();
                        String Nick = Nickname.getText().toString();

                        new PostDetails().execute(status,AccNo, Emailstr, Nick);
                    }

                }

            }
        });

        accNoCheck.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!(accNoCheck.getText().toString().equals(benAccNo.getText().toString()))) {
                    accNoCheck.setError("Account numbers don't match");
                } else if (((accNoCheck.getText().toString()).matches(""))) {
                    accNoCheck.setError("This field cannot be left blank");
                }

            }

        });

        emailUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final String email = emailUser.getText().toString();
                    if (!(emailValidator(email))) {
                        emailUser.setError("Enter a valid email id");
                    } else if ((emailUser.getText().toString()).matches("")) {
                        emailUser.setError(null);
                    }
                }
            }
        });

        ifscEtext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String ifsc = ifscEtext.getText().toString();
                    boolean check = ifscMatcher(ifsc);
                    if (!check) {
                        ifscEtext.setError("IFSC is a 11 digit alpha numeric string");
                    }
                    else if (ifscEtext.getText().toString().matches("")) {
                        ifscEtext.setError("This field cannot be left blank");
                    }
                }
            }
        });
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean ifscMatcher(String ifsc) {
        Pattern pattern;
        Matcher matcher;
        final String ifscChecker = "^[A-Z]{4}\\d{7}$" ;
        pattern = Pattern.compile(ifscChecker);
        matcher = pattern.matcher(ifsc);
        return matcher.matches();
    }

    public void showHelpText(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.ifsc_help)
                .setTitle(R.string.ifschelppopup)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //nothing is done
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }



    private class PostDetails extends AsyncTask<String, Void, Void> {

        public String localStatus;
        public HashMap<String, String> localobj;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... param) {
            String urlStr = "";
            JSONObject postdata = new JSONObject();
            JSONArray array = new JSONArray();
            JSONObject jsonObjarray = new JSONObject();
            String BenAcctNo = param[1];
            System.out.println(BenAcctNo);
            String Email = param[2];
            String Nickname = param[3];
            String Ifsc = "";
            localStatus= param[0];
            HttpHandler sh1 = new HttpHandler();

            // Log.e(TAG,"Response from url: " + jsonStr);
            String cusurl;
            String BencustomerNo = "";
            String Benname = "";
            Bundle benBundle = new Bundle();
            String text, info;
            // COMMON FOR BOTH
            try {
                postdata.put("BenAcctNo", BenAcctNo);
                PropertiesReader property1 = new PropertiesReader();
                cusurl = property1.getProperty("url_BenCustomer", getApplicationContext());
                GetBenCustomer benObj = new GetBenCustomer();
                localobj = benObj.getBenCus(cusurl, BenAcctNo);
                BencustomerNo = localobj.get("BencustomerNo");
                Benname = localobj.get("Benname");
                System.out.println(localobj.get("BencustomerNo"));
                System.out.println(localobj.get("Benname"));

                postdata.put("BenCustomer", BencustomerNo);
                postdata.put("BeneficiaryId", BenID);
                postdata.put("Email", Email);


                //common bundle
                benBundle.putString("BenAcctNo", BenAcctNo);
                benBundle.putString("BenCustomer",  BencustomerNo);
                benBundle.putString("BeneficiaryId", BenID);
                benBundle.putString("Email", Email);
                benBundle.putString("Nickname", Nickname);
                benBundle.putString("Benname", Benname);
                benBundle.putString("OwningCustomer", "190077");
                benBundle.putString("getintent", intentData);
                if (localStatus.equals("yes")) {
                    Ifsc=param[4];
                    postdata.put("Ifsc", Ifsc);
                    jsonObjarray.put("Nickname", Nickname);
                    array.put(jsonObjarray);
                    postdata.put("NicknameMvGroup", array);
                    postdata.put("OwningCustomer", "190077");

                    //adding the bundle
                    benBundle.putString("Ifsc", Ifsc);
                    // get it from constant properties
                    urlStr = "http://10.93.22.116:9089/Test-iris/Test.svc/GB0010001/verBeneficiary_Obnks(\'" + BenID + "\')/validate";
                } else {
                    jsonObjarray.put("Nickname", Nickname);
                    array.put(jsonObjarray);
                    postdata.put("NicknameMvGroup", array);
                    postdata.put("OwningCustomer", "190077");
                    // get it from constant properties
                    urlStr = "http://10.93.22.116:9089/Test-iris/Test.svc/GB0010001/verBeneficiary_Wbnks(\'" + BenID + "\')/validate";

                }
                commit = new Intent(Addbeneficiary.this, ConfirmPage.class);
                commit.putExtras(benBundle);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            success=sh1.jsonWrite(urlStr,postdata);
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(success) {

                startActivity(commit);
            }
            else
                Toast.makeText(Addbeneficiary.this, "error in connection ", Toast.LENGTH_LONG).show();
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


