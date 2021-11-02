package services;

import DAO.UserDAO;
import models.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    UserDAO userDAO = new UserDAO();

    public User createUser(User user) {
        User newUser = userDAO.createUser(user);
        new EventService().getPublicEvents().forEach(e -> new EventService().createRow(newUser, e));
        return newUser;
    }

    public void delete(User user) {
        userDAO.deleteUser(user);
    }

    public Optional<User> findUserByID(int id) {
        return userDAO.findUserByID(id);
    }

    public Optional<User> findUserByLogin(String login) {
        return userDAO.findUserByLogin(login);
    }

    public boolean checkPassword(String login, String passwordHash) {
        return userDAO.checkPassword(login, passwordHash);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
}
