package com.temenos.dshubhamrajput.genericnet;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Typeface;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public ListView mDrawerList;
    public DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    public ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    public int backpress=0;
    SessionManager session1;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    LinkedHashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session1 = new SessionManager(getApplicationContext());
        Intent intent = getIntent();
//        Intent intent = getIntent();
        getSupportActionBar().setTitle("TEMENOS");
        //mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final Intent intent1 = new Intent(MainActivity.this, Addbeneficiary.class);
        //mActivityTitle = getTitle().toString();
//        TextView textView7 = (TextView) findViewById(R.id.textView7);

//        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        TextView marqueeText1 = (TextView) findViewById(R.id.textview7);
        marqueeText1.setSelected(true);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData();
        System.out.print("Object List"+expandableListDetail);
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {



            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
               if(expandableListTitle.get(groupPosition) == "Account Summary")
               {
                   Intent intent = new Intent(MainActivity.this, AcctSumActivity.class);
                   startActivity(intent);
//
               }
               else if(expandableListTitle.get(groupPosition) == "Account Statement")
               {
                   Intent intent = new Intent(MainActivity.this, AcctStmtActivity.class);
                   startActivity(intent);
               }

            }
        });


        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                Toast.makeText(
//                        getApplicationContext(),
//                        expandableListTitle.get(groupPosition)
//                                + " -> "
//                                + expandableListDetail.get(
//                                expandableListTitle.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT
//                ).show();

                if (expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition) == "Add Beneficiary")

                    startActivity(intent1);
                return false;
            }
        });
//        textView7.setMovementMethod(new ScrollingMovementMethod());

//        int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
//        TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);
//        Typeface robotoBoldCondensedItalic = Typeface.createFromAsset(getAssets(), "font/CorisandeRegular.ttf");
//        if(actionBarTitleView != null){
//            actionBarTitleView.setTypeface(robotoBoldCondensedItalic);
        // }
    }

//    private void addDrawerItems() {
//        String[] osArray = { "Account Summary", "Account Statement", "Account Transfer", "Settings","Feedback", "Help", "Logout" };
//        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
//        mDrawerList.setAdapter(mAdapter);
//        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()//add an event on clicking an item in menu
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//        DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawer_layout);
//
//                if(position == 0) {
////                Toast.makeText(MainActivity.this, "Testing!" , Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MainActivity.this, AcctSumActivity.class);
//                    startActivity(intent);
//                    layout.closeDrawer(GravityCompat.START);
//                }
//
//                if(position == 1) {
////                Toast.makeText(MainActivity.this, "Testing!" , Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MainActivity.this, AcctStmtActivity.class);
//                    startActivity(intent);
//                    layout.closeDrawer(GravityCompat.START);
//                }
//                if(position == 6)
//                {
//                    logout();
//                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    layout.closeDrawer(GravityCompat.START);
//                }
//
//            }
//        });
//    }

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


//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.my_options_menu, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        // Activate the navigation drawer toggle
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){

        DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
            backpress = 0;
        }
        else {
        backpress = (backpress + 1);
        if(backpress<1||backpress==1) {
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


}
