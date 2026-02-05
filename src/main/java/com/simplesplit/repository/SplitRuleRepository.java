package com.simplesplit.repository;

import com.simplesplit.model.SplitRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SplitRuleRepository extends JpaRepository<SplitRule, Long> {
    List<SplitRule> findByUserId(Long userId);
}
