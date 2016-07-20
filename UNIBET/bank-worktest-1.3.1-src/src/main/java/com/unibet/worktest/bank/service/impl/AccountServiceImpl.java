package com.unibet.worktest.bank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.repository.AccountRepository;
import com.unibet.worktest.bank.service.AccountService;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	@Autowired
	public AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	@Transactional
	public void createAccount(String accountRef, Money amount) {
		accountRepository.createAccount(accountRef, amount);

	}

	@Override
	public Money getAccountBalance(String accountRef) {
		return accountRepository.getAccount(accountRef).getBalance();
	}

}
