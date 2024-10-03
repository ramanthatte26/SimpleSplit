package com.simplesplit.service;

import com.simplesplit.dao.UserDAO;
import com.simplesplit.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public void addUser(User user) throws SQLException {
        userDAO.addUser(user);
    }

    public User getUserById(int id) throws SQLException {
        return userDAO.getUserById(id);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    public void updateUser(User user) throws SQLException {
        userDAO.updateUser(user);
    }

    public void deleteUser(int id) throws SQLException {
        userDAO.deleteUser(id);
    }
}