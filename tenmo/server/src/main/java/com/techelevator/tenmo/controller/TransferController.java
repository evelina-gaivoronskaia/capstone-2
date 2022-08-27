package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private final String API_PATH_TRANSFER = "http://localhost:8080/";
    @Autowired
    TransferDao transferDao;
    @Autowired
    UserDao userDao;

    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public boolean transferBucks(@RequestBody Transfer transfer){
        return transferDao.transferBucks(transfer);
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.GET)
    public List<Transfer> getMyTransfers(Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        return transferDao.getMyTransfers(userId);
    }

    @RequestMapping(path = "/transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferByTransferId(@PathVariable int transferId){
        return transferDao.getTransferByTransferId(transferId);
    }

    @RequestMapping(path = "/transfer/request", method = RequestMethod.POST)
    public boolean requestTransfer(@RequestBody Transfer transfer){
        return transferDao.requestTransfer(transfer);
    }

    @RequestMapping(path = "/transfer/approve", method = RequestMethod.POST)
    public boolean approveTransfer(@RequestBody Transfer transfer){
        return transferDao.approveTransfer(transfer);
    }

    @RequestMapping(path = "/transfer/pending", method = RequestMethod.GET)
    public List<Transfer> listPendingTransfersByUserId(Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        return transferDao.listPendingTransfersByUserId(userId);
    }

    @RequestMapping(path = "/transfer/reject", method = RequestMethod.POST)
    public boolean rejectTransfer(@RequestBody Transfer transfer){
        return transferDao.rejectTransfer(transfer);
    }

}
