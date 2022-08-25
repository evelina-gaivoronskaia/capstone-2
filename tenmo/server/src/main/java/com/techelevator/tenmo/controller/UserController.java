package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final String API_PATH = "http://localhost:8080/";
    @Autowired
    AccountDao accountDao;


//    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/balance/{id}", method = RequestMethod.GET)
    public BigDecimal requestBalance(@PathVariable int id){
        return accountDao.requestBalance(id);
    }

    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public List<Account> getAllAccounts(){
        return accountDao.getAllAccounts();
    }

/*
    @ResponseStatus(HttpStatus.)
*/

    /*@RequestMapping(path = "/transfer", method = RequestMethod.PUT)
    public boolean transferBucks(@RequestBody Transfer transfer){
        return accountDao.transferBucks(transfer);*/
        // we are  need to rework this to recognize the logged-in user and using that to provide the idFrom
        // therefore, not passing in an idFrom int, and just using the idTo int as a pathVariable ???
        //Problem ~ our path PUT returns 404, 400, 500/ "URI" inconsistency of path variable in request url

}


