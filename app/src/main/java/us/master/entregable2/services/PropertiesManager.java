package us.master.entregable2.services;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {
    public static Properties loadProperties(Context context) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = context.getAssets().open("secrets.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
