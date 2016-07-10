package com.qwe7002.reallct.smartradio;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwe7002 on 16/5/27.
 */
public class APIs {
    public static String setsetting(String mode, String value) {
        List<NameValuePair> Postinfo = new ArrayList<NameValuePair>(4);
        Postinfo.add(new BasicNameValuePair("mode", mode));
        Postinfo.add(new BasicNameValuePair("value", value));
        Postinfo.add(new BasicNameValuePair("resultkey", public_value.sessionid));
        Postinfo.add(new BasicNameValuePair("username", public_value.username));
        return Network.SendPost(public_value.HostURl + "/api/admin_control/setting.php", Postinfo);
    }

    public static String getsetting() {
        return Network.SendGet(public_value.HostURl + "/api/admin_control/systeminfo.php?resultkey=" + public_value.sessionid + "&username=" + public_value.username);
    }

    public static String Login(String User, String Passwd) {
        List<NameValuePair> Postinfo = new ArrayList<NameValuePair>(3);
        Postinfo.add(new BasicNameValuePair("username", User));
        Postinfo.add(new BasicNameValuePair("password", Passwd));
        Postinfo.add(new BasicNameValuePair("mode", "login"));
        return Network.SendPost(public_value.HostURl + "/api/admin_control/auth_control.php", Postinfo);
    }

    public static String getlistjson() {
        return Network.SendGet(public_value.HostURl + "/api/admin_control/tableinfo.php?resultkey=" + public_value.sessionid + "&username=" + public_value.username);
    }

    public static String ItemsControl(String mode, String id, Boolean muilt) {
        List<NameValuePair> Postinfo = new ArrayList<NameValuePair>(3);
        if (muilt) {
            Postinfo = new ArrayList<NameValuePair>(4);
            Postinfo.add(new BasicNameValuePair("muilt", "true"));
            Log.i("send", id);
        }
        Postinfo.add(new BasicNameValuePair("mode", mode));
        Postinfo.add(new BasicNameValuePair("id", id));
        Postinfo.add(new BasicNameValuePair("resultkey", public_value.sessionid));
        Postinfo.add(new BasicNameValuePair("username", public_value.username));
        return Network.SendPost(public_value.HostURl + "/api/admin_control/items.php", Postinfo);
    }
}
