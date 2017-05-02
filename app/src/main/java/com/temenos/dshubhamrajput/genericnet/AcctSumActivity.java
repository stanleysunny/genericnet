package com.temenos.dshubhamrajput.genericnet;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AcctSumActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> contactList;
    TextView customer;
    TextView shortName;
    String Customer="";
    String ShortTitle = "";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acct_sum);
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        TextView AcctTitle = (TextView) findViewById(R.id.textView22);
        TextView CustId = (TextView) findViewById(R.id.textView7);
        customer = (TextView) findViewById(R.id.Customer);
        shortName = (TextView) findViewById(R.id.ShortTitle);
        
        AcctTitle.setText(R.string.account_title);
        CustId.setText(R.string.cus_id);

        new GetContacts().execute();

        getSupportActionBar().setTitle("Account Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Toast.makeText(AcctSumActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
            progressDialog= new ProgressDialog(AcctSumActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String owningCustomer;
            URLRelated urlObj = new URLRelated(getApplicationContext());
            // Making a request to url and getting response
            HashMap<String,String> owner;
            SessionManager session =new SessionManager(getApplicationContext());
            owner=session.getUserDetails();
            owningCustomer= owner.get("cusId");
            String[] URLAddressList= {"url_ip","url_iris_project","url_company","url_account_summary"};
            String url = urlObj.getURLParameter(URLAddressList,owningCustomer);
            String jsonStr = sh.makeServiceCallGet(url);



            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONObject fobj = jsonObj.getJSONObject("_embedded");
                    JSONArray item = fobj.getJSONArray("item");

                    // looping through All Contacts
                    for (int i = 0; i < item.length(); i++) {
                        JSONObject c = item.getJSONObject(i);

                        String CustomerID = "";

                        String AcctTit = "";

                        if (i == 0) {
                            CustomerID = "Customer ID";
                            Customer = c.getString("CustomerNo");
                            ShortTitle = c.getString("Name");
                            AcctTit = "Account Title";
                        }
                        String AccountNumber = c.getString("AccountNo");
                        String Currency = c.getString("Currency");
                        String Category = c.getString("Category");
                        String WorkingBalance = c.getString("WorkingBalance");

                        // tmp hash map for single contact
                        HashMap<String, String> Account = new HashMap<>();

                        // adding each child node to HashMap key => value
                        Account.put("AccountNumber", AccountNumber);
                        Account.put("Currency", Currency);
                        Account.put("Customer", Customer);
                        Account.put("Category", Category);
                        Account.put("Name", ShortTitle);
                        Account.put("WorkingBalance", WorkingBalance);
                        Account.put("CustomerID", CustomerID);
                        Account.put("AcctTit", AcctTit);
                        // adding contact to contact list
                        contactList.add(Account);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            super.onPostExecute(result);
            customer.setText(Customer);
            shortName.setText(ShortTitle);
            ListAdapter adapter = new SimpleAdapter(AcctSumActivity.this, contactList,
                    R.layout.list_item, new String[]{"Category","AcctTit","CustomerID","AccountNumber","Currency","Name","WorkingBalance"},
                    new int[]{R.id.Category,R.id.textView22,R.id.textView7, R.id.AccountNumber, R.id.Currency, R.id.ShortTitle, R.id.WorkingBalance});
            lv.setAdapter(adapter);
        }
    }
}