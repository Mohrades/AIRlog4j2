package util;

public class UsageCounterUsageThresholdInformation {
 private int usageCounterID;
 private long usageCounterValue;
 private boolean usageCounterMonetary;
 private long usageThresholdValue100;
 private boolean adjustmentUsageCounterRelative;
 
 
public boolean isAdjustmentUsageCounterRelative() {
	return adjustmentUsageCounterRelative;
}
public void setAdjustmentUsageCounterRelative(
		boolean adjustmentUsageCounterRelative) {
	this.adjustmentUsageCounterRelative = adjustmentUsageCounterRelative;
}
public UsageCounterUsageThresholdInformation(){
	 
 }
public int getUsageCounterID() {
	return usageCounterID;
}

public void setUsageCounterID(int usageCounterID) {
	this.usageCounterID = usageCounterID;
}

 public UsageCounterUsageThresholdInformation(int usageCounterID,long usageCounterValue,boolean usageCounterMonetary){
	 this.usageCounterID=usageCounterID;
	 this.usageCounterValue=usageCounterValue;
	 this.usageCounterMonetary=usageCounterMonetary;
 }

public long getUsageCounterValue() {
	return usageCounterValue;
}
public void setUsageCounterValue(long usageCounterValue) {
	this.usageCounterValue = usageCounterValue;
}
public boolean isUsageCounterMonetary() {
	return usageCounterMonetary;
}
public void setUsageCounterMonetary(boolean usageCounterMonetary) {
	this.usageCounterMonetary = usageCounterMonetary;
}
public long getUsageThresholdValue100() {
	return usageThresholdValue100;
}
public void setUsageThresholdValue100(long usageThresholdValue100) {
	this.usageThresholdValue100 = usageThresholdValue100;
}
}
