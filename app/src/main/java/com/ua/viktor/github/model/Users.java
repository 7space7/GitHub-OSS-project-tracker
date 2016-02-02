package com.ua.viktor.github.model;

/**
 * Created by viktor on 14.01.16.
 */
public class Users {
    private String login;
    private String name;
    private String avatar_url;
    private String location;
    private String email;
    private int public_repos;
    private int public_gists;

    public int getPublic_gists() {
        return public_gists;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public String getLocation() {
        return location;
    }

    public String getEmail() {
        return email;
    }

    public int getPublic_repos() {
        return public_repos;
    }


}
