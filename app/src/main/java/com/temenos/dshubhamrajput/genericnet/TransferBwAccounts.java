package com.temenos.dshubhamrajput.genericnet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.tem.pack.GenUrl;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TransferBwAccounts extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_bw_accts);
        getSupportActionBar().setTitle("Account Transfer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String basicAuth = "";
        String response = "";

        try {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response

            URL e = new URL("10.93.22.116:9089/Test-iris/Test.svc/GB0010001/verFundsTransfer_AcTranss()/new");
            HttpURLConnection uc = (HttpURLConnection) e.openConnection();
            String userPass = "CREDITMGR" + ":" + "123456";
            basicAuth = "Basic " + new String((new Base64()).encode(userPass.getBytes()));
            uc.setRequestProperty("Authorization", basicAuth);
            uc.setRequestProperty("Accept", "application/json");
            uc.setRequestMethod("POST");
            BufferedInputStream in = new BufferedInputStream(uc.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();

            try {
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append('\n');
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }
            } finally {
                try {
                    in.close();
                } catch (IOException var13) {
                    var13.printStackTrace();
                }

            }
            String result = sb.toString();
            Log.e(TAG, "Response from url: " + result);

            try {
                JSONObject jsonObj = new JSONObject(result);
                String idfunds = jsonObj.getString("RefNo");

                System.out.println(idfunds);
            } catch (final JSONException eb) {
                Log.e(TAG, "Json parsing error: " + eb.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + eb.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }

        } catch (Exception var8) {
            var8.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
