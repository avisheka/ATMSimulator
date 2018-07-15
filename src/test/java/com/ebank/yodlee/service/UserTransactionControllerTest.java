package com.ebank.yodlee.service;

import static org.junit.Assert.assertEquals;

import java.util.StringTokenizer;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ebank.yodlee.controller.ATMOperationsExecutor;
import com.ebank.yodlee.exception.DepositInvalidDenominationException;
import com.ebank.yodlee.exception.InsufficientFundException;
import com.ebank.yodlee.exception.WithdrawInvalidAmountException;
import com.ebank.yodlee.model.ATMTenderStore;
import com.ebank.yodlee.model.UserAccount;
import com.ebank.yodlee.service.dispenser.USD10CurencyDispenser;
import com.ebank.yodlee.service.dispenser.USD20CurencyDispenser;
import com.ebank.yodlee.service.dispenser.USD50CurencyDispenser;
import com.ebank.yodlee.util.TransactionValidator;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= {
		UserTransactionController.class, 
		UserAccount.class, 
		TransactionValidator.class, 
		ATMTenderStore.class, 
		CurrencyDispenserImpl.class,
		USD50CurencyDispenser.class,
		USD20CurencyDispenser.class,
		USD10CurencyDispenser.class,
		ATMOperationsExecutor.class
})
public class UserTransactionControllerTest {

	@Autowired
	private UserTransactionController userTransaction;

	@BeforeClass
	public static void testUserController() {
		System.out.println("In testUserController");
		assertEquals("class com.ebank.yodlee.service.UserTransactionController",
				UserTransactionController.class.toString());
	}

	@After
	public void performResetBalance() {
		System.out.println("\nIn performResetBalance::before reset::"+userTransaction.performBalanceCheck());
		StringBuffer strBalance = new StringBuffer(String.valueOf(userTransaction.performBalanceCheck()));
		String balance = strBalance.substring(0, strBalance.indexOf("."));
		if(userTransaction.performBalanceCheck() > 0)
			userTransaction.performWithdraw(balance.toString());
		System.out.println("In performResetBalance::after reset::"+userTransaction.performBalanceCheck());
	}
	
	/**
	 * 1. Deposit - 20 10 50 20 .
	 * 		Accepted: 20
	 * 		Accepted: 10
	 * 		Accepted: 50
	 * 		Accepted: 20
	 * 2. Display Balance - 100.0
	 */
	@Test
	public void program1Test() {
		System.out.println("\nIn program1Test::");
		String ipStr = "20 10 50 20 .";
		StringTokenizer st = new StringTokenizer(ipStr," ",false);
		
		while(st.hasMoreTokens()) {
			String currencyToken = st.nextToken();
			try {
				userTransaction.performDeposit(currencyToken);
			}catch(DepositInvalidDenominationException e) {
				continue;
			}
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
		assertEquals(new Double(100.0), new Double(userTransaction.performBalanceCheck()));
	}

	/**
	 * 1. Deposit - 30 40 20 10 .
	 * 		Invalid denomination: 30$
	 * 		Invalid denomination: 40$
	 * 		Accepted: 20
	 * 		Accepted: 10
	 * 2. Display Balance - 30.0
	 * 3. Withdraw - 10
	 * 		Dispensing 1 10$ note
	 * 4. Display Balance - 20 
	 */
	@Test
	public void program2Test() {
		System.out.println("\nIn program2Test::");
		String ipStr = "30 40 20 10 .";
		StringTokenizer st = new StringTokenizer(ipStr," ",false);
		
		while(st.hasMoreTokens()) {
			String currencyToken = st.nextToken();
			try {
				userTransaction.performDeposit(currencyToken);
			}catch(DepositInvalidDenominationException ide) {
				System.out.println(ide.getMessage());
				continue;
			}
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
		assertEquals(new Double(30.0), new Double(userTransaction.performBalanceCheck()));
		try {
			userTransaction.performWithdraw("10");
		}catch(WithdrawInvalidAmountException | InsufficientFundException ex) {
			System.out.println(ex.getMessage());
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
		assertEquals(new Double(20.0), new Double(userTransaction.performBalanceCheck()));
	}
	
	/**
	 * 1. Deposit - 20 20 10 50 20 . 
	 * 		Accepted: 20
	 * 		Accepted: 20
	 * 		Accepted: 10
	 * 		Accepted: 50
	 * 		Accepted: 20
	 * 2. Display Balance - 120.0 
	 * 3. Withdraw - 80  
	 * 		Dispensing 1 50$ note
	 * 		Dispensing 1 20$ note
	 * 		Dispensing 1 10$ note 
	 * 4. Display Balance - 40.0 
	 */
	@Test
	public void program3Test() {
		System.out.println("\nIn program3Test::");
		String ipStr = "20 20 10 50 20 . ";
		StringTokenizer st = new StringTokenizer(ipStr," ",false);
		
		while(st.hasMoreTokens()) {
			String currencyToken = st.nextToken();
			try {
				userTransaction.performDeposit(currencyToken);
			}catch(DepositInvalidDenominationException e) {
				continue;
			}
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
		assertEquals(new Double(120.0), new Double(userTransaction.performBalanceCheck()));
		try {
			userTransaction.performWithdraw("80");
		}catch(WithdrawInvalidAmountException | InsufficientFundException ex) {
			System.out.println(ex.getMessage());
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
		assertEquals(new Double(40.0), new Double(userTransaction.performBalanceCheck()));
	}
	
	/**
	 * 1. Deposit - 50 20 20 20 10 . 
	 * 		Accepted: 50
	 * 		Accepted: 20
	 * 		Accepted: 20
	 * 		Accepted: 20
	 * 		Accepted: 10
	 * 2. Display Balance - 120.0 
	 * 3. Withdraw - 60
	 * 		Dispensing 1 50$ note
	 * 		Dispensing 1 10$ note
	 * 4. Display Balance - 60.0 
	 */
	@Test
	public void program4Test() {
		System.out.println("\nIn program4Test::");
		String ipStr = "50 20 20 20 10 .";
		StringTokenizer st = new StringTokenizer(ipStr," ",false);
		
		while(st.hasMoreTokens()) {
			String currencyToken = st.nextToken();
			try {
				userTransaction.performDeposit(currencyToken);
			}catch(DepositInvalidDenominationException e) {
				continue;
			}
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
		assertEquals(new Double(120.0), new Double(userTransaction.performBalanceCheck()));
		try {
			userTransaction.performWithdraw("60");
		}catch(WithdrawInvalidAmountException | InsufficientFundException ex) {
			System.out.println(ex.getMessage());
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
		assertEquals(new Double(60.0), new Double(userTransaction.performBalanceCheck()));
	}

	/**
	 * 1. Deposit - 20 20 20 10 10 10 . 
	 * 		Accepted: 20
	 * 		Accepted: 20
	 * 		Accepted: 20
	 * 		Accepted: 10
	 * 		Accepted: 10
	 * 		Accepted: 10
	 * 2. Display Balance - 90.0 
	 * 3. Withdraw - 60
	 * 		Dispensing 3 20$ note 
	 * 4. Display Balance - 30.0 
	 */
	@Test
	public void program5Test() {
		System.out.println("\nIn program5Test::");
		String ipStr = "20 20 20 10 10 10 .";
		StringTokenizer st = new StringTokenizer(ipStr," ",false);
		
		while(st.hasMoreTokens()) {
			String currencyToken = st.nextToken();
			try {
				userTransaction.performDeposit(currencyToken);
			}catch(DepositInvalidDenominationException e) {
				continue;
			}
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
		assertEquals(new Double(90.0), new Double(userTransaction.performBalanceCheck()));
		try {
			userTransaction.performWithdraw("60");
		}catch(WithdrawInvalidAmountException | InsufficientFundException ex) {
			System.out.println(ex.getMessage());
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
		assertEquals(new Double(30.0), new Double(userTransaction.performBalanceCheck()));
	}
	
	/**
	 * 1. Deposit - 20 20 10 10 10 . 
	 * 2. Display Balance - 70.0 
	 * 		Accepted: 20
	 * 		Accepted: 20
	 * 		Accepted: 10
	 * 		Accepted: 10
	 * 		Accepted: 10
	 * 3. Withdraw - 60
	 * 		Dispensing 2 20$ note
	 * 		Dispensing 2 10$ note
	 * 4. Display Balance - 10.0 
	 */
	@Test
	public void program6Test() {
		System.out.println("\nIn program6Test::");
		String ipStr = "20 20 10 10 10 .";
		StringTokenizer st = new StringTokenizer(ipStr," ",false);
		
		while(st.hasMoreTokens()) {
			String currencyToken = st.nextToken();
			try {
				userTransaction.performDeposit(currencyToken);
			}catch(DepositInvalidDenominationException e) {
				continue;
			}
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
		assertEquals(new Double(70.0), new Double(userTransaction.performBalanceCheck()));
		try {
			userTransaction.performWithdraw("60");
		}catch(WithdrawInvalidAmountException | InsufficientFundException ex) {
			System.out.println(ex.getMessage());
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
		assertEquals(new Double(10.0), new Double(userTransaction.performBalanceCheck()));
	}
	
	/**
	 * 1. Deposit - 20 20 10 10 10 . 
	 * 2. Display Balance - 70.0 
	 * 3. Withdraw - 80
	 * 		withdrawal of 80$ failed due to either low balance or tender not available in atm		 
	 */
	@Test
	public void program7Test() {
		System.out.println("\nIn program7Test::");
		String ipStr = "20 20 10 10 10 .";
		StringTokenizer st = new StringTokenizer(ipStr," ",false);
		
		while(st.hasMoreTokens()) {
			String currencyToken = st.nextToken();
			try {
				userTransaction.performDeposit(currencyToken);
			}catch(DepositInvalidDenominationException e) {
				continue;
			}
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
		assertEquals(new Double(70.0), new Double(userTransaction.performBalanceCheck()));
		try {
			userTransaction.performWithdraw("80");
		}catch(WithdrawInvalidAmountException | InsufficientFundException ex) {
			System.out.println(ex.getMessage());
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
	}
	
	/**
	 * 1. Deposit - 50 . 
	 * 2. Display Balance - 50.0 
	 * 3. Withdraw - 40
	 * 		withdrawal of 80$ failed due to either low balance or tender not available in atm		 
	 */
	@Test
	public void program8Test() {
		System.out.println("\nIn program8Test::");
		String ipStr = "50 .";
		StringTokenizer st = new StringTokenizer(ipStr," ",false);
		
		while(st.hasMoreTokens()) {
			String currencyToken = st.nextToken();
			try {
				userTransaction.performDeposit(currencyToken);
			}catch(DepositInvalidDenominationException e) {
				continue;
			}
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
		assertEquals(new Double(50.0), new Double(userTransaction.performBalanceCheck()));
		try {
			userTransaction.performWithdraw("40");
		}catch(WithdrawInvalidAmountException | InsufficientFundException ex) {
			System.out.println(ex.getMessage());
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
	}

	/**
	 * 1. Deposit - 50 10 20 20 20  . 
	 * 		Accepted: 50
	 * 		Accepted: 10
	 * 		Accepted: 20
	 * 		Accepted: 20
	 * 		Accepted: 20
	 * 2. Display Balance - 120.0 
	 * 3. Withdraw - 40
	 * 		Dispensing 1 50$ note
	 * 		Dispensing 1 20$ note
	 * 		Dispensing 1 10$ note
	 * 4. Display Balance - 80.0 
	 * 5. Withdraw - 10
	 * 		Dispensing 1 10$ note
	 * 6. Display Balance - 70.0 
	 * 7. Withdraw - 60
	 * 		withdrawal of 60$ failed due to either low balance or tender not available in atm
	 */
	@Test
	public void program9Test() {
		System.out.println("\nIn program9Test::");
		String ipStr = "50 10 20 20 20 .";
		StringTokenizer st = new StringTokenizer(ipStr," ",false);
		
		while(st.hasMoreTokens()) {
			String currencyToken = st.nextToken();
			try {
				userTransaction.performDeposit(currencyToken);
			}catch(DepositInvalidDenominationException e) {
				continue;
			}
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
		assertEquals(new Double(120.0), new Double(userTransaction.performBalanceCheck()));
		try {
			userTransaction.performWithdraw("40");
		}catch(WithdrawInvalidAmountException | InsufficientFundException ex) {
			System.out.println(ex.getMessage());
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
		assertEquals(new Double(80.0), new Double(userTransaction.performBalanceCheck()));
		try {
			userTransaction.performWithdraw("10");
		}catch(WithdrawInvalidAmountException | InsufficientFundException ex) {
			System.out.println(ex.getMessage());
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
		assertEquals(new Double(70.0), new Double(userTransaction.performBalanceCheck()));
		try {
			userTransaction.performWithdraw("60");
		}catch(WithdrawInvalidAmountException | InsufficientFundException ex) {
			System.out.println(ex.getMessage());
		}
		System.out.println("Available Balance: "+userTransaction.performBalanceCheck());
	}

}

