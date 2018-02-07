package com.adobe.assignment;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class GenericTest extends BaseTest
{
    @Test
    public void GivenNotImplementedRequestThen501() throws IOException
    {
        String urlString = HOST_URL + "/";
        System.out.println(urlString);
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("OPTIONS");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();

        Assert.assertEquals("Response code expected is 501",501, responseCode);
    }
}
