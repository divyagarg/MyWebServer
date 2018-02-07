package com.adobe.assignment.server.app;

import com.adobe.assignment.server.http.HttpMethod;
import com.adobe.assignment.server.http.HttpRequest;
import com.adobe.assignment.server.http.HttpStatus;
import com.adobe.assignment.server.http.response.*;
import com.adobe.assignment.server.utils.MyLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileServingApp implements WebApp
{
    private static final String TAG = FileServingApp.class.getSimpleName();
    private String webRoot;

    public FileServingApp(String documentRoot) throws FileNotFoundException
    {
        if (Files.exists(Paths.get(documentRoot)))
        {
            this.webRoot = documentRoot;
        }
        else
        {
            throw new FileNotFoundException();
        }

    }

    @Override
    public HttpResponse handle(HttpRequest request)
    {
        HttpResponse response = null;
        String urlPath = request.getUrlPath();
        Path requestedFile = null;
        switch( request.getMethod())
        {

            case HttpMethod.GET:
                requestedFile = Paths.get(webRoot, urlPath);
                if (requestedFile.normalize().startsWith(Paths.get(webRoot).normalize()))
                {
                    if (Files.exists(requestedFile))
                    {
                        if (Files.isDirectory(requestedFile))
                        {
                            response = new RawHttpResponse(HttpStatus.FORBIDDEN, "Access is Forbidden");
                        }
                        else
                        {
                            response = new FileHttpResponse(HttpStatus.OK,
                                    new File(requestedFile.toString()));
                        }
                    }
                    else
                    {
                        response = new RawHttpResponse(HttpStatus.NOT_FOUND, "Resource not found.");
                    }
                }
                else
                {
                    response = new RawHttpResponse(HttpStatus.FORBIDDEN, "Access is Forbidden");
                }
                break;
            case HttpMethod.HEAD:
                requestedFile = Paths.get(webRoot, urlPath);
                if ( Files.exists(requestedFile))
                {
                    response = new RawHttpResponse(HttpStatus.OK, "Request was Successful.");
                }
                else
                {
                    response = new RawHttpResponse(HttpStatus.NOT_FOUND, "Resource not found.");
                }
                break;
            case HttpMethod.TRACE:
                response = new StreamHttpResponse(HttpStatus.OK, request.getInputStream());
                break;

            case HttpMethod.PUT:
            case HttpMethod.POST:
                if (request.getBody() != null)
                {
                    response = new RawHttpResponse(HttpStatus.OK, "Request Successfully Handled");
                }
                else
                {
                    response = new RawHttpResponse(HttpStatus.BAD_REQUEST, "Bad Request");
                }
                break;

            case HttpMethod.DELETE:
                Path toDeleteFile = Paths.get(webRoot, urlPath);
                if (toDeleteFile.normalize().startsWith(Paths.get(webRoot).normalize()))
                {
                    if (Files.exists(toDeleteFile))
                    {
                        if (Files.isDirectory(toDeleteFile))
                        {
                            response = new RawHttpResponse(HttpStatus.FORBIDDEN, "Access is Forbidden");
                        }
                        else
                        {
                            try
                            {
                                Files.delete(toDeleteFile);
                                response = new RawHttpResponse(HttpStatus.OK, "Successully Deleted the file "+ urlPath);
                            }
                            catch (IOException e)
                            {
                                MyLogger.logError(TAG, e.getMessage());
                            }
                        }
                    }
                    else
                    {
                        response = new RawHttpResponse(HttpStatus.NOT_FOUND, "Resource not found");
                    }
                }
                else
                {
                    response = new RawHttpResponse(HttpStatus.FORBIDDEN, "Access is Forbidden");
                }
                break;
            default:
                response = new RawHttpResponse(HttpStatus.NOT_IMPLEMENTED, "Not Implemented");
                break;
        }
        return response;
    }
}
