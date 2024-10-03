package com.simplesplit;

import com.simplesplit.model.User;
import com.simplesplit.model.Transaction;
import com.simplesplit.model.SplitRule;
import com.simplesplit.service.UserService;
import com.simplesplit.service.TransactionService;
import com.simplesplit.service.SplitRuleService;

import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
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
            System.out.println("2. View All Users");
            System.out.println("3. Delete User");
            System.out.println("4. Add Transaction");
            System.out.println("5. View All Transactions");
            System.out.println("6. View Debt Summary");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        addUser();
                        break;
                    case 2:
                        viewAllUsers();
                        break;
                    case 3:
                        deleteUser();
                        break;
                    case 4:
                        addTransaction();
                        break;
                    case 5:
                        viewAllTransactions();
                        break;
                    case 6:
                        viewDebtSummary();
                        break;
                    case 7:
                        System.out.println("Exiting SimpleSplit. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (SQLException e) {
                System.out.println("A database error occurred: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
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

    private static void viewAllUsers() throws SQLException {
        List<User> users = userService.getAllUsers();
        System.out.println("\nCurrent Users:");
        for (User user : users) {
            System.out.println(user.getId() + ": " + user.getUsername() + " (" + user.getEmail() + ")");
        }
    }

    private static void deleteUser() throws SQLException {
        viewAllUsers();
        System.out.print("Enter the ID of the user to delete: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        userService.deleteUser(userId);
        System.out.println("User deleted successfully!");
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
            User payer = userService.getUserById(transaction.getPayerId());
            System.out.println(transaction + " (Paid by: " + payer.getUsername() + ")");
            List<SplitRule> splitRules = splitRuleService.getSplitRulesByTransactionId(transaction.getId());
            for (SplitRule splitRule : splitRules) {
                User user = userService.getUserById(splitRule.getUserId());
                System.out.println("  - " + user.getUsername() + ": " + splitRule.getAmount());
            }
        }
    }

    private static void viewDebtSummary() throws SQLException {
        Map<Integer, Map<Integer, BigDecimal>> debts = new HashMap<>();
        List<User> users = userService.getAllUsers();
        List<Transaction> transactions = transactionService.getAllTransactions();

        for (Transaction transaction : transactions) {
            User payer = userService.getUserById(transaction.getPayerId());
            List<SplitRule> splitRules = splitRuleService.getSplitRulesByTransactionId(transaction.getId());

            for (SplitRule splitRule : splitRules) {
                User debtor = userService.getUserById(splitRule.getUserId());
                if (payer.getId() != debtor.getId()) {
                    debts.computeIfAbsent(debtor.getId(), k -> new HashMap<>());
                    debts.get(debtor.getId()).merge(payer.getId(), splitRule.getAmount(), BigDecimal::add);
                }
            }
        }

        System.out.println("\nDebt Summary:");
        for (User debtor : users) {
            Map<Integer, BigDecimal> debtorDebts = debts.get(debtor.getId());
            if (debtorDebts != null) {
                for (User creditor : users) {
                    BigDecimal debt = debtorDebts.get(creditor.getId());
                    if (debt != null && debt.compareTo(BigDecimal.ZERO) > 0) {
                        System.out.printf("%s owes %s: %.2f%n", debtor.getUsername(), creditor.getUsername(), debt);
                    }
                }
            }
        }
    }
}