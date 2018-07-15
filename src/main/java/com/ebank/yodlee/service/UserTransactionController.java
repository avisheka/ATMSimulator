package com.ebank.yodlee.service;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebank.yodlee.exception.InsufficientFundException;
import com.ebank.yodlee.model.ATMTenderStore;
import com.ebank.yodlee.model.Currency;
import com.ebank.yodlee.model.TransactionDetails;
import com.ebank.yodlee.model.UserAccount;
import com.ebank.yodlee.util.TransactionValidator;

@Service
public class UserTransactionController {
	
	@Autowired
	private UserAccount userAccount;
	
	@Autowired
	private TransactionValidator validator;

	@Autowired
	private ATMTenderStore atmTenderStore;
	
	@Autowired
	private CurrencyDispenserImpl currencyDispenserImpl;
	/**
	 * deposit
	 * 	- validate tenders
	 * 	- update account
	 */
	public void performDeposit(String depositIp) {
		String currencyToken="";
	    boolean isDeposited = false;
	    double depositedAmount = 0.0;

	    StringTokenizer st = new StringTokenizer(depositIp," ",false);
	    
	    while(st.hasMoreTokens()) {
	    	currencyToken = st.nextToken();
	    	
	    	if(".".equals(currencyToken))
	    		break;
	    	
			//- validate tenders/ transaction
	    	if(validator.validateDepositDenomination(currencyToken)) {
	    		//- deposit into atm store
	    		atmTenderStore.increaseTenderCount(currencyToken);
	    		
	    		//- update account
	    		userAccount.setAmountDepositted(currencyToken);
	    		depositedAmount += Double.parseDouble(currencyToken);
	    		System.out.println("Accepted: "+currencyToken);

	    		isDeposited = true;
	    	}
	    }
	    //update transaction details
	    if(isDeposited) {
	    	TransactionDetails transaction = new TransactionDetails();
			transaction.setTransactionAmount(depositedAmount);
			transaction.setClosingBalance(userAccount.getClosingBalance());
			transaction.setTransactionType("Credit");
			transaction.setTransactionTimestamp(new Date());
			userAccount.getTransactions().add(transaction);	
	    }
	}
	
	/**
	 * withdraw
	 * - validate requested amount/ transaction
	 * 		- check account for availability
	 * 		- atm store for tender availability
	 * - update account
	 * - update atm store
	 * */
	public void performWithdraw(String withdrawIp) {
		boolean isWithdrawn = false;
		if(validator.validateWithdrawDenomination(withdrawIp)) {
			// process the request
			isWithdrawn = currencyDispenserImpl.dispenseCurrency(new Currency(Integer.parseInt(withdrawIp)));
		}
		
	    //update transaction details
		if(isWithdrawn) {
	    	TransactionDetails transaction = new TransactionDetails();
			transaction.setTransactionAmount(Double.parseDouble(""+withdrawIp));
			transaction.setClosingBalance(userAccount.getClosingBalance());
			transaction.setTransactionType("Debit");
			transaction.setTransactionTimestamp(new Date());
			userAccount.getTransactions().add(transaction);	
	    } else {
	    	throw new InsufficientFundException("withdrawal of "+withdrawIp+"$ failed due to either low balance or tender not available in atm");
	    }
	}
	
	/**
	 * balance check - usr a/c balance
	 */
	public double performBalanceCheck() {
		return userAccount.getClosingBalance();
	}
	
	/**
	 * balance stmt - user transaction stmt
	 */
	public List<TransactionDetails> performAccountStatement() {
		return userAccount.getTransactions();
	}
}
