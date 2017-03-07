package com.temenos.dshubhamrajput.genericnet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Addbeneficiary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbeneficiary);
        getSupportActionBar().setTitle("Add beneficiary");
        final CheckBox withinbank1=(CheckBox)findViewById(R.id.withinbank);
        final CheckBox neft1=(CheckBox)findViewById(R.id.neft);
        final TextView t = (TextView) findViewById(R.id.textView5);
        final EditText e = (EditText) findViewById(R.id.editText5);
        withinbank1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                neft1.setChecked(false);
             



                t.setVisibility(View.INVISIBLE);
                e.setVisibility(View.INVISIBLE);


            }
        });


        neft1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                withinbank1.setChecked(false);


                t.setVisibility(View.VISIBLE);
                e.setVisibility(View.VISIBLE);



            }
        });
    }


}
