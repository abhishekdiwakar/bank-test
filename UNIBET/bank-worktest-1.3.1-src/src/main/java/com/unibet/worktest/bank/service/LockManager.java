package com.unibet.worktest.bank.service;

import java.util.Collection;

/**
 * Manages the different state of locks associated with a list of references.It
 * makes sure that the same reference is accessed synchronously by different
 * users. At a time only a single user can update the reference information.
 * 
 * @author abhishek
 * @version 1.0
 *
 */
public interface LockManager<T> {

	/**
	 * Acquires and saves the locks associated with a list of references.
	 * 
	 * 
	 * @param references
	 *            all the references which needs to be modified.
	 */
	public void acquireLocks(Collection<T> references);

	/**
	 * Releases the locks associated with a list of references.
	 * 
	 * @param references
	 *            all the references which needs to be released.
	 */

	public void releaseLocks(Collection<T> references);

}
