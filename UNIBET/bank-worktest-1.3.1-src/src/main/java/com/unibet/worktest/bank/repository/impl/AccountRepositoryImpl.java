package com.unibet.worktest.bank.repository.impl;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.entities.Account;
import com.unibet.worktest.bank.exception.AccountAlreadyExistsException;
import com.unibet.worktest.bank.exception.AccountNotFoundException;
import com.unibet.worktest.bank.exception.InfrastructureException;
import com.unibet.worktest.bank.repository.AccountRepository;

@Repository
public class AccountRepositoryImpl implements AccountRepository {
	private static final Logger LOG = LoggerFactory.getLogger(AccountRepositoryImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void createAccount(String accountRef, Money amount) {
		try {
			Account account = new Account(accountRef, amount);
			entityManager.persist(account);
			entityManager.flush();
			LOG.info("Account with reference {} was created.", accountRef);
		} catch (EntityExistsException exception) {
			LOG.error(exception.getMessage());
			throw new AccountAlreadyExistsException(accountRef);
		} catch (PersistenceException exception) {
			LOG.error(exception.getMessage());
			throw new InfrastructureException(exception.getMessage());
		}
	}

	@Override
	public Account getAccount(String accountRef) {
		Account account = entityManager.find(Account.class, accountRef);

		if (account == null) {
			LOG.error("Account with reference {} was not found.", accountRef);
			throw new AccountNotFoundException("Account with id = " + accountRef + "not found");
		}
		LOG.info("Account with reference {} was fetched", account.getAccountRef());
		
		return account;
	}

	@Override
	public void updateAccount(Account account) {
		try {
			entityManager.merge(account);
			LOG.info("Account with reference {} updated", account.getAccountRef());
		} catch (TransactionRequiredException exception) {
			LOG.error(exception.getMessage());
			throw new InfrastructureException(exception.getMessage());
		}
	}
}