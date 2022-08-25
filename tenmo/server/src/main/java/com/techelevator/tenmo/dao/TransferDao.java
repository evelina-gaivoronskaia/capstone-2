package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.security.Principal;
import java.util.List;

public interface TransferDao {

    boolean transferBucks(Transfer transfer);

    List<Transfer> getMyTransfers(int userId);
}
