package com.ebank.yodlee.util;

import org.springframework.stereotype.Component;

import com.ebank.yodlee.exception.DepositInvalidDenominationException;
import com.ebank.yodlee.exception.WithdrawInvalidAmountException;

@Component
public class TransactionValidator {

	public boolean validateDepositDenomination(String denomination) {
		boolean isValidDepositToken = false;
		try {
			switch(denomination) {
			case "10":
				isValidDepositToken = true;
				break;
				
			case "20":
				isValidDepositToken = true;
				break;
						
			case "50":
				isValidDepositToken = true;
				break;
			
			}
			if(isValidDepositToken) {
				Double.parseDouble(denomination);
			}else {
				throw new DepositInvalidDenominationException("Invalid denomination: "+denomination+"$");
			}
		}catch(NumberFormatException nfe) {
			throw new DepositInvalidDenominationException("Invalid denomination: "+denomination+"$");
		}
		return isValidDepositToken;
	}
	
	public boolean validateWithdrawDenomination(String denomination) {
		boolean isValidWithdrawToken = false;
		try {
			if (Integer.parseInt(denomination) % 10 != 0) {
				throw new WithdrawInvalidAmountException("Amount should be in multiple of 10s.");
			}
			isValidWithdrawToken = true;
		}catch(NumberFormatException nfe) {
			throw new WithdrawInvalidAmountException("Invalid denomination: "+denomination+"$");
		}
		return isValidWithdrawToken;
	}
}
