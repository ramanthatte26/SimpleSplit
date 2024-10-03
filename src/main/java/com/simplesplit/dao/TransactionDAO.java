package com.simplesplit.dao;

import com.simplesplit.model.Transaction;
import com.simplesplit.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private Connection connection;

    public TransactionDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public void addTransaction(Transaction transaction) throws SQLException {
        String query = "INSERT INTO Transactions (description, amount, date, payer_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, transaction.getDescription());
            pstmt.setBigDecimal(2, transaction.getAmount());
            pstmt.setDate(3, Date.valueOf(transaction.getDate()));
            pstmt.setInt(4, transaction.getPayerId());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transaction.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Transaction getTransactionById(int id) throws SQLException {
        String query = "SELECT * FROM Transactions WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Transaction(
                            rs.getInt("id"),
                            rs.getString("description"),
                            rs.getBigDecimal("amount"),
                            rs.getDate("date").toLocalDate(),
                            rs.getInt("payer_id"));
                }
            }
        }
        return null;
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM Transactions";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getBigDecimal("amount"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("payer_id")));
            }
        }
        return transactions;
    }

    public void updateTransaction(Transaction transaction) throws SQLException {
        String query = "UPDATE Transactions SET description = ?, amount = ?, date = ?, payer_id = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, transaction.getDescription());
            pstmt.setBigDecimal(2, transaction.getAmount());
            pstmt.setDate(3, Date.valueOf(transaction.getDate()));
            pstmt.setInt(4, transaction.getPayerId());
            pstmt.setInt(5, transaction.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteTransaction(int id) throws SQLException {
        String query = "DELETE FROM Transactions WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}