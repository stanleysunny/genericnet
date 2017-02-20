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
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(AcctSumActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://10.93.21.84:8085/Test-iris/Test.svc/GB0010001/enqAcctHomes()?$filter=Customer%20eq%20100292";
            String jsonStr = sh.makeServiceCall(url);

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
                        String Heading="";
                        if(i==0)
                        {
                            Heading = "Account Summary";
                        }
                        String AccountNumber = c.getString("AccountNumber");
                        String Currency = c.getString("Currency");
                        String Customer = c.getString("Customer");
                        String ShortTitle = c.getString("ShortTitle");
                        String WorkingBalance = c.getString("WorkingBalance");

                        // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("phone");
//                        String mobile = phone.getString("mobile");
//                        String home = phone.getString("home");
//                        String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("AccountNumber", AccountNumber);
                        contact.put("Currency", Currency);
                        contact.put("Customer", Customer);
                        contact.put("ShortTitle", ShortTitle);
                        contact.put("WorkingBalance", WorkingBalance);
                        contact.put("Heading", Heading);

                        // adding contact to contact list
                        contactList.add(contact);
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

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(AcctSumActivity.this, contactList,
                    R.layout.list_item, new String[]{"Heading","AccountNumber","Currency","Customer","ShortTitle","WorkingBalance"},
                    new int[]{R.id.textView,R.id.AccountNumber, R.id.Currency, R.id.Customer, R.id.ShortTitle, R.id.WorkingBalance});
            lv.setAdapter(adapter);
        }
    }
}