package io.github.yhdesai.makertoolbox.model;

/**
 * Created by yash on 26/2/18.
 */

public class DeveloperMessage {


    private String name;
    /*private String displayName;*/
    private String profilePic;
    private String text;
    private String photoMessage;
    private String time;
    private String date;
    private String platform;
    private String version;

    public DeveloperMessage() {
    }

    public DeveloperMessage(String name,
                            /*String displayName,*/
                            String profilePic,
                            String text,
                            String photoMessage,
                            String time,
                            String date,
                            String platform,
                            String version) {
        this.name = name;
        /*this.displayName = displayName;*/
        this.profilePic = profilePic;
        this.text = text;
        this.photoMessage = photoMessage;
        this.time = time;
        this.date = date;
        this.platform = platform;
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

   /* public String getDisplayName() {
        return displayName;
    }*/

    public String getProfilePic() {
        return profilePic;
    }

    public String getName() {
        return name;
    }

    /*public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }*/

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getPhotoMessage() {
        return photoMessage;
    }

    public void setPhotoMessage(String photoMessage) {
        this.photoMessage = photoMessage;
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