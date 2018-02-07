package com.adobe.assignment.server.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpStatus
{

    /**
     * Informational 1xx
     */
    public static final int CONTINUE = 100;

    /**
     * Successful 2xx
     */
    public static final int OK = 200;
    public static final int CREATED = 201;

    /**
     * Redirection 3xx
     */
    public static final int MOVED_PERMANENTLY = 301;

    /**
     * Client Error 4xx
     */
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;

    /**
     * Server Error 1xx
     */
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int NOT_IMPLEMENTED = 501;

    /**
     * User readable status descriptions
     */
    public static Map<Integer, String> statusMessage;

    static {
        Map<Integer, String> status = new HashMap<>();
        status.put(100, "Continue");
        status.put(101, "Switching Protocols");
        status.put(200, "OK");
        status.put(201, "Created");
        status.put(300, "Multiple Choices");
        status.put(301, "Moved Permanently");
        status.put(302, "Found");
        status.put(400, "Bad Request");
        status.put(401, "Unauthorized");
        status.put(402, "Payment Required");
        status.put(403, "Not Found");
        status.put(404, "Continue");
        status.put(500, "Internal Server Error");
        status.put(501, "Not Implemented");
        status.put(502, "Bad Gateway");
        status.put(503, "Service Unavailable");
        status.put(504, "Gateway Time-out");
        status.put(505, "HTTP Version not supported");

        //provide users with "read-only" access to internal map
        statusMessage = Collections.unmodifiableMap(status);
    }

}
