package com.unibet.worktest.bank.mapper;

import org.springframework.stereotype.Service;

import com.unibet.worktest.bank.TransactionLeg;
import com.unibet.worktest.bank.entities.TransactionLegDetails;

/**
 * The <code>TransactionLegMapper</code> maps the transaction leg entity object to its corresponding value object. 
 * 
 * @author abhishek
 * @version 1.0
 */
@Service
public class TransactionLegMapper extends AbstractMapper<TransactionLegDetails, TransactionLeg> {

	@Override
	public TransactionLeg map(TransactionLegDetails leg) {
		return new TransactionLeg(leg.getAccount().getAccountRef(), leg.getMoney());
	}

}
