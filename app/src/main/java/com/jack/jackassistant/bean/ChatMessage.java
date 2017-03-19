package com.jack.jackassistant.bean;

import java.util.Date;

/**
 * Created by xiaofeng on 2017/3/12.
 */

public class ChatMessage {

    private String name;
    private Type type;
    private Date date;
    private String msg;

    public enum Type {
        INCOMING, OUTCOMING
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
