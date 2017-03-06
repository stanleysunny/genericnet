package com.temenos.dshubhamrajput.genericnet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Addbeneficiary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbeneficiary);
        getSupportActionBar().setTitle("Add beneficiary");
    }
    public void Clicked(View V){

        final TextView t = (TextView) findViewById(R.id.textView18);
        final EditText e = (EditText) findViewById(R.id.editText15);
        t.setVisibility(View.VISIBLE);
        e.setVisibility(View.VISIBLE);
    }
    public void hide(View V)
    {
        final TextView t = (TextView) findViewById(R.id.textView18);
        final EditText e = (EditText) findViewById(R.id.editText15);
        t.setVisibility(View.INVISIBLE);
        e.setVisibility(View.INVISIBLE);

    }

}
