package com.qwe7002.reallct.smartradio;

import java.io.Serializable;

/**
 * Created by 江华 on 2016/5/28.
 */
public class song implements Serializable{
    private String title;
    private String message;
    private String songid;
    private int taskstate;

    /**
     * Constructs a new instance of {@code Object}.
     */
    public song(String name, String message,String songid,int taskstate) {
        this.title = name;
        this.message = message;
        this.songid = songid;
        this.taskstate = taskstate;
    }
    public void settaskstate(int taskstate) {
        this.taskstate = taskstate;
    }
    public int gettaskstate() {
        return taskstate;
    }
    public void setmessage(String message) {
        this.message = message;
    }
    public void setsongid(String songid) {
        this.songid = songid;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getsongid() {
        return songid;
    }
    public String getmessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }
}