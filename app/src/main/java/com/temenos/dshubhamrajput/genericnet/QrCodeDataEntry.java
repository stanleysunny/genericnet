package com.temenos.dshubhamrajput.genericnet;

/*
 * Created by dshubhamrajput on 13-04-2017.
 */

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class QrCodeDataEntry extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    public static String imp;
    static public String RefNo="";
    Button buttonScan;
    public static String status;
    public Intent commit;
    String[] errorMessage;
    ProgressDialog preProgressDialog;
    ProgressDialog progressDialog;
    public static HashMap<String,String> ifscSpinnerVal = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_transfer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Bundle extras = getIntent().getExtras();
        imp = extras.getString("acctAndIfsc");
        String[] divide = imp.split(":");
        String toAccount = divide[0];
        final String ifsc = divide[1];
        final String nickName = divide[2];
        final String branchPass = divide[3];

        final String firstFourLetterToIfsc = ifsc.substring(0,4);
        final TextView Accno= (TextView)findViewById(R.id.qr_from_acct_value);
        Accno.setText(toAccount);
        new fromAccountSpinnerVal().execute(firstFourLetterToIfsc);

        buttonScan = (Button) findViewById(R.id.button4);
        buttonScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Spinner fromAcctNo = (Spinner) findViewById(R.id.qr_to_acct_value);
                String fromAccountNo = fromAcctNo.getSelectedItem().toString();
                String toAccountNo = Accno.getText().toString();
                EditText descr = (EditText) findViewById(R.id.qr_description_value);
                String description = descr.getText().toString();
                EditText amt = (EditText) findViewById(R.id.qr_amount_value);
                String amount = amt.getText().toString();
                String transType = "";

                String ifscCodeString = ifscSpinnerVal.get("ifscCodeString");
                String firstFourLetterFromIfsc = ifscCodeString.substring(0,4);
                if (firstFourLetterFromIfsc.equals(firstFourLetterToIfsc))
                {
                    String typeOfBankTransfer = "withinBank";
                    new confirmPageData().execute(typeOfBankTransfer,fromAccountNo,toAccountNo,description,amount,transType,nickName);
                }
                else
                {
                    String typeOfBankTransfer = "others";
                    new confirmPageData().execute(typeOfBankTransfer,fromAccountNo,toAccountNo,description,amount,transType,nickName,ifsc,branchPass);
                }

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
    private class fromAccountSpinnerVal extends AsyncTask<String, Void, Boolean> {
        /**
         * Establishes connection with the url and authenticates the user name
         * and password.
         */
        @Override
        protected void onPreExecute() {
            preProgressDialog = new ProgressDialog(QrCodeDataEntry.this);
            preProgressDialog.setMessage("Please wait...");
            preProgressDialog.show();
            preProgressDialog.setCancelable(false);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            URLRelated urlObj = new URLRelated(getApplicationContext());
            // Making a request to url and getting response
            String owningCustomer;
            // changes here
            HashMap<String,String> owner;
            SessionManager session =new SessionManager(getApplicationContext());
            owner=session.getUserDetails();
            owningCustomer= owner.get("cusId");

            String[] URLAddressList= {"url_ip","url_iris_project","url_company","url_cusaccno"};
            String cusAcctNos= urlObj.getURLParameter(URLAddressList,owningCustomer);

            String jsonCusAcct = sh.makeServiceCallGet(cusAcctNos);

            if (jsonCusAcct != null) {
                try {
                    JSONObject jsonObjCusAcct = new JSONObject(jsonCusAcct);
                    JSONObject firstObj = jsonObjCusAcct.getJSONObject("_embedded");
                    JSONArray item = firstObj.getJSONArray("item");
                    final Spinner spinner = (Spinner)findViewById(R.id.qr_to_acct_value);

                    final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, android.R.id.text1);

                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                    for (int i = 0; i < item.length(); i++) {
                        JSONObject acctNoOfCustomer = item.getJSONObject(i);
                        final String diffAcctNo = acctNoOfCustomer.getString("AccountNo");
                        final String ifscCode = acctNoOfCustomer.getString("Ifsc");
                        if(!ifscCode.equals(""))
                            ifscSpinnerVal.put("ifscCodeString",ifscCode);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                spinner.setAdapter(spinnerAdapter);

                                spinnerAdapter.add(diffAcctNo);
                            }
                        });
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spinnerAdapter.notifyDataSetChanged();
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

            String firstFourLetterFromIfsc = ifscSpinnerVal.get("ifscCodeString").substring(0,4);
            String url;
            if(firstFourLetterFromIfsc.equals(params[0]))
            {
                String[] URLAddressList2= {"url_ip","url_iris_project","url_company","new_id_url"};
                url = urlObj.getURL(URLAddressList2);
            }
            else
            {
                String[] URLAddressList2= {"url_ip","url_iris_project","url_company","new_id_url_other_bnk"};
                url= urlObj.getURL(URLAddressList2);
            }
            String jsonStr = sh.makeServiceCall(url);
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

            return true;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            preProgressDialog.dismiss();
            super.onPostExecute(aBoolean);
        }
    }

    private class confirmPageData extends AsyncTask<String,Void,Boolean> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(QrCodeDataEntry.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            super.onPreExecute();
        }

        protected Boolean doInBackground(String... params) {
            if(params[0].equals("withinBank"))
            {
                //added by priya
                URLRelated urlObj = new URLRelated(getApplicationContext());
                String[] URLAddressList= {"url_ip","url_iris_project","url_company","url_verFundsTransfer_AcTranss"};
                String urlStr= urlObj.getURL(URLAddressList);
                String url= urlObj.getValidateURL(urlStr,RefNo);
                //---------------------------------------------------------------------------
                try {
                    String json;

                    // 3. build jsonObject
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("RefNo", RefNo);
                    jsonObject.accumulate("TransactionType", params[5]);
                    jsonObject.accumulate("DebitAcctNo", params[1]);
                    jsonObject.accumulate("DebitCurrency","");
                    jsonObject.accumulate("DebitAmount", params[4]);
                    jsonObject.accumulate("CreditAcctNo", params[2]);
                    jsonObject.accumulate("Description", params[3]);

                    // 4. convert JSONObject to JSON to String
                    json = jsonObject.toString();

                    HttpHandler newObj = new HttpHandler();
                    status = newObj.postfunc(url,json);
                    if(status.equals("YES")) {
                        Bundle fundsTransferData = new Bundle();
                        fundsTransferData.putString("RefNo", RefNo);
                        fundsTransferData.putString("fromAccountNo", params[1]);
                        fundsTransferData.putString("toAccountNo", params[2]);
                        fundsTransferData.putString("description", params[3]);
                        fundsTransferData.putString("amount", params[4]);
                        fundsTransferData.putString("transType", params[5]);
                        fundsTransferData.putString("Currency","");
                        fundsTransferData.putString("getintent", params[0]);
                        fundsTransferData.putString("nickName", params[6]);

                        commit = new Intent(QrCodeDataEntry.this, ConfirmPage.class);
                        commit.putExtras(fundsTransferData);
                        return true;
                    }
                    return false;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                //added by priya
                URLRelated urlObj = new URLRelated(getApplicationContext());
                String[] URLAddressList= {"url_ip","url_iris_project","url_company","url_verFundsTransfer_AcTransObnks"};
                String urlStr= urlObj.getURL(URLAddressList);
                String url= urlObj.getValidateURL(urlStr,RefNo);
                try {
                    String json;
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("RefNo", RefNo);
                    jsonObject.accumulate("TransactionType", params[5]);
                    jsonObject.accumulate("BcBankSortCode", params[7]);
                    jsonObject.accumulate("BenAcctNo", params[2]);

                    JSONArray array = new JSONArray();
                    JSONObject jsonObjarray = new JSONObject();

                    jsonObjarray.accumulate("BenCustomer",params[6]);
                    array.put(jsonObjarray);

                    jsonObject.put("BenCustomerMvGroup",array);
                    jsonObject.accumulate("BranchName", params[8]);
                    jsonObject.accumulate("CreditAcctNo", "USD1000110000001");
                    jsonObject.accumulate("DebitAcctNo", params[1]);
                    jsonObject.accumulate("DebitAmount", params[4]);
                    jsonObject.accumulate("DebitCurrency","");
                    jsonObject.accumulate("Description", params[3]);

                    // 4. convert JSONObject to JSON to String
                    json = jsonObject.toString();

                    HttpHandler newObj = new HttpHandler();
                    status = newObj.postfunc(url,json);
                    if(status.equals("YES")) {
                        Bundle fundsTransferData = new Bundle();
                        fundsTransferData.putString("RefNo", RefNo);
                        fundsTransferData.putString("fromAccountNo", params[1]);
                        fundsTransferData.putString("toAccountNo", params[2]);
                        fundsTransferData.putString("description", params[3]);
                        fundsTransferData.putString("creAcctNo", "USD1000110000001");
                        fundsTransferData.putString("amount", params[4]);
                        fundsTransferData.putString("transType", params[5]);
                        fundsTransferData.putString("Currency","");
                        fundsTransferData.putString("benCustomer",params[6]);
                        fundsTransferData.putString("bankSortCode",params[7]);
                        fundsTransferData.putString("branchName",params[8]);
                        fundsTransferData.putString("getintent", params[0]);

                        commit = new Intent(QrCodeDataEntry.this, ConfirmPage.class);
                        commit.putExtras(fundsTransferData);
                        return true;
                    }
                    return false;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

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
//                    info = error.get("info");//field
                    errorMessage[i]=text;
                }
                for(int i=0;i<errorList.size();i++)
                {

                    String[] errorList1 = errorMessage[i].split("\\(");
                    new AlertDialog.Builder(QrCodeDataEntry.this)
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