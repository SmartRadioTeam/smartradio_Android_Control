package com.qwe7002.reallct.smartradio;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwe7002 on 16/5/27.
 */
public class APIs
{
    public static String Login(String User, String Passwd)
    {
        List<NameValuePair> Postinfo = new ArrayList<NameValuePair>(2);
        Postinfo.add(new BasicNameValuePair("username", User));
        Postinfo.add(new BasicNameValuePair("password", Passwd));
        return Network.SendPost(public_value.HostURl + "/api/admin_control/login.php", Postinfo);
    }

    public static String getlistjson()
    {
        return Network.SendGet(public_value.HostURl + "/api/admin_control/tableinfo.php");
    }

    public static String ItemsControl(String mode, String id)
    {
        List<NameValuePair> Postinfo = new ArrayList<NameValuePair>(2);
        Postinfo.add(new BasicNameValuePair("mode", mode));
        Postinfo.add(new BasicNameValuePair("id", id));
        return Network.SendPost(public_value.HostURl + "/api/admin_control/items.php", Postinfo);
    }

    public static String ItemsControlMuilt(String mode, String List)
    {
        List<NameValuePair> Postinfo = new ArrayList<NameValuePair>(3);
        Postinfo.add(new BasicNameValuePair("muilt", "true"));
        Postinfo.add(new BasicNameValuePair("mode", mode));
        Postinfo.add(new BasicNameValuePair("id", List));
        return Network.SendPost(public_value.HostURl + "/api/admin_control/items.php", Postinfo);

    }
}
