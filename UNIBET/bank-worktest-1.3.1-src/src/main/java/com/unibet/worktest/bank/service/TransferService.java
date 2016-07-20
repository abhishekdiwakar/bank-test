package com.unibet.worktest.bank.service;

import java.util.List;

import com.unibet.worktest.bank.Sealed;
import com.unibet.worktest.bank.Transaction;
import com.unibet.worktest.bank.TransferRequest;

/**
 * Defines the business logic for performing monetary transactions between accounts.
 *
 * @author Unibet
 */
@Sealed
public interface TransferService {
    /**
     * Executes a balanced, multi-legged monetary transaction as a single unit of work.
     *
     * @param transferRequest a transfer request describing the transactions
     * @throws IllegalArgumentException if the request has less than two legs or any other missing
     * or malformed properties
     * @throws com.unibet.worktest.bank.exception.AccountNotFoundException if a referenced account does not exist
     * @throws com.unibet.worktest.bank.exception.InsufficientFundsException if a referenced account is overdrawn
     * @throws com.unibet.worktest.bank.exception.UnbalancedLegsException if the transaction legs are unbalanced (sum is not zero)
     * @throws com.unibet.worktest.bank.exception.BusinessException if there are any other problems transfering funds
     */
    void transferFunds(TransferRequest transferRequest);

    /**
     * Finds all monetary transactions performed towards a given account.
     *
     * @param accountRef the client defined account reference to find transactions for
     * @return list of transactions or an empty list if none is found
     * @throws com.unibet.worktest.bank.exception.BusinessException if there are any problems retrieving the transactions
     */
    List<Transaction> findTransactions(String accountRef);

    /**
     * Get a given transaction by reference.
     *
     * @param transactionRef the transaction reference
     * @return the transaction or {@code null} if it doesnt exist
     * @throws com.unibet.worktest.bank.exception.BusinessException if there are any problems retrieving the transaction
     */
    Transaction getTransaction(String transactionRef);
}
