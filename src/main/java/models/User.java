package models;

import lombok.Data;

@Data
public class User implements Comparable<User> {
    private int id;
    private String name;
    private String email;
    private UserRole role;
    private String password;

    public User(int id, String name, String email, UserRole role, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public User(String name, String email, UserRole role, String password) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    @Override
    public int compareTo(User user) {
        return name.compareTo(user.getName());
    }
}
