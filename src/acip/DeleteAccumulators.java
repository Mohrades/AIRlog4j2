package acip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

import util.AccumulatorInformation;
import util.DateTime_iso8601;
import connexions.SocketConnection;

public class DeleteAccumulators {

    public StringBuffer formerRequete(String msisdn,Integer serviceClassCurrent ,HashSet<AccumulatorInformation> accumulatorIdentifier,String originOperatorID){
    	StringBuffer serviceClass = new StringBuffer("");
    	StringBuffer accumulatorIDs = new StringBuffer("");

    	if(serviceClassCurrent != null) {
    		serviceClass.append("<member><name>serviceClassCurrent</name><value><i4>");
    		serviceClass.append(serviceClassCurrent);
    		serviceClass.append("</i4></value></member>");
    	}

    	for (AccumulatorInformation accumulator:accumulatorIdentifier) {
    		accumulatorIDs.append("<value><struct>");

    		accumulatorIDs.append("<member><name>accumulatorID</name><value><int>");
    		accumulatorIDs.append(accumulator.getAccumulatorID());
    		accumulatorIDs.append("</int></value></member>");

    		if(accumulator.getAccumulatorEndDate()!=null){
    			accumulatorIDs.append("<member><name>accumulatorEndDate</name><value><dateTime.iso8601>");
    			accumulatorIDs.append((new DateTime_iso8601()).format(accumulator.getAccumulatorEndDate()));
    			accumulatorIDs.append("</dateTime.iso8601></value></member>");
    		}

    		accumulatorIDs.append("</struct></value>");
    	}
    	accumulatorIDs.append("</data></array></value></member>");
    	StringBuffer entete = new StringBuffer("<member><name>accumulatorIdentifier</name><value><array><data>");
    	accumulatorIDs = entete.append(accumulatorIDs);

    	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>DeleteAccumulators</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
    	requete.append((new SimpleDateFormat("yyMMddHHmmssS")).format(new Date()));
    	requete.append("</string></value></member><member><name>originTimeStamp</name><value><dateTime.iso8601>");
    	requete.append((new DateTime_iso8601()).format(new Date()));
    	requete.append("</dateTime.iso8601></value></member><member><name>subscriberNumberNAI</name><value><int>1</int></value></member>");
    	requete.append("<member><name>subscriberNumber</name><value><string>");
    	requete.append(msisdn);
    	requete.append("</string></value></member>");
    	if(originOperatorID!=null){
    	requete.append("<member><name>originOperatorID</name><value><string>");
    	requete.append(originOperatorID);
    	requete.append("</string></value></member>");
    	}
    	
    	requete.append(serviceClass);
    	requete.append(accumulatorIDs);
    	
    	return requete;
    }
    
    public boolean delete(SocketConnection air, String msisdn,Integer serviceClassCurrent ,HashSet<AccumulatorInformation> accumulatorIdentifier,String originOperatorID){
    	boolean responseCode = false;

    	try {
        	if(air.isOpen()){
            	StringBuffer requete = formerRequete(msisdn, serviceClassCurrent, accumulatorIdentifier,originOperatorID);
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
                            responseCode = Integer.parseInt(code_reponse.substring(11, last)) == 0;
                            
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
