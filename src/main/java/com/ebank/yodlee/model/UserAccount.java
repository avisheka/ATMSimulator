package com.ebank.yodlee.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserAccount {

	private List<TransactionDetails> transactions;
	
	private double amountDepositted=0.0;
	private double amountWithdrawn=0.0;
	private double closingBalance=0.0;
	
	public double getAmountDepositted() {
		return amountDepositted;
	}
	public void setAmountDepositted(String tenderDepositted) {
		this.amountDepositted = Double.parseDouble(tenderDepositted);
		this.closingBalance += Double.parseDouble(tenderDepositted);
	}
	public double getAmountWithdrawn() {
		return amountWithdrawn;
	}
	public void setAmountWithdrawn(String tenderWithdrawn) {
		if(this.closingBalance>0) {
			this.closingBalance -= Double.parseDouble(tenderWithdrawn);
			this.amountWithdrawn = Double.parseDouble(tenderWithdrawn);
		}
	}
	public double getClosingBalance() {
		return closingBalance;
	}
	public List<TransactionDetails> getTransactions() {
		if(null == transactions) {
			transactions = new ArrayList<TransactionDetails>();
		}
		return transactions;
	}
}
