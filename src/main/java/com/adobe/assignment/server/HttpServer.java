package com.adobe.assignment.server;

import com.adobe.assignment.server.app.WebApp;
import com.adobe.assignment.server.utils.MyLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpServer implements Runnable
{
    private final static String TAG = HttpServer.class.getSimpleName();

    private final int port;
    private final int maxThreadLimit;
    private final WebApp app;

    private ServerSocket server;

    //For Thread safety
    private LinkedBlockingQueue<Runnable> workQueue;
    private ThreadPoolExecutor threadPool;
    private Thread monitor;

    public HttpServer(int port, int threadLimit, WebApp app)
    {
        this.port = port;
        this.maxThreadLimit = threadLimit;
        this.app = app;
    }

    @Override
    public void run()
    {
        try
        {
            server = new ServerSocket(port);
            workQueue = new LinkedBlockingQueue<>();
            threadPool = new ThreadPoolExecutor(maxThreadLimit, maxThreadLimit,10 ,TimeUnit.SECONDS, workQueue);
            monitor = new Thread(new ServerMonitor(threadPool));
        }
        catch (IOException e)
        {
            MyLogger.logError(TAG, e.getMessage());
        }

        // Since we want to monitor thread to work till the server is running.
        monitor.setDaemon(true);
        monitor.start();


        while(!Thread.interrupted())
        {
            Runnable handler = null;
            try
            {
                handler = new RequestHandler(server.accept(), app);
            }
            catch (IOException e)
            {
                MyLogger.logError(TAG, e.getMessage());
            }
            threadPool.execute(new Thread(handler));
        }


    }
}
