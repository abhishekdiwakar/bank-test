package com.unibet.worktest.bank.entities;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unibet.worktest.bank.Money;

public class TransactionLegDetailsTest {
	
	private TransactionLegDetails legDetails;

	@Before
	public void setUp(){
		legDetails = new TransactionLegDetails(new Account("ACC_REF",Money.create("1000", "EUR")), Money.create("1000", "EUR"));
		legDetails.setMoneyDetails();
	}

	@After
	public void tearDown(){
		legDetails = null;
	}

	@Test
	public void testGetMoneyDetails() {
		legDetails.getMoneyDetails();
		
		assertEquals(BigDecimal.valueOf(1000),legDetails.getMoney().getAmount());
		assertEquals("EUR",legDetails.getMoney().getCurrency().toString());
	}

	@Test
	public void testToString() {
		String legDetailsRepresentation = legDetails.toString();
		assertEquals("TransactionLegDetails [reference=" + legDetails.getTransactionLegId() + ", accountReference=" + legDetails.getAccount().getAccountRef() + ", amount=" + legDetails.getMoney().getAmount() + ", currency=" + legDetails.getMoney().getCurrency().toString()
				+ ", money=" + legDetails.getMoney() + "]",legDetailsRepresentation);
	}

}
