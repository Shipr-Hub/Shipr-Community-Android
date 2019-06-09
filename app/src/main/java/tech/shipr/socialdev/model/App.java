package tech.shipr.socialdev.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class App {

    public int versionCode;
    public String message;
    public String updateUrl;

    public App(){}

}
