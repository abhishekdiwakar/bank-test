package com.unibet.worktest.bank.repository;

import java.util.List;

import com.unibet.worktest.bank.entities.TransactionDetails;

/**
 * Defines the business logic for performing monetary transactions between
 * two or more accounts.
 *
 * @author abhishek
 * @version 1.0
 */
public interface TransactionRepository {

	/**
	 * Provides a list of transactions related to a given account.
	 * 
	 * @param accountRef the reference value associated with an account. 
	 * @return list containing all the fetched transactions associated with the given account.
	 * @throws com.unibet.worktest.bank.exception.InfrastructureException 
	 * 			if there are unrecoverable infrastructure errors.
	 */
	List<TransactionDetails> findTransactions(String accountRef);

	/**
	 * Provides transaction details for a particular transaction
	 * 
	 * @param transactionRef  reference associated with a given transaction.
	 * @return the fetched transaction details
	 * @throws com.unibet.worktest.bank.exception.TransactionNotFoundException 
	 * 		  	if there is no transaction record associated with the given transaction reference.
	 */
	TransactionDetails getTransaction(String transactionRef);

	/**
	 * Saves the transaction details into the persistent storage. 
	 * 
	 * @param transactionDetails  details associated with a given transaction.
	 * @throws com.unibet.worktest.bank.exception.TransactionAlreadyExistsException 
	 * 			if there is already a transaction with the given transaction details.
	 * @throws com.unibet.worktest.bank.exception.InfrastructureException 
	 * 			if there is a problem in saving the transaction details.
	 */
	void saveTransaction(TransactionDetails transactionDetails);
}
