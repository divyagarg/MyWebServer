package com.adobe.assignment.server.http.response;

import com.adobe.assignment.server.utils.MyLogger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class EmptyHttpResponse extends HttpResponse
{
    private final static String TAG = EmptyHttpResponse.class.getSimpleName();

    public EmptyHttpResponse(int statusCode) {
        super(statusCode);
    }


    @Override
    public void writeToOutputStream(OutputStream out)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(getResponseLine());
            writer.write("\r\n");

            for (String key: headers.keySet())
            {
                writer.write(key + ":" + headers.get(key));
                writer.write("\r\n");
            }
            writer.write("\r\n");
            writer.flush();
        }
        catch (IOException exception)
        {
            MyLogger.logError(TAG, exception.getMessage());
        }

    }
}
