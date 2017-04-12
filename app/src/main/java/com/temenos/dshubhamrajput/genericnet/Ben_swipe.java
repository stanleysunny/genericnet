package com.temenos.dshubhamrajput.genericnet;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;


public class Ben_swipe extends AppCompatActivity {

    private SwipeMenuListView ListBen;
    private ArrayList<String> mArrayList=new ArrayList<>();
    private ListDataAdapter mListDataAdapter;
    String Ben="";

    static ArrayList<HashMap<String, String>> beneficiaryList ;
    ListAdapter adapter1;
    ListAdapter adapter;
    int id;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ben_swipe);
        getSupportActionBar().setTitle("View beneficiary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListBen=(SwipeMenuListView)findViewById(R.id.ListBen);

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
                    new Ben_swipe.FetchBenWithin().execute();
                    ListBen.setVisibility(View.VISIBLE);
                }
                else
                {
                    new Ben_swipe.FetchBenOutside().execute();
                    ListBen.setVisibility(View.VISIBLE);

                }
            }
        });

        SwipeControl();

    }
    private class FetchBenWithin extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Void doInBackground(Void... param) {

            String owningCustomer;
            beneficiaryList = new ArrayList<>();
            HttpHandler sh = new HttpHandler();
            URLRelated urlObj = new URLRelated(getApplicationContext());
            HashMap<String,String> owner;
            SessionManager session =new SessionManager(getApplicationContext());
            owner=session.getUserDetails();
            owningCustomer= owner.get("cusId");
            String[] URLAddressList1= {"url_ip","url_iris_project","url_company","url_enqEnqWbnks"};
            String owingCust= urlObj.getURLParameter(URLAddressList1,owningCustomer);
            String jsonOwingCus = sh.makeServiceCallGet(owingCust);
            JSONObject jsonObjOwingCust;
            try {
                jsonObjOwingCust = new JSONObject(jsonOwingCus);
                JSONObject firstObjOwingCust = jsonObjOwingCust.getJSONObject("_embedded");
                JSONArray itemOwingCust = firstObjOwingCust.getJSONArray("item");
                for (int i = 0; i < itemOwingCust.length(); i++) {
                    HashMap<String, String> benList= new HashMap<>();

                    JSONObject benAccountNo = itemOwingCust.getJSONObject(i);
                    benList.put("BenAcctNo",benAccountNo.getString("BenAcctNo"));

                    JSONArray NicknameMyGroup = benAccountNo.getJSONArray("NicknameMvGroup");
                    JSONObject nickName = NicknameMyGroup.getJSONObject(0);
                    benList.put("Nickname", nickName.getString("Nickname"));
                    beneficiaryList.add(benList);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            adapter = new SimpleAdapter(Ben_swipe.this, beneficiaryList,
                    R.layout.list_ben_internal, new String[]{"BenAcctNo","Nickname"},
                    new int[]{R.id.AccountNumber,R.id.NickName});

            ListBen.setAdapter(adapter);


        }
    }
    private class FetchBenOutside extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Void doInBackground(Void... param) {
            String owningCustomer;
            beneficiaryList = new ArrayList<>();
            HttpHandler sh = new HttpHandler();
            URLRelated urlObj = new URLRelated(getApplicationContext());
            HashMap<String,String> owner;
            SessionManager session =new SessionManager(getApplicationContext());
            owner=session.getUserDetails();
            owningCustomer= owner.get("cusId");

            String[] URLAddressList1= {"url_ip","url_iris_project","url_company","url_enqEnqObnks"};
            String owingCust= urlObj.getURLParameter(URLAddressList1,owningCustomer);
            String jsonOwingCus = sh.makeServiceCallGet(owingCust);
            try {
                JSONObject jsonObjOwingCust = new JSONObject(jsonOwingCus);
                JSONObject firstObjOwingCust = jsonObjOwingCust.getJSONObject("_embedded");
                JSONArray itemOwingCust = firstObjOwingCust.getJSONArray("item");
                for (int i = 0; i < itemOwingCust.length(); i++) {
                    HashMap<String, String> benList= new HashMap<>();

                    JSONObject benAccountNo = itemOwingCust.getJSONObject(i);
                    benList.put("BenAccNo" ,benAccountNo.getString("BenAcctNo"));
                    benList.put("BankSortCode" ,benAccountNo.getString("BankSortCode"));
                    benList.put("Branch" ,benAccountNo.getString("Branch"));

                    JSONArray NicknameMyGroup = benAccountNo.getJSONArray("NicknameMvGroup");
                    JSONObject nickName = NicknameMyGroup.getJSONObject(0);
                    benList.put("Nickname" , nickName.getString("Nickname"));
                    beneficiaryList.add(benList);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            adapter1 = new SimpleAdapter(Ben_swipe.this, beneficiaryList,
                    R.layout.list_beneficiary, new String[]{"BenAccNo","Nickname","BankSortCode","Branch"},
                    new int[]{R.id.AccountNumber,R.id.NickName,R.id.Ifsc,R.id.BranchName});
            ListBen.setAdapter(adapter1);
        }
    }


    private void SwipeControl() {



        ListBen.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override

            public void create(SwipeMenu menu) {

// Create different menus depending on the view type

                SwipeMenuItem goodItem = new SwipeMenuItem(

                        getApplicationContext());

// set item background

                goodItem.setBackground(new ColorDrawable(Color.rgb(44,238,144)));

// set item width

                goodItem.setWidth(dp2px(90));

// set a icon

                goodItem.setIcon(R.drawable.edit);

// add to menu

                menu.addMenuItem(goodItem);

// create "delete" item

                SwipeMenuItem deleteItem = new SwipeMenuItem(

                        getApplicationContext());

// set item background

                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,

                        0x3F, 0x25)));

// set item width

                deleteItem.setWidth(dp2px(90));

// set a icon

                deleteItem.setIcon(R.drawable.delete);

// add to menu

                menu.addMenuItem(deleteItem);

            }

        };

// set creator

        ListBen.setMenuCreator(creator);

        ListBen.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override

            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {

                    case 0:

                        Toast.makeText(Ben_swipe.this,"Like button press from position" + position,Toast.LENGTH_SHORT).show();

                        break;

                    case 1:

                        Toast.makeText(Ben_swipe.this,"Item deleted from position" + position,Toast.LENGTH_SHORT).show();
                        //beneficiaryList.remove(position);
                        //adapter.notifyDataSetChanged();


                        break;

                }

                return true;

            }

        });


        ListBen.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {

            @Override

            public void onMenuOpen(int position) {

            }

            @Override

            public void onMenuClose(int position) {

            }

        });

        ListBen.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override

            public void onSwipeStart(int position) {

            }

            @Override

            public void onSwipeEnd(int position) {

            }

        });

    }

    class ListDataAdapter extends BaseAdapter {

        ViewHolder holder;

        @Override

        public int getCount() {

            return beneficiaryList.size();

        }

        @Override

        public Object getItem(int i) {

            return null;

        }

        @Override

        public long getItemId(int i) {

            return 0;

        }

        @Override

        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView==null){

                holder=new ViewHolder();

                convertView=getLayoutInflater().inflate(R.layout.list_item,null);

                holder.mTextview=(TextView)convertView.findViewById(R.id.textView);

                convertView.setTag(holder);

            }else {

                holder= (ViewHolder) convertView.getTag();

            }

            //holder.mTextview.setText(beneficiaryList.get(position));//why set text??

            return convertView;

        }

        class ViewHolder {

            TextView mTextview;

        }

    }

    private int dp2px(int dp) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,

                getResources().getDisplayMetrics());

    }

}