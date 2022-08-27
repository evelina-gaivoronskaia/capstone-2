package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    boolean transferBucks(Transfer transfer);

    List<Transfer> getMyTransfers(int userId);

    List<Transfer> listPendingTransfersByUserId(int userId);

    Transfer getTransferByTransferId(int transferId);

    boolean requestTransfer(Transfer transfer);

    boolean approveTransfer(Transfer transfer);

    boolean rejectTransfer(Transfer transfer);
}
