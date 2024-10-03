package com.simplesplit.service;

import com.simplesplit.dao.SplitRuleDAO;
import com.simplesplit.model.SplitRule;

import java.sql.SQLException;
import java.util.List;

public class SplitRuleService {
    private SplitRuleDAO splitRuleDAO;

    public SplitRuleService() {
        this.splitRuleDAO = new SplitRuleDAO();
    }

    public void addSplitRule(SplitRule splitRule) throws SQLException {
        splitRuleDAO.addSplitRule(splitRule);
    }

    public SplitRule getSplitRuleById(int id) throws SQLException {
        return splitRuleDAO.getSplitRuleById(id);
    }

    public List<SplitRule> getSplitRulesByTransactionId(int transactionId) throws SQLException {
        return splitRuleDAO.getSplitRulesByTransactionId(transactionId);
    }

    public void updateSplitRule(SplitRule splitRule) throws SQLException {
        splitRuleDAO.updateSplitRule(splitRule);
    }

    public void deleteSplitRule(int id) throws SQLException {
        splitRuleDAO.deleteSplitRule(id);
    }
}