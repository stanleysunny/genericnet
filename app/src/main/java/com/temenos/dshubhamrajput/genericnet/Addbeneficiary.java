package com.temenos.dshubhamrajput.genericnet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;


/**
 * Created by upriya on 06-03-2017.
 */

public class AddBeneficiary extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //change the layout accordingly
        setContentView(R.layout.activity_acct_sum);

        // sets title for the activity page
        getSupportActionBar().setTitle("Add Beneficiary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    // add the code for the check boxes for assigning value





    // add the code for the button submit to connect to the url
      /*  Button mSubmitButton = (Button) findViewById(R.id.email_sign_in_button);
       mSubmitButton .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                    new PostDetails().execute();
            }
        });
        */

    }
//find the purpose
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    private class PostDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //  json object posting code goes here
            HashMap<String,String> postDataParams = new HashMap<>();

            postDataParams.put("BeneficiaryAccountNo", "value");// get Account No from UI
            postDataParams.put("NickName", "value");//get Name from UI
            postDataParams.put("EmailID", "value");//get EmailID from UI

            //read the url from property class
            /*
                PropertiesReader property= new PropertiesReader();
                    String urlBeneficiary = property.getProperty("url_add_beneficiary", getApplicationContext());
             */
            performPostCall postCallObj= new  performPostCall();
            postCallObj.performPostCallPost("URL", postDataParams);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }
}

