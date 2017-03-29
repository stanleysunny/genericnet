package com.temenos.dshubhamrajput.genericnet;

import android.content.Context;
import android.content.ContextWrapper;

import java.io.IOException;

/**
 * Created by upriya on 29-03-2017.
 */

public class URLRelated  {
    private Context context;
    public URLRelated(Context paramContext)
    {
        context = paramContext.getApplicationContext();
    }
    public String getValidateURL(String initialURL,String benID)
    {
        String urlStr;
        String trial[] = initialURL.split("\\(");
        System.out.println(trial[0]);
        System.out.println(trial[1]);
        String str="'"+ benID +"'";
        trial[0]=trial[0]+"(";
        trial[0]=trial[0]+str;
        urlStr  = trial[0]+trial[1];
        return urlStr;
    }
    public String getURLParameter(String[] urlAddressList,String param)
    {
        String urlString=null,user;
        try {
            PropertiesReader pro = new PropertiesReader();
            urlString = pro.getProperty(urlAddressList[0],context);
            urlString += pro.getProperty(urlAddressList[1],context);
            urlString += pro.getProperty(urlAddressList[2],context);
            urlString += pro.getProperty(urlAddressList[3],context);
            urlString= urlString+param;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlString;
    }
    public String getURL(String[] urlAddressList)
    {
        String urlString=null;
        try {
            PropertiesReader pro = new PropertiesReader();
            urlString = pro.getProperty(urlAddressList[0],context);
            urlString += pro.getProperty(urlAddressList[1],context);
            urlString += pro.getProperty(urlAddressList[2],context);
            urlString += pro.getProperty(urlAddressList[3],context);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlString;
    }
}
