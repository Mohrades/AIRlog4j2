package util;

public class UsageThreshold {
 private int usageThresholdID;
 private long value;
 private boolean monetary;
 
public int getUsageThresholdID() {
	return usageThresholdID;
}

public void setUsageThresholdID(int usageThresholdID) {
	this.usageThresholdID = usageThresholdID;
}

 public UsageThreshold(int usageThresholdID,long value,boolean monetary){
	 this.usageThresholdID=usageThresholdID;
	 this.value=value;
	 this.monetary=monetary;
 }

public long getValue() {
	return value;
}

public void setValue(long value) {
	this.value = value;
}

public boolean isMonetary() {
	return monetary;
}

public void setMonetary(boolean monetary) {
	this.monetary = monetary;
}
 
}
