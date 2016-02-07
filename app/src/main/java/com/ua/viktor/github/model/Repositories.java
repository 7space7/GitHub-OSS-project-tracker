package com.ua.viktor.github.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by viktor on 28.01.16.
 */
public class Repositories implements Parcelable {
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

    public static class Owner implements Parcelable {
        public String avatar_url;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.avatar_url);
        }

        public Owner() {
        }

        protected Owner(Parcel in) {
            this.avatar_url = in.readString();
        }

        public static final Creator<Owner> CREATOR = new Creator<Owner>() {
            public Owner createFromParcel(Parcel source) {
                return new Owner(source);
            }

            public Owner[] newArray(int size) {
                return new Owner[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.forks_count);
        dest.writeInt(this.stargazers_count);
        dest.writeParcelable(this.owner, flags);
    }

    public Repositories() {
    }

    protected Repositories(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        this.forks_count = in.readInt();
        this.stargazers_count = in.readInt();
        this.owner = in.readParcelable(Owner.class.getClassLoader());
    }

    public static final Parcelable.Creator<Repositories> CREATOR = new Parcelable.Creator<Repositories>() {
        public Repositories createFromParcel(Parcel source) {
            return new Repositories(source);
        }

        public Repositories[] newArray(int size) {
            return new Repositories[size];
        }
    };
}
