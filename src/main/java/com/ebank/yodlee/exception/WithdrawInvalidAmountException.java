package com.ebank.yodlee.exception;

public class WithdrawInvalidAmountException extends RuntimeException {

	public WithdrawInvalidAmountException(String message){
		super(message);
	}
}
