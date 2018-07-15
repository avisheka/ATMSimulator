package com.ebank.yodlee.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class TransactionDetails {

	private double transactionAmount=0.0;
	private double closingBalance=0.0;
	private String transactionType="";
	private String transactionTimestamp;
	
	public double getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public String getTransactionTimestamp() {
		return transactionTimestamp;
	}
	public void setTransactionTimestamp(Date transactionTimestamp) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");  
	    String strDate = formatter.format(transactionTimestamp);  
		this.transactionTimestamp = strDate;
	}
	public double getClosingBalance() {
		return closingBalance;
	}
	public void setClosingBalance(double closingBalance) {
		this.closingBalance = closingBalance;
	}
	@Override
	public String toString() {
		return ""+transactionTimestamp+
				" \t\t\t"+transactionType+
				" \t\t"+transactionAmount+
				" \t\t"+closingBalance;
	}
}
