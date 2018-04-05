package io.github.yhdesai.makertoolbox;

/**
 * Created by yash on 26/2/18.
 */

public class DeveloperMessage {

    private String text;
    private String name;
    private String photoUrl;
    private String time;
    private String date;
    private String platform;

    public DeveloperMessage() {
    }

    public DeveloperMessage(String text, String name, String photoUrl, String time, String date, String platform) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.time = time;
        this.date = date;
        this.platform = platform;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }


}