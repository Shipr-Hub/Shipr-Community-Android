package io.github.yhdesai.makertoolbox.model;

/**
 * Created by yash on 23/3/18.
 */

public class DeveloperToDo {
    private String tName;
    private String tDesc;
    private String tDate;
    private String feature;
    private String bug;

    public DeveloperToDo() {
    }

    public DeveloperToDo(String tName, String tDesc, String tDate, String feature, String bug) {
        this.tName = tName;
        this.tDesc = tDesc;
        this.feature = feature;
        this.bug = bug;
        this.tDate = tDate;
    }


    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String gettDesc() {
        return tDesc;
    }

    public void settDesc(String tDesc) {
        this.tDesc = tDesc;
    }

    public String gettDate() {
        return tDate;
    }

    public void setDate(String tDate) {
        this.tDate = tDate;
    }

    public String getBug() {
        return bug;
    }

    public void setBug(String bug) {
        this.bug = bug;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }
}

