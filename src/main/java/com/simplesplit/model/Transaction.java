package com.simplesplit.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private int id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private int payerId;

    public Transaction(int id, String description, BigDecimal amount, LocalDate date, int payerId) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.payerId = payerId;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getPayerId() {
        return payerId;
    }

    public void setPayerId(int payerId) {
        this.payerId = payerId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", payerId=" + payerId +
                '}';
    }
}