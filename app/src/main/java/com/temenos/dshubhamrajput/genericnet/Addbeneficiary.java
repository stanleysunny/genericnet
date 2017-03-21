package com.temenos.dshubhamrajput.genericnet;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Addbeneficiary extends AppCompatActivity {


    public String intentData = "internal";
    public Intent commit;
    CheckBox withinbank1;
    CheckBox neft1;
    TextView ifscTextview;
    EditText ifscEtext;
    EditText benAccNo;
    EditText accNoCheck;
    EditText emailUser;
    ImageView helpicon;
    EditText nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbeneficiary);
        getSupportActionBar().setTitle("Add beneficiary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        withinbank1 = (CheckBox) findViewById(R.id.withinbank);
        neft1 = (CheckBox) findViewById(R.id.neft);
        ifscTextview = (TextView) findViewById(R.id.textView5);
        ifscEtext = (EditText) findViewById(R.id.Ifsc);
        benAccNo = (EditText) findViewById(R.id.BenAccNo);
        accNoCheck = (EditText) findViewById(R.id.ReenterAccNo);
        emailUser = (EditText) findViewById(R.id.Email);
        helpicon = (ImageView) findViewById(R.id.help_icon);
        nickName = (EditText) findViewById(R.id.NickName);


        withinbank1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (withinbank1.isChecked()) {
                    neft1.setChecked(false);
                    ifscTextview.setVisibility(View.INVISIBLE);
                    ifscEtext.setVisibility(View.INVISIBLE);
                    helpicon.setVisibility(View.INVISIBLE);
                    intentData = "internal";

                }
            }
        });

        neft1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (neft1.isChecked()) {
                    withinbank1.setChecked(false);
                    ifscTextview.setVisibility(View.VISIBLE);
                    ifscEtext.setVisibility(View.VISIBLE);
                    helpicon.setVisibility(View.VISIBLE);
                    intentData = "external";

                }
            }
        });
        accNoCheck.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!(accNoCheck.getText().toString().equals(benAccNo.getText().toString()))) {
                    accNoCheck.setError("Account numbers don't match");
                } else if (((accNoCheck.getText().toString()).matches(""))) {
                    accNoCheck.setError("This field cannot be left blank");
                }

            }

        });
        emailUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final String email = emailUser.getText().toString();
                    if (!(emailValidator(email))) {
                        emailUser.setError("Enter a valid email id");
                    } else if ((emailUser.getText().toString()).matches("")) {
                        emailUser.setError(null);
                    }
                }
            }
        });
        ifscEtext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String ifsc = ifscEtext.getText().toString();
                    boolean check = ifscMatcher(ifsc);
                    if (!check) {
                        ifscEtext.setError("IFSC is a 11 digit alpha numeric string");
                    }
                    else if (ifscEtext.getText().toString().matches("")) {
                        ifscEtext.setError("This field cannot be left blank");
                    }
                }
            }
        });
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean ifscMatcher(String ifsc) {
        Pattern pattern;
        Matcher matcher;
        final String ifscChecker = "^[A-Z]{4}\\d{7}$" ;
        pattern = Pattern.compile(ifscChecker);
        matcher = pattern.matcher(ifsc);
        return matcher.matches();
    }

    public void showHelpText(View v) {
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

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void ButtonClicked(View V) {
        if(intentData.equals("external")) {
            if ((ifscEtext.getText().toString()).matches(""))
                ifscEtext.setError("This field cannot be left blank");
        }
        if ((benAccNo.getText().toString()).matches(""))
            benAccNo.setError("This field cannot be left blank");
        if ((accNoCheck.getText().toString()).matches(""))
            accNoCheck.setError("This field cannot be left blank");
        if ((nickName.getText().toString()).matches(""))
            nickName.setError("This field cannot be left blank");
        if (((benAccNo.getError() == null) && (accNoCheck.getError() == null) && (emailUser.getError() == null) && (nickName.getError() == null) && (ifscEtext.getError() == null)))
            {
                System.out.print("Intent should  work");
                commit = new Intent(this, ConfirmPage.class);
                commit.putExtra("getintent", intentData);
                startActivity(commit);
            }

        }

    }
