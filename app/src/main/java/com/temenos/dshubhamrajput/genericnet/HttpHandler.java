package com.temenos.dshubhamrajput.genericnet;

/**
 * Created by Administrator on 20-02-2017.
 */



import com.tem.pack.GenUrl;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHandler {
    private String response = "";
    private static String basicAuth = "";
    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {
    }

    public String makeServiceCall(String reqUrl) {
        String response = null;

        try {
           // GenUrl gen1= new GenUrl();
            //response= gen1.getUrlConnectionWithoutPassword(reqUrl);


                    URL e = new URL(reqUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection)e.openConnection();
            String userPass = "PAYUSER1"+ ":" +"123456";
            basicAuth = "Basic " + new String((new Base64()).encode(userPass.getBytes()));
                    urlConnection.setRequestProperty("Authorization", basicAuth);
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestMethod("POST");
                    BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    this.response = this.convertStreamToString(in);
                } catch (Exception var5) {
                    var5.printStackTrace();
                }

                return this.response;


        }
        public String makeServiceCallGet(String reqUrl)
        {
            String response = null;

            try {

                URL e = new URL(reqUrl);
                HttpURLConnection urlConnection = (HttpURLConnection)e.openConnection();
                String userPass = "PAYUSER1"+ ":" +"123456";
                basicAuth = "Basic " + new String((new Base64()).encode(userPass.getBytes()));
                urlConnection.setRequestProperty("Authorization", basicAuth);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("GET");
                BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
                this.response = this.convertStreamToString(in);
            } catch (Exception var5) {
                var5.printStackTrace();
            }

            return this.response;
        }


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        try {
            String line;
            try {
                while((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException var14) {
                var14.printStackTrace();
            }
        } finally {
            try {
                is.close();
            } catch (IOException var13) {
                var13.printStackTrace();
            }

        }

        return sb.toString();
    }

}
