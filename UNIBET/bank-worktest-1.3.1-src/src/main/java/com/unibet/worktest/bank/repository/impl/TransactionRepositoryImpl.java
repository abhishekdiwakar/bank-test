package com.unibet.worktest.bank.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.LockTimeoutException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.unibet.worktest.bank.entities.TransactionDetails;
import com.unibet.worktest.bank.exception.InfrastructureException;
import com.unibet.worktest.bank.exception.TransactionAlreadyExistsException;
import com.unibet.worktest.bank.exception.TransactionNotFoundException;
import com.unibet.worktest.bank.repository.TransactionRepository;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
	public static final String FIND_TRANSACTIONS_QUERY = "select td from TransactionDetails td join td.legs tl join tl.account acc where acc.accountRef = :accountRef";
	private static final Logger LOG = LoggerFactory.getLogger(TransactionRepositoryImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<TransactionDetails> findTransactions(String accountRef) {
		List<TransactionDetails> transactionDetails = new ArrayList<TransactionDetails>();
		
		try {
			transactionDetails = entityManager.createQuery(FIND_TRANSACTIONS_QUERY)
											  .setParameter("accountRef", accountRef)
											  .getResultList();
			LOG.info("Fetched transaction details for account reference : {} ", accountRef);
		} catch (QueryTimeoutException | TransactionRequiredException | PessimisticLockException | LockTimeoutException exception) {
			LOG.error(exception.getMessage());
			throw new InfrastructureException(exception.getMessage());
		}
		
		return transactionDetails;
	}

	@Override
	public TransactionDetails getTransaction(String transactionRef) {
		TransactionDetails transactionDetails = entityManager.find(TransactionDetails.class, transactionRef);

		if (transactionDetails == null) {
			LOG.error("Transaction with reference {} was not found.", transactionRef);
			throw new TransactionNotFoundException(transactionRef);
		}
		LOG.info("Transaction details are fetched for transaction reference {} ", transactionRef);
		
		return transactionDetails;
	}

	@Override
	public void saveTransaction(TransactionDetails transactionDetails) {
		try {
			entityManager.persist(transactionDetails);
			entityManager.flush();
			LOG.info("Transaction with reference {} was created.", transactionDetails.getReference());
		} catch (EntityExistsException exception) {
			LOG.error(exception.getMessage());
			throw new TransactionAlreadyExistsException(transactionDetails.getReference());
		} catch (PersistenceException exception) {
			LOG.error(exception.getMessage());
			throw new InfrastructureException(exception.getMessage());
		}
	}
}
