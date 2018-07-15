package com.ebank.yodlee.service.dispenser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebank.yodlee.model.ATMTenderStore;
import com.ebank.yodlee.model.Currency;
import com.ebank.yodlee.model.UserAccount;

@Service
public class USD50CurencyDispenser{

	@Autowired
	private USD20CurencyDispenser chain;

	@Autowired
	private UserAccount userAccount;
	
	@Autowired
	private ATMTenderStore atmTenderStore;
	
	public void setNextChain(USD20CurencyDispenser nextChain) {
		this.chain=nextChain;
	}
	
	public boolean dispenseAmount(Currency cur) {
		boolean isWithdrawn = false;
		int dispensedAmt = cur.getDenomination();
		//System.out.println("USD50:\tdispensedAmt:"+dispensedAmt+"\tuserAccount.getClosingBalance():"+userAccount.getClosingBalance()+"\tatmTenderStore.isTenderAvailable(\"50\"):"+atmTenderStore.isTenderAvailable("50"));
		if(dispensedAmt >= 50 && 
				userAccount.getClosingBalance() >= dispensedAmt &&
				atmTenderStore.isTenderAvailable("50")){
			int num = dispensedAmt/50;
			int remainder = dispensedAmt % 50;
			//System.out.println("USD50:\tnum:"+num+"\tremainder:"+remainder+"\ttenderCnt:"+atmTenderStore.getTender50Cnt());
			if(0 != num && 0 != atmTenderStore.getTender50Cnt())
			{
				int iCnt = 0;
				if(num>=atmTenderStore.getTender50Cnt()) {
					iCnt = atmTenderStore.getTender50Cnt();
					remainder =  dispensedAmt - 50*iCnt; 
				} else {
					iCnt = num;
				}
				if(remainder !=0) 
					isWithdrawn = this.chain.dispenseAmount(new Currency(remainder));
				
				if((remainder !=0 && isWithdrawn) || (remainder ==0 && !isWithdrawn)) {
					for(int i=0;i<num;i++) {
						userAccount.setAmountWithdrawn("50");
						atmTenderStore.decreaseTenderCount("50");
					}
					System.out.println("Dispensing "+iCnt+" 50$ note & amount remaining :"+remainder);
					isWithdrawn = true;
				}
			}
		}else{
			isWithdrawn = this.chain.dispenseAmount(cur);
		}
		return isWithdrawn;
	}
}
