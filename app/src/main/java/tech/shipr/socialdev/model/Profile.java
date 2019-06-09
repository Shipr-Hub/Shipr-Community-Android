package tech.shipr.socialdev.model;

/**
 * Created by yash on 26/2/18.
 */

public class Profile {


    private String fullName;
    private String username;
    private String title;
    private String profilePic;

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

    public String getEmail() {
        return email;
    }

    public String getAge() {
        return age;
    }

    public String getLanguages() {
        return languages;
    }

    public String getGithub() {
        return github;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public Profile(
            String fullName,
            String username,
            String title,
            String email,
            String age,
            String languages,
            String github,
            String twitter,
            String linkedin) {


        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.languages = languages;
        this.github = github;
        this.twitter = twitter;
        this.linkedin = linkedin;
        this.title = title;
        this.username = username;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}