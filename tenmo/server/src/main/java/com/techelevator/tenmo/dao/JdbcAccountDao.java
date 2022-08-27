package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.UserNotActivatedException;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcAccountDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public BigDecimal requestBalance(int userId) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        try {
            BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
            if (balance != null) {
                return balance;
            }
        } catch (Exception ex){
            return null;
        }
        return null;
    }

    @Override
    public List<Account> getAllAccounts() {
       List<Account> accountList = new ArrayList<>();
        String sql = "SELECT  user_id, account_id FROM account";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
        while (result.next()){
           Account account = new Account();
            account.setAccountId(result.getInt("account_id"));
            account.setUserId(result.getInt("user_id"));
            accountList.add(account);
        }
        return accountList;
    }

    @Override
    public int createAccount(int userId) {
        String sql1 = "INSERT INTO account (user_id, balance) VALUES (?, ?) RETURNING account_id";
        Account account = new Account();
        BigDecimal balance = new BigDecimal("1000");
        Integer accountId = null;
        try {
            accountId = jdbcTemplate.queryForObject(sql1, Integer.class,
                    userId, balance);
        } catch (DataAccessException ex){
            return accountId;
        }
        return accountId;
    }


    private Account mapRowToAccount(SqlRowSet rs){
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}
