package ucip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

import connexions.SocketConnection;
import util.DateTime_iso8601;

public class UpdateOffer {
    
    public StringBuffer formerRequete(String msisdn, int offerID, Object startDate, Object expiryDate, Integer offerType, String originOperatorID) {
    	StringBuffer date = new StringBuffer("");

    	if(offerType == null) offerType=0;

    	if(offerType != 2) {
    		if(startDate != null) {
	    		if(startDate instanceof Integer) {	
	        			date.append("<member><name>startDateRelative</name><value><int>");
	        			date.append(((Integer)startDate));
	        			date.append("</int></value></member>");
	    		}
	    		else {
	    			date.append("<member><name>startDate</name><value><dateTime.iso8601>");
					date.append((new DateTime_iso8601()).format(((Date)startDate)));
					date.append("</dateTime.iso8601></value></member>");
	    		}
    		}

    		if(expiryDate != null) {
    			if(expiryDate instanceof Integer) {
    				date.append("<member><name>expiryDateRelative</name><value><int>");
    				date.append(((Integer)expiryDate));
    				date.append("</int></value></member>");
    			}
    		   else{
    				date.append("<member><name>expiryDate</name><value><dateTime.iso8601>");
    				date.append((new DateTime_iso8601()).format(((Date)expiryDate)));
    				date.append("</dateTime.iso8601></value></member>");
    		   }
    		}
    	}
    	else {
    		if(startDate != null) {
    			if(startDate instanceof Integer) {
    				date.append("<member><name>startDateTimeRelative</name><value><int>");
    				date.append(((Integer)startDate));
    				date.append("</int></value></member>");
    			}
    			else {
    				date.append("<member><name>startDateTime</name><value><dateTime.iso8601>");
    				date.append((new DateTime_iso8601()).format(((Date)startDate)));
    				date.append("</dateTime.iso8601></value></member>");
    			}
    		}
    		if(expiryDate != null) {
    			if(expiryDate instanceof Integer) {
    				date.append("<member><name>expiryDateTimeRelative</name><value><int>");
					date.append(((Integer)expiryDate));
    				date.append("</int></value></member>");
    			}
    			else {
    				date.append("<member><name>expiryDateTime</name><value><dateTime.iso8601>");
    				date.append((new DateTime_iso8601()).format(((Date)expiryDate)));
    				date.append("</dateTime.iso8601></value></member>");
    			}
    		}
    	}
    
    	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>UpdateOffer</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
    	requete.append((new SimpleDateFormat("yyMMddHHmmssS")).format(new Date()));
    	requete.append("</string></value></member><member><name>originTimeStamp</name><value><dateTime.iso8601>");
    	requete.append((new DateTime_iso8601()).format(new Date()));
    	requete.append("</dateTime.iso8601></value></member><member><name>subscriberNumberNAI</name><value><int>1</int></value></member>");
    	requete.append("<member><name>subscriberNumber</name><value><string>");
    	requete.append(msisdn);
    	requete.append("</string></value></member>");

    	if(originOperatorID != null) {
        	requete.append("<member><name>originOperatorID</name><value><string>");
        	requete.append(originOperatorID);
        	requete.append("</string></value></member>");
        }

    	requete.append("<member><name>offerID</name><value><int>");
    	requete.append(offerID);
    	requete.append("</int></value></member>");
    	requete.append("<member><name>offerType</name><value><int>");
		requete.append(offerType);
		requete.append("</int></value></member>");
    	requete.append(date);
    	
    	return requete;
    }
    
    public boolean update(SocketConnection air, String msisdn, int offerID, Object startDate, Object expiryDate, Integer offerType, String originOperatorID){
    	boolean responseCode = false;

    	try{
	    	if(air.isOpen()) {
	    		StringBuffer requete = formerRequete(msisdn, offerID,startDate,expiryDate,offerType,originOperatorID);
	    		requete.append("</struct></value></param></params></methodCall>");
	    		String reponse=air.execute(requete.toString());
	    		@SuppressWarnings("resource")
	    		Scanner sortie= new Scanner(reponse);

                while(true) {
                    String ligne=sortie.nextLine();
                    if(ligne==null) {
                        break;
                    }
                    else if(ligne.equals("<name>responseCode</name>")) {
                        String code_reponse=sortie.nextLine();
                        int last=code_reponse.indexOf("</i4></value>");
                        responseCode = Integer.parseInt(code_reponse.substring(11, last)) == 0;

                        break;
                    }
                }
	    	}
    	}

    catch(NoSuchElementException ex) {
    	
    } finally {
       	air.fermer();

       }
    
	return responseCode;
}
    
}
