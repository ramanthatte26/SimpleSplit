package com.simplesplit.service;

import com.simplesplit.model.SplitRule;
import com.simplesplit.model.Transaction;
import com.simplesplit.model.User;
import com.simplesplit.repository.SplitRuleRepository;
import com.simplesplit.repository.TransactionRepository;
import com.simplesplit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SplitRuleRepository splitRuleRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        // Ensure payer exists
        User payer = userRepository.findById(transaction.getPayer().getId())
                .orElseThrow(() -> new RuntimeException("Payer not found"));
        transaction.setPayer(payer);

        // Fix bidirectional relationship for split rules
        if (transaction.getSplitRules() != null) {
            for (SplitRule rule : transaction.getSplitRules()) {
                rule.setTransaction(transaction);
                User debtor = userRepository.findById(rule.getUser().getId())
                        .orElseThrow(() -> new RuntimeException("Debtor not found"));
                rule.setUser(debtor);
            }
        }

        return transactionRepository.save(transaction);
    }

    public Map<String, BigDecimal> getBalances() {
        List<User> users = userRepository.findAll();
        Map<String, BigDecimal> balances = new HashMap<>();

        for (User user : users) {
            BigDecimal paid = transactionRepository.findByPayerId(user.getId()).stream()
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal owed = splitRuleRepository.findByUserId(user.getId()).stream()
                    .map(SplitRule::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            balances.put(user.getUsername(), paid.subtract(owed));
        }
        return balances;
    }
}
