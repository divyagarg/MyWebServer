package com.adobe.assignment;

import org.junit.Assert;
import org.junit.Test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPostTest extends BaseTest
{
    @Test
    public void GivenPostRequestWhenBodyExistThen200() throws IOException
    {
        String urlString = HOST_URL + "/";
        System.out.println(urlString);
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type","application/json");

        String jsonData = "{\"id\":5,\"countryName\":\"USA\",\"population\":8000}";
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(jsonData);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        Assert.assertEquals("Response code expected is 200",200, responseCode);
    }

    @Test
    public void GivenPostRequestWhenBodyDoesNotExistThen400() throws IOException
    {
        String urlString = HOST_URL + "/";
        System.out.println(urlString);
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type","application/json");

        int responseCode = con.getResponseCode();

        Assert.assertEquals("Response code expected is 400",400, responseCode);
    }
}
