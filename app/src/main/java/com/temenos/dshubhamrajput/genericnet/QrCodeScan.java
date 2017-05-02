package com.temenos.dshubhamrajput.genericnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/*
 * Created by dshubhamrajput on 31-03-2017.
 */

public class QrCodeScan extends AppCompatActivity
        implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    public Intent intentShift;
    String imp="";
    String flag="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Programmatically initialize the scanner view
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intentShift=getIntent();


        final Bundle extras = getIntent().getExtras();
        if(extras != null){
            flag="set";
        }

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

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("TAG", rawResult.getText()); // Prints scan results
        // Prints the scan format (qrcode, pdf417 etc.)
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String data = rawResult.getText();
//        String[] divide= data.split(":");
//        builder.setTitle("Scan Result");
//        builder.setMessage("Account:"+divide[0]+"IFSC Code:"+divide[1]);
//        AlertDialog alert1 = builder.create();
//        alert1.show();
        Bundle accountAndIfsc = new Bundle();
        accountAndIfsc.putString("acctAndIfsc",data);
        if(flag.equals("set")) {
            accountAndIfsc.putString("Intentorigin", "ben");
            intentShift = new Intent(QrCodeScan.this, Addbeneficiary.class);
        }
        else {
            intentShift = new Intent(QrCodeScan.this, QrCodeDataEntry.class);
        }
        intentShift.putExtras(accountAndIfsc);
        startActivity(intentShift);
        // If you would like to resume scanning, call this method below:
//        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }
}
