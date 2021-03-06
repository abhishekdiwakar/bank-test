package com.unibet.worktest.bank.exception;

import com.unibet.worktest.bank.Sealed;

/**
 * Business exception thrown if a referenced account does not exist.
 *
 * @author Unibet
 */
@Sealed
public class AccountNotFoundException extends BusinessException {
    public AccountNotFoundException(String accountRef) {
        super("No account found for reference '" + accountRef + "'");
    }
}
