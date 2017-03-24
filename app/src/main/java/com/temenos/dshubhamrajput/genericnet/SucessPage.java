package com.temenos.dshubhamrajput.genericnet;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SucessPage extends AppCompatActivity {
    String imp="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucess_page);
        TextView setSucessText=(TextView)findViewById(R.id.textView8);
        Button confirm = (Button) findViewById(R.id.button3);
        final Bundle extras = getIntent().getExtras();
        imp = extras.getString("Sucess");
        if(imp.equals("Ben"))
            setSucessText.setText("Beneficiary Added");
        else
            setSucessText.setText("Transaction Successfull!");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SucessPage.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SucessPage.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
