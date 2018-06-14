/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author TestAppli
 */
public class AccountDetails {
    private HashMap<String,String> details;
    
    public AccountDetails(HashMap<String,String> details){
        this.details = details;
    }

    public int getServiceClassCurrent() {
        return Integer.parseInt(details.get("serviceClassCurrent"));
    }
    public int getAccountGroupID() {
        return Integer.parseInt(details.get("accountGroupID"));
    }
    public Date getActivationDate() {
        return (new DateTime_iso8601()).parse(details.get("activationDate"));
    }
    public Date getSupervisionExpiryDate() {
        return (new DateTime_iso8601()).parse(details.get("supervisionExpiryDate"));
    }
    public boolean isAccountFlags() {
        return details.get("accountFlags").equals("1") ? true :  false;
    }
    public int getLanguageIDCurrent() {
        return Integer.parseInt(details.get("languageIDCurrent"));
    }
    public int[] getCommunityInformationCurrent() {
    	try{
	        String []chaine=details.get("communityIDs").split("-");
	        int len=chaine.length;
	        int[] communityIDs=new int[len]; 
	        
	        for (int i=0;i<len;i++){
	            communityIDs[i]=Integer.parseInt(chaine[i]);
	        }
	        
	        return communityIDs;

        } catch(NumberFormatException e) {
            return null;
            
        } catch(NullPointerException e) {
    		return null;
    		
    	}
    }

    public ServiceOfferings getServiceOfferings() {
    	ServiceOfferings serviceOfferings = new ServiceOfferings();

        String []chaine=details.get("serviceOfferings").split(" ");
        int len=chaine.length/2;

        for (int i=0;i<len;i++){
        	serviceOfferings.SetActiveFlag(Integer.parseInt(chaine[2*i+1]), chaine[2*i].equals("1") ? true : false);
        }

        return serviceOfferings;
    }

    public int getServiceClassOriginal() {
        try {
        	return Integer.parseInt(details.get("serviceClassOriginal"));
        	
        } catch(NumberFormatException e) {
            return -1;
            
        } catch(NullPointerException e) {
        	return -1;
        }
    }
    
    public Date getServiceClassTemporaryExpiryDate(){
        try {
        	return (new DateTime_iso8601()).parse(details.get("serviceClassTemporaryExpiryDate"));

        } catch(NullPointerException e) {
        	return null;
        }
    }
}
