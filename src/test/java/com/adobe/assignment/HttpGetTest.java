package com.adobe.assignment;


import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetTest extends BaseTest
{

    @Test
    public void GivenGetRequestWhenResourceNotExistThen404() throws IOException
    {
        String urlString = HOST_URL + "/abcd.html";
        System.out.println(urlString);
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();

        Assert.assertEquals("Response code was 404",404, responseCode);
    }

    @Test
    public void GivenGetRequestWhenAccessWrongPathThen403() throws IOException
    {
        String urlString = HOST_URL + "/";
        System.out.println(urlString);
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();

        Assert.assertEquals("Response code was 403",403, responseCode);
    }

    @Test
    public void GivenGetRequestWhenResourceExistThen200() throws IOException
    {
        String urlString = HOST_URL + "/Object.html";
        System.out.println(urlString);
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();

        Assert.assertEquals("Response code was 200",200, responseCode);
    }
}
