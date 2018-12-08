package tech.shipr.socialdev.model;

/**
 * Created by yash on 26/2/18.
 */

public class Profile {


    private String fullName;
    private String username;
    private String email;
    private String age;
    private String languages;
    private String github;
    private String twitter;
    private String linkedin;


    public Profile() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public Profile(String fullName,
                            String username,
                            String email,
                            String age,
                            String languages,
                            String github,
                            String twitter,
                            String linkedin) {

        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.age = age;
        this.languages = languages;
        this.github = github;
        this.twitter = twitter;
        this.linkedin = linkedin;
    }


}