package com.ebank.yodlee.service.dispenser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebank.yodlee.model.ATMTenderStore;
import com.ebank.yodlee.model.Currency;
import com.ebank.yodlee.model.UserAccount;

@Service
public class USD20CurencyDispenser{

	@Autowired
	private USD10CurencyDispenser chain;

	@Autowired
	private UserAccount userAccount;
	
	@Autowired
	private ATMTenderStore atmTenderStore;
	
	public void setNextChain(USD10CurencyDispenser nextChain) {
		this.chain=nextChain;
	}
	
	public boolean dispenseAmount(Currency cur) {
		boolean isWithdrawn = false;
		int dispensedAmt = cur.getDenomination();
		//System.out.println("USD20:\tdispensedAmt:"+dispensedAmt+"\tuserAccount.getClosingBalance():"+userAccount.getClosingBalance()+"\tatmTenderStore.isTenderAvailable(\"20\"):"+atmTenderStore.isTenderAvailable("20"));
		if(dispensedAmt >= 20 &&
				userAccount.getClosingBalance() >= dispensedAmt &&
				atmTenderStore.isTenderAvailable("20")){
			int num = dispensedAmt/20;
			int remainder = dispensedAmt % 20;
			//System.out.println("USD20:\tnum:"+num+"\tremainder:"+remainder+"\ttenderCnt:"+atmTenderStore.getTender20Cnt());
			if(0 != num && 0 != atmTenderStore.getTender20Cnt())
			{
				int iCnt = 0;
				if(num>=atmTenderStore.getTender20Cnt()) {
					iCnt = atmTenderStore.getTender20Cnt();
					remainder =  dispensedAmt - 20*iCnt; 
				} else {
					iCnt = num;
				}
				if(remainder !=0) 
					isWithdrawn = this.chain.dispenseAmount(new Currency(remainder));
				
				if((remainder !=0 && isWithdrawn) || (remainder ==0 && !isWithdrawn)) {
					for(int i=0;i<iCnt;i++) {
						userAccount.setAmountWithdrawn("20");
						atmTenderStore.decreaseTenderCount("20");	
					}
					System.out.println("Dispensing "+iCnt+" 20$ note & amount remaining :"+remainder);
					isWithdrawn = true;
				}
			}
		}else{
			isWithdrawn = this.chain.dispenseAmount(cur);
		}
		return isWithdrawn;
	}
}
