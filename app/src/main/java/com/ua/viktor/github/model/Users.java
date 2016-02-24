package com.ua.viktor.github.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by viktor on 14.01.16.
 */
public class Users implements Parcelable {
    private String login;
    private String name;
    private String avatar_url;
    private String location;
    private String email;
    private String blog;
    private int public_repos;
    private int public_gists;
    private String created_at;

    public String getBlog() {
        return blog;
    }

    public String getCreated_at() {
        return created_at;
    }

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.login);
        dest.writeString(this.name);
        dest.writeString(this.avatar_url);
        dest.writeString(this.location);
        dest.writeString(this.email);
        dest.writeString(this.blog);
        dest.writeInt(this.public_repos);
        dest.writeInt(this.public_gists);
        dest.writeString(this.created_at);
    }

    public Users() {
    }

    protected Users(Parcel in) {
        this.login = in.readString();
        this.name = in.readString();
        this.avatar_url = in.readString();
        this.location = in.readString();
        this.email = in.readString();
        this.blog = in.readString();
        this.public_repos = in.readInt();
        this.public_gists = in.readInt();
        this.created_at = in.readString();
    }

    public static final Parcelable.Creator<Users> CREATOR = new Parcelable.Creator<Users>() {
        public Users createFromParcel(Parcel source) {
            return new Users(source);
        }

        public Users[] newArray(int size) {
            return new Users[size];
        }
    };
}
