package io.github.yhdesai.makertoolbox;

/**
 * Created by yash on 23/3/18.
 */

public class DeveloperToDo {
    private String tName;
    private String tDesc;

    public DeveloperToDo() {
    }

    public DeveloperToDo(String tName, String tDesc) {
        this.tName = tName;
        this.tDesc = tDesc;
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

}

