package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface AccountDao {

    BigDecimal requestBalance(int userId);

    List<Account> getAllAccounts();

    int createAccount(int userId);
}
