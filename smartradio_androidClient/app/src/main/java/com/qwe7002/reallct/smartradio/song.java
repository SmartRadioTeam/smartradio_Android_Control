package com.qwe7002.reallct.smartradio;

import java.io.Serializable;

/**
 * Created by 江华 on 2016/5/28.
 */
public class song implements Serializable{
    private int row;
    private int id;
    private String title;
    private String message;
    private String songid;
    private String user;
    private String to;
    private String playtime;
    private int taskstate;

    /**
     * Constructs a new instance of {@code Object}.
     */
    public song(int row,int id,String title, String message,String user,String to,String playtime,String songid,int taskstate) {
        this.row=row;
        this.id=id;
        this.title = title;
        this.message = "「"+message+"」";
        this.songid = songid;
        this.taskstate = taskstate;
        this.user="点歌人："+user;
        this.to="送给："+to;
        this.playtime="播放时间："+playtime;
    }
    public int gettaskstate() {
        return taskstate;
    }
    public String getmessage() {
        return message;
    }
    public String getUser() {
        return user;
    }
    public String getTo() {
        return to;
    }
    public String getPlaytime() {
        return playtime;
    }
    public String getTitle() {
        return title;
    }
    public String getsongid() {
        return songid;
    }
    public int getrow(){return row;}
    public int getid(){return id;}
}