package com.adobe.assignment.server.http.response;

import com.adobe.assignment.server.http.HttpStatus;

import java.io.OutputStream;
import java.util.HashMap;

public abstract class HttpResponse
{
    //represents the protocol version, used in the status line (e. g. HTTP/1.1).
    protected String protocol = "HTTP/1.1";
    protected int statusCode;
    protected HashMap<String, String> headers;

    public HttpResponse()
    {
        this.headers = new HashMap<>();
    }

    public HttpResponse(int statusCode)
    {
        this.statusCode = statusCode;
        this.headers = new HashMap<>();
    }

    abstract public void writeToOutputStream(OutputStream out);


    /**
     * @return the response line containing protocol version and response status.
     */
    public String getResponseLine() {
        return protocol
                + " "
                + String.valueOf(statusCode)
                + " "
                + HttpStatus.statusMessage.getOrDefault(statusCode, "Not Specified");
    }

    public String getProtocol()
    {
        return protocol;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public HashMap<String, String> getHeaders()
    {
        return headers;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public void setHeaders(HashMap<String, String> headers)
    {
        this.headers = headers;
    }
}
