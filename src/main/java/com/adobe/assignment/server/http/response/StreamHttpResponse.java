package com.adobe.assignment.server.http.response;

import com.adobe.assignment.server.utils.MyLogger;

import java.io.*;

public class StreamHttpResponse extends HttpResponse
{
    private final static String TAG = StreamHttpResponse.class.getSimpleName();

    /**
     * Stream to be sent to the user.
     */
    private InputStream inputStream;

    public StreamHttpResponse(int statusCode, InputStream inputStream)
    {
        super(statusCode);
        inputStream = inputStream;
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

            if (inputStream != null)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                char[] buffer = new char[1024];
                int read;
                while ((read = reader.read(buffer)) != -1)
                {
                    writer.write(buffer, 0, read);
                }
                reader.close();
            }

            writer.flush();
        }
        catch (IOException e)
        {
            MyLogger.logError(TAG, e.getMessage());
        }
    }
}
