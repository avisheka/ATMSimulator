package com.ebank.yodlee;

import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ebank.yodlee.controller.ATMOperationsExecutor;
import com.ebank.yodlee.exception.DepositInvalidDenominationException;
import com.ebank.yodlee.exception.InsufficientFundException;
import com.ebank.yodlee.exception.WithdrawInvalidAmountException;

/**
 * @author aarang
 *
 */
@SpringBootApplication
public class AtmSimulatorApplication {

	@Autowired
	private ATMOperationsExecutor atmExecutor;
	
	@PostConstruct
	public void initATMExecutor() {
		try (Scanner input = new Scanner(System.in)){
			do {
				System.out.println("\n1. Deposit");
				System.out.println("2. Withdraw");
				System.out.println("3. Display Balance");
				System.out.println("4. Mini Statement");
				System.out.println("5. Exit");
				
				System.out.println("\nSelect an option:");
				
				switch(input.nextInt()) {
				case 1: 
					try {
						atmExecutor.processDeposit();
					}catch(DepositInvalidDenominationException ide) {
						System.out.println(ide.getMessage());
						continue;
					}
					break;
				
				case 2: 
					try {
						atmExecutor.processWithdraw();
					}catch(WithdrawInvalidAmountException | InsufficientFundException ex) {
						System.out.println(ex.getMessage());
						continue;
					}
					break;
		
				case 3: 
					atmExecutor.processBalanceDisplay();
					break;
		
				case 4: 
					atmExecutor.processMiniStatement();
					break;
		
				case 5: 
					atmExecutor.processExit();
					break;
				}
			}while(true);
		}
		
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AtmSimulatorApplication.class, args);
	}
}
