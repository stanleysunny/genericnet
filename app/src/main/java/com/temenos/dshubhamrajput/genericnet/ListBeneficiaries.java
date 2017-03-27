package com.temenos.dshubhamrajput.genericnet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class ListBeneficiaries extends AppCompatActivity {
    String Ben="";
    ListView ListBen,List;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_beneficiaries);
        getSupportActionBar().setTitle("Add beneficiary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListBen=(ListView)findViewById(R.id.ListBen);
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
                Ben=AddChoice.getSelectedItem().toString();
                if(Ben.equals("Within Bank"))
                {
                    ListBen.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
