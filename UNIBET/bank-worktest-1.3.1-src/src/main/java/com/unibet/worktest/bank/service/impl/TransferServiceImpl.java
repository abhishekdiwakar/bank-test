package com.unibet.worktest.bank.service.impl;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.unibet.worktest.bank.Transaction;
import com.unibet.worktest.bank.TransactionLeg;
import com.unibet.worktest.bank.TransferRequest;
import com.unibet.worktest.bank.entities.Account;
import com.unibet.worktest.bank.entities.TransactionDetails;
import com.unibet.worktest.bank.entities.TransactionLegDetails;
import com.unibet.worktest.bank.exception.InsufficientFundsException;
import com.unibet.worktest.bank.exception.UnbalancedLegsException;
import com.unibet.worktest.bank.mapper.TransactionMapper;
import com.unibet.worktest.bank.repository.AccountRepository;
import com.unibet.worktest.bank.repository.TransactionRepository;
import com.unibet.worktest.bank.service.LockManager;
import com.unibet.worktest.bank.service.TransferService;

@Service("transferService")
public class TransferServiceImpl implements TransferService {
	private static final Logger LOG = LoggerFactory.getLogger(TransferServiceImpl.class);

	private final TransactionRepository transactionRepository;
	private final AccountRepository accountRepository;
	private final TransactionMapper transactionMapper;
	private final LockManager<String> lockManager;

	@Autowired
	public TransferServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository, TransactionMapper transactionMapper, LockManager<String> lockManager) {
		this.transactionRepository = transactionRepository;
		this.accountRepository = accountRepository;
		this.transactionMapper = transactionMapper;
		this.lockManager = lockManager;
	}

	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public void transferFunds(TransferRequest transferRequest) {
		validateTransferRequest(transferRequest);
		validateTransactionLegsAreBalanced(transferRequest);

		// The lock is acquired to make sure only one account is updated at a time
		lockManager.acquireLocks(getAssociatedAccountReferences(transferRequest));
		try {
			List<TransactionLegDetails> legs = new ArrayList<>();
			for (TransactionLeg leg : transferRequest.getTransactionLegs()) {
				Account account = accountRepository.getAccount(leg.getAccountRef());
				account.updateBalance(leg.getAmount().getAmount());

				if (account.isOverdrawn()) {
					LOG.error("The account with reference {} was overdrawn",leg.getAccountRef());
					throw new InsufficientFundsException(leg.getAccountRef());
				}
				accountRepository.updateAccount(account);
				legs.add(new TransactionLegDetails(account, leg.getAmount()));
			}

			TransactionDetails transactionDetails = new TransactionDetails(transferRequest.getReference(), Calendar.getInstance().getTime(), transferRequest.getType(), legs);
			transactionRepository.saveTransaction(transactionDetails);
			LOG.info("The funds for transaction with reference {} was transferred",transactionDetails.getReference());
		}
		finally {
			lockManager.releaseLocks(getAssociatedAccountReferences(transferRequest));
		}
	}
	
	@Override
	public List<Transaction> findTransactions(String accountRef) {
		List<TransactionDetails> transactionDetails = transactionRepository.findTransactions(accountRef);
		return transactionMapper.map(transactionDetails);
	}

	@Override
	public Transaction getTransaction(String transactionRef) {
		TransactionDetails transactionDetails = transactionRepository.getTransaction(transactionRef);
		return transactionMapper.map(transactionDetails);

	}

	private Set<String> getAssociatedAccountReferences(TransferRequest transferRequest) {
		Set<String> accountReferences = new HashSet<>();
		for (TransactionLeg leg : transferRequest.getTransactionLegs()) {
			accountReferences.add(leg.getAccountRef());
		}
		LOG.debug("The accounts {} are involved in transfer of funds",accountReferences);
		
		return accountReferences;
	}

	private void validateTransferRequest(TransferRequest transferRequest) {
		if (transferRequest == null) {
			LOG.error("The transfer request received is null.");
			throw new IllegalArgumentException("Transfer Request cannot be null");
		}
		if (isEmpty(transferRequest.getTransactionLegs()) || transferRequest.getTransactionLegs().size() < 2) {
			LOG.error("Request for transaction cannot be completed as transaction legs cannot be less than 2.");
			throw new IllegalArgumentException("Transaction legs cannot be less than 2");
		}

		if (isBlank(transferRequest.getReference())) {
			LOG.error("Request for transaction cannot be completed as the transaction reference value is null or empty.");
			throw new IllegalArgumentException("Transaction reference cannot be empty");
		}
		
		LOG.debug("Tranfer request {} is valid", transferRequest.getReference());
	}

	private void validateTransactionLegsAreBalanced(TransferRequest transferRequest) {
		Map<Currency, BigDecimal> balancePerCurrency = new HashMap<>();
		for (TransactionLeg transactionLeg : transferRequest.getTransactionLegs()) {
			BigDecimal amount = transactionLeg.getAmount().getAmount();
			Currency currency = transactionLeg.getAmount().getCurrency();
			BigDecimal balance = balancePerCurrency.get(currency);
			balance = balance != null ? balance.add(amount) : amount;
			balancePerCurrency.put(currency, balance);
		}

		for (Map.Entry<Currency, BigDecimal> entry : balancePerCurrency.entrySet()) {
			if (entry.getValue().intValue() != 0) {
				LOG.error("Transaction legs are unbalanced for currency {} by amount {}", entry.getKey(), entry.getValue());
				throw new UnbalancedLegsException("The legs for currency :" + entry.getKey() + " are unbalanced by amount : " + entry.getValue());
			}
		}
		LOG.debug("Transaction legs are balanced for the transfer request with reference {}", transferRequest.getReference());
	}
}
