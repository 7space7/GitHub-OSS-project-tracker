package com.ua.viktor.github.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by viktor on 02.02.16.
 */
public class Organizations implements Parcelable {
    private String login;
    private String avatar_url;
    private String url;

    public String getLogin() {
        return login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.login);
        dest.writeString(this.avatar_url);
        dest.writeString(this.url);
    }

    public Organizations() {
    }

    protected Organizations(Parcel in) {
        this.login = in.readString();
        this.avatar_url = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<Organizations> CREATOR = new Parcelable.Creator<Organizations>() {
        public Organizations createFromParcel(Parcel source) {
            return new Organizations(source);
        }

        public Organizations[] newArray(int size) {
            return new Organizations[size];
        }
    };
}
