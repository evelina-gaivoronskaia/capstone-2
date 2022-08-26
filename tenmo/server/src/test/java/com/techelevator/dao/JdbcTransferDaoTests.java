package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcTransferDaoTests extends BaseDaoTests{

    JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(dataSource);
    }

    @Test
    public void transferBucks_returns_true_when_transfer_sent(){
        Transfer transfer = new Transfer();
        BigDecimal amount = new BigDecimal("100.00");
        transfer.setTransferId(3003);
        transfer.setIdFrom(1001);
        transfer.setIdTo(1002);
        transfer.setAmount(amount);
        transfer.setType("send");
        transfer.setStatus("Approved");

        boolean actual = sut.transferBucks(transfer);
        Assert.assertTrue(actual);
    }
}
