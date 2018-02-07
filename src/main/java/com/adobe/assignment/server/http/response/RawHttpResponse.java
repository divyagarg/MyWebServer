package com.adobe.assignment.server.http.response;

import com.adobe.assignment.server.utils.MyLogger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class RawHttpResponse extends HttpResponse
{
    private final static String TAG = RawHttpResponse.class.getSimpleName();
    private String content;

    public RawHttpResponse(int statusCode, String content)
    {
        super(statusCode);
        this.content = content;
    }

    @Override
    public void writeToOutputStream(OutputStream out)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(getResponseLine());
            writer.write("\r\n");

            for (String key : headers.keySet())
            {
                writer.write(key + ":" + headers.get(key));
                writer.write("\r\n");
            }
            writer.write("\r\n");

            writer.write(content);

            writer.flush();
        }
        catch (IOException e)
        {
            MyLogger.logError(TAG, e.getMessage());
        }
    }
}
