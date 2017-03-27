package com.temenos.dshubhamrajput.genericnet;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import static java.security.AccessController.getContext;


public class AcctStmtActivity extends AppCompatActivity  {
    static EditText DateEdit;
    static EditText DateEdit2;
    String imp;
    public static int year1,year,month1,month,day1,day;
    public Intent test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acct_stmt);
        getSupportActionBar().setTitle("Account Statement");
        Spinner spinner = (Spinner)findViewById(R.id.editText5);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.add("Adhoc");
        spinnerAdapter.add("Last 3 months");
        spinnerAdapter.add("Last month");
        spinnerAdapter.notifyDataSetChanged();


        DateEdit = (EditText) findViewById(R.id.editText4);
        DateEdit2 = (EditText) findViewById(R.id.editText3);


        DateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);   //FOR TO DATE
            }
        });

        DateEdit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog2(v);  //FOR FROM DATE
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView Fromdate = (TextView) findViewById(R.id.textView15);
        final ImageView cal = (ImageView) findViewById(R.id.imageView3);
        final TextView Todate = (TextView) findViewById(R.id.textView16);
        final ImageView cal2 = (ImageView) findViewById(R.id.imageView4);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 0){
                    DateEdit.setVisibility(View.GONE);
                    DateEdit2.setVisibility(View.GONE);
                    Fromdate.setVisibility(View.GONE);
                    cal.setVisibility(View.GONE);
                    Todate.setVisibility(View.GONE);
                    cal2.setVisibility(View.GONE);


                }else{
                    DateEdit.setVisibility(View.VISIBLE);
                    DateEdit2.setVisibility(View.VISIBLE);
                    Fromdate.setVisibility(View.VISIBLE);
                    cal.setVisibility(View.VISIBLE);
                    Todate.setVisibility(View.VISIBLE);
                    cal2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });



        Button viewStmt = (Button) findViewById(R.id.submitbutton);
        viewStmt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(AcctStmtActivity.this, AccountStatmentResult.class);
                String toDate = DateEdit.getText().toString();
                String fromDate = DateEdit2.getText().toString();
                EditText accountNo = (EditText) findViewById(R.id.editText2);
                String account = accountNo.getText().toString();

                Bundle bundle = new Bundle();

                bundle.putString("toDateText", toDate);
                bundle.putString("fromDateText", fromDate);
                bundle.putString("accountNoText", account);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @NonNull
        public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {

            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            year1 = c.get(Calendar.YEAR);
            month1 = c.get(Calendar.MONTH);
            day1 = c.get(Calendar.DAY_OF_MONTH);
            //   System.out.println("From Yr,Mnth,Day"+" "+year1+" "+month1+" "+day1);

            // Create a new instance of D0atePickerDialog and return it
            //DatePickerDialog.OnDateSetListener listener;
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year1, month1, day1);
            Field mDatePickerField;
            try {
                mDatePickerField = dialog.getClass().getDeclaredField("mDatePicker");
                mDatePickerField.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            c.add(Calendar.DATE, -180);
            dialog.getDatePicker().setMinDate(c.getTimeInMillis());
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            return dialog;

            // Create a new instance of D0atePickerDialog and return it
            //DatePickerDialog.OnDateSetListener listener;

        }

        public void onDateSet(DatePicker view, int year1, int month1, int day1) {

            if (day < day1 && year == year1 && month == month1) {
                Toast.makeText(getContext(), "Please select valid date", Toast.LENGTH_LONG).show();
            } else if (month < month1 && year == year1) {
                Toast.makeText(getContext(), "Please select valid month", Toast.LENGTH_LONG).show();
            } else if (year < year1) {
                Toast.makeText(getContext(), "Please select valid year", Toast.LENGTH_LONG).show();
            }
            else {

                System.out.println("To Yr,Mnth,Day" + " " + year1 + " " + (month1 + 1) + " " + day1);
                DateEdit.setText(day1 + "/" + (month1 + 1) + "/" + year1);
            }

        }

    }

    public void showDatePickerDialog2(View v) {
        DialogFragment newFragment = new DatePickerFragment2();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment2 extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @NonNull
        public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            //System.out.println("To Yr,Mnth,Day"+" "+year+" "+month+" "+day);
            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            Field mDatePickerField;
            try {
                mDatePickerField = dialog.getClass().getDeclaredField("mDatePicker");
                mDatePickerField.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            c.add(Calendar.DATE, -180);
            dialog.getDatePicker().setMinDate(c.getTimeInMillis());
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);

            return dialog;


        }

        public void onDateSet(DatePicker view, int year2, int month2, int day2) {


            if (day < day1 && year == year1 && month == month1) {
                Toast.makeText(getContext(), "Please select valid date", Toast.LENGTH_LONG).show();
            } else if (month < month1 && year == year1) {
                Toast.makeText(getContext(), "Please select valid month", Toast.LENGTH_LONG).show();
            } else if (year < year1) {
                Toast.makeText(getContext(), "Please select valid year", Toast.LENGTH_LONG).show();
            }
            else {
                System.out.println("From Yr,Mnth,Day" + " " + year + " " + (month + 1) + " " + day);

                // Do something with the date chosen by the user
                DateEdit2.setText(day2 + "/" + (month2 + 1) + "/" + year2);
            }
        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}



