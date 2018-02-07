package com.adobe.assignment.server.http.response;

import com.adobe.assignment.server.utils.MyLogger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHttpResponse extends HttpResponse
{
    private static String TAG = FileHttpResponse.class.getSimpleName();

    private File inputFile;

    public FileHttpResponse(int statusCode, File file)
    {
        super(statusCode);
        this.inputFile = file;

        try
        {
            setContentType();
            setContentLength();
        }
        catch (IOException e)
        {
            MyLogger.logError(TAG, e.getMessage());
        }
    }

    private void setContentType() throws IOException
    {
        Path source = Paths.get(this.inputFile.toURI());
        String contentType = Files.probeContentType(source);
        if (contentType != null)
        {
            headers.put("Content-Type", contentType);
        }
    }

    private void setContentLength()
    {
        headers.put("Content-Length", String.valueOf(this.inputFile.length()));
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

            if (inputFile != null)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
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
