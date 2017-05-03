package com.temenos.dshubhamrajput.genericnet;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mDrawerToggle;
    public int backpress = 0;
    SessionManager session1;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    LinkedHashMap<String, List<String>> expandableListDetail;
    DrawerLayout layout;
    public Intent callSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        session1 = new SessionManager(getApplicationContext());
        Intent intent = getIntent();
        getSupportActionBar().setTitle("TEMENOS");
        HashMap<String,String> owner;
        SessionManager session =new SessionManager(getApplicationContext());
        owner=session.getUserDetails();
        String user= owner.get( "name");
        TextView welcomeText= (TextView) findViewById(R.id.textView9);
                welcomeText.setText("Welcome\n "+user);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final Intent Addbeneficiary = new Intent(MainActivity.this, Addbeneficiary.class);
        final Intent TransferBwAccounts = new Intent(MainActivity.this, TransferBwAccounts.class);
        final Intent TransferWithinBnk = new Intent(MainActivity.this, TransferWithinBnk.class);
        final Intent TransferOtherBnk = new Intent(MainActivity.this, TransferOtherBnk.class);
        final Intent ListOfBen=new Intent(MainActivity.this,Ben_swipe.class);
        final Intent Help_activity=new Intent(MainActivity.this,Help_activity.class);

        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        TextView marqueeText1 = (TextView) findViewById(R.id.textview7);
        marqueeText1.setSelected(true);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.state_list));
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {



            @Override
            public void onGroupExpand(int groupPosition) {

                if(expandableListTitle.get(groupPosition).equals("Account Summary"))
                {
                    Intent intent = new Intent(MainActivity.this, AcctSumActivity.class);
                    startActivity(intent);
                    layout.closeDrawer(GravityCompat.START);


                }
                else if(expandableListTitle.get(groupPosition).equals("Account Statement"))
                {
                    Intent intent = new Intent(MainActivity.this, AcctStmtActivity.class);
                    startActivity(intent);
                    layout.closeDrawer(GravityCompat.START);

                }
                else if(expandableListTitle.get(groupPosition).equals("Logout"))
                {
                    logout();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    layout.closeDrawer(GravityCompat.START);

                }

                else if(expandableListTitle.get(groupPosition).equals("Generate QR Code"))
                {
                    Intent intent = new Intent(MainActivity.this, QrCodeGenerate.class);
                    startActivity(intent);
                    layout.closeDrawer(GravityCompat.START);
                }

                else if(expandableListTitle.get(groupPosition).equals("Scan QR Code"))
                {
                    Intent intent = new Intent(MainActivity.this, QrCodeScan.class);
                    startActivity(intent);
                    layout.closeDrawer(GravityCompat.START);
                }

            }
        });


        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                if (expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).equals( "Add Beneficiary"))

                    startActivity(Addbeneficiary);

                else if (expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).equals( "View list of Beneficiaries"))

                    startActivity(ListOfBen);

                else if(expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).equals("Transfer within Bank"))

                    startActivity(TransferWithinBnk);

                else if(expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).equals("Transfer between my Accounts"))

                    startActivity(TransferBwAccounts);

                else if(expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).equals("Transfer to other Bank"))

                    startActivity(TransferOtherBnk);

                else if(expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).equals("Help"))

                    startActivity(Help_activity);

                layout.closeDrawer(GravityCompat.START);

                return false;
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

            }


            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        //noinspection SimplifiableIfStatement

        // Activate the navigation drawer toggle
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){


        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
            backpress = 0;
        }
        else {
            backpress = (backpress + 1);
            if(backpress<1||backpress == 1) {
                Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
            }

            if (backpress>1) {
                session1.logoutUser();
                this.finish();
            }
        }
    }
    public void logout()
    {
        session1.logoutUser();
        this.finish();
    }

    public void callSummary(View v)
    {
        callSum = new Intent(MainActivity.this, AcctSumActivity.class);
        startActivity(callSum );
    }
    public void generateQr(View v)
    {
        callSum = new Intent(MainActivity.this, QrCodeGenerate.class);
        startActivity(callSum );
    }

    public void addBen(View v)
    {
        callSum = new Intent(MainActivity.this, Addbeneficiary.class);
        startActivity(callSum );
    }
    public void callFT(View v)
    {
        callSum = new Intent(MainActivity.this, TransferWithinBnk.class);
        startActivity(callSum );
    }
}
