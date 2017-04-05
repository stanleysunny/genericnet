package com.temenos.dshubhamrajput.genericnet;

/**
 * Created by ckavya on 03-03-2017.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
public class ExpandableListDataPump {
    public static LinkedHashMap<String, List<String>> getData() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();

        List<String> Account_Summary = new ArrayList<String>();





        List<String> Account_Statement = new ArrayList<String>();

        List<String> Account_Transfer = new ArrayList<String>();
        Account_Transfer.add("Add Beneficiary");
        Account_Transfer.add("View list of Beneficiaries");
        Account_Transfer.add("Transfer between my Accounts");
        Account_Transfer.add("Transfer within Bank");
        Account_Transfer.add("Transfer to other Bank");

        List<String> Settings = new ArrayList<String>();

        List<String> Feedback = new ArrayList<String>();

        List<String> Help = new ArrayList<String>();

        List<String> Logout = new ArrayList<String>();

        List<String> qrCode = new ArrayList<>();

        List<String> qrGenerator = new ArrayList<>();


        expandableListDetail.put("Account Summary", Account_Summary);
        expandableListDetail.put("Account Statement", Account_Statement);
        expandableListDetail.put("Account Transfer", Account_Transfer);
        expandableListDetail.put("Settings", Settings);
        expandableListDetail.put("Qr Code Generator",qrGenerator);
        expandableListDetail.put("Qr Code Scanner",qrCode);
        expandableListDetail.put("Feedback", Feedback);
        expandableListDetail.put("Help", Help);
        expandableListDetail.put("Logout", Logout);


        System.out.print(expandableListDetail);

        return expandableListDetail;
    }
}

