package com.simplesplit.dao;

import com.simplesplit.model.SplitRule;
import com.simplesplit.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SplitRuleDAO {
    private Connection connection;

    public SplitRuleDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public void addSplitRule(SplitRule splitRule) throws SQLException {
        String query = "INSERT INTO SplitRules (transaction_id, user_id, amount) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, splitRule.getTransactionId());
            pstmt.setInt(2, splitRule.getUserId());
            pstmt.setBigDecimal(3, splitRule.getAmount());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    splitRule.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public SplitRule getSplitRuleById(int id) throws SQLException {
        String query = "SELECT * FROM SplitRules WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new SplitRule(
                            rs.getInt("id"),
                            rs.getInt("transaction_id"),
                            rs.getInt("user_id"),
                            rs.getBigDecimal("amount"));
                }
            }
        }
        return null;
    }

    public List<SplitRule> getSplitRulesByTransactionId(int transactionId) throws SQLException {
        List<SplitRule> splitRules = new ArrayList<>();
        String query = "SELECT * FROM SplitRules WHERE transaction_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, transactionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    splitRules.add(new SplitRule(
                            rs.getInt("id"),
                            rs.getInt("transaction_id"),
                            rs.getInt("user_id"),
                            rs.getBigDecimal("amount")));
                }
            }
        }
        return splitRules;
    }

    public void updateSplitRule(SplitRule splitRule) throws SQLException {
        String query = "UPDATE SplitRules SET transaction_id = ?, user_id = ?, amount = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, splitRule.getTransactionId());
            pstmt.setInt(2, splitRule.getUserId());
            pstmt.setBigDecimal(3, splitRule.getAmount());
            pstmt.setInt(4, splitRule.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteSplitRule(int id) throws SQLException {
        String query = "DELETE FROM SplitRules WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}