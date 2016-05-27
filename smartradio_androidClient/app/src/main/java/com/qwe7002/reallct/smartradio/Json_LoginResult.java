package com.qwe7002.reallct.smartradio;


public class Json_LoginResult
{
    private String mod;
    public String Sessionid;
    public boolean getmod()
    {
        if (mod == "success")
        {
            return true;
        } else
        {
            return false;
        }
    }
    public String getSessionid()
    {
        return Sessionid;
    }
}
