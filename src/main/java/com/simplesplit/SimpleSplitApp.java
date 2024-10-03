package com.simplesplit;

import com.simplesplit.model.User;
import com.simplesplit.model.Transaction;
import com.simplesplit.model.SplitRule;
import com.simplesplit.service.UserService;
import com.simplesplit.service.TransactionService;
import com.simplesplit.service.SplitRuleService;

import java.util.Scanner;
import java.util.List;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class SimpleSplitApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserService();
    private static final TransactionService transactionService = new TransactionService();
    private static final SplitRuleService splitRuleService = new SplitRuleService();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- SimpleSplit App ---");
            System.out.println("1. Add User");
            System.out.println("2. Add Transaction");
            System.out.println("3. View All Transactions");
            System.out.println("4. View User Balance");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        addUser();
                        break;
                    case 2:
                        addTransaction();
                        break;
                    case 3:
                        viewAllTransactions();
                        break;
                    case 4:
                        viewUserBalance();
                        break;
                    case 5:
                        System.out.println("Exiting SimpleSplit. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (SQLException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void addUser() throws SQLException {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        System.out.print("Enter user email: ");
        String email = scanner.nextLine();

        User user = new User(0, name, email, LocalDate.now().atStartOfDay());
        userService.addUser(user);
        System.out.println("User added successfully!");
    }

    private static void addTransaction() throws SQLException {
        System.out.print("Enter payer's email: ");
        String payerEmail = scanner.nextLine();
        System.out.print("Enter transaction description: ");
        String description = scanner.nextLine();
        System.out.print("Enter total amount: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine(); // Consume newline

        User payer = userService.getAllUsers().stream()
                .filter(u -> u.getEmail().equals(payerEmail))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Payer not found"));

        Transaction transaction = new Transaction(0, description, amount, LocalDate.now(), payer.getId());
        transactionService.addTransaction(transaction);

        System.out.print("Enter number of participants: ");
        int participantCount = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < participantCount; i++) {
            System.out.print("Enter participant's email: ");
            String participantEmail = scanner.nextLine();
            System.out.print("Enter share amount for " + participantEmail + ": ");
            BigDecimal shareAmount = scanner.nextBigDecimal();
            scanner.nextLine(); // Consume newline

            User participant = userService.getAllUsers().stream()
                    .filter(u -> u.getEmail().equals(participantEmail))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Participant not found"));

            SplitRule splitRule = new SplitRule(0, transaction.getId(), participant.getId(), shareAmount);
            splitRuleService.addSplitRule(splitRule);
        }

        System.out.println("Transaction and split rules added successfully!");
    }

    private static void viewAllTransactions() throws SQLException {
        List<Transaction> transactions = transactionService.getAllTransactions();
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
            List<SplitRule> splitRules = splitRuleService.getSplitRulesByTransactionId(transaction.getId());
            for (SplitRule splitRule : splitRules) {
                User user = userService.getUserById(splitRule.getUserId());
                System.out.println("  - " + user.getEmail() + ": " + splitRule.getAmount());
            }
        }
    }

    private static void viewUserBalance() throws SQLException {
        System.out.print("Enter user email: ");
        String email = scanner.nextLine();

        User user = userService.getAllUsers().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        BigDecimal balance = calculateUserBalance(user);
        System.out.println("Balance for " + email + ": " + balance);
    }

    private static BigDecimal calculateUserBalance(User user) throws SQLException {
        BigDecimal balance = BigDecimal.ZERO;

        List<Transaction> allTransactions = transactionService.getAllTransactions();
        for (Transaction transaction : allTransactions) {
            if (transaction.getPayerId() == user.getId()) {
                balance = balance.add(transaction.getAmount());
            }

            List<SplitRule> splitRules = splitRuleService.getSplitRulesByTransactionId(transaction.getId());
            for (SplitRule splitRule : splitRules) {
                if (splitRule.getUserId() == user.getId()) {
                    balance = balance.subtract(splitRule.getAmount());
                }
            }
        }

        return balance;
    }
}