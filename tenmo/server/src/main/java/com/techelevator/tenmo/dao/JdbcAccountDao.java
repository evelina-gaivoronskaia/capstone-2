package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

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
    public boolean transferBucks(int idFrom, int idTo, BigDecimal amount) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        BigDecimal balanceFrom = jdbcTemplate.queryForObject(sql, BigDecimal.class, idFrom);
        BigDecimal balanceTo = jdbcTemplate.queryForObject(sql, BigDecimal.class, idTo);
         if (amount.compareTo(balanceFrom) >= 0 && idTo != idFrom){
             String sql1 = "UPDATE account SET balance = ? WHERE user_id = ?";
             jdbcTemplate.update(sql1, balanceFrom.subtract(amount), idFrom);
             jdbcTemplate.update(sql1, balanceTo.add(amount), idTo);

             String sql2 = "INSERT INTO transfer (id_from, id_to, amount, date_time) " +
                     "VALUES (?, ?, ?, ?) RETURNING transfer_id";
             Transfer transfer = new Transfer();
             LocalDateTime dateTime = LocalDateTime.now();
             Integer transferId;
             try {
                 transferId = jdbcTemplate.queryForObject(sql2, Integer.class, idFrom, idTo, amount, dateTime);
             } catch (DataAccessException ex){
                 return false;
             }
             return true;
         }

        return false;
    }

//    private Account mapRowToAccountDTO(SqlRowSet rs){
//        AccountDTO accountDto = new AccountDTO();
//    }

    private Account mapRowToAccount(SqlRowSet rs){
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}
