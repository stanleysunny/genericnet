package com.temenos.dshubhamrajput.genericnet;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by upriya on 21-03-2017.
 */

public class GetBenCustomer {
     HashMap getBenCus( String cusurl,String BenAcctNo)
    {
        final HashMap<String, String> obj = new HashMap<>();
        HttpHandler sh = new HttpHandler();
        String  jsonStr ;
        cusurl = cusurl + BenAcctNo;
        jsonStr = sh.makeServiceCallGet(cusurl);
        if (jsonStr != null) {
            try {
                JSONObject cus1 = new JSONObject(jsonStr);
                JSONObject cus2 = cus1.getJSONObject("_embedded");
                JSONArray cusarray1 = cus2.getJSONArray("item");
                for (int k = 0; k < cusarray1.length(); k++) {
                    JSONObject cus3 = cusarray1.getJSONObject(k);

                     obj.put("BencustomerNo",cus3.getString("CustomerNo"));
                    obj.put("Benname", cus3.getString("Name"));

                }

            } catch (final JSONException e) {

            }
        }
        return obj;
    }
}
