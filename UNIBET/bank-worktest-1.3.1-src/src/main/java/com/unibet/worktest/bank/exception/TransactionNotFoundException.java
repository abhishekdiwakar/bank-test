package com.unibet.worktest.bank.exception;

import com.unibet.worktest.bank.Sealed;

/**
 * Business exception thrown if a referenced transaction does not exist.
 *
 * @author Unibet
 */
@Sealed
public class TransactionNotFoundException extends BusinessException {
	public TransactionNotFoundException(String transactionRef) {
		super("No transaction found for reference '" + transactionRef + "'");
	}
}
