package com.ua.viktor.github.model;

/**
 * Created by viktor on 28.01.16.
 */
public class Event {
    private String type;
    private String id;
    private String created_at;
    private Actor actor;
    private Payload payload;
    private Repo repo;

    public Repo getRepo() {
        return repo;
    }

    public Payload getPayload() {
        return payload;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Actor getActor() {
        return actor;
    }

    public String getCreated_at() {
        return created_at;
    }

    public static class Actor {

        private String login;
        private String url;
        private String avatar_url;

        public String getLogin() {
            return login;
        }

        public String getUrl() {
            return url;
        }

        public String getAvatar_url() {
            return avatar_url;
        }
    }

    public static class Payload {
        private String head;
        private String action;

        public String getAction() {
            return action;
        }

        public String getHead() {
            return head;
        }
    }

    public static class Repo {
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }

}
