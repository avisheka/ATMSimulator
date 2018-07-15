package com.ebank.yodlee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebank.yodlee.model.Currency;
import com.ebank.yodlee.service.dispenser.USD10CurencyDispenser;
import com.ebank.yodlee.service.dispenser.USD20CurencyDispenser;
import com.ebank.yodlee.service.dispenser.USD50CurencyDispenser;

@Service
public class CurrencyDispenserImpl {

	@Autowired
	private USD50CurencyDispenser currency50Dispenser;
	
	@Autowired
	private USD20CurencyDispenser currency20Dispenser;
	
	@Autowired
	private USD10CurencyDispenser currency10Dispenser;

	public boolean dispenseCurrency(Currency currency) {
		
		currency50Dispenser.setNextChain(currency20Dispenser);
		currency20Dispenser.setNextChain(currency10Dispenser);
		
		return currency50Dispenser.dispenseAmount(currency);
	}
}
