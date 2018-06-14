/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Date;

/**
 *
 * @author TestAppli
 */
public class AccumulatorInformation {
    private int accumulatorID,accumulatorValue;
    private Date accumulatorStartDate,accumulatorEndDate;
    private boolean accumulatorValueRelative=true;
    
    public boolean isAccumulatorValueRelative() {
		return accumulatorValueRelative;
	}
	public void setAccumulatorValueRelative(boolean accumulatorValueRelative) {
		this.accumulatorValueRelative = accumulatorValueRelative;
	}
	public AccumulatorInformation(int accumulatorID,int accumulatorValue,Date accumulatorStartDate,Date accumulatorEndDate){
        this.accumulatorEndDate=accumulatorEndDate;
        this.accumulatorID=accumulatorID;
        this.accumulatorStartDate=accumulatorStartDate;
        this.accumulatorValue=accumulatorValue;
    }
	public int getAccumulatorID() {
		return accumulatorID;
	}
	public void setAccumulatorID(int accumulatorID) {
		this.accumulatorID = accumulatorID;
	}
	public int getAccumulatorValue() {
		return accumulatorValue;
	}
	public void setAccumulatorValue(int accumulatorValue) {
		this.accumulatorValue = accumulatorValue;
	}
	public Date getAccumulatorStartDate() {
		return accumulatorStartDate;
	}
	public void setAccumulatorStartDate(Date accumulatorStartDate) {
		this.accumulatorStartDate = accumulatorStartDate;
	}
	public Date getAccumulatorEndDate() {
		return accumulatorEndDate;
	}
	public void setAccumulatorEndDate(Date accumulatorEndDate) {
		this.accumulatorEndDate = accumulatorEndDate;
	}
}
