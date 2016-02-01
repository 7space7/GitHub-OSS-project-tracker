package com.ua.viktor.github.model;

/**
 * Created by viktor on 28.01.16.
 */
public class Repositories {
    private String name;
    private String description;
    private int forks_count;
    private int stargazers_count;

    public Owner getOwner() {
        return owner;
    }

    private Owner owner;

    public int getForks_count() {
        return forks_count;
    }

    public int getStargazers_count() {
        return stargazers_count;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static class Owner{
        public String avatar_url;
    }

}
