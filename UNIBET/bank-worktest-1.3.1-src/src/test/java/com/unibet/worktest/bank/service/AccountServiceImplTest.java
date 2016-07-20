package com.unibet.worktest.bank.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.entities.Account;
import com.unibet.worktest.bank.repository.AccountRepository;
import com.unibet.worktest.bank.service.impl.AccountServiceImpl;

public class AccountServiceImplTest {

	private static final String ACCOUNT_REF = "REF";

	private AccountService accountService;

	private AccountRepository accountRepository;

	@Before
	public void setUp() throws Exception {
		accountRepository = mock(AccountRepository.class);
		accountService = new AccountServiceImpl(accountRepository);
	}

	@Test
	public void testCreateAccount() {
		//given
		Money money = Money.create("1000", "EUR");
		
		// when
		accountService.createAccount(ACCOUNT_REF, money);

		// then
		verify(accountRepository).createAccount(ACCOUNT_REF, money);
	}

	@Test
	public void testGetAccountBalance() {
		// given
		Money balance = Money.create("1000", "EUR");
		when(accountRepository.getAccount(ACCOUNT_REF)).thenReturn(new Account(ACCOUNT_REF,balance));
		
		// when
		Money accountBalance = accountService.getAccountBalance(ACCOUNT_REF);

		// then
		verify(accountRepository).getAccount(ACCOUNT_REF);
		assertEquals(balance, accountBalance);

	}

}
