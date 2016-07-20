package com.unibet.worktest.bank.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import com.unibet.worktest.bank.entities.TransactionDetails;
import com.unibet.worktest.bank.entities.TransactionLegDetails;
import com.unibet.worktest.bank.exception.InfrastructureException;
import com.unibet.worktest.bank.exception.TransactionAlreadyExistsException;
import com.unibet.worktest.bank.exception.TransactionNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TransactionRepositoryImplTest {

	private static final String TRANSACTION_REF = "REFR";
	private static final String ACCOUNT_REF = "REF_0";
	private static final String ACCOUNT_REF1 = "REF_1";

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@Transactional
	public void testFindAllTransactionsByAccountRef() {
		TransactionDetails transaction = createTransactionDetails();
		entityManager.persist(transaction);

		// when
		List<TransactionDetails> transactions = transactionRepository.findTransactions(ACCOUNT_REF);

		// then
		assertNotNull(transactions);
		assertEquals(1, transactions.size());
		assertEquals(transaction, transactions.get(0));
	}
	
	@Test(expected = InfrastructureException.class)
	public void testFindAllTransactionsByAccountRefWhenNoTransaction() {
		accountRepository.createAccount(ACCOUNT_REF, Money.create("1000", "EUR"));

		// when
		transactionRepository.findTransactions(ACCOUNT_REF);
	}
	
	@Test(expected = TransactionNotFoundException.class)
	@Transactional
	public void testTransactionNotFound() {
		transactionRepository.getTransaction("REF_2");
	}

	@Test
	@Transactional
	public void testGetTransaction() {
		// given
		TransactionDetails transaction = createTransactionDetails();
		entityManager.persist(transaction);
		
		// when
		assertEquals(transaction, transactionRepository.getTransaction(TRANSACTION_REF));
	}
	
	@Test
	@Transactional
	public void testSaveTransaction() {
		//given
		TransactionDetails transaction = createTransactionDetails();
		
		//when
		transactionRepository.saveTransaction(transaction);
		
		//then
		assertEquals(transaction, entityManager.find(TransactionDetails.class, TRANSACTION_REF));
	}
	
	@Test(expected = InfrastructureException.class)
	public void testSaveTransactionWhenNoTransaction() {
		//given
		TransactionDetails transaction = createTransactionDetails();
		
		//when
		transactionRepository.saveTransaction(transaction);
	}
	
	@Test(expected = TransactionAlreadyExistsException.class)
	@Transactional
	public void testSaveTransactionWhenTransactionAlreadyExists() {
		//given
		transactionRepository.saveTransaction(createTransactionDetails());
		
		//when
		Account account = accountRepository.getAccount(ACCOUNT_REF);
		Account account1 = accountRepository.getAccount(ACCOUNT_REF1);

		List<TransactionLegDetails> legs = new ArrayList<>();
		legs.add(new TransactionLegDetails(account, Money.create("100", "EUR")));
		legs.add(new TransactionLegDetails(account1, Money.create("-100", "EUR")));

		TransactionDetails transaction = new TransactionDetails(TRANSACTION_REF, Calendar.getInstance().getTime(), null, legs);
		transactionRepository.saveTransaction(transaction);
	}

	private TransactionDetails createTransactionDetails() {
		accountRepository.createAccount(ACCOUNT_REF, Money.create("1000", "EUR"));
		accountRepository.createAccount(ACCOUNT_REF1, Money.create("1000", "EUR"));
		Account account = accountRepository.getAccount(ACCOUNT_REF);
		Account account1 = accountRepository.getAccount(ACCOUNT_REF1);

		List<TransactionLegDetails> legs = new ArrayList<>();
		legs.add(new TransactionLegDetails(account, Money.create("100", "EUR")));
		legs.add(new TransactionLegDetails(account1, Money.create("-100", "EUR")));

		TransactionDetails transaction = new TransactionDetails(TRANSACTION_REF, Calendar.getInstance().getTime(), null, legs);
		return transaction;
	}
}
