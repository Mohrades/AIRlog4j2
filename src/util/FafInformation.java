/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author TestAppli
 */
public class FafInformation { 
	private String fafNumber;
	private int fafIndicator;
	private static String Subscriber="Subscriber";
	@SuppressWarnings("unused")
	private static String Account="Account";
	private String owner;
	
    public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public FafInformation(String fafNumber,int fafIndicator) {
       this.fafIndicator=fafIndicator;
       this.fafNumber=fafNumber;
       setOwner(FafInformation.Subscriber);
    }
	public String getFafNumber() {
		return fafNumber;
	}
	public void setFafNumber(String fafNumber) {
		this.fafNumber = fafNumber;
	}
	public int getFafIndicator() {
		return fafIndicator;
	}
	public void setFafIndicator(int fafIndicator) {
		this.fafIndicator = fafIndicator;
	}
	
}
