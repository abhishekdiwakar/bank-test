package com.unibet.worktest.bank.exception;

/**
 * Business exception thrown if a transaction already exists.
 * 
 * @author abhishek
 * @version 1.0
 */
public class TransactionAlreadyExistsException extends BusinessException {
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 5610960851001215779L;
	
	private final String transactionRef;

	public TransactionAlreadyExistsException(String transactionRef) {
		super("Transaction already exists for reference :" + transactionRef);
		this.transactionRef = transactionRef;
	}

	public String getTransactionRef() {
		return transactionRef;
	}
}
