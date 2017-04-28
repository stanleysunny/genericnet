package com.temenos.dshubhamrajput.genericnet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;


public class TransferBwAccounts extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    static public String RefNo="";  //ID of the F.T. returned when new() routine URL is called
    public String intentData;
    public static String status;
    public Intent commit;
    ProgressDialog progressDialog;
    ProgressDialog preProgressDialog;
    String[] errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_bw_accts);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Account Transfer");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        /* Takes the id of the spinner and EditText field*/
        Spinner from = (Spinner) findViewById(R.id.editText);
        Spinner to = (Spinner) findViewById(R.id.editText6);
        EditText desc = (EditText) findViewById(R.id.editText7);
        EditText amt = (EditText) findViewById(R.id.editText8);
        amt.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
        from.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        to.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        desc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        amt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        new FundTransfer().execute();

        Button fundsTransfer = (Button) findViewById(R.id.button);
        fundsTransfer.setOnClickListener(new View.OnClickListener() {

            /*
            * onClick function triggers when SUBMIT button
            * is clicked*/
            @Override
            public void onClick(View arg0) {
                //Fetches the spinner value
                Spinner fromAcctNo = (Spinner) findViewById(R.id.editText);
                String fromAccountNo = fromAcctNo.getSelectedItem().toString();
                Spinner toAcctNo = (Spinner) findViewById(R.id.editText6);
                String toAccountNo = toAcctNo.getSelectedItem().toString();
                EditText descr = (EditText) findViewById(R.id.editText7);
                String description = descr.getText().toString();
                EditText amt = (EditText) findViewById(R.id.editText8);
                String amount = amt.getText().toString();
                String transType = "";

                /*Field value are paassed to doinbackground function to form JSON response
                and validate the values of the fields
                 */
                new jsonResponse().execute(fromAccountNo,toAccountNo,description,amount,transType);
            }
        });
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

    private class FundTransfer extends AsyncTask<Void, Void, Boolean> {
        /**
         * Establishes connection with the url and authenticates the user name
         * and password.
         */
        @Override
        protected void onPreExecute() {
            preProgressDialog= new ProgressDialog(TransferBwAccounts.this);
            preProgressDialog.setMessage("Please wait...");
            preProgressDialog.show();
            preProgressDialog.setCancelable(false);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url;
            HttpHandler sh = new HttpHandler();
            URLRelated urlObj = new URLRelated(getApplicationContext());
            // Making a request to url and getting response
            String owningCustomer;
            // changes here
            HashMap<String,String> owner;
            SessionManager session =new SessionManager(getApplicationContext());
            owner=session.getUserDetails();
            owningCustomer= owner.get("cusId");
            //added by priya to form the URL
            String[] URLAddressList= {"url_ip","url_iris_project","url_company","url_cusaccno"};
            String cusAcctNos= urlObj.getURLParameter(URLAddressList,owningCustomer);
            String[] URLAddressList1= {"url_ip","url_iris_project","url_company","new_id_url"};
            url = urlObj.getURL(URLAddressList1);
            //----------------
            String jsonStr = sh.makeServiceCall(url);
            String jsonCusAcct = sh.makeServiceCallGet(cusAcctNos);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    RefNo = jsonObj.getString("RefNo");
                    System.out.println(RefNo);
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

            if (jsonCusAcct != null) {
                try {
                    JSONObject jsonObjCusAcct = new JSONObject(jsonCusAcct);
                    JSONObject firstObj = jsonObjCusAcct.getJSONObject("_embedded");
                    JSONArray item = firstObj.getJSONArray("item");
                    final Spinner spinner = (Spinner)findViewById(R.id.editText);
                    final Spinner secondSpinner = (Spinner)findViewById(R.id.editText6);
                    final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
                    final ArrayAdapter<String> secondSpinnerAdptr = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    secondSpinnerAdptr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    for (int i = 0; i < item.length(); i++) {
                        JSONObject acctNoOfCustomer = item.getJSONObject(i);
                        final String diffAcctNo = acctNoOfCustomer.getString("AccountNo");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                spinner.setAdapter(spinnerAdapter);
                                secondSpinner.setAdapter(secondSpinnerAdptr);
                                spinnerAdapter.add(diffAcctNo);
                                secondSpinnerAdptr.add(diffAcctNo);
                            }
                        });
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spinnerAdapter.notifyDataSetChanged();
                            secondSpinnerAdptr.notifyDataSetChanged();
                        }
                    });

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
        protected void onPostExecute(Boolean aBoolean) {
            preProgressDialog.dismiss();
            super.onPostExecute(aBoolean);
        }
    }

    private class jsonResponse extends AsyncTask<String,Void,Boolean>
    {
        @Override
        protected void onPreExecute() {
            progressDialog= new ProgressDialog(TransferBwAccounts.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            super.onPreExecute();
        }

        protected Boolean doInBackground(String... params) {
            URLRelated urlObj= new URLRelated(getApplicationContext());
            // added by priya
            String[] URLAddressList= {"url_ip","url_iris_project","url_company","url_verFundsTransfer_AcTranss"};
            String urlStr= urlObj.getURL(URLAddressList);
            String url= urlObj.getValidateURL(urlStr,RefNo);
            //-----------------------------------
            try {
                String json;

                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("RefNo", RefNo);
                jsonObject.accumulate("TransactionType", params[4]);
                jsonObject.accumulate("DebitAcctNo", params[0]);
                jsonObject.accumulate("DebitCurrency","");
                jsonObject.accumulate("DebitAmount", params[3]);
                jsonObject.accumulate("CreditAcctNo", params[1]);
                jsonObject.accumulate("Description", params[2]);

                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();

                HttpHandler newObj = new HttpHandler();
                status = newObj.postfunc(url,json);
                if(status.equals("YES")) {
                    intentData = "bwAccounts";
                    Bundle fundsTransferData = new Bundle();
                    fundsTransferData.putString("RefNo", RefNo);
                    fundsTransferData.putString("fromAccountNo", params[0]);
                    fundsTransferData.putString("toAccountNo", params[1]);
                    fundsTransferData.putString("description", params[2]);
                    fundsTransferData.putString("amount", params[3]);
                    fundsTransferData.putString("transType", params[4]);
                    fundsTransferData.putString("Currency","");
                    fundsTransferData.putString("getintent", intentData);

                    commit = new Intent(TransferBwAccounts.this, ConfirmPage.class);
                    commit.putExtras(fundsTransferData);
                    return true;
                }
                return false;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return true;
            // 11. return result
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            HttpHandler errorObj;
            String text;

            HashMap<String, HashMap<String, String>> errorList;
            HashMap<String, String> error;
            if(aBoolean){
                progressDialog.dismiss();
                startActivity(commit);
            }
            else{
                progressDialog.dismiss();
                errorObj = new HttpHandler();
                errorList = errorObj.getErrorList();
                errorMessage= new String[errorList.size()];
                for (int i = 0; i < errorList.size(); i++) {
                    error = errorList.get("Error" + i);
                    text = error.get("text");
                    errorMessage[i]=text;

                }
                for(int i=0;i<errorList.size();i++)
                {
                    String[] errorList1 = errorMessage[i].split("\\(");
                    new AlertDialog.Builder(TransferBwAccounts.this)
                            .setTitle("Error")
                            .setMessage(errorList1[0])
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }

            }
        }
    }

}
