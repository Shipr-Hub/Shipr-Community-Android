package io.github.yhdesai.makertoolbox;

/**
 * Created by yash on 23/3/18.
 */

public class DeveloperToDo {
    private String tName;
    private String tDesc;
    private Boolean feature;
    private Boolean bug;

    public DeveloperToDo() {
    }

    public DeveloperToDo(String tName, String tDesc, Boolean feature, Boolean bug) {
        this.tName = tName;
        this.tDesc = tDesc;
        this.feature = feature;
        this.bug = bug;
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

    public Boolean getBug() {
        return bug;
    }

    public void setBug(Boolean bug) {
        this.bug = bug;
    }

    public Boolean getFeature() {
        return feature;
    }

    public void setFeature(Boolean feature) {
        this.feature = feature;
    }
}

