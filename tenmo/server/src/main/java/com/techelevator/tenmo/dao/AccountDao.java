package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal requestBalance(int userId);

    boolean transferBucks(int idFrom, int idTo, BigDecimal amount);
}
