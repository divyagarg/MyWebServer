package com.adobe.assignment.server;

import com.adobe.assignment.server.utils.MyLogger;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * This monitor will keep printing the thread pool status after every 10 seconds.
 */
public class ServerMonitor implements Runnable
{
    private static final String TAG = ServerMonitor.class.getSimpleName();

    private ThreadPoolExecutor threadPool;

    public ServerMonitor(ThreadPoolExecutor threadPool)
    {
        this.threadPool = threadPool;
    }

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                MyLogger.logInfo(TAG, String.format("[%d/%d] Active: %d, Completed: %d, Task: %d, queueSize: %d",
                        threadPool.getPoolSize(),
                        threadPool.getCorePoolSize(),
                        threadPool.getActiveCount(),
                        threadPool.getCompletedTaskCount(),
                        threadPool.getTaskCount(),
                        threadPool.getQueue().size()));
                Thread.sleep(10000);
            }
        } catch (InterruptedException ex)
        {
            MyLogger.logError(TAG, ex.getMessage());
        }

    }
}
