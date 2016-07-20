package com.unibet.worktest.bank.mapper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.Transaction;
import com.unibet.worktest.bank.TransactionLeg;
import com.unibet.worktest.bank.entities.Account;
import com.unibet.worktest.bank.entities.TransactionDetails;
import com.unibet.worktest.bank.entities.TransactionLegDetails;

public class TransactionMapperTest {

	private TransactionMapper mapper;
	private TransactionLegMapper legMapper;

	@Before
	public void setUp() {
		legMapper = mock(TransactionLegMapper.class);
		mapper = new TransactionMapper(legMapper);
	}

	@Test
	public void testMapTransactionDetails() {
		List<TransactionLegDetails> legs = new ArrayList<>();
		TransactionLegDetails legDetails = new TransactionLegDetails(new Account("ACC_REF", Money.create("1000", "EUR")), Money.create("1000", "EUR"));
		legs.add(legDetails);
		when(legMapper.map(legs)).thenReturn(Collections.singletonList(new TransactionLeg("ACC_REF", Money.create("5", "EUR"))));
		TransactionDetails details = new TransactionDetails("tx1", Calendar.getInstance().getTime(), null, legs);

		Transaction transaction = mapper.map(details);

		assertEquals(details.getReference(), transaction.getReference());
		assertEquals(details.getDate(), transaction.getDate());
		assertEquals(details.getType(), transaction.getType());
		assertEquals(details.getLegs().size(), transaction.getLegs().size());
	}
}
