package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests{

    JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(dataSource);
    }

    @Test
    public void transferBucks_returns_createsTransfer_when_transfer_sent(){
        Transfer transfer = new Transfer();
        BigDecimal amount = new BigDecimal("100.00");
        transfer.setTransferId(3003);
        transfer.setIdFrom(1001);
        transfer.setIdTo(1002);
        transfer.setAmount(amount);
        transfer.setType("send");
        transfer.setStatus("Approved");
        sut.transferBucks(transfer);

        Transfer actual = sut.getTransferByTransferId(transfer.getTransferId());

        assertTransfersMatch(transfer, actual);
    }

    @Test
    public void getMyTransfers_returns_listOfTwo_for_user1001(){
        List<Transfer> transferList = sut.getMyTransfers(1001);
        Assert.assertEquals(2, transferList.size());
    }

    @Test
    public void getMyTransfers_returns_listOfZero_for_user1005(){
        List<Transfer> transferList = sut.getMyTransfers(1005);
        Assert.assertEquals(0, transferList.size());
    }

    @Test
    public void getTransferByTransferId_returns_transferObject(){
        Transfer transfer = new Transfer();
        BigDecimal amount = new BigDecimal("100.00");
        transfer.setTransferId(3003);
        transfer.setIdFrom(1001);
        transfer.setIdTo(1002);
        transfer.setAmount(amount);
        transfer.setType("send");
        transfer.setStatus("Approved");
        sut.transferBucks(transfer);

        Transfer actual = sut.getTransferByTransferId(3003);
        assertTransfersMatch(transfer, actual);
    }

    @Test
    public void getTransferByTransferId_returns_exception(){
        Transfer actual = sut.getTransferByTransferId(3007);
        Assert.assertNull(actual);
    }

    private void assertTransfersMatch(Transfer expected, Transfer actual){
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getAmount(), actual.getAmount());
        Assert.assertEquals(expected.getIdFrom(), actual.getIdFrom());
        Assert.assertEquals(expected.getIdTo(), actual.getIdTo());
        Assert.assertEquals(expected.getType(), actual.getType());
        Assert.assertEquals(expected.getStatus(), actual.getStatus());
    }
}
