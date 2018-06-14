package util;

import java.util.Date;

public class PamUpdateInformation {
	
	private int pamServiceID;
	private int pamClassIDOld, pamClassIDNew;
	private int scheduleIDOld, scheduleIDNew;
	private String currentPamPeriod;
	private Date deferredToDate;
	private int pamServicePriorityOld, pamServicePriorityNew;

	public PamUpdateInformation() {
		pamServicePriorityOld = -1;
		pamServicePriorityNew = -1;
	}
	
	public PamUpdateInformation(int pamServiceID, int pamClassIDOld, int pamClassIDNew, int scheduleIDOld, int scheduleIDNew) {
		this();
		this.pamServiceID = pamServiceID;
		this.pamClassIDOld = pamClassIDOld;
		this.pamClassIDNew = pamClassIDNew;
		this.scheduleIDOld = scheduleIDOld;
		this.scheduleIDNew = scheduleIDNew;
	}

	public int getPamServiceID() {
		return pamServiceID;
	}

	public void setPamServiceID(int pamServiceID) {
		this.pamServiceID = pamServiceID;
	}

	public int getPamClassIDOld() {
		return pamClassIDOld;
	}

	public void setPamClassIDOld(int pamClassIDOld) {
		this.pamClassIDOld = pamClassIDOld;
	}

	public int getPamClassIDNew() {
		return pamClassIDNew;
	}

	public void setPamClassIDNew(int pamClassIDNew) {
		this.pamClassIDNew = pamClassIDNew;
	}

	public int getScheduleIDOld() {
		return scheduleIDOld;
	}

	public void setScheduleIDOld(int scheduleIDOld) {
		this.scheduleIDOld = scheduleIDOld;
	}

	public int getScheduleIDNew() {
		return scheduleIDNew;
	}

	public void setScheduleIDNew(int scheduleIDNew) {
		this.scheduleIDNew = scheduleIDNew;
	}

	public int getPamServicePriorityOld() {
		return pamServicePriorityOld;
	}

	public void setPamServicePriorityOld(int pamServicePriorityOld) {
		this.pamServicePriorityOld = pamServicePriorityOld;
	}

	public int getPamServicePriorityNew() {
		return pamServicePriorityNew;
	}

	public void setPamServicePriorityNew(int pamServicePriorityNew) {
		this.pamServicePriorityNew = pamServicePriorityNew;
	}

	public String getCurrentPamPeriod() {
		return currentPamPeriod;
	}

	public void setCurrentPamPeriod(String currentPamPeriod) {
		this.currentPamPeriod = currentPamPeriod;
	}

	public Date getDeferredToDate() {
		return deferredToDate;
	}

	public void setDeferredToDate(Date deferredToDate) {
		this.deferredToDate = deferredToDate;
	}

}
