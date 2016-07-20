package com.unibet.worktest.bank.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.TransferRequest;
import com.unibet.worktest.bank.entities.Account;
import com.unibet.worktest.bank.entities.TransactionDetails;
import com.unibet.worktest.bank.exception.InsufficientFundsException;
import com.unibet.worktest.bank.exception.UnbalancedLegsException;
import com.unibet.worktest.bank.mapper.TransactionMapper;
import com.unibet.worktest.bank.repository.AccountRepository;
import com.unibet.worktest.bank.repository.TransactionRepository;
import com.unibet.worktest.bank.service.impl.TransferServiceImpl;

public class TransferServiceImplTest {
	private static final String ACCOUNT_REF = "ACC1";
	private static final String TRANSACTION_REF = "TX1";
	private static final String CASH_ACCOUNT = "cash:1:EUR";
	private static final String DEPOSIT_ACCOUNT = "deposit:1:EUR";

	private TransferService transferService;
	private TransactionRepository transactionRepository;
	private AccountRepository accountRepository;
	private TransactionMapper transactionMapper;
	private LockManager<String> lockManager;

	@Before
	public void setUp() throws Exception {
		transactionRepository = mock(TransactionRepository.class);
		accountRepository = mock(AccountRepository.class);
		transactionMapper = mock(TransactionMapper.class);
		lockManager = mock(LockManager.class);

		transferService = new TransferServiceImpl(transactionRepository, accountRepository, transactionMapper,lockManager);
	}

	@Test
	public void testTransferFunds() {
		// given
		TransferRequest transferRequest = TransferRequest.builder()
											.reference("tx1")
											.account(CASH_ACCOUNT)
											.amount(Money.create("-5.00", "EUR"))
											.account(DEPOSIT_ACCOUNT)
											.amount(Money.create("5.00", "EUR"))
											.build();
		
		Account account1 = mock(Account.class); 
		when(account1.getAccountRef()).thenReturn(CASH_ACCOUNT);
		when(account1.isOverdrawn()).thenReturn(false);
		when(account1.getBalance()).thenReturn(Money.create("1000", "EUR"));
		
		Account account2 = mock(Account.class); 
		when(account2.getAccountRef()).thenReturn(DEPOSIT_ACCOUNT);
		when(account2.isOverdrawn()).thenReturn(false);
		when(account2.getBalance()).thenReturn(Money.create("1000", "EUR"));
	

		when(accountRepository.getAccount(CASH_ACCOUNT)).thenReturn(account1);
		when(accountRepository.getAccount(DEPOSIT_ACCOUNT)).thenReturn(account2);
		
		// when
		transferService.transferFunds(transferRequest);

		// then
		verify(accountRepository).updateAccount(account1);
		verify(accountRepository).updateAccount(account2);
		verify(lockManager).acquireLocks(anyCollectionOf(String.class));
		verify(lockManager).releaseLocks(anyCollectionOf(String.class));
		
		ArgumentCaptor<TransactionDetails> captor = ArgumentCaptor.forClass(TransactionDetails.class);
		verify(transactionRepository).saveTransaction(captor.capture());
		
		TransactionDetails transactionDetails = captor.getValue();
		assertEquals(transferRequest.getReference(),transactionDetails.getReference());
		assertEquals(transferRequest.getType(),transactionDetails.getType());
		assertEquals(transferRequest.getTransactionLegs().size(), transactionDetails.getLegs().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTransferFundsForNullRequest() {
		// when
		transferService.transferFunds(null);
	}

	@Test(expected = UnbalancedLegsException.class)
	public void testTransferFundsForUnbalancedLegs() {
		// given
		TransferRequest transferRequest = TransferRequest.builder().reference("tx1").account(CASH_ACCOUNT).amount(Money.create("-5.00", "EUR")).account(DEPOSIT_ACCOUNT)
				.amount(Money.create("5.00", "EUR")).account(CASH_ACCOUNT).amount(Money.create("15.00", "EUR")).account(DEPOSIT_ACCOUNT).amount(Money.create("5.00", "EUR"))
				.build();
		// when
		transferService.transferFunds(transferRequest);
	}
	
	
	@Test(expected = InsufficientFundsException.class)
	public void testTransferFundsWhenAccountIsOverdrawn() {
		// given
		TransferRequest transferRequest = TransferRequest.builder()
				.reference("tx1")
				.account(CASH_ACCOUNT).amount(Money.create("-1010.00", "EUR"))
				.account(DEPOSIT_ACCOUNT).amount(Money.create("1010.00", "EUR"))
				.build();
		
		Account account1 = mock(Account.class); 
		when(account1.getAccountRef()).thenReturn(CASH_ACCOUNT);
		when(account1.isOverdrawn()).thenReturn(true);
		
		Account account2 = mock(Account.class); 
		
		when(accountRepository.getAccount(CASH_ACCOUNT)).thenReturn(account1);
		when(accountRepository.getAccount(DEPOSIT_ACCOUNT)).thenReturn(account2);
		
		// when
		transferService.transferFunds(transferRequest);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTransferFundsForNullReference() {
		// given
		TransferRequest transferRequest = TransferRequest.builder().reference(null).account(CASH_ACCOUNT).amount(Money.create("-1010.00", "EUR")).account(DEPOSIT_ACCOUNT)
				.amount(Money.create("1010.00", "EUR")).account(CASH_ACCOUNT).amount(Money.create("15.00", "EUR")).account(DEPOSIT_ACCOUNT).amount(Money.create("-15.00", "EUR"))
				.build();
		// when
		transferService.transferFunds(transferRequest);
	}

	@Test
	public void testFindTransactions() {
		// when
		transferService.findTransactions(ACCOUNT_REF);

		// then
		verify(transactionRepository).findTransactions(ACCOUNT_REF);
		verify(transactionMapper).map(anyListOf(TransactionDetails.class));
	}

	@Test
	public void testGetTransaction() {
		// when
		transferService.getTransaction(TRANSACTION_REF);

		// then
		verify(transactionRepository).getTransaction(TRANSACTION_REF);
		verify(transactionMapper).map(transactionRepository.getTransaction(TRANSACTION_REF));
	}
}
