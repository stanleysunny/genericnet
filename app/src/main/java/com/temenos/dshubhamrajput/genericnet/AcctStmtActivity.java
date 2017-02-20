package com.temenos.dshubhamrajput.genericnet;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.Locale;
import android.view.View.OnClickListener;

import static android.R.id.edit;
import static android.support.v7.appcompat.R.id.text;
import static com.temenos.dshubhamrajput.genericnet.R.id.image;

import java.util.Calendar;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


public class AcctStmtActivity extends AppCompatActivity {
    static EditText DateEdit;
    static EditText DateEdit2;
    public static int year1,year,month1,month,day1,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acct_stmt);
        getSupportActionBar().setTitle("Account Statement");
        Date d=new Date();
        System.out.print("Today"+" "+d.toString());
        DateEdit = (EditText) findViewById(R.id.editText4);
        DateEdit2 = (EditText) findViewById(R.id.editText3);
        DateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);   //FOR FROM DATE
            }
        });

        DateEdit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog2(v);  //FOR TO DATE
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }





    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            year1 = c.get(Calendar.YEAR);
           month1 = c.get(Calendar.MONTH);
             day1 = c.get(Calendar.DAY_OF_MONTH);


            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year1, month1, day1);
        }

        public void onDateSet(DatePicker view, int year1, int month1, int day1) {
            // Do something with the date chosen by the user
            //finddiff();
            DateEdit.setText(day1 + "/" + (month1 + 1) + "/" + year1);
        }

    }

    public void showDatePickerDialog2(View v) {
        DialogFragment newFragment = new DatePickerFragment2();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment2 extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
             year = c.get(Calendar.YEAR);
          month = c.get(Calendar.MONTH);
          day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
//            if(year1-year == 0)
//            {
//                if(!(month1-month <=6)) {
//                    Toast toast = Toast.makeText(getApplicationContext(),"Test", Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//            }
            DateEdit2.setText(day + "/" + (month + 1) + "/" + year);
        }

    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}



