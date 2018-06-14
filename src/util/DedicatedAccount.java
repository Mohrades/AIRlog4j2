package util;

public class DedicatedAccount extends BalanceAndDate{
	private Object startDate;
 public DedicatedAccount(Integer accountID,long accountValue,Object expiryDate){
	super(accountID,accountValue,expiryDate);
}
public Object getStartDate() {
	return startDate;
}
public void setStartDate(Object startDate) {
	this.startDate = startDate;
}

}
