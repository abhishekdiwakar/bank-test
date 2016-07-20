package com.unibet.worktest.bank.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Immutable;

/**
 * Represents an entity to store transaction details.
 * 
 * @author abhishek
 * @version 1.0
 *
 */
@Entity
@Immutable
public class TransactionDetails {

	@Id
	private String reference;

	@Column(nullable = false)
	private Date date;

	private String type;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "reference")
	private List<TransactionLegDetails> legs;

	public TransactionDetails() {

	}

	public TransactionDetails(String reference, Date date, String type, List<TransactionLegDetails> legs) {
		this.reference = reference;
		this.date = date;
		this.type = type;
		this.legs = legs;
	}

	public String getReference() {
		return reference;
	}

	public Date getDate() {
		return date;
	}

	public String getType() {
		return type;
	}

	public List<TransactionLegDetails> getLegs() {
		return legs;
	}

	@Override
	public String toString() {
		return "TransactionDetails [reference=" + reference + ", date=" + date + ", type=" + type + ", transactionLegs=" + legs + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionDetails other = (TransactionDetails) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}	
}
