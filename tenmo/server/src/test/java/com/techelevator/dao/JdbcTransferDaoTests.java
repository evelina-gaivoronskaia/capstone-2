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
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void getTransferByTransferId_returns_transferObject(){
        Transfer transfer1 = new Transfer();
        BigDecimal amount = new BigDecimal("50.00");
        transfer1.setTransferId(3002);
        transfer1.setIdFrom(1002);
        transfer1.setIdTo(1001);
        transfer1.setAmount(amount);
        transfer1.setType("send");
        transfer1.setStatus("Approved");
        sut.transferBucks(transfer1);

        Transfer actual = sut.getTransferByTransferId(3002);
        assertTransfersMatch(transfer1, actual);
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

        Transfer actual = sut.getTransferByTransferId(3003);

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
    public void getTransferByTransferId_returns_exception(){
        Transfer actual = sut.getTransferByTransferId(3007);
        Assert.assertNull(actual);
    }

    @Test
    public void listPendingTransfers_returns_list_of1(){
        Transfer transfer = new Transfer();
        BigDecimal amount = new BigDecimal("100.00");
        transfer.setTransferId(3003);
        transfer.setIdFrom(1001);
        transfer.setIdTo(1002);
        transfer.setAmount(amount);
        transfer.setType("send");
        transfer.setStatus("Pending");
        sut.requestTransfer(transfer);

        List<Transfer> pendingTransferList = sut.listPendingTransfersByUserId(1001);
        Assert.assertEquals(1, pendingTransferList.size());
    }

    @Test
    public void listPendingTransfers_returns_list_of0(){
        List<Transfer> pendingTransferList = sut.listPendingTransfersByUserId(1001);
        Assert.assertEquals(0, pendingTransferList.size());
    }

    @Test
    public void requestTransfer_returns_false_given_invalidId(){
        Transfer transfer = new Transfer();
        BigDecimal amount = new BigDecimal("100.00");
        transfer.setTransferId(3003);
        transfer.setIdFrom(1001);
        transfer.setIdTo(1001);
        transfer.setAmount(amount);
        transfer.setType("Request");
        transfer.setStatus("Pending");

        boolean actual = sut.requestTransfer(transfer);
        Assert.assertFalse(actual);
    }
    @Test
    public void requestTransfer_returns_true_given_validId(){
        Transfer transfer = new Transfer();
        BigDecimal amount = new BigDecimal("100.00");
        transfer.setTransferId(3003);
        transfer.setIdFrom(1001);
        transfer.setIdTo(1002);
        transfer.setAmount(amount);
        transfer.setType("Request");
        transfer.setStatus("Pending");

        boolean actual = sut.requestTransfer(transfer);
        Assert.assertTrue(actual);
    }

    @Test
    public void approveTransfer_returns_false_given_zero_amount(){
        Transfer transfer = new Transfer();
        BigDecimal amount = new BigDecimal("00.00");
        transfer.setTransferId(3005);
        transfer.setIdFrom(1001);
        transfer.setIdTo(1002);
        transfer.setAmount(amount);
        transfer.setType("Request");
        transfer.setStatus("Pending");
        sut.requestTransfer(transfer);

        boolean actual = sut.approveTransfer(transfer);
        Assert.assertFalse(actual);
    }

    @Test
    public void approveTransfer_returns_false_given_invalid_amount(){
        Transfer transfer = new Transfer();
        BigDecimal amount = new BigDecimal("10000.00");
        transfer.setTransferId(3006);
        transfer.setIdFrom(1001);
        transfer.setIdTo(1002);
        transfer.setAmount(amount);
        transfer.setType("Request");
        transfer.setStatus("Pending");
        sut.requestTransfer(transfer);

        boolean actual = sut.approveTransfer(transfer);
        Assert.assertFalse(actual);
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
