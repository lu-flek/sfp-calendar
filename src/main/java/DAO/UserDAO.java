package DAO;

import models.User;
import models.UserRole;

import static DAO.DBUtils.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    public User createUser(User user) {
        String creating = "INSERT INTO USERS (name, email, role, password) VALUES(?,?,?,?)";
        User newUser = null;
        try (PreparedStatement stat = getConnection().prepareStatement(creating)) {
            stat.setString(1, user.getName());
            stat.setString(2, user.getEmail());
            stat.setObject(3, user.getRole(), Types.OTHER); // enum
            stat.setString(4, user.getPassword());

            stat.executeUpdate();
            newUser = findUserByLogin(user.getEmail()).get();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return newUser;
    }

    public void deleteUser(User user) {
        String deletion = "DELETE FROM USERS WHERE id=?";

        try (PreparedStatement stat = getConnection().prepareStatement(deletion)) {
            stat.setInt(1, user.getId());
            stat.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public Optional<User> findUserByID(int id) {
        String selection = "SELECT id, name, email, role, password FROM USERS WHERE id=?";
        Optional<User> user = Optional.empty();

        try (PreparedStatement stat = getConnection().prepareStatement(selection)) {
            stat.setInt(1, id);
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                user = Optional.of(createUseByResultSet(rs));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return user;
    }

    public Optional<User> findUserByLogin(String login) {
        String selection = "SELECT id, name, email, role, password FROM USERS WHERE email=?";
        Optional<User> user = Optional.empty();

        try (PreparedStatement stat = getConnection().prepareStatement(selection)) {
            stat.setString(1, login);
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                user = Optional.of(createUseByResultSet(rs));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return user;
    }

    public boolean checkPassword(String login, String password) {
        Optional<User> user = findUserByLogin(login);
        return user.filter(value -> password.equals(value.getPassword())).isPresent();
    }

    public List<User> getAllUsers() {
        String selection = "SELECT id, name, email, role, password FROM users";
        List<User> users = new ArrayList<>();

        try (PreparedStatement stat = getConnection().prepareStatement(selection)) {
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                users.add(createUseByResultSet(rs));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return users;
    }

    public User createUseByResultSet(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                Enum.valueOf(UserRole.class, rs.getString("role")),
                rs.getString("password")
        );
    }
}
