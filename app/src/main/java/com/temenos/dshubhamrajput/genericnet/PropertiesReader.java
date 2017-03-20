package com.temenos.dshubhamrajput.genericnet;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 16-02-2017.
 */

public class PropertiesReader {
    public static String getProperty(String key,Context context) throws IOException {
        Properties properties = new Properties();;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("constant.properties");
        properties.load(inputStream);
        return properties.getProperty(key);
    }
}
