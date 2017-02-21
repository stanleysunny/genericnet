package com.temenos.dshubhamrajput.genericnet;

/**
 * Created by Administrator on 20-02-2017.
 */



import com.tem.pack.GenUrl;

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {
    }

    public String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            GenUrl gen1= new GenUrl();
            response= gen1.getUrlConnectionWithoutPassword(reqUrl);
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
