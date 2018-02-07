package com.adobe.assignment.server.http;

import com.adobe.assignment.server.utils.BadRequestException;
import com.adobe.assignment.server.utils.ConnectionClosedException;
import com.adobe.assignment.server.utils.MyLogger;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class HttpRequest
{
    private final static String TAG = HttpRequest.class.getSimpleName();

    //Input stream associated with the source socket
    private InputStream inputStream;

    //Request line containing protocol version, URI, and request method
    private String requestLine;

    // Represent Http request method - GET/POST/PUT/DELETE
    private String method;

    private String uri;
    private String version;
    private String urlPath;
    private String query;
    private boolean keepAlive = false;
    private String body;

    private HashMap<String, String> reqHeaders;
    private HashMap<String, String> queryParams;

    public HttpRequest()
    {
        this.reqHeaders = new HashMap<>();
        this.queryParams = new HashMap<>();
    }


    public String getRequestLine()
    {
        return requestLine;
    }

    public static HttpRequest parseRequest(InputStream in) throws ConnectionClosedException, BadRequestException
    {
        try
        {
            HttpRequest request = new HttpRequest();
            request.inputStream = in;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            request.requestLine = reader.readLine();

            if (request.requestLine == null)
            {
                throw new ConnectionClosedException();
            }

            //Example - GET /Object.html HTTP/1.1
            String[] requestLineParts = request.requestLine.split(" ", 3);

            request.method = requestLineParts[0];
            request.uri = requestLineParts[1];
            request.version = requestLineParts[2];

            String line = reader.readLine();

            while (line != null && !line.equals(""))
            {
//                Splitting line with colon and space to take care other colons in value side.
//                Example - Host: localhost:9000
                String[] lineParts = line.split(": ", 2);
                if (lineParts.length == 2)
                {
                    request.reqHeaders.put(lineParts[0], lineParts[1]);
                }
                line = reader.readLine();
            }

            String[] uriParts = request.uri.split("\\?", 2);
            if (uriParts.length == 2)
            {
                request.urlPath = uriParts[0];
                request.query = uriParts[1];

                String[] keyValuePairs = request.query.split("&");
                for (String keyValuePair : keyValuePairs)
                {
                    String[] keyValue = keyValuePair.split("=", 2);
                    if (keyValue.length == 2)
                    {
                        request.queryParams.put(keyValue[0], keyValue[1]);
                    }
                }
            }
            else
            {
                request.urlPath = request.uri;
                request.query = "";
            }
            if (request.reqHeaders.getOrDefault("Connection", "close").equalsIgnoreCase("keep-alive")) {
                request.keepAlive = true;
            }
            int contentLength = Integer.valueOf(request.reqHeaders.getOrDefault("Content-Length", "0"));
            //Parse request body
            if ( contentLength > 0)
            {
                int contentLengthLocal = contentLength;
                int chunk = 1024;

                CharArrayWriter charArrayWriter = new CharArrayWriter();

                boolean firstBoundary = true;
                while (contentLengthLocal > 0)
                {
                    char[] contentBuffer = new char[chunk];
                    int result = reader.read(contentBuffer, 0, chunk);


                    String boundary = request.reqHeaders.get("boundary");
                    if (boundary != null)
                    {
                        String contentStr = contentBuffer.toString();
                        int boundaryIndex = contentStr.indexOf(boundary);
                        if (boundaryIndex >= 0 && firstBoundary)
                        {
                            int newlineIndex = contentStr.indexOf("\r\n\r\n", boundaryIndex);
                            int skip = newlineIndex + 4;
                            charArrayWriter.write(contentBuffer, skip, result - skip);
                            firstBoundary = false;
                        }
                        else if (boundaryIndex >= 0 && !firstBoundary) {
                            charArrayWriter.write(contentBuffer, 0, boundaryIndex);
                        }
                        else {
                            charArrayWriter.write(contentBuffer, 0, result);
                        }
                    }
                    else {
                        charArrayWriter.write(contentBuffer, 0, result);
                    }
                    contentLengthLocal -= result;
                }
                request.body = charArrayWriter.toString();
            }

            return request;
        }
        catch (Exception e)
        {
            MyLogger.logError(TAG, e.getMessage());
            throw new BadRequestException();
        }
    }

    public String getMethod()
    {
        return method;
    }

    public String getUri()
    {
        return uri;
    }

    public String getVersion()
    {
        return version;
    }

    public String getUrlPath()
    {
        return urlPath;
    }

    public String getQuery()
    {
        return query;
    }

    public boolean isKeepAlive()
    {
        return keepAlive;
    }

    public String getBody()
    {
        return body;
    }

    public HashMap<String, String> getReqHeaders()
    {
        return reqHeaders;
    }

    public HashMap<String, String> getQueryParams()
    {
        return queryParams;
    }

    public InputStream getInputStream()
    {
        return inputStream;
    }
}
