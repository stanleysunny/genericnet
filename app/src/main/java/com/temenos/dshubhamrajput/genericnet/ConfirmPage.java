package com.temenos.dshubhamrajput.genericnet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ConfirmPage extends AppCompatActivity {
    public Intent test;
    public String imp;
    public EditText e5;
    public TextView t1,t2,t3,t4,t5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        e5=(EditText)findViewById(R.id.editText8);
        t1=(TextView) findViewById(R.id.textView);
        t2=(TextView) findViewById(R.id.textView3);
        t3=(TextView) findViewById(R.id.textView5);
        t4=(TextView) findViewById(R.id.textView6);
        t5=(TextView) findViewById(R.id.textView11);
        test=getIntent();
        Bundle extras = getIntent().getExtras();
        imp=extras.getString("getintent");
        imp=test.getExtras().getString("getintent");
        if(imp.equals("internal"))
        {
            getSupportActionBar().setTitle("Confirm Beneficiary");
            e5.setVisibility(View.GONE);
            t1.setText("Account Number");
            t2.setText("Email");
            t3.setText("Nick Name");
            t4.setText("Customer Name");
        }
        else if(imp.equals("external"))
        {
            getSupportActionBar().setTitle("Confirm Beneficiary");
            t1.setText("Account Number");
            t2.setText("Email");
            t3.setText("Nick Name");
            t4.setText("IFSC code");
            t5.setText("Customer Name");
        }
        else if(imp.equals("account"))
        {
            getSupportActionBar().setTitle("Confirm Transfer");
            e5.setVisibility(View.GONE);
            t1.setText("From Account");
            t2.setText("To Account");
            t3.setText("Description");
            t4.setText("Amount");
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
}
