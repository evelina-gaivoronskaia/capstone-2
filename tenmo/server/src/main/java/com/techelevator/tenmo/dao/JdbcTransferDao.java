package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class JdbcTransferDao implements TransferDao{

    JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean transferBucks(Transfer transfer) {
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
}
