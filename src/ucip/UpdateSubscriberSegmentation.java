/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

import connexions.SocketConnection;
import util.DateTime_iso8601;
import util.ServiceOfferings;

/**
 *
 * @author TestAppli
 */
public class UpdateSubscriberSegmentation {

    public StringBuffer formerRequete(String msisdn,Integer accountGroupID,ServiceOfferings serviceOfferings,String originOperatorID) {
    	StringBuffer groupID=new StringBuffer("");

        if(accountGroupID == null);
        else {
            groupID.append("<member><name>accountGroupID</name><value><i4>");
            groupID.append(accountGroupID);
            groupID.append("</i4></value></member>");
        }

        HashSet<Integer> offerings=serviceOfferings.getServiceOfferingActiveFlags();
        StringBuffer serviceOffering=new StringBuffer("<member><name>serviceOfferings</name><value><array><data>");
        for (Integer id:offerings) {

        	int value = 0;
        	if(id < 0) {
        		id=-id;
        	}
        	else value = 1;

        	serviceOffering.append("<value><struct>");

            serviceOffering.append("<member><name>serviceOfferingActiveFlag</name><value><boolean>");
            serviceOffering.append(value);
            serviceOffering.append("</boolean></value></member><member><name>serviceOfferingID</name><value><i4>");
            serviceOffering.append(id);
            serviceOffering.append("</i4></value></member>");

            serviceOffering.append("</struct></value>");
        }
        serviceOffering.append("</data></array></value></member>"); 
        
        StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>UpdateSubscriberSegmentation</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
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
    	
    	requete.append(groupID);
    	requete.append(serviceOffering);	

    	return requete;
    }
    
    public boolean update(SocketConnection air, String msisdn,Integer accountGroupID,ServiceOfferings serviceOfferings,String originOperatorID){
    	boolean responseCode = false;
    	
    	try{
    	if(air.isOpen()) {
        	StringBuffer requete = formerRequete(msisdn, accountGroupID, serviceOfferings, originOperatorID);
        	requete.append("</struct></value></param></params></methodCall>");
            String reponse = air.execute(requete.toString());
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
