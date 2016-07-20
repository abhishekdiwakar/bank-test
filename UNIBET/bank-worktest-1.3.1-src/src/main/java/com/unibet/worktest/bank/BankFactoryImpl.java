package com.unibet.worktest.bank;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.unibet.worktest.bank.service.AccountService;
import com.unibet.worktest.bank.service.TransferService;

@Component
public class BankFactoryImpl implements ApplicationContextAware, BankFactory {

	private static ApplicationContext applicationContext = null;

	@Override
	public AccountService getAccountService() {
		return (AccountService) applicationContext.getBean("accountService");
	}

	@Override
	public TransferService getTransferService() {
		return (TransferService) applicationContext.getBean("transferService");
	}

	@Override
	public void setupInitialData() {
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
