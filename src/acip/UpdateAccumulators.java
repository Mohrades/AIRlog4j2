package acip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

import util.AccumulatorInformation;
import util.DateTime_iso8601;
import connexions.SocketConnection;

public class UpdateAccumulators {

    public StringBuffer formerRequete(String msisdn,HashSet<AccumulatorInformation> accumulatorUpdateInformation,String originOperatorID){
    	StringBuffer accumulatorInformation=new StringBuffer("<member><name>accumulatorUpdateInformation</name><value><array><data>");
    	for (AccumulatorInformation accumulator:accumulatorUpdateInformation){
    		accumulatorInformation.append("<value><struct><member><name>accumulatorID</name><value><i4>");
    		accumulatorInformation.append(accumulator.getAccumulatorID());
    		accumulatorInformation.append("</i4></value></member>");
    		if(accumulator.getAccumulatorStartDate()!=null){
    			accumulatorInformation.append("<member><name>accumulatorStartDate</name><value><dateTime.iso8601>");
    			accumulatorInformation.append((new DateTime_iso8601()).format(accumulator.getAccumulatorStartDate()));
    			accumulatorInformation.append("</dateTime.iso8601></value></member>");
    		}
    		if(accumulator.isAccumulatorValueRelative()){
    			accumulatorInformation.append("<member><name>accumulatorValueRelative</name><value><i4>");
    			accumulatorInformation.append(accumulator.getAccumulatorValue());
    			accumulatorInformation.append("</i4></value></member>");
    		}
    		else{
    			accumulatorInformation.append("<member><name>accumulatorValueAbsolute</name><value><i4>");
    			accumulatorInformation.append(accumulator.getAccumulatorValue());
    			accumulatorInformation.append("</i4></value></member>");
    		}  					
    		accumulatorInformation.append("</struct></value>");
}
    	accumulatorInformation.append("</data></array></value></member>");
    	
    	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>UpdateAccumulators</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
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
    	
    	requete.append(accumulatorInformation);
    	
    	return requete;
    }
    
    public boolean update(SocketConnection air, String msisdn,HashSet<AccumulatorInformation> accumulatorUpdateInformation,String originOperatorID){
    	boolean responseCode = false;
    	
    	try {
        	if(air.isOpen()){
            	StringBuffer requete = formerRequete(msisdn, accumulatorUpdateInformation,originOperatorID);
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
