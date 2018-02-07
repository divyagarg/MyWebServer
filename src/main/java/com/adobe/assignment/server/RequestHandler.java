package com.adobe.assignment.server;

import com.adobe.assignment.server.app.WebApp;
import com.adobe.assignment.server.http.HttpRequest;
import com.adobe.assignment.server.http.HttpStatus;
import com.adobe.assignment.server.http.response.HttpResponse;
import com.adobe.assignment.server.http.response.RawHttpResponse;
import com.adobe.assignment.server.utils.BadRequestException;
import com.adobe.assignment.server.utils.ConnectionClosedException;
import com.adobe.assignment.server.utils.MyLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable
{
    private static final String TAG = RequestHandler.class.getSimpleName();

    private WebApp app;
    private Socket connection;
    private InputStream in;
    private OutputStream out;

    public RequestHandler(Socket socket, WebApp webApp)
    {
        connection = socket;
        app = webApp;
    }

    @Override
    public void run()
    {
        try
        {
            in = connection.getInputStream();
            out = connection.getOutputStream();

            HttpRequest request = HttpRequest.parseRequest(in);

            if (request != null)
            {
                MyLogger.logInfo(TAG, request.getRequestLine() +
                        " from "
                        + connection.getInetAddress()
                        + ":"
                        + connection.getPort());

                // Server delegate the request to webapp, and receiving the response
                HttpResponse response = app.handle(request);
                if ( response != null)
                {
                    response.writeToOutputStream(out);
                }
                else
                {
                    new RawHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something Bad occurred.");
                }
            }
            else
            {
                MyLogger.logError(TAG, "Server accepts only HTTP protocol.");
                new RawHttpResponse(HttpStatus.BAD_REQUEST, "Request is malformed").writeToOutputStream(out);
            }
        }
        catch (IOException e)
        {
            MyLogger.logError(TAG, "Error in client's IO");
        }
        catch (ConnectionClosedException e)
        {
            MyLogger.logError(TAG, "Connection closed by client");

        }
        catch (BadRequestException e)
        {
            MyLogger.logError(TAG, e.getMessage());
            new RawHttpResponse(HttpStatus.BAD_REQUEST, "Server only accepts HTTP protocol").writeToOutputStream(out);
        }
        finally
        {
            try
            {
                in.close();
                out.close();
                connection.close();
            }
            catch (IOException e)
            {
                MyLogger.logError(TAG, e.getMessage());
            }
        }
    }
}
