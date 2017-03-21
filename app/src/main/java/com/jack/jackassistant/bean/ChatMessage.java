package com.jack.jackassistant.bean;

import java.util.Date;

/**
 * Created by xiaofeng on 2017/3/12.
 */

public class ChatMessage {

    private long id;
    private String fromName;
    private String fromAvatar;
    private String toName;
    private String toAvatar;
    private SendType sendType;
    private SendStatus sendStatus;
    private Date date;
    private String content;
    private ContentType contentType;

    public enum SendType {
        INCOMING, OUTCOMING
    }

    public enum ContentType {
        TEXT, PHOTO, FACE
    }

    public enum SendStatus {
        ONGOING, SUCCESS, FAIL
    }

    public ChatMessage() {
    }

    public ChatMessage(String fromName, String fromAvatar,
                       String toName, String toAvatar,
                       SendType sendType, SendStatus sendStatus,
                       Date date, String content, ContentType contentType) {
        this.fromName = fromName;
        this.fromAvatar = fromAvatar;
        this.toName = toName;
        this.toAvatar = toAvatar;
        this.sendType = sendType;
        this.sendStatus = sendStatus;
        this.date = date;
        this.content = content;
        this.contentType = contentType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromAvatar() {
        return fromAvatar;
    }

    public void setFromAvatar(String fromAvatar) {
        this.fromAvatar = fromAvatar;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToAvatar() {
        return toAvatar;
    }

    public void setToAvatar(String toAvatar) {
        this.toAvatar = toAvatar;
    }

    public SendType getSendType() {
        return sendType;
    }

    public void setSendType(SendType sendType) {
        this.sendType = sendType;
    }

    public SendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
}
