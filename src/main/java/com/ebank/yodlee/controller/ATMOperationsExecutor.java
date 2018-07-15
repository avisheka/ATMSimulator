package com.ebank.yodlee.controller;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ebank.yodlee.model.TransactionDetails;
import com.ebank.yodlee.service.UserTransactionController;

/**
 * @author aarang
 *
 */
@Controller
public class ATMOperationsExecutor {
	
	@Autowired
	private UserTransactionController userTransaction;
	
	public void processDeposit() {
		System.out.println("Enter currency to deposit terminated by . e.g. 10 20 50 .");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
	    
		userTransaction.performDeposit(input);
	}
	
	public void processWithdraw() {
		System.out.println("Enter amount to withdraw:");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		
		userTransaction.performWithdraw(input);
	}
	
	public void processBalanceDisplay() {
		System.out.println("Available balance is:"+userTransaction.performBalanceCheck());
	}
	
	public void processMiniStatement() {
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("Date Time	\t\tTransaction		Amount		Closing Balance");
		System.out.println("-----------------------------------------------------------------------------");
		List<TransactionDetails> transactions = userTransaction.performAccountStatement();
		for (TransactionDetails transaction : transactions) {
			System.out.println(transaction.toString());
		}
		System.out.println("-----------------------------------------------------------------------------");
	}
	
	public void processExit() {
		System.out.println("Have a good day!");
		System.exit(0);
	}
	
}
