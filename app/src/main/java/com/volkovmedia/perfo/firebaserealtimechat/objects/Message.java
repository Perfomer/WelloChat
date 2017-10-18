package com.volkovmedia.perfo.firebaserealtimechat.objects;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

public class Message implements DataContainer {

    @Exclude
    private String mIdentifier;

    @PropertyName("text")
    private String mText;

    @PropertyName("author_id")
    private String mAuthorId;

    @PropertyName("timestamp")
    private long mTimestamp;

    public Message() {} //for firebase only

    public Message(String authorId, long timestamp, String text) {
        this.mAuthorId = authorId;
        this.mTimestamp = timestamp;
        this.mText = text;
    }

    public String getId() {
        return mIdentifier;
    }

    public String getText() {
        return mText;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public void setId(String id) {
        this.mIdentifier = id;
    }

    public void setAuthorId(String authorId) {
        this.mAuthorId = authorId;
    }

    public void setTimestamp(long timestamp) {
        this.mTimestamp = timestamp;
    }
}