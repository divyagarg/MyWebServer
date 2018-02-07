package com.adobe.assignment;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHeadTest extends BaseTest
{
    @Test
    public void GivenHeadRequestWhenResourceExistThen200() throws IOException
    {
        String urlString = HOST_URL + "/Object.html";
        System.out.println(urlString);
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("HEAD");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();

        Assert.assertEquals("Response code was 200",200, responseCode);
    }

    @Test
    public void GivenSuccessHeadRequestThenBodyShouldBeAbsent() throws IOException
    {
        String urlString = HOST_URL + "/Object.html";
        System.out.println(urlString);
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("HEAD");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode != 200)
        {
            assert (false);
        }
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String line = in.readLine();
        if ( line == null || line == "")
        {
            assert(true);
        }
        else
        {
            assert (false);
        }
    }

}
