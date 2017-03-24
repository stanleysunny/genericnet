package com.temenos.dshubhamrajput.genericnet;
import android.content.Intent;
import android.os.AsyncTask;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.HashMap;



public class ConfirmPage extends AppCompatActivity {
    public Intent test;
    public static String imp;
    public TextView e5;
    public TextView t1,t2,t3,t4,t5,t6;
    public TextView e1,e2,e3,e4;
    final HashMap<String, String> obj = new HashMap<>();
    boolean success=true;
    static public String status;
    static String response;
    // Bundle Confirmbundle = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button backbutton = (Button) findViewById(R.id.button);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        e5=(TextView) findViewById(R.id.editText8);
        t1=(TextView) findViewById(R.id.textView);
        t2=(TextView) findViewById(R.id.textView3);
        t3=(TextView) findViewById(R.id.textView5);
        t4=(TextView) findViewById(R.id.textView6);
        t5=(TextView) findViewById(R.id.textView11);
        t6=(TextView) findViewById(R.id.textView13);


        test=getIntent();
        final Bundle extras = getIntent().getExtras();
        imp = extras.getString("getintent");
        if(imp.equals("internal"))
        {


            TextView Accno= (TextView) findViewById(R.id.editText);
            TextView Email = (TextView)findViewById(R.id.editText6);
            TextView nickname=(TextView)findViewById(R.id.editText7);
            TextView Customername=(TextView)findViewById(R.id.editText10);
            e5.setVisibility(View.GONE);
            t6.setVisibility(View.GONE);



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
            obj.put("OwningCustomer",extras.getString("190090"));
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

                    final Intent Back = new Intent(ConfirmPage.this,Addbeneficiary.class);
                    startActivity(Back);
                }
            });


        }
        else if(imp.equals("external"))
        {

            TextView Accno= (TextView)findViewById(R.id.editText);
            TextView Email = (TextView)findViewById(R.id.editText6);
            TextView nickname=(TextView)findViewById(R.id.editText7);
            TextView IfscBranch=(TextView)findViewById(R.id.editText10);
            TextView Ifsc=(TextView)findViewById(R.id.editText8);
            //TextView IfscBranch=(TextView)findViewById(R.id.editText20);
            t6.setVisibility(View.GONE);



            t1.setText("Account Number");
            t2.setText("Email");
            t3.setText("Nick Name");
            t4.setText("IFSC code");
            t5.setText("Branch Name");
            Accno.setText(extras.getString("BenAcctNo"));
            Email.setText(extras.getString("Email"));
            nickname.setText(extras.getString("Nickname"));
            //Customername.setText(extras.getString("Ifsc"));
            Ifsc.setText(extras.getString("IfscBranch"));
            IfscBranch.setText(extras.getString("Ifsc"));
            obj.put("BenAcctNo",extras.getString("BenAcctNo"));
            obj.put("BenCustomer",extras.getString("BenCustomer"));
            obj.put("BeneficiaryId",extras.getString("BeneficiaryId"));
            obj.put("Email",extras.getString("Email"));
            obj.put("Nickname",extras.getString("Nickname"));
            obj.put("Ifsc",extras.getString("Ifsc"));
            obj.put("OwningCustomer",extras.getString("190090"));
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

                    final Intent Back = new Intent(ConfirmPage.this,Addbeneficiary.class);
                    startActivity(Back);
                }
            });
        }
        else if(imp.equals("account"))
        {
            TextView frmAcct = (TextView) findViewById(R.id.editText);
            TextView toAcct = (TextView) findViewById(R.id.editText6);
            TextView des = (TextView) findViewById(R.id.editText7);
            TextView amtFunds = (TextView) findViewById(R.id.editText10);

            getSupportActionBar().setTitle("Confirm Transfer");
            e5.setVisibility(View.GONE);
            t6.setVisibility(View.GONE);
            t1.setText("From Account");
            t2.setText("To Account");
            t3.setText("Description");
            t4.setText("Amount");
            frmAcct.setText(extras.getString("fromAccountNo"));
            toAcct.setText(extras.getString("toAccountNo"));
            des.setText(extras.getString("description"));
            amtFunds.setText(extras.getString("amount"));
            final String url = "http://10.93.22.116:9089/Test-iris/Test.svc/GB0010001/verFundsTransfer_AcTranss()/input";

            Button confirmButton = (Button) findViewById(R.id.button2);
            confirmButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    new commitFunCall().execute(url,extras.getString("RefNo"),extras.getString("transType"),extras.getString("fromAccountNo"),extras.getString("Currency"),extras.getString("amount")
                            ,extras.getString("toAccountNo"),extras.getString("description"));
                }
            });
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class commitFunCall extends AsyncTask<String,Void,Boolean>
    {
        protected Boolean doInBackground(String... params)
        {
            try {
                String json = "";

                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("RefNo", params[1]);
                jsonObject.accumulate("TransactionType", params[2]);
                jsonObject.accumulate("DebitAcctNo", params[3]);
                jsonObject.accumulate("DebitCurrency", params[4]);
                jsonObject.accumulate("DebitAmount", params[5]);
                jsonObject.accumulate("CreditAcctNo", params[6]);
                jsonObject.accumulate("Description", params[7]);

                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();

                HttpHandler newObj = new HttpHandler();
                status = newObj.posCommit(params[0], json);
                if(status.equals("YES")) {
                    return true;
                }
                else
                {
                    return false;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                final Intent TransferBwAccounts = new Intent(ConfirmPage.this, SucessPage.class);
                startActivity(TransferBwAccounts);
            }
            else{
                final Intent TransferBwAccounts = new Intent(ConfirmPage.this, ConfirmPage.class);
                startActivity(TransferBwAccounts);
            }
        }
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
                    postdata.put("BankSortCode", Ifsc);
                    jsonObjarray.put("Nickname", Nickname);
                    array.put(jsonObjarray);
                    postdata.put("NicknameMvGroup", array);
                    postdata.put("OwningCustomer", "190090");

                } else {
                    postdata.put("BenAcctNo", BenAcctNo);
                    postdata.put("BenCustomer", BencustomerNo);
                    postdata.put("BeneficiaryId", BenID);
                    postdata.put("Email", Email);
                    jsonObjarray.put("Nickname", Nickname);
                    array.put(jsonObjarray);
                    postdata.put("NicknameMvGroup", array);
                    postdata.put("OwningCustomer", "190090");
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
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            if(success) {
                final Intent AddBeneficiary = new Intent(ConfirmPage.this, SucessPage.class);

                AddBeneficiary.putExtra("Sucess","Ben");
                startActivity( AddBeneficiary);

            }
            else
            {
                Toast.makeText(ConfirmPage.this, "error in connection ", Toast.LENGTH_LONG).show();
            }
        }


    }
}