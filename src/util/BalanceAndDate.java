package util;

public class BalanceAndDate {
private Object expiryDate;
private Object serviceFee;
private long accountValue1, accountValue2;
private boolean relative = true;
private Integer accountID;

public BalanceAndDate() {
	
}

public BalanceAndDate(Integer accountID, long accountValue1, Object expiryDate) {
	this.accountID = accountID;
	this.expiryDate = expiryDate;
	this.accountValue1 = accountValue1;
}

public Integer getAccountID() {
	return accountID;
}
public void setAccountID(Integer accountID) {
	this.accountID = accountID;
}

public long getAccountValue() {
	return getAccountValue1();
}
public void setAccountValue(long accountValue) {
	setAccountValue1(accountValue);
}

public long getAccountValue1() {
	return accountValue1;
}
public void setAccountValue1(long accountValue1) {
	this.accountValue1 = accountValue1;
}

public long getAccountValue2() {
	return accountValue2;
}
public void setAccountValue2(long accountValue2) {
	this.accountValue2 = accountValue2;
}

public Object getExpiryDate() {
	return expiryDate;
}
public void setExpiryDate(Object expiryDate) {
	this.expiryDate = expiryDate;
}

public Object getServiceFee() {
	return serviceFee;
}

public void setServiceFee(Object serviceFee) {
	if(accountID == 0) {
		this.serviceFee = serviceFee;
	}
}

public boolean isRelative() {
	return relative;
}

public void setRelative(boolean relative) {
	this.relative = relative;
}

}
