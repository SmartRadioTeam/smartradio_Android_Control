package com.qwe7002.reallct.smartradio;

import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwe7002 on 16/5/27.
 */
public class APIs {
    public static String setsetting(String mode, String value) {
        String url = public_value.HostURl + "/api/admin_control/setting.php";
        RequestBody formBody = new FormEncodingBuilder()
                .add("mode", mode)
                .add("value", value)
                .add("resultkey", public_value.sessionid)
                .add("username", public_value.username)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        return Network.Send(request);
    }

    public static String getsetting() {
        String url = public_value.HostURl + "/api/admin_control/systeminfo.php?resultkey=" + public_value.sessionid + "&username=" + public_value.username;
        Request request = new Request.Builder()
                .url(url)
                .build();
        return Network.Send(request);
    }

    public static String Login(String User, String Passwd) {
        String url = public_value.HostURl + "/api/admin_control/auth_control.php";
        RequestBody formBody = new FormEncodingBuilder()
                .add("mode", "login")
                .add("password", Passwd)
                .add("username", User)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        return Network.Send(request);
    }

    public static String getlistjson() {
        String url = public_value.HostURl + "/api/admin_control/tableinfo.php?resultkey=" + public_value.sessionid + "&username=" + public_value.username;
        Request request = new Request.Builder()
                .url(url)
                .build();
        return Network.Send(request);
    }

    public static String ItemsControl(String mode, String id, Boolean mulit) {
        String url = public_value.HostURl + "/api/admin_control/items.php";
        String Mulitmode;
        if (mulit) {
            Mulitmode = "true";
        } else {
            Mulitmode = "false";
        }
        RequestBody formBody = new FormEncodingBuilder()
                .add("mode", mode)
                .add("multi", Mulitmode)
                .add("id", id)
                .add("resultkey", public_value.sessionid)
                .add("username", public_value.username)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        return Network.Send(request);
    }
}
