package com.temenos.dshubhamrajput.genericnet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

        public int backpress=0;
    SessionManager session1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session1 = new SessionManager(getApplicationContext());
        Intent intent = getIntent();
    }
    @Override
    public void onBackPressed(){

        backpress = (backpress + 1);
        Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

        if (backpress>1) {
            session1.logoutUser();
            this.finish();
        }
    }


}
