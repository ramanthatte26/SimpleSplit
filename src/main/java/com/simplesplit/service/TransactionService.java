package com.simplesplit.service;

import com.simplesplit.dao.TransactionDAO;
import com.simplesplit.model.Transaction;

import java.sql.SQLException;
import java.util.List;

public class TransactionService {
    private TransactionDAO transactionDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
    }

    public void addTransaction(Transaction transaction) throws SQLException {
        transactionDAO.addTransaction(transaction);
    }

    public Transaction getTransactionById(int id) throws SQLException {
        return transactionDAO.getTransactionById(id);
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        return transactionDAO.getAllTransactions();
    }

    public void updateTransaction(Transaction transaction) throws SQLException {
        transactionDAO.updateTransaction(transaction);
    }

    public void deleteTransaction(int id) throws SQLException {
        transactionDAO.deleteTransaction(id);
    }
}