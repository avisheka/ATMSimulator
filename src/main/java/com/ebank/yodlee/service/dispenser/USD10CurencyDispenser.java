package com.ebank.yodlee.service.dispenser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebank.yodlee.model.ATMTenderStore;
import com.ebank.yodlee.model.Currency;
import com.ebank.yodlee.model.UserAccount;

@Service
public class USD10CurencyDispenser{

	@Autowired
	private USD50CurencyDispenser chain;

	@Autowired
	private UserAccount userAccount;
	
	@Autowired
	private ATMTenderStore atmTenderStore;
	
	public void setNextChain(USD50CurencyDispenser nextChain) {
		this.chain=nextChain;
	}
	
	public boolean dispenseAmount(Currency cur) {
		boolean isWithdrawn = false;
		int dispensedAmt = cur.getDenomination();
		//System.out.println("USD10:\tdispensedAmt:"+dispensedAmt+"\tuserAccount.getClosingBalance():"+userAccount.getClosingBalance()+"\tatmTenderStore.isTenderAvailable(\"10\"):"+atmTenderStore.isTenderAvailable("10"));
		if(dispensedAmt >= 10 && 
				userAccount.getClosingBalance() >= dispensedAmt &&
				atmTenderStore.isTenderAvailable("10")){
			int num = dispensedAmt/10;
			int remainder = dispensedAmt % 10;
			//System.out.println("USD10:\tnum:"+num+"\tremainder:"+remainder+"\ttenderCnt:"+atmTenderStore.getTender10Cnt());
			if(0 != num  && 0 != atmTenderStore.getTender10Cnt())
			{
				int iCnt = 0;
				if(num>=atmTenderStore.getTender10Cnt()) {
					iCnt = atmTenderStore.getTender10Cnt();
					remainder =  dispensedAmt - 10*iCnt; 
				} else {
					iCnt = num;
				}
				if(remainder !=0) 
					isWithdrawn = this.chain.dispenseAmount(new Currency(remainder));
				
				if((remainder !=0 && isWithdrawn) || (remainder ==0 && !isWithdrawn)) {
					for(int i=0;i<num;i++) {
						userAccount.setAmountWithdrawn("10");
						atmTenderStore.decreaseTenderCount("10");	
					}
					System.out.println("Dispensing "+iCnt+" 10$ note & amount remaining :"+remainder);
					isWithdrawn = true;
				}
			}
			
		}else{
			isWithdrawn = false;
		}
		return isWithdrawn;
	}
}
