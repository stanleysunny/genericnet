package com.temenos.dshubhamrajput.genericnet;

/**
 * Created by Administrator on 20-02-2017.
 */


import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class HttpHandler {
    private String response = "";
    private static String basicAuth = "";
    private static final String TAG = HttpHandler.class.getSimpleName();
    private String returnResponse = "";
    private static String userName="";
    private static String passWord="";
    private static HashMap<String,String> innerErrorObj = new HashMap<>();
    private static HashMap<String,HashMap<String,String>> outerErrorObj = new HashMap<>();


    HttpHandler() {
    }

    String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            URL e = new URL(reqUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) e.openConnection();
            urlConnection.setRequestProperty("Authorization", basicAuth);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
            response = convertStreamToString(in);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return response;
    }

    public String makeServiceCallGet(String reqUrl) {
        String response = null;

        try {

            URL e = new URL(reqUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) e.openConnection();
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

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        try {
            String line;
            try {
                while ((line = reader.readLine()) != null) {
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

    String postfunc(String reurl, String jsonstring) {
        String response = "";
        try {

            URL e = new URL(reurl);
            HttpURLConnection urlConnectio = (HttpURLConnection) e.openConnection();
            urlConnectio.setDoOutput(true);
            urlConnectio.setRequestProperty("Authorization", basicAuth);
            urlConnectio.setRequestProperty("Accept", "application/json");
            urlConnectio.setRequestProperty("Content-Type", "application/json");
            urlConnectio.setRequestMethod("POST");
            urlConnectio.connect();
            OutputStreamWriter out = new OutputStreamWriter(urlConnectio.getOutputStream());
            out.write(jsonstring);// here i sent the parameter
            out.flush();
            out.close();


            int responseCode = urlConnectio.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                response = "YES";
            } else {
                response = "NO";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 11. return result
        return response;
    }

    String posCommit(String reurl, String jsonstring) {
        String response = "";
        try {
            URL commit = new URL(reurl);
            HttpURLConnection urlcommit = (HttpURLConnection) commit.openConnection();
            urlcommit.setDoOutput(true);
            urlcommit.setRequestProperty("Authorization", basicAuth);
            urlcommit.setRequestProperty("Accept", "application/json");
            urlcommit.setRequestProperty("Content-Type", "application/json");
            urlcommit.setRequestMethod("POST");
            urlcommit.connect();
            OutputStreamWriter commitout = new OutputStreamWriter(urlcommit.getOutputStream());
            commitout.write(jsonstring);// here i sent the parameter
            commitout.flush();
            commitout.close();

            int commitresponse = urlcommit.getResponseCode();
            if (commitresponse == HttpsURLConnection.HTTP_CREATED) {
                response = "YES";
            } else {
                response = "NO";
            }
        } catch (Exception commit) {
            commit.printStackTrace();
        }
        return response;
    }

    boolean jsonWrite(String urlStr, JSONObject postdata) {
        boolean success = true;
        String text;
        String info;
        try {
            URL u = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestProperty("Authorization", basicAuth);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(postdata.toString());

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            System.out.println(responseCode);
            BufferedInputStream in;
            BufferedReader reader;
            StringBuilder sb;

            if (responseCode >= 200 && responseCode < 400) {
                success=true;
                in = new BufferedInputStream(conn.getInputStream());
                reader = new BufferedReader(new InputStreamReader(in));
                sb = new StringBuilder();

                try {
                    String line;
                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append('\n');
                        }
                        returnResponse = sb.toString();
                    } catch (IOException var14) {
                        var14.printStackTrace();
                    }
                } finally {
                    try {
                        in.close();
                    } catch (IOException var13) {
                        var13.printStackTrace();
                    }

                }


            } else {
                // READING THE ERROR
                success = false;
                in = new BufferedInputStream(conn.getErrorStream());
                reader = new BufferedReader(new InputStreamReader(in));
                sb = new StringBuilder();
                try {
                    String line;
                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append('\n');
                        }
                    } catch (IOException var14) {
                        var14.printStackTrace();
                    }
                } finally {
                    try {
                        in.close();
                    } catch (IOException var13) {
                        var13.printStackTrace();
                    }

                }
                String jsonStr1 = sb.toString();


                JSONObject jsonErrorObj = new JSONObject(jsonStr1);
                try {
                    JSONObject jsonEmbedObj = jsonErrorObj.getJSONObject("_embedded");
                    JSONArray jsonErrorArrObj = jsonEmbedObj.getJSONArray("http://temenostech.temenos.com/rels/errors");
                    for (int i = 0; i < jsonErrorArrObj.length(); i++) {
                        JSONObject item = jsonErrorArrObj.getJSONObject(i);
                        JSONArray errorlist = item.getJSONArray("ErrorsMvGroup");

                        for (int j = 0; j < errorlist.length(); j++) {
                            JSONObject error = errorlist.getJSONObject(j);
                            text = error.getString("Text");
                            info = error.getString("Info");
                            innerErrorObj.put("text",text);
                            innerErrorObj.put("info",info);
                            outerErrorObj.put("Error"+j,innerErrorObj);
                        }

                    }
                } catch (Exception exception) {
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return success;
    }

    public String getResponse() {
        return returnResponse;
    }

    public void setCredentials(String user,String mpassword)
    {
            userName = user;
            passWord = mpassword;
        String userPass =  user + ":" + mpassword;
        basicAuth = "Basic " + new String((new Base64()).encode(userPass.getBytes()));
    }
    public HashMap<String,HashMap<String,String>> getErrorList()
    {
            return outerErrorObj;
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

}

