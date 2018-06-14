package util;

import java.util.Date;
import java.util.HashSet;

public class FafInformationList {

	private Date fafChangeUnbarDate;
    private boolean fafMaxAllowedNumbersReachedFlag;
    private HashSet<FafInformation> list;

	public Date getFafChangeUnbarDate() {
		return fafChangeUnbarDate;
	}
	
	public FafInformationList() {
		list = new HashSet<FafInformation>();
	}

	public FafInformationList(HashSet<FafInformation> list) {
		this.list = list;
	}

	public HashSet<FafInformation> getList() {
		return list;
	}

	public void setList(HashSet<FafInformation> list) {
		this.list = list;
	}

	public void add(FafInformation fafInformation) {
		if(list != null) {
			list.add(fafInformation);
		}
	}

	public void setFafChangeUnbarDate(Date fafChangeUnbarDate) {
		this.fafChangeUnbarDate = fafChangeUnbarDate;
	}

	public boolean isFafMaxAllowedNumbersReachedFlag() {
		return fafMaxAllowedNumbersReachedFlag;
	}

	public void setFafMaxAllowedNumbersReachedFlag(boolean fafMaxAllowedNumbersReachedFlag) {
		this.fafMaxAllowedNumbersReachedFlag = fafMaxAllowedNumbersReachedFlag;
	}
}
