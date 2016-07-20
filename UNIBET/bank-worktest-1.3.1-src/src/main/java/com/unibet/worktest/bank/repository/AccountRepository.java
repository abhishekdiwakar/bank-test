package com.unibet.worktest.bank.repository;

import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.entities.Account;

/**
 * The <code>AccountRepository</code> defines the logic to handle account related operations and to persist the
 * information in the data storage.
 * 
 * @author abhishek
 * @version 1.0
 *
 */
public interface AccountRepository {

	/**
	 * Creates the account with given information and stores it in a persistent
	 * storage.
	 * 
	 * @param accountRef
	 *            the reference associated with a given account.
	 * @param amount
	 *            the initial balance for a given account.
	 * @throws com.unibet.worktest.bank.exception.AccountAlreadyExistsException
	 *             if the account already exists for the passed details.
	 * @throws com.unibet.worktest.bank.exception.InfrastructureException
	 *             if there is a problem in persisting the account details for new Account.
	 *         
	 */
	public void createAccount(String accountRef, Money amount);

	/**
	 * Retrieves the account details for a given account reference.
	 * 
	 * @param accountRef
	 *            the reference of a particular account.
	 * @return the account details.
	 * @throws com.unibet.worktest.bank.exception.AccountNotFoundException
	 *             if there are no account details for the given account
	 *             reference.
	 */
	public Account getAccount(String accountRef);

	
	/**
	 * Updates the given account.
	 * 
	 * @param account 
	 * 			the account information to be updated.
	 * @throws com.unibet.worktest.bank.exception.InfrastructureException 
	 * 			if there are no active transactions.
	 */
	public void updateAccount(Account account);

}
