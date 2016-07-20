package com.unibet.worktest.bank.exception;

/**
 * Business exception thrown if a referenced account already exist.
 * 
 * @author abhishek
 * @version 1.0
 */
public class AccountAlreadyExistsException extends BusinessException {
	
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 7151489383170428293L;
	
	private final String accountRef;

	public AccountAlreadyExistsException(String accountRef) {
		super("Account already exists for reference :" + accountRef);
		this.accountRef = accountRef;
	}

	public String getAccountRef() {
		return accountRef;
	}
}
