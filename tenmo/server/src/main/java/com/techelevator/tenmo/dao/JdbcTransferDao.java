package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    JdbcTemplate jdbcTemplate;
    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    public JdbcTransferDao(DataSource dataSource){
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }

    // completes a transfer w/ approved status
    @Override
    public boolean transferBucks(Transfer transfer) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        BigDecimal balanceFrom = jdbcTemplate.queryForObject(sql, BigDecimal.class, transfer.getIdFrom());
        BigDecimal balanceTo = jdbcTemplate.queryForObject(sql, BigDecimal.class, transfer.getIdTo());
        System.out.println(transfer);
        if (transfer.getAmount().compareTo(balanceFrom) <= 0 &&
                transfer.getIdTo() != transfer.getIdFrom() && transfer.getStatus().equals("Approved")){
            String sql1 = "UPDATE account SET balance = ? WHERE user_id = ?";
            jdbcTemplate.update(sql1, balanceFrom.subtract(transfer.getAmount()), transfer.getIdFrom());
            jdbcTemplate.update(sql1, balanceTo.add(transfer.getAmount()), transfer.getIdTo());

            String sql2 = "INSERT INTO transfer (id_from, id_to, amount, type, status) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
            Transfer newTransfer = new Transfer();
            Integer transferId;
            try {
                transferId = jdbcTemplate.queryForObject(sql2, Integer.class, transfer.getIdFrom(),
                        transfer.getIdTo(), transfer.getAmount(),
                        transfer.getType(), transfer.getStatus());
                return true;
            } catch (DataAccessException ex){
                return false;
            }
        }
        return false;
    }

    @Override
    public List<Transfer> getMyTransfers(int userId) {
        List<Transfer> myTransfers = new ArrayList<>();
        String sql = "SELECT transfer_id, id_from, id_to, amount, type, status " +
                "FROM transfer WHERE id_from = ? OR id_to = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()){
            myTransfers.add(mapToRowTransfer(results));
        }
        return myTransfers;
    }

   @Override
    public Transfer getTransferByTransferId(int transferId) {
       Transfer transfer = new Transfer();
       String sql = "SELECT transfer_id, id_from, id_to, amount, type, status FROM transfer " +
                "WHERE transfer_id = ?";
       try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfer = mapToRowTransfer(results);
            }
            if (transfer.getTransferId() == 0){
                return null;
            }
        } catch (DataAccessException ex){
            System.out.println("nope");
        }
        return transfer;
    }

    @Override
    public boolean requestTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (id_from, id_to, amount, type, status) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, transfer.getIdFrom(), transfer.getIdTo(), transfer.getAmount(),
                transfer.getType(), transfer.getStatus());
        if (transfer.getType().equals("Request")){
            transfer.setStatus("Pending");
            return true;
        }
        return false;
    }

    @Override
    public boolean approveTransfer(Transfer transfer) {
        boolean success = false;
        String sql = "UPDATE transfer SET status = 'Approved' WHERE transfer_id = ?";
        jdbcTemplate.update(sql, transfer.getTransferId());
        String sql3 = "SELECT status FROM transfer WHERE transfer_id = ?";
        String status = jdbcTemplate.queryForObject(sql3, String.class, transfer.getTransferId());
        if (status.equals("Approved")) {
            String sql1 = "SELECT balance FROM account WHERE user_id = ?";
            BigDecimal balanceFrom = jdbcTemplate.queryForObject(sql1, BigDecimal.class, transfer.getIdFrom());
            BigDecimal balanceTo = jdbcTemplate.queryForObject(sql1, BigDecimal.class, transfer.getIdTo());
            if (transfer.getAmount().compareTo(balanceFrom) <= 0 &&
                    transfer.getIdTo() != transfer.getIdFrom() && status.equals("Approved")) {
                String sql2 = "UPDATE account SET balance = ? WHERE user_id = ?";
                jdbcTemplate.update(sql2, balanceFrom.subtract(transfer.getAmount()), transfer.getIdFrom());
                jdbcTemplate.update(sql2, balanceTo.add(transfer.getAmount()), transfer.getIdTo());
                 success = true;
            }
        }
            return success;
    }



    private Transfer mapToRowTransfer(SqlRowSet srs){
        Transfer transfer = new Transfer();
        transfer.setTransferId(srs.getInt("transfer_id"));
        transfer.setIdFrom(srs.getInt("id_from"));
        transfer.setIdTo(srs.getInt("id_to"));
        transfer.setAmount(srs.getBigDecimal("amount"));
        transfer.setType(srs.getString("type"));
        transfer.setStatus(srs.getString("status"));
        return transfer;
    }

}
