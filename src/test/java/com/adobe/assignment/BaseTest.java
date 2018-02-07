package com.adobe.assignment;

import com.adobe.assignment.server.HttpServer;
import com.adobe.assignment.server.app.FileServingApp;
import com.adobe.assignment.server.app.WebApp;
import com.adobe.assignment.server.utils.MyLogger;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.FileNotFoundException;

public abstract class BaseTest
{
    private static final String TAG = BaseTest.class.getSimpleName();
    protected static final String USER_AGENT = "Mozilla/5.0";
    protected static final String PORT = "9005";
    protected static final String HOST_URL = "http://localhost"+":"+PORT;

    protected static Thread serverThread;

    @BeforeClass
    public static void setup() throws FileNotFoundException
    {
        String documentRoot = "/Users/divgarg/IdeaProjects/mywebserver/src/main/resources/web/root";
        WebApp app;
        try
        {
            app = new FileServingApp(documentRoot);
        }
        catch (FileNotFoundException e)
        {
            MyLogger.logError(TAG, e.getMessage());
            throw new FileNotFoundException();
        }
        Runnable serverRunnable = new HttpServer(9005, 10, app);
        serverThread = new Thread(serverRunnable);
        serverThread.start();
    }

    @AfterClass
    public static void cleanup()
    {
        if (serverThread != null)
        {
            serverThread.interrupt();
            serverThread = null;
        }
    }

}
