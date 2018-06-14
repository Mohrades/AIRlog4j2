package util;

import java.util.Date;
import java.util.LinkedList;

public class OfferInformation {
	private int offerID,offerType;
    private Date startDate,expiryDate;
    private LinkedList<Object[]> dedicatedAccountInformation;

    public OfferInformation(int offerID,int offerType,Date startDate,Date expiryDate,LinkedList<Object[]>dedicatedAccountInformation){
        this.expiryDate=expiryDate;
        this.offerID=offerID;
        this.startDate=startDate;
        this.offerType=offerType;
        this.dedicatedAccountInformation=dedicatedAccountInformation;
    }
	public int getOfferID() {
		return offerID;
	}
	public void setOfferID(int offerID) {
		this.offerID = offerID;
	}
	public int getOfferType() {
		return offerType;
	}
	public void setOfferType(int offerType) {
		this.offerType = offerType;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public LinkedList<Object[]> getDedicatedAccountInformation() {
		return dedicatedAccountInformation;
	}
	public void setDedicatedAccountInformation(
			LinkedList<Object[]> dedicatedAccountInformation) {
		this.dedicatedAccountInformation = dedicatedAccountInformation;
	}
}
