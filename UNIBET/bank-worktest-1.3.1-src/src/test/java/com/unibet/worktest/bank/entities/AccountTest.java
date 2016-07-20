package com.unibet.worktest.bank.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

import com.unibet.worktest.bank.Money;

public class AccountTest {

	private Account account;

	@Before
	public void setUp() {
		account = new Account("REF_1", Money.create("1000", Currency.getInstance("EUR")));
		account.setBalanceDetails();
	}

	@Test
	public void testUpdateBalance() {
		account.updateBalance(BigDecimal.valueOf(10));
		assertEquals("The amount is not updated by correct value", BigDecimal.valueOf(1010), account.getBalance().getAmount());
	}

	@Test
	public void testIsOverdrawn() {
		account.updateBalance(BigDecimal.valueOf(-1001));
		assertTrue(account.isOverdrawn());
	}

	@Test
	public void testIsNotOverdrawn() {
		account.updateBalance(BigDecimal.valueOf(-100));
		assertFalse(account.isOverdrawn());
	}
	
	@Test
	public void testToString() {
		String accountRepresentation = account.toString();
		assertEquals("Account [accountRef=" + account.getAccountRef() + ", amount=" + account.getBalance().getAmount() + ", currency=" + account.getBalance().getCurrency() + ", balance=" + account.getBalance() + "]",accountRepresentation);
	}
	
	@Test
	public void getBalanceDetails() {
		account.getBalanceDetails();
		
		assertEquals(BigDecimal.valueOf(1000),account.getBalance().getAmount());
		assertEquals("EUR",account.getBalance().getCurrency().toString());
	}

}
