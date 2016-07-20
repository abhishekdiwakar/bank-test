package com.unibet.worktest.bank.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.TransactionLeg;
import com.unibet.worktest.bank.entities.Account;
import com.unibet.worktest.bank.entities.TransactionLegDetails;

public class TransactionLegMapperTest {

	private TransactionLegMapper legMapper;

	@Before
	public void setUp() throws Exception {
		legMapper = new TransactionLegMapper();
	}

	@After
	public void tearDown() throws Exception {
		legMapper = null;
	}

	@Test
	public void testMapTransactionLegDetails() {
		TransactionLegDetails leg = new TransactionLegDetails(new Account("ACC_REF", Money.create("100", "EUR")), Money.create("100", "EUR"));

		TransactionLeg transactionLeg = legMapper.map(leg);

		assertEquals(leg.getAccount().getAccountRef(), transactionLeg.getAccountRef());
		assertEquals(leg.getMoney(), transactionLeg.getAmount());
	}

}
