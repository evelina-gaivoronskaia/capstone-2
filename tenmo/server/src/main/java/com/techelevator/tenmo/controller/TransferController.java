package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private final String API_PATH_TRANSFER = "http://localhost:8080/";
    @Autowired
    TransferDao transferDao;

    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public boolean transferBucks(@RequestBody Transfer transfer){
        return transferDao.transferBucks(transfer);
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.GET)
    public List<Transfer> getMyTransfers(@RequestBody int userId){
        return transferDao.getMyTransfers(userId);
    }

}
