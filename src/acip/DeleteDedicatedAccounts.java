package acip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

import util.DateTime_iso8601;
import util.DedicatedAccount;
import connexions.SocketConnection;

public class DeleteDedicatedAccounts {
    
    public StringBuffer formerRequete(String msisdn,Integer serviceClassCurrent ,HashSet<DedicatedAccount> dedicatedAccountIdentifier,String originOperatorID){
    	StringBuffer serviceClass = new StringBuffer("");
    	StringBuffer dedicatedAccountIDs = new StringBuffer("");

    	if(serviceClassCurrent != null) {
    		serviceClass.append("<member><name>serviceClassCurrent</name><value><i4>");
    		serviceClass.append(serviceClassCurrent);
    		serviceClass.append("</i4></value></member>");
    	}

    	for (DedicatedAccount DA : dedicatedAccountIdentifier) {
    		dedicatedAccountIDs.append("<value><struct>");

    		dedicatedAccountIDs.append("<member><name>dedicatedAccountID</name><value><int>");
    		dedicatedAccountIDs.append(DA.getAccountID());
    		dedicatedAccountIDs.append("</int></value></member>");

    		if(DA.getExpiryDate() != null) {
    			dedicatedAccountIDs.append("<member><name>expiryDate</name><value><dateTime.iso8601>");
    			dedicatedAccountIDs.append((new DateTime_iso8601()).format((Date)DA.getExpiryDate()));
    			dedicatedAccountIDs.append("</dateTime.iso8601></value></member>");
    		}

    		dedicatedAccountIDs.append("</struct></value>");
    	}

    	StringBuffer entete = new StringBuffer("<member><name>dedicatedAccountIdentifier</name><value><array><data>");
    	dedicatedAccountIDs.append("</data></array></value></member>");
    	dedicatedAccountIDs = entete.append(dedicatedAccountIDs);

    	StringBuffer requete = new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>DeleteDedicatedAccounts</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
    	requete.append((new SimpleDateFormat("yyMMddHHmmssS")).format(new Date()));
    	requete.append("</string></value></member><member><name>originTimeStamp</name><value><dateTime.iso8601>");
    	requete.append((new DateTime_iso8601()).format(new Date()));
    	requete.append("</dateTime.iso8601></value></member><member><name>subscriberNumberNAI</name><value><int>1</int></value></member>");
    	requete.append("<member><name>subscriberNumber</name><value><string>");
    	requete.append(msisdn);
    	requete.append("</string></value></member>");
    	if(originOperatorID!=null) {
        	requete.append("<member><name>originOperatorID</name><value><string>");
        	requete.append(originOperatorID);
        	requete.append("</string></value></member>");
        }
    	
    	requete.append(serviceClass);
    	requete.append(dedicatedAccountIDs);	
        			
    	return requete;
    }
    
    public boolean delete(SocketConnection air, String msisdn,Integer serviceClassCurrent ,HashSet<DedicatedAccount> dedicatedAccountIdentifier,String originOperatorID){
    	boolean responseCode = false;
    	
    	try{
        	if(air.isOpen()){
            	StringBuffer requete = formerRequete(msisdn, serviceClassCurrent, dedicatedAccountIdentifier,originOperatorID);
            	requete.append("</struct></value></param></params></methodCall>");
            	String reponse=air.execute(requete.toString());
                @SuppressWarnings("resource")
				Scanner sortie= new Scanner(reponse);
                    while(true){
                        String ligne=sortie.nextLine();
                        if(ligne==null) {
                            break;
                        }
                        else if(ligne.equals("<name>responseCode</name>")){
                            String code_reponse=sortie.nextLine();
                            int last=code_reponse.indexOf("</i4></value>");
                            responseCode = Integer.parseInt(code_reponse.substring(11, last))==0;
                            
                            break;
                        }
                    }        		
        	}
}
        catch(NoSuchElementException ex){
        	
        } finally {
           	air.fermer();

        }
        
    	return responseCode;
    }
    
}
