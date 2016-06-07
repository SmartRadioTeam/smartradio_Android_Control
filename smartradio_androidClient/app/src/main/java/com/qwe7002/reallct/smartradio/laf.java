package com.qwe7002.reallct.smartradio;

import java.io.Serializable;

/**
 * Created by 江华 on 2016/5/28.
 */
public class laf implements Serializable
{
    private String title;
    private String message;

    /**
     * Constructs a new instance of {@code Object}.
     */
    public laf(int id, String name, String message)
    {
        this.title = name;
        this.message = message;

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