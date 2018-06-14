package ucip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

import connexions.SocketConnection;
import util.DateTime_iso8601;
import util.UsageCounterUsageThresholdInformation;
import util.UsageThreshold;

public class UpdateUsageThresholdsAndCounters {

    public StringBuffer formerRequete(String msisdn,HashSet<UsageCounterUsageThresholdInformation> usageCounterUpdateInformation,HashSet<UsageThreshold>usageThresholdUpdateInformation,String originOperatorID){
    	StringBuffer counter=counter(usageCounterUpdateInformation);
    	StringBuffer threshold=threshold(usageThresholdUpdateInformation);

    	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>UpdateUsageThresholdsAndCounters</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
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
    	
    	requete.append("<member><name>transactionCurrency</name><value><string>XOF</string></value></member>");
    	requete.append(counter);
    	requete.append(threshold);

    	return requete;
    }
    
    public boolean update(SocketConnection air, String msisdn,HashSet<UsageCounterUsageThresholdInformation> usageCounterUpdateInformation,HashSet<UsageThreshold>usageThresholdUpdateInformation,String originOperatorID){
    	boolean responseCode = false;
    	
    	try{
        	if(air.isOpen()){
            	StringBuffer requete = formerRequete(msisdn,usageCounterUpdateInformation,usageThresholdUpdateInformation,originOperatorID);
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
    
    public StringBuffer counter(HashSet<UsageCounterUsageThresholdInformation> usageCounterUpdateInformation){
    	if(usageCounterUpdateInformation==null)return new StringBuffer("");
    	else{
    	StringBuffer counter=new StringBuffer("<member><name>usageCounterUpdateInformation</name><value><array><data>");
    	for(UsageCounterUsageThresholdInformation usageCounter:usageCounterUpdateInformation){
    		counter.append("<value><struct><member><name>usageCounterID</name><value><int>");
    		counter.append(usageCounter.getUsageCounterID());
    		counter.append("</int></value></member>");
    		if(usageCounter.isAdjustmentUsageCounterRelative()){
                    if(!usageCounter.isUsageCounterMonetary()){
                    	counter.append("<member><name>adjustmentUsageCounterValueRelative</name><value><string>");
                    	counter.append(usageCounter.getUsageCounterValue());
                    	counter.append("</string></value></member></struct></value>");
                    }
                    else{
                    	counter.append("<member><name>adjustmentUsageCounterMonetaryValueRelative</name><value><string>");
                    	counter.append(usageCounter.getUsageCounterValue());
                    	counter.append("</string></value></member></struct></value>");
                    }
           }
    		else{
                if(!usageCounter.isUsageCounterMonetary()){
                	counter.append("<member><name>usageCounterValueNew</name><value><string>");
                	counter.append(usageCounter.getUsageCounterValue());
                	counter.append("</string></value></member></struct></value>");
                }
                else{
                	counter.append("<member><name>usageCounterMonetaryValueNew</name><value><string>");
                	counter.append(usageCounter.getUsageCounterValue());
                	counter.append("</string></value></member></struct></value>");
                }
    		}
    	}
    	counter.append("</data></array></value></member>");
    	return counter;
    }
    }
    
    public StringBuffer threshold(HashSet<UsageThreshold>usageThresholdUpdateInformation){
    	if(usageThresholdUpdateInformation==null)return new StringBuffer("");
    	else{
    	StringBuffer threshold=new StringBuffer("<member><name>usageThresholdUpdateInformation</name><value><array><data>");
    	for(UsageThreshold usageThreshold:usageThresholdUpdateInformation){
    		threshold.append("<value><struct><member><name>usageThresholdID</name><value><int>");
    		threshold.append(usageThreshold.getUsageThresholdID());
    		threshold.append("</int></value></member>");
                if(!usageThreshold.isMonetary()){
                	threshold.append("<member><name>usageThresholdValueNew </name><value><string>");
                	threshold.append(usageThreshold.getValue());
                	threshold.append("</string></value></member></struct></value>");
                }
                else{
                	threshold.append("<member><name>usageThresholdMonetaryValueNew</name><value><string>");
                	threshold.append(usageThreshold.getValue());
                	threshold.append("</string></value></member></struct></value>");
                }
    		}
    	threshold.append("</data></array></value></member>");
    	return threshold;
    }
    }

}
