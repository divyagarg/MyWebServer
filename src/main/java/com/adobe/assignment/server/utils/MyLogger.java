package com.adobe.assignment.server.utils;

import java.util.Calendar;

public class MyLogger
{
    public static void logError(String tag, String message)
    {
        System.err.println("["
                + Calendar.getInstance().getTime().toString()
                + "] "
                + tag
                + " ERROR - "
                + message
        );
    }

    public static void logInfo(String tag, String message)
    {
        System.out.println("["
                + Calendar.getInstance().getTime().toString()
                + "] "
                + tag
                + " - "
                + message
        );
    }
}
