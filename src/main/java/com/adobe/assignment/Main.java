package com.adobe.assignment;

import com.adobe.assignment.server.HttpServer;
import com.adobe.assignment.server.app.FileServingApp;
import com.adobe.assignment.server.app.WebApp;
import com.adobe.assignment.server.utils.MyLogger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main
{

    private static final String TAG = Main.class.getSimpleName();

    static Properties properties = new Properties();
    private static String root;
    private static int port;
    private static int maxThreadLimit;

    public static void main(String[] args)
    {
	    loadProperties();
        WebApp app = null;
        try
        {
            app = new FileServingApp(root);
        }
        catch (FileNotFoundException e)
        {
            MyLogger.logError(TAG, e.getMessage());
            assert (false);
            return;
        }
	    new Thread(new HttpServer(port, maxThreadLimit, app)).start();
    }

    private static void loadProperties()
    {
        String configPath = "/Users/divgarg/IdeaProjects/mywebserver/src/main/java/com/adobe/assignment/conf/server.properties";
        try
        {
            InputStream in = new FileInputStream(configPath);

            properties.load(in);
            in.close();

            root = properties.getProperty("root");
            port = Integer.parseInt(properties.getProperty("port"));
            maxThreadLimit = Integer.parseInt(properties.getProperty("maxThreadLimit"));
        }
        catch (FileNotFoundException e)
        {
            MyLogger.logError(TAG, e.getMessage());
        }
        catch (IOException e)
        {
            MyLogger.logError(TAG, e.getMessage());
        }
    }

}
