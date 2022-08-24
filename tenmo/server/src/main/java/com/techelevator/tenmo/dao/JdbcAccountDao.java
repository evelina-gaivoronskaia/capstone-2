package com.techelevator.tenmo.dao;

import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal requestBalance(int userId) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
        if (balance != null){
            return balance;
        }
        return null;
    }

    @Override
    public boolean transferBucks(int idFrom, int idTo, BigDecimal amount) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        BigDecimal balanceFrom = jdbcTemplate.queryForObject(sql, BigDecimal.class, idFrom);
        // if (BigDec amountToTransfer >= balance &&  int userIdToTransfer != int userIdTooRecieve)

        return false;
    }
}
