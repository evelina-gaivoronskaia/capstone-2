package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
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
    @Autowired
    UserDao userDao;


//    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal requestBalance(Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        return accountDao.requestBalance(userId);
    }

    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public List<Account> getAllAccounts(){
        return accountDao.getAllAccounts();
    }

}


