package com.qwe7002.reallct.smartradio;

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
                return Network.SendPost(public_value.HostURl + "/login.php", Postinfo);

            }

        public static String ItemsControl(String mode, String id)
            {
                return null;
            }

        public static String ItemsControlMuilt(String mode, ArrayList List)
            {
                return null;
            }
    }
