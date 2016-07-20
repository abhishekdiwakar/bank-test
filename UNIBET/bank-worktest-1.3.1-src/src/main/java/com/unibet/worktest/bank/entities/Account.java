package com.unibet.worktest.bank.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import com.unibet.worktest.bank.Money;

/**
 * Represents an entity to store account related details.
 * 
 * @author abhishek
 * @version 1.0
 *
 */
@Entity
public class Account {

	@Id
	private String accountRef;

	@Column(nullable = false)
	private BigDecimal amount;

	@Column(nullable = false)
	private String currency;

	@Transient
	private Money balance;

	public Account() {
	}

	public Account(String accountRef, Money balance) {
		this.accountRef = accountRef;
		this.balance = balance;
	}

	public String getAccountRef() {
		return accountRef;
	}

	public Money getBalance() {
		return balance;
	}

	public void updateBalance(BigDecimal amount) {
		this.amount = this.amount.add(amount);
		balance = Money.create(String.valueOf(this.amount), currency);
	}

	public boolean isOverdrawn() {
		return balance.getAmount().signum() == -1;
	}

	@PrePersist
	@PreUpdate
	public void setBalanceDetails() {
		amount = balance.getAmount();
		currency = balance.getCurrency().toString();
	}

	@PostLoad
	public void getBalanceDetails() {
		balance = Money.create(String.valueOf(amount), currency);
	}

	@Override
	public String toString() {
		return "Account [accountRef=" + accountRef + ", amount=" + amount + ", currency=" + currency + ", balance=" + balance + "]";
	}

}