package com.simplesplit.model;

import java.math.BigDecimal;

public class SplitRule {
    private int id;
    private int transactionId;
    private int userId;
    private BigDecimal amount;

    public SplitRule(int id, int transactionId, int userId, BigDecimal amount) {
        this.id = id;
        this.transactionId = transactionId;
        this.userId = userId;
        this.amount = amount;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "SplitRule{" +
                "id=" + id +
                ", transactionId=" + transactionId +
                ", userId=" + userId +
                ", amount=" + amount +
                '}';
    }
}