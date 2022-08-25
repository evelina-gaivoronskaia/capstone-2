package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("permitAll")
public class UserController {

    private final String API_PATH = "http://localhost:8080/";
    @Autowired
    AccountDao dao;

//    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/balance/{id}", method = RequestMethod.GET)
    public BigDecimal requestBalance(@PathVariable int id){
        return dao.requestBalance(id);
    }

    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public List<Account> getAllAccounts(){
        return dao.getAllAccounts();
    }

/*
    @ResponseStatus(HttpStatus.)
*/

    @RequestMapping(path = "/transfer/{idFrom}/{isTo}", method = RequestMethod.PUT)
    public boolean transferBucks(@PathVariable int idFrom, int idTo,
                                 @RequestBody BigDecimal amount){
        return dao.transferBucks(idFrom, idTo, amount);
        // we are gonna need to rework this to recognize the logged-in user and using that to provide the idFrom
        // therefore, not passing in an idFrom int, and just using the idTo int as a pathVariable ???
        //Problem ~ our path PUT returns 404, 400, 500/ "URI" inconsistency of path variable in request url
    }
}


