package io.github.yhdesai.makertoolbox.model;

public class DeveloperAwesomeLibraries {

    private String title;
    private String subtitle;
    private String photoUrl;
    private String wiki;
    private String docs;
    private String git;

    public DeveloperAwesomeLibraries() {
    }

    public DeveloperAwesomeLibraries(String title, String subtitle, String photoUrl, String wiki, String docs, String git) {
        this.title = title;
        this.subtitle = subtitle;
        this.photoUrl = photoUrl;
        this.wiki = wiki;
        this.docs = docs;
        this.git = git;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }


    public String getGit() {
        return git;
    }


    public void setGit(String git) {
        this.git = git;
    }
}