package com.unibet.worktest.bank.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

import com.unibet.worktest.bank.Money;

/**
 * Represents an entity to store every leg of transaction.
 * 
 * @author abhishek
 * @version 1.0
 *
 */
@Entity
@Immutable
public class TransactionLegDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long transactionLegId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountRef")
	private Account account;

	@Column(nullable = false)
	private BigDecimal amount;

	@Column(nullable = false)
	private String currency;

	@Transient
	private Money money;

	public TransactionLegDetails() {

	}

	public TransactionLegDetails(Account account, Money money) {
		this.account = account;
		this.money = money;
	}

	public Account getAccount() {
		return account;
	}

	public Money getMoney() {
		return money;
	}

	public Long getTransactionLegId() {
		return transactionLegId;
	}

	@PrePersist
	@PreUpdate
	public void setMoneyDetails() {
		amount = money.getAmount();
		currency = money.getCurrency().toString();
	}

	@PostLoad
	public void getMoneyDetails() {
		money = Money.create(String.valueOf(amount), currency);
	}

	@Override
	public String toString() {
		return "TransactionLegDetails [reference=" + transactionLegId + ", accountReference=" + account.getAccountRef() + ", amount=" + amount + ", currency=" + currency
				+ ", money=" + money + "]";
	}
	
	

}
