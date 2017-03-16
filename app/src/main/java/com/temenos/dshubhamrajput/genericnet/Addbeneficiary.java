package com.temenos.dshubhamrajput.genericnet;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Addbeneficiary extends AppCompatActivity  {

    Matcher matcher;
    public String intentData="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbeneficiary);
        getSupportActionBar().setTitle("Add beneficiary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CheckBox withinbank1=(CheckBox)findViewById(R.id.withinbank);
        final CheckBox neft1=(CheckBox)findViewById(R.id.neft);
        final TextView ifscTextview = (TextView) findViewById(R.id.textView5);
        final EditText ifscEtext = (EditText) findViewById(R.id.Ifsc);
        final EditText benAccNo=(EditText)findViewById(R.id.BenAccNo) ;
        final EditText accNoCheck=(EditText)findViewById(R.id.ReenterAccNo) ;
        final EditText emailUser=(EditText)findViewById(R.id.Email) ;
        final ImageView helpicon=(ImageView) findViewById(R.id.help_icon);
        final String ifscChecker="/^[A-Z]{4}[0][0-9A-Z]{6}$/";

//        Pattern pattern = Pattern.compile(ifscChecker);
        final String ifsc_matcher=ifscEtext.getText().toString();
//        matcher = pattern.matcher(ifscEtext.getText().toString());




        withinbank1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(withinbank1.isChecked()){
                    neft1.setChecked(false);
                    ifscTextview.setVisibility(View.INVISIBLE);
                    ifscEtext.setVisibility(View.INVISIBLE);
                    helpicon.setVisibility(View.INVISIBLE);

                }
            }
        });

        neft1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(neft1.isChecked()){
                    withinbank1.setChecked(false);
                    ifscTextview.setVisibility(View.VISIBLE);
                    ifscEtext.setVisibility(View.VISIBLE);
                    helpicon.setVisibility(View.VISIBLE);

                }
            }
        });
        accNoCheck.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                accNoCheck.setError(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                accNoCheck.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (!(accNoCheck.getText().toString().equals(benAccNo.getText().toString()))) {
                    accNoCheck.setError("Account numbers don't match");
                } else {
                    accNoCheck.setError(null);
                }
            }
        });
        emailUser.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailUser.setError(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                emailUser.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s)  {
                final String email = emailUser.getText().toString();
                if (!(emailValidator(email))) {
                    emailUser.setError("Enter a valid email id");
                } else {
                    emailUser.setError(null);
                }
            }
        });
        ifscEtext.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ifscEtext.setError(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ifscEtext.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if(!(ifsc_matcher.matches(ifscChecker)))
                    ifscEtext.setError("IFSC should be a 11 character alpha numeric string");
                else
                    ifscEtext.setError(null);
            }


        });
    }

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public void showHelpText(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.ifsc_help)
                .setTitle(R.string.ifschelppopup)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       //nothing is done
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
