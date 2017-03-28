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


public class TransferWithinBnk extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    static public String RefNo="";
    public String intentData;
    public static String status;
    public Intent commit;
    ProgressDialog progressDialog;
    ProgressDialog preprogressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_within_bnk);
        getSupportActionBar().setTitle("Account Transfer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner from = (Spinner) findViewById(R.id.edit_from_within);
        Spinner to = (Spinner) findViewById(R.id.edit_to_within);
        EditText desc = (EditText) findViewById(R.id.edit_desc_within);
        EditText amt = (EditText) findViewById(R.id.edit_amt_within);
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
                Spinner fromAcctNo = (Spinner) findViewById(R.id.edit_from_within);
                String fromAccountNo = fromAcctNo.getSelectedItem().toString();
                Spinner toAcctNo = (Spinner) findViewById(R.id.edit_to_within);
                String toStringAcctNo = toAcctNo.getSelectedItem().toString();
                String[] firstValue = toStringAcctNo.split("-");
                String toAccountNo = firstValue[0];
                EditText descr = (EditText) findViewById(R.id.edit_desc_within);
                String description = descr.getText().toString();
                EditText amt = (EditText) findViewById(R.id.edit_amt_within);
                String amount = amt.getText().toString();
                String transType = "AC";

                new jsonResponse().execute(fromAccountNo,toAccountNo,description,amount,transType,firstValue[1]);
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
            preprogressDialog= new ProgressDialog(TransferWithinBnk.this);
            preprogressDialog.setMessage("Please wait...");
            preprogressDialog.show();
            preprogressDialog.setCancelable(false);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                PropertiesReader property = new PropertiesReader();
                String cusAcctNos="http://10.93.22.116:9089/Test-iris/Test.svc/GB0010001/enqAcctHomes()?$filter=CustomerNo%20eq%20100292";
                String owingCust="http://10.93.22.116:9089/Test-iris/Test.svc/GB0010001/enqEnqWbnks()?$filter=OwningCustomer%20eq%20190090";
                String url = property.getProperty("new_id_url", getApplicationContext());
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

                if (jsonCusAcct != null) {
                    try {
                        JSONObject jsonObjCusAcct = new JSONObject(jsonCusAcct);
                        JSONObject jsonObjOwingCust = new JSONObject(jsonOwingCus);

                        JSONObject firstObj = jsonObjCusAcct.getJSONObject("_embedded");
                        JSONObject firstObjOwingCust = jsonObjOwingCust.getJSONObject("_embedded");

                        JSONArray item = firstObj.getJSONArray("item");
                        JSONArray itemOwingCust = firstObjOwingCust.getJSONArray("item");

                        final Spinner spinner = (Spinner)findViewById(R.id.edit_from_within);
                        final Spinner secondSpinner = (Spinner)findViewById(R.id.edit_to_within);

                        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
                        final ArrayAdapter<String> secondSpinnerAdptr = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, android.R.id.text1);

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

                            JSONArray NicknameMyGroup = benAccountNo.getJSONArray("NicknameMvGroup");
                            JSONObject nickName = NicknameMyGroup.getJSONObject(0);
                            final String nickNameCust = nickName.getString("Nickname");

                            final String dispNickName = benAcct+"-"+nickNameCust;
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

            }
            catch(IOException e ){
                e.printStackTrace();
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
            progressDialog= new ProgressDialog(TransferWithinBnk.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            super.onPreExecute();
        }

        protected Boolean doInBackground(String... params) {
            String currencyDeb="";
            String url = "http://10.93.22.116:9089/Test-iris/Test.svc/GB0010001/verFundsTransfer_AcTranss(\'"+RefNo+"\')/validate";
            String debitCurrency = "http://10.93.22.116:9089/Test-iris/Test.svc/GB0010001/enqAcctHomes()?$filter=AccountNo%20eq%20"+params[0];
            try {
                String json;

                HttpHandler debCur = new HttpHandler();
                String debitCurrJson = debCur.makeServiceCallGet(debitCurrency);

                if (debitCurrJson != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(debitCurrJson);
                        JSONObject fobj = jsonObj.getJSONObject("_embedded");
                        JSONArray item = fobj.getJSONArray("item");
                        JSONObject c = item.getJSONObject(0);
                        currencyDeb = c.getString("Currency");
                        System.out.println(currencyDeb);
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                }

                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("RefNo", RefNo);
                jsonObject.accumulate("TransactionType", params[4]);
                jsonObject.accumulate("DebitAcctNo", params[0]);
                jsonObject.accumulate("DebitCurrency",currencyDeb);
                jsonObject.accumulate("DebitAmount", params[3]);
                jsonObject.accumulate("CreditAcctNo", params[1]);
                jsonObject.accumulate("Description", params[2]);

                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();

                HttpHandler newObj = new HttpHandler();
                status = newObj.postfunc(url,json);
                if(status.equals("YES")) {
                    intentData = "withinBank";
                    Bundle fundsTransferData = new Bundle();
                    fundsTransferData.putString("RefNo", RefNo);
                    fundsTransferData.putString("fromAccountNo", params[0]);
                    fundsTransferData.putString("toAccountNo", params[1]);
                    fundsTransferData.putString("description", params[2]);
                    fundsTransferData.putString("amount", params[3]);
                    fundsTransferData.putString("transType", params[4]);
                    fundsTransferData.putString("Currency",currencyDeb);
                    fundsTransferData.putString("getintent", intentData);
                    fundsTransferData.putString("nickName", params[5]);

                    commit = new Intent(TransferWithinBnk.this, ConfirmPage.class);
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
            if(aBoolean){
                progressDialog.dismiss();
                startActivity(commit);
            }
            else{
                new AlertDialog.Builder(TransferWithinBnk.this)
                        .setTitle("Error")
                        .setMessage("The value you entered are wrong, Please Recheck?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                final Intent TransferWithinAccounts = new Intent(TransferWithinBnk.this, TransferWithinBnk.class);
                                finish();
                                startActivity(TransferWithinAccounts);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

}
