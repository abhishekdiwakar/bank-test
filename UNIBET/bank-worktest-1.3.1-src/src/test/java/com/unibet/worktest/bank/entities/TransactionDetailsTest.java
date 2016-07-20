package com.unibet.worktest.bank.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.unibet.worktest.bank.Money;

public class TransactionDetailsTest {
	
	private TransactionDetails transactionDetails;

	@Before
	public void setUp(){
		List<TransactionLegDetails> legDetails = new ArrayList<>();
		TransactionLegDetails leg = new TransactionLegDetails(new Account("ACC_REF",Money.create("1000", "EUR")),Money.create("1000", "EUR"));
		legDetails.add(leg);
		
		transactionDetails = new TransactionDetails("tx1", Calendar.getInstance().getTime(), "testing", legDetails);
	}

	@Test
	public void testHashCode() {
		int hashValue1 = transactionDetails.hashCode();
		int hashValue2 = transactionDetails.hashCode();
		
		assertEquals(hashValue1,hashValue2);
	}
	
	@Test
	public void testHashCodeWithDateAsNull() {
		List<TransactionLegDetails> legDetails = new ArrayList<>();
		TransactionLegDetails leg = new TransactionLegDetails(new Account("ACC_REF",Money.create("1000", "EUR")),Money.create("1000", "EUR"));
		legDetails.add(leg);
		
		TransactionDetails transactionDetailsWithNullDate = new TransactionDetails("tx1", null, "testing", legDetails);
		
		int hashValue1 = transactionDetailsWithNullDate.hashCode();
		int hashValue2 = transactionDetailsWithNullDate.hashCode();
		
		assertEquals(hashValue1,hashValue2);
	}
	
	@Test
	public void testHashCodeWithReferenceAsNull() {
		List<TransactionLegDetails> legDetails = new ArrayList<>();
		TransactionLegDetails leg = new TransactionLegDetails(new Account("ACC_REF",Money.create("1000", "EUR")),Money.create("1000", "EUR"));
		legDetails.add(leg);
		
		TransactionDetails transactionDetailsWithNullRefernce = new TransactionDetails(null, Calendar.getInstance().getTime(), "testing", legDetails);
		
		int hashValue1 = transactionDetailsWithNullRefernce.hashCode();
		int hashValue2 = transactionDetailsWithNullRefernce.hashCode();
		
		assertEquals(hashValue1,hashValue2);
	}

	@Test
	public void testToString() {
		String transactionRepresentation = transactionDetails.toString();
		
		assertEquals("TransactionDetails [reference=" + transactionDetails.getReference() + ", date=" + transactionDetails.getDate() + ", type=" + transactionDetails.getType() + ", transactionLegs=" + transactionDetails.getLegs() + "]",transactionRepresentation);
	}

	@Test
	public void testEqualsObjectWhenNullIsPassed() {
		assertFalse(transactionDetails.equals(null));
	}
	
	@Test
	public void testEqualsObjectWhenWithDifferentInstance() {
		assertFalse(transactionDetails.equals(new TransactionLegDetails()));
	}
	
	@Test
	public void testEqualsObjectWhenDifferentReferenceButSameContents() {
		List<TransactionLegDetails> legDetails = new ArrayList<>();
		TransactionLegDetails leg = new TransactionLegDetails(new Account("ACC_REF",Money.create("1000", "EUR")),Money.create("1000", "EUR"));
		legDetails.add(leg);
		
		TransactionDetails transactionDetailsNew = new TransactionDetails("tx1", transactionDetails.getDate(), "testing", legDetails);
		
		assertTrue(transactionDetails.equals(transactionDetailsNew));
	}
	
	@Test
	public void testEqualsObjectWhenDifferentReference() {
		List<TransactionLegDetails> legDetails = new ArrayList<>();
		TransactionLegDetails leg = new TransactionLegDetails(new Account("ACC_REF",Money.create("1001", "EUR")),Money.create("1000", "EUR"));
		legDetails.add(leg);
		
		TransactionDetails transactionDetailsNew = new TransactionDetails("tx2", Calendar.getInstance().getTime(), "testing", legDetails);
		
		assertFalse(transactionDetails.equals(transactionDetailsNew));
	}
	
	@Test
	public void testEqualsObjectForDifferentDateValues() {
		// given
		List<TransactionLegDetails> legDetails = new ArrayList<>();
		TransactionLegDetails leg = new TransactionLegDetails(new Account("ACC_REF",Money.create("1001", "EUR")),Money.create("1000", "EUR"));
		legDetails.add(leg);
		
		// when date is null
		TransactionDetails transactionDetailsNew = new TransactionDetails("tx2", null, "testing", legDetails);
		TransactionDetails transactionDetailsNew1 = new TransactionDetails("tx2", null, "testing", legDetails);
		
		// then
		assertFalse(transactionDetails.equals(transactionDetailsNew));
		assertTrue(transactionDetailsNew1.equals(transactionDetailsNew));
		assertFalse(transactionDetailsNew1.equals(transactionDetails));
		
		// when date is different
		transactionDetailsNew1 = new TransactionDetails("tx2", Calendar.getInstance().getTime(), "testing", legDetails);
		
		// then
		assertFalse(transactionDetails.equals(transactionDetailsNew1));
	}
	
	@Test
	public void testEqualsObjectForDifferentReferenceValues() {
		// given
		List<TransactionLegDetails> legDetails = new ArrayList<>();
		TransactionLegDetails leg = new TransactionLegDetails(new Account("ACC_REF",Money.create("1001", "EUR")),Money.create("1000", "EUR"));
		legDetails.add(leg);
		
		// when reference is null
		TransactionDetails transactionDetailsNew = new TransactionDetails(null, Calendar.getInstance().getTime(), "testing", legDetails);
		
		// when reference is different
		TransactionDetails transactionDetailsNew1 = new TransactionDetails(null, transactionDetailsNew.getDate(), "testing", legDetails);
		
		// then
		assertFalse(transactionDetailsNew.equals(transactionDetails));
		assertTrue(transactionDetailsNew1.equals(transactionDetailsNew));		
	}
	
	@Test
	public void testEqualsObjectWhenSameReference() {
		assertTrue(transactionDetails.equals(transactionDetails));
	}

}
