package com.qwe7002.reallct.smartradio;

import java.io.Serializable;

/**
 * Created by 江华 on 2016/5/28.
 */
public class laf implements Serializable{
    private String title;
    private String message;

    /**
     * Constructs a new instance of {@code Object}.
     */
    public laf(String name, String message, String songid, int taskstate) {
        this.title = name;
        this.message = message;

    }
    public void setmessage(String message) {
        this.message = message;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getmessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }
}