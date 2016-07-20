package com.unibet.worktest.bank.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.entities.Account;
import com.unibet.worktest.bank.exception.AccountAlreadyExistsException;
import com.unibet.worktest.bank.exception.AccountNotFoundException;
import com.unibet.worktest.bank.exception.InfrastructureException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class AccountRepositoryImplTest {

	private static final String ACCOUNT_REF = "REF_1";

	@Autowired
	private AccountRepository accountRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@Transactional
	public void testCreateAccount() {
		accountRepository.createAccount(ACCOUNT_REF, Money.create("1000", "EUR"));
		assertNotNull(entityManager.find(Account.class, ACCOUNT_REF));
	}

	@Test(expected = AccountAlreadyExistsException.class)
	@Transactional
	public void testCreateAccountWhenAccountAlreadyExists() {
		accountRepository.createAccount(ACCOUNT_REF, Money.create("1000", "EUR"));
		assertNotNull(entityManager.find(Account.class, ACCOUNT_REF));

		accountRepository.createAccount(ACCOUNT_REF, Money.create("90", "EUR"));
	}

	@Test(expected = InfrastructureException.class)
	public void testCreateAccountWhenNoTransactionExists() {
		accountRepository.createAccount(ACCOUNT_REF, Money.create("1000", "EUR"));
		assertNotNull(entityManager.find(Account.class, ACCOUNT_REF));
	}

	@Test(expected = AccountNotFoundException.class)
	@Transactional
	public void testAccountNotFound() {
		accountRepository.getAccount("REF_2");
	}

	@Test
	@Transactional
	public void testGetAccount() {
		accountRepository.createAccount(ACCOUNT_REF, Money.create("1000", "EUR"));
		assertNotNull(accountRepository.getAccount(ACCOUNT_REF));
	}

	@Test
	@Transactional
	public void testUpdateAccount() {
		// given
		accountRepository.createAccount(ACCOUNT_REF, Money.create("1000", "EUR"));
		Account account = accountRepository.getAccount(ACCOUNT_REF);
		account.updateBalance(BigDecimal.valueOf(10));

		// when
		accountRepository.updateAccount(account);

		// then
		Account updatedAccount = accountRepository.getAccount(ACCOUNT_REF);
		assertNotNull(updatedAccount);
		assertEquals(BigDecimal.valueOf(1010), updatedAccount.getBalance().getAmount());
	}

	@Test(expected = InfrastructureException.class)
	public void testUpdateAccountWithNoTransaction() {
		// given
		accountRepository.createAccount(ACCOUNT_REF, Money.create("1000", "EUR"));
		Account account = accountRepository.getAccount(ACCOUNT_REF);
		account.updateBalance(BigDecimal.valueOf(10));

		// when
		accountRepository.updateAccount(account);
	}
}
