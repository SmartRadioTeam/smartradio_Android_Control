package com.qwe7002.reallct.smartradio;

import java.io.Serializable;

/**
 * Created by 江华 on 2016/5/28.
 */
public class laf implements Serializable
{
    private int id;
    private String title;
    private String message;

    public laf(int id, String name, String message)
    {
        this.id=id;
        this.title = name;
        this.message = message;

    }
    public int getid()
    {
        return id;
    }

    public String getmessage()
    {
        return message;
    }

    public String getTitle()
    {
        return title;
    }
}