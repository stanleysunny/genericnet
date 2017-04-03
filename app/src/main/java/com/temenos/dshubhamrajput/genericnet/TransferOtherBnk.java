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
import android.text.Spanned;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;


public class TransferOtherBnk extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    public static HashMap<String,String> secondSpinnerVal=new HashMap<>();
    static public String RefNo="";
    public String intentData;
    public static String status;
    public Intent commit;
    ProgressDialog progressDialog;
    ProgressDialog preprogressDialog;
    String[] errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_other_bnk);
        getSupportActionBar().setTitle("Account Transfer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner from = (Spinner) findViewById(R.id.edit_from_other);
        Spinner to = (Spinner) findViewById(R.id.edit_to_other);
        EditText desc = (EditText) findViewById(R.id.edit_desc_other);
        EditText amt = (EditText) findViewById(R.id.edit_amt_other);
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        amt.setFilters(new InputFilter[] { filter });
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

            @Override
            public void onClick(View arg0) {
                Spinner fromAcctNo = (Spinner) findViewById(R.id.edit_from_other);
                String fromAccountNo = fromAcctNo.getSelectedItem().toString();
                Spinner toAcctNo = (Spinner) findViewById(R.id.edit_to_other);
                String toStringAcctNo = toAcctNo.getSelectedItem().toString();
                toStringAcctNo = toStringAcctNo+"-"+secondSpinnerVal.get(toStringAcctNo);
                String[] firstValue = toStringAcctNo.split("-");
                String toAccountNo = firstValue[0];
                String nickName = firstValue[1];
                String bankSortCodePass = firstValue[2];
                String branchPass = firstValue[3];
                EditText descr = (EditText) findViewById(R.id.edit_desc_other);
                String description = descr.getText().toString();
                EditText amt = (EditText) findViewById(R.id.edit_amt_other);
                String amount = amt.getText().toString();
                String transType = "";
                new jsonResponse().execute(fromAccountNo,toAccountNo,description,amount,transType,nickName,bankSortCodePass,branchPass);
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

    public class FundTransfer extends AsyncTask<Void, Void, Boolean> {
        /**
         * Establishes connection with the url and authenticates the user name
         * and password.
         */
        @Override
        protected void onPreExecute() {
            preprogressDialog= new ProgressDialog(TransferOtherBnk.this);
            preprogressDialog.setMessage("Please wait...");
            preprogressDialog.show();
            preprogressDialog.setCancelable(false);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            HttpHandler sh = new HttpHandler();
            URLRelated urlObj = new URLRelated(getApplicationContext());
            // Making a request to url and getting response
            String owningCustomer;
            //
            HashMap<String,String> owner;
            SessionManager session =new SessionManager(getApplicationContext());
            owner=session.getUserDetails();
            owningCustomer= owner.get("cusId");

            //ADDED BY PRIYA
            String[] URLAddressList= {"url_ip","url_iris_project","url_company","url_cusaccno"};
            String cusAcctNos= urlObj.getURLParameter(URLAddressList,owningCustomer);
            String[] URLAddressList1= {"url_ip","url_iris_project","url_company","url_enqEnqObnks"};
            String owingCust= urlObj.getURLParameter(URLAddressList1,owningCustomer);
            String[] URLAddressList2= {"url_ip","url_iris_project","url_company","new_id_url_other_bnk"};
            String url= urlObj.getURL(URLAddressList2);
            //-------------------------------
            String jsonStr = sh.makeServiceCall(url);
            String jsonCusAcct = sh.makeServiceCallGet(cusAcctNos);
            String jsonOwingCus = sh.makeServiceCallGet(owingCust);
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

            if (jsonCusAcct != null && jsonOwingCus!= null) {
                try {
                    JSONObject jsonObjCusAcct = new JSONObject(jsonCusAcct);
                    JSONObject jsonObjOwingCust = new JSONObject(jsonOwingCus);

                    JSONObject firstObj = jsonObjCusAcct.getJSONObject("_embedded");
                    JSONObject firstObjOwingCust = jsonObjOwingCust.getJSONObject("_embedded");

                    JSONArray item = firstObj.getJSONArray("item");
                    JSONArray itemOwingCust = firstObjOwingCust.getJSONArray("item");

                    final Spinner spinner = (Spinner)findViewById(R.id.edit_from_other);
                    final Spinner secondSpinner = (Spinner)findViewById(R.id.edit_to_other);

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
                                spinnerAdapter.add(diffAcctNo);
                            }
                        });
                    }
                    for (int i = 0; i < itemOwingCust.length(); i++) {
                        JSONObject benAccountNo = itemOwingCust.getJSONObject(i);
                        final String benAcct = benAccountNo.getString("BenAcctNo");
                        final String bankSortCode = benAccountNo.getString("BankSortCode");
                        final String branch = benAccountNo.getString("Branch");

                        JSONArray NicknameMyGroup = benAccountNo.getJSONArray("NicknameMvGroup");
                        JSONObject nickName = NicknameMyGroup.getJSONObject(0);
                        final String nickNameCust = nickName.getString("Nickname");

                        final String dispNickName = benAcct+"-"+nickNameCust;
                        secondSpinnerVal.put(dispNickName,bankSortCode+"-"+branch);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                secondSpinner.setAdapter(secondSpinnerAdptr);
                                secondSpinnerAdptr.add(dispNickName);
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
            preprogressDialog.dismiss();
            super.onPostExecute(aBoolean);
        }

    }

    public class jsonResponse extends AsyncTask<String,Void,Boolean>
    {
        @Override
        protected void onPreExecute() {
            progressDialog= new ProgressDialog(TransferOtherBnk.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            super.onPreExecute();
        }

        protected Boolean doInBackground(String... params) {
            String currencyDeb="";
            //added by priya
            URLRelated urlObj = new URLRelated(getApplicationContext());
            String[] URLAddressList= {"url_ip","url_iris_project","url_company","url_verFundsTransfer_AcTransObnks"};
            String urlStr= urlObj.getURL(URLAddressList);
            String url= urlObj.getValidateURL(urlStr,RefNo);
            try {
                String json;
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("RefNo", RefNo);
                jsonObject.accumulate("TransactionType", params[4]);
                jsonObject.accumulate("BcBankSortCode", params[6]);
                jsonObject.accumulate("BenAcctNo", params[1]);

                JSONArray array = new JSONArray();
                JSONObject jsonObjarray = new JSONObject();

                jsonObjarray.accumulate("BenCustomer",params[5]);
                array.put(jsonObjarray);

                jsonObject.put("BenCustomerMvGroup",array);
                jsonObject.accumulate("BranchName", params[7]);
                jsonObject.accumulate("CreditAcctNo", "USD1000110000001");
                jsonObject.accumulate("DebitAcctNo", params[0]);
                jsonObject.accumulate("DebitAmount", params[3]);
                jsonObject.accumulate("DebitCurrency","");
                jsonObject.accumulate("Description", params[2]);

                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();

                HttpHandler newObj = new HttpHandler();
                status = newObj.postfunc(url,json);
                if(status.equals("YES")) {
                    intentData = "others";
                    Bundle fundsTransferData = new Bundle();
                    fundsTransferData.putString("RefNo", RefNo);
                    fundsTransferData.putString("fromAccountNo", params[0]);
                    fundsTransferData.putString("toAccountNo", params[1]);
                    fundsTransferData.putString("description", params[2]);
                    fundsTransferData.putString("creAcctNo", "USD1000110000001");
                    fundsTransferData.putString("amount", params[3]);
                    fundsTransferData.putString("transType", params[4]);
                    fundsTransferData.putString("Currency","");
                    fundsTransferData.putString("benCustomer",params[5]);
                    fundsTransferData.putString("bankSortCode",params[6]);
                    fundsTransferData.putString("branchName",params[7]);
                    fundsTransferData.putString("getintent", intentData);

                    commit = new Intent(TransferOtherBnk.this, ConfirmPage.class);
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
            String text, info;

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
                    info = error.get("info");//field
                    errorMessage[i]=text;
                }
                for(int i=0;i<errorList.size();i++)
                {
                    new AlertDialog.Builder(TransferOtherBnk.this)
                            .setTitle("Error")
                            .setMessage(errorMessage[i])
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
