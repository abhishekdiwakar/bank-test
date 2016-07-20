package com.unibet.worktest.bank.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unibet.worktest.bank.Transaction;
import com.unibet.worktest.bank.TransactionLeg;
import com.unibet.worktest.bank.entities.TransactionDetails;

/**
 * The <code>TransactionMapper</code> maps the entity object to its corresponding value object.It maps a
 * transaction between two or more accounts.
 * 
 * @author abhishek
 * @version 1.0
 */
@Service
public class TransactionMapper extends AbstractMapper<TransactionDetails, Transaction> {

	private TransactionLegMapper mapper;

	@Autowired
	public TransactionMapper(TransactionLegMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public Transaction map(TransactionDetails transactionDetails) {
		List<TransactionLeg> legs = mapper.map(transactionDetails.getLegs());
		return new Transaction(transactionDetails.getReference(), transactionDetails.getType(), transactionDetails.getDate(), legs);
	}
}
