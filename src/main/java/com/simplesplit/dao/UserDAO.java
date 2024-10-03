package com.simplesplit.dao;

import com.simplesplit.model.User;
import com.simplesplit.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;

    public UserDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO Users (username, email) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public User getUserById(int id) throws SQLException {
        String query = "SELECT * FROM Users WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getTimestamp("created_at").toLocalDateTime());
                }
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM Users";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at").toLocalDateTime()));
            }
        }
        return users;
    }

    public void updateUser(User user) throws SQLException {
        String query = "UPDATE Users SET username = ?, email = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setInt(3, user.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteUser(int id) throws SQLException {
        String query = "DELETE FROM Users WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}