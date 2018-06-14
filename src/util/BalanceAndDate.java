package util;

public class BalanceAndDate {
private Object expiryDate;
private Object serviceFee;
private long accountValue;
private boolean relative=true;
private Integer accountID;


public boolean isRelative() {
	return relative;
}
public void setRelative(boolean relative) {
	this.relative = relative;
}
public BalanceAndDate(Integer accountID,long accountValue,Object expiryDate){
	this.accountID=accountID;
	this.expiryDate=expiryDate;
	this.accountValue=accountValue;
}
public Object getExpiryDate() {
	return expiryDate;
}
public void setExpiryDate(Object expiryDate) {
	this.expiryDate = expiryDate;
}
public long getAccountValue() {
	return accountValue;
}
public void setAccountValue(long accountValue) {
	this.accountValue = accountValue;
}
public Integer getAccountID() {
	return accountID;
}
public void setAccountID(Integer accountID) {
	this.accountID = accountID;
}
public BalanceAndDate(){
	
}
public Object getServiceFee() {
	return serviceFee;
}
public void setServiceFee(Object serviceFee) {
	if(accountID==0)this.serviceFee = serviceFee;
}

}
