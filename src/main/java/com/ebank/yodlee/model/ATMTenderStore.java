package com.ebank.yodlee.model;

import org.springframework.stereotype.Component;

@Component
public class ATMTenderStore {
	
	private final static String TEN = "10";
	private final static String TWENTY = "20";
	private final static String FIFTY = "50";
	
	private int tender10Cnt=0;
	private int tender20Cnt=0;
	private int tender50Cnt=0;
	
	public int getTender10Cnt() {
		return tender10Cnt;
	}
	public void setTender10Cnt(int tender10Cnt) {
		this.tender10Cnt = tender10Cnt;
	}
	public int getTender20Cnt() {
		return tender20Cnt;
	}
	public void setTender20Cnt(int tender20Cnt) {
		this.tender20Cnt = tender20Cnt;
	}
	public int getTender50Cnt() {
		return tender50Cnt;
	}
	public void setTender50Cnt(int tender50Cnt) {
		this.tender50Cnt = tender50Cnt;
	}
	
	public void tendercount() {
		System.out.println(
				"{ \n\'tender50Cnt\':"+tender50Cnt+
				"  \n\'tender20Cnt\':"+tender20Cnt+
				"  \n\'tender10Cnt\':"+tender10Cnt+" \n}"
				);
	}
	
	public int getTenderCount(String tenderType) {
		int tendercount = 0;
		switch(tenderType) {
		case ATMTenderStore.TEN:
			tendercount = this.getTender10Cnt();
			break;
			
		case ATMTenderStore.TWENTY:
			tendercount = this.getTender20Cnt();
			break;
					
		case ATMTenderStore.FIFTY:
			tendercount = this.getTender50Cnt();
			break;
		}
		
		return tendercount;
	}
	
	public boolean isTenderAvailable(String tenderType) {
		boolean isTenderAvailable = false;
		switch(tenderType) {
		case ATMTenderStore.TEN:
			isTenderAvailable = tender10Cnt>0;
			break;
			
		case ATMTenderStore.TWENTY:
			isTenderAvailable = tender20Cnt>0;
			break;
					
		case ATMTenderStore.FIFTY:
			isTenderAvailable = tender50Cnt>0;
			break;
		}
		
		return isTenderAvailable;
	}
	
	public void increaseTenderCount(String denomination) {
		switch(denomination) {
		case ATMTenderStore.TEN:
			tender10Cnt++;
			break;
			
		case ATMTenderStore.TWENTY:
			tender20Cnt++;
			break;
					
		case ATMTenderStore.FIFTY:
			tender50Cnt++;
			break;
		}
	}

	public void decreaseTenderCount(String denomination) {
		switch(denomination) {
		case ATMTenderStore.TEN:
			if(this.getTender10Cnt()>0)
			tender10Cnt--;
			break;
			
		case ATMTenderStore.TWENTY:
			if(this.getTender20Cnt()>0)
			tender20Cnt--;
			break;
					
		case ATMTenderStore.FIFTY:
			if(this.getTender50Cnt()>0)
			tender50Cnt--;
			break;
		}
	}
}
