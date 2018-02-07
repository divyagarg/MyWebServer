package com.adobe.assignment.server.app;

import com.adobe.assignment.server.http.HttpRequest;
import com.adobe.assignment.server.http.response.HttpResponse;

/**
 * It represent the web app deployed on the server.
 * All business logic will be part of this web app.
 */
public interface WebApp
{
    /**
     *
     * @param request sent by the client
     * @return HttpResponse
     */
    HttpResponse handle(HttpRequest request);
}
