package xyz.nedderhoff.citytweets.domain;

public class User {
    private final long id;
    private final String name;
    private final String username;
    private final String location;

    public User(long id, String name, String username, String location) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
