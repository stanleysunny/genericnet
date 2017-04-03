package com.temenos.dshubhamrajput.genericnet;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListBeneficiaries extends AppCompatActivity {
    String Ben="";
    ListView ListBen;
    TextView AccNo,NickName,IfscCode,BranchName,Edit,Delete;
    static ArrayList<HashMap<String, String>> beneficiaryList ;
    ListAdapter adapter1;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_beneficiaries);
        getSupportActionBar().setTitle("View beneficiary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListBen=(ListView)findViewById(R.id.ListBen);
        AccNo=(TextView)findViewById(R.id.AccountNumber);
        final Spinner AddChoice=(Spinner)findViewById(R.id.listben);
        ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AddChoice.setAdapter(spinnerAdapter);
        spinnerAdapter.add("Within Bank");
        spinnerAdapter.add("External Bank");
        spinnerAdapter.notifyDataSetChanged();



        Button ben = (Button) findViewById(R.id.ViewBenButton);
        ben.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                adapter=null;
//                adapter1=null;
//                beneficiaryList=null;
                Ben=AddChoice.getSelectedItem().toString();
                if(Ben.equals("Within Bank"))
                {
                    new FetchBenWithin().execute();
                    ListBen.setVisibility(View.VISIBLE);
                }
                else
                {
                    new FetchBenOutside().execute();
                }
            }
        });
    }
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private class FetchBenWithin extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Void doInBackground(Void... param) {

                String owningCustomer;
            beneficiaryList = new ArrayList<>();
            HttpHandler sh = new HttpHandler();
            URLRelated urlObj = new URLRelated(getApplicationContext());
            HashMap<String,String> owner;
            SessionManager session =new SessionManager(getApplicationContext());
            owner=session.getUserDetails();
            owningCustomer= owner.get("cusId");
            String[] URLAddressList1= {"url_ip","url_iris_project","url_company","url_enqEnqWbnks"};
            String owingCust= urlObj.getURLParameter(URLAddressList1,owningCustomer);
            String jsonOwingCus = sh.makeServiceCallGet(owingCust);
            JSONObject jsonObjOwingCust;
            try {
                jsonObjOwingCust = new JSONObject(jsonOwingCus);
                JSONObject firstObjOwingCust = jsonObjOwingCust.getJSONObject("_embedded");
                JSONArray itemOwingCust = firstObjOwingCust.getJSONArray("item");
                for (int i = 0; i < itemOwingCust.length(); i++) {
                    HashMap<String, String> benList= new HashMap<>();

                    JSONObject benAccountNo = itemOwingCust.getJSONObject(i);
                    benList.put("BenAcctNo",benAccountNo.getString("BenAcctNo"));

                    JSONArray NicknameMyGroup = benAccountNo.getJSONArray("NicknameMvGroup");
                    JSONObject nickName = NicknameMyGroup.getJSONObject(0);
                    benList.put("Nickname", nickName.getString("Nickname"));
                    beneficiaryList.add(benList);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

             adapter = new SimpleAdapter(ListBeneficiaries.this, beneficiaryList,
                    R.layout.list_ben_internal, new String[]{"BenAcctNo","Nickname"},
                    new int[]{R.id.AccountNumber,R.id.NickName});

            ListBen.setAdapter(adapter);


        }
    }
    private class FetchBenOutside extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Void doInBackground(Void... param) {
            String owningCustomer;
            beneficiaryList = new ArrayList<>();
            HttpHandler sh = new HttpHandler();
            URLRelated urlObj = new URLRelated(getApplicationContext());
            HashMap<String,String> owner;
            SessionManager session =new SessionManager(getApplicationContext());
            owner=session.getUserDetails();
            owningCustomer= owner.get("cusId");

            String[] URLAddressList1= {"url_ip","url_iris_project","url_company","url_enqEnqObnks"};
            String owingCust= urlObj.getURLParameter(URLAddressList1,owningCustomer);
            String jsonOwingCus = sh.makeServiceCallGet(owingCust);
            try {
                JSONObject jsonObjOwingCust = new JSONObject(jsonOwingCus);
                JSONObject firstObjOwingCust = jsonObjOwingCust.getJSONObject("_embedded");
                JSONArray itemOwingCust = firstObjOwingCust.getJSONArray("item");
                for (int i = 0; i < itemOwingCust.length(); i++) {
                    HashMap<String, String> benList= new HashMap<>();

                    JSONObject benAccountNo = itemOwingCust.getJSONObject(i);
                    benList.put("BenAccNo" ,benAccountNo.getString("BenAcctNo"));
                    benList.put("BankSortCode" ,benAccountNo.getString("BankSortCode"));
                    benList.put("Branch" ,benAccountNo.getString("Branch"));

                    JSONArray NicknameMyGroup = benAccountNo.getJSONArray("NicknameMvGroup");
                    JSONObject nickName = NicknameMyGroup.getJSONObject(0);
                    benList.put("Nickname" , nickName.getString("Nickname"));
                    beneficiaryList.add(benList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

             adapter1 = new SimpleAdapter(ListBeneficiaries.this, beneficiaryList,
                    R.layout.list_beneficiary, new String[]{"BenAccNo","Nickname","BankSortCode","Branch"},
                    new int[]{R.id.AccountNumber,R.id.NickName,R.id.Ifsc,R.id.BranchName});
            ListBen.setAdapter(adapter1);
        }
    }
}
