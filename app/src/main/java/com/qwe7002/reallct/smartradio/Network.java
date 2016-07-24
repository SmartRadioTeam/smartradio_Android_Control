package com.qwe7002.reallct.smartradio;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

public class Network {
    public static String SendPost(String url, List<NameValuePair> pairList) {
        try {
            HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList, HTTP.UTF_8);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(requestHttpEntity);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpPost);
            return showResponseResult(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String SendGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse response = httpClient.execute(httpGet);
            return showResponseResult(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String showResponseResult(HttpResponse response) {
        if (null == response) {
            return null;
        }

        HttpEntity httpEntity = response.getEntity();
        try {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String result = "";
            String line = "";
            while (null != (line = reader.readLine())) {
                result += line;

            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
