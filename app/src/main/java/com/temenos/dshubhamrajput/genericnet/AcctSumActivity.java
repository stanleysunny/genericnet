package com.temenos.dshubhamrajput.genericnet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
public class AcctSumActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acct_sum);
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

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
            Toast.makeText(AcctSumActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                    HttpHandler sh = new HttpHandler();
                String user,owningCustomer;
                    // Making a request to url and getting response
                PropertiesReader property= new PropertiesReader();
                String url = property.getProperty("url_account_summary", getApplicationContext());
                HashMap<String,String> owner;
                SessionManager session =new SessionManager(getApplicationContext());
                owner=session.getUserDetails();
                owningCustomer= owner.get("cusId");
                     url= url+owningCustomer;
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
                                String Customer = "";
                                String CustomerID = "";
                                if (i == 0) {
                                    CustomerID = "Customer ID";
                                    Customer = c.getString("CustomerNo");
                                }
                                String AccountNumber = c.getString("AccountNo");
                                String Currency = c.getString("Currency");
                                String Category = c.getString("Category");
                                String ShortTitle = c.getString("Name");
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
                            // adding contact to contact list
                                contactList.add(Account);
                            }
                            } catch (final JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                    public void run() {
                                    Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                                }
                                 });

                             }

                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            catch(IOException e ){e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(AcctSumActivity.this, contactList,
                    R.layout.list_item, new String[]{"Category","CustomerID","AccountNumber","Currency","Customer","Name","WorkingBalance"},
                    new int[]{R.id.Category,R.id.textView7, R.id.AccountNumber, R.id.Currency, R.id.Customer, R.id.ShortTitle, R.id.WorkingBalance});
            lv.setAdapter(adapter);
        }
    }
}