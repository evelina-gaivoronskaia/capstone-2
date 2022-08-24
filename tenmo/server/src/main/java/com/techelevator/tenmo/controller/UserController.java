package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@PreAuthorize("permitAll")
public class UserController {

    private final String API_PATH = "http://localhost:8080/";
    @Autowired
    AccountDao dao;

    @RequestMapping(path = "/balance/{id}", method = RequestMethod.GET)
    public BigDecimal requestBalance(@PathVariable int id){
        return dao.requestBalance(id);
    }
}
