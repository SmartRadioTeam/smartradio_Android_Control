package com.qwe7002.reallct.smartradio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by qwe7002 on 16/5/27.
 */
public class Network {
    public String SendPost(String url,List<NameValuePair> pairList)
    {
        try
        {
            HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList);
            // URL使用基本URL即可，其中不需要加参数
            HttpPost httpPost = new HttpPost(url);
            // 将请求体内容加入请求中
            httpPost.setEntity(requestHttpEntity);
            // 需要客户端对象来发送请求
            HttpClient httpClient = new DefaultHttpClient();
            // 发送请求
            HttpResponse response = httpClient.execute(httpPost);
            // 显示响应
            return showResponseResult(response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public String SendGet(String url){
        // 生成请求对象

        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();

        // 发送请求
        try
        {

            HttpResponse response = httpClient.execute(httpGet);

            // 显示响应
            return showResponseResult(response);// 一个私有方法，将响应结果显示出来

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private String showResponseResult(HttpResponse response)
    {
        if (null == response)
        {
            return null;
        }

        HttpEntity httpEntity = response.getEntity();
        try
        {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String result = "";
            String line = "";
            while (null != (line = reader.readLine()))
            {
                result += line;

            }
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
