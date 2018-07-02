package ucip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

import connexions.SocketConnection;
import util.DateTime_iso8601;

public class Refill {
	
    
    public StringBuffer formerRequete(String msisdn,String transactionAmount,String transactionCurrency,String refillProfileID,String voucherActivationCode,String originOperatorID,String transactionType,String transactionCode){
    	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>Refill</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
    	requete.append((new SimpleDateFormat("yyMMddHHmmssS")).format(new Date()));
    	requete.append("</string></value></member><member><name>originTimeStamp</name><value><dateTime.iso8601>");
    	requete.append((new DateTime_iso8601()).format(new Date()));
    	requete.append("</dateTime.iso8601></value></member><member><name>subscriberNumberNAI</name><value><int>1</int></value></member>");
    	requete.append("<member><name>subscriberNumber</name><value><string>");
    	requete.append(msisdn);
    	requete.append("</string></value></member>");

    	if((transactionAmount != null) && (transactionCurrency != null) && (refillProfileID != null)) {
    		requete.append("<member><name>transactionAmount</name><value><string>");
        	requete.append(transactionAmount);
        	requete.append("</string></value></member>");
        	requete.append("<member><name>transactionCurrency</name><value><string>");
        	requete.append(transactionCurrency);
        	requete.append("</string></value></member>");
        	requete.append("<member><name>refillProfileID</name><value><string>");
        	requete.append(refillProfileID);
        	requete.append("</string></value></member>");
    	}
    	else if(voucherActivationCode != null){
    		requete.append("<member><name>voucherActivationCode</name><value><string>");
        	requete.append(voucherActivationCode);
        	requete.append("</string></value></member>");
    	}
    	
    	requete.append("<member><name>requestRefillAccountAfterFlag</name><value><boolean>1</boolean></value></member>");
    	requete.append("<member><name>requestRefillAccountBeforeFlag</name><value><boolean>1</boolean></value></member>");
    	requete.append("<member><name>requestRefillDetailsFlag</name><value><boolean>1</boolean></value></member>");
    	//requete.append("<member><name>locationNumber</name><value><string>"+number+"</string></value></member>");
    	//requete.append("<member><name>locationNumberNAI</name><value><int>1</int></value></member>");
    	//requete.append("<member><name>validateSubscriberLocation</name><value><boolean>1</boolean></value></member>");
    	if(transactionType != null)
    		requete.append("<member><name>transactionType</name><value><string>"+transactionType+"</string></value></member>");
    	else
    		requete.append("<member><name>transactionType</name><value><string>refill</string></value></member>");
    	if(transactionCode != null)
    		requete.append("<member><name>transactionCode</name><value><string>"+transactionCode+"</string></value></member>");
    	else
    		requete.append("<member><name>transactionCode</name><value><string>account</string></value></member>");
    	
    	requete.append("<member><name>refillType</name><value><int>2</int></value></member>");
    	
    	if(originOperatorID!=null){
        	requete.append("<member><name>originOperatorID</name><value><string>");
        	requete.append(originOperatorID);
        	requete.append("</string></value></member>");
        	}
    	requete.append("<member><name>messageCapabilityFlag</name><value><struct><member><name>accountActivationFlag</name><value><boolean>1</boolean></value></member></struct></value></member>");
    
    	return requete;
    }
    
    public boolean update(SocketConnection air, String msisdn,String transactionAmount,String transactionCurrency,String refillProfileID,String voucherActivationCode,String originOperatorID,String transactionType,String transactionCode){
    	HashMap<String, Long> details=new HashMap<String, Long>();
    	boolean responseCode = false;
    	
    	try{
    	if(air.isOpen()){
        	StringBuffer requete = formerRequete(msisdn,transactionAmount,transactionCurrency,refillProfileID,voucherActivationCode,originOperatorID,transactionType,transactionCode);
        	requete.append("</struct></value></param></params></methodCall>");
        	String reponse=air.execute(requete.toString());

            @SuppressWarnings("resource")
			Scanner sortie= new Scanner(reponse);
            String flag = "-";

                while(true){
                    String ligne=sortie.nextLine();
                    if(ligne==null) {
                        break;
                    }
                    else if(ligne.equals("<name>faultCode</name>")){
                    	return false;
                    }
                    else if(details.size() == 4){
                    	return details.get("responseCode") == 0;
                    }
                    else if(ligne.equals("<name>accountAfterRefill</name>")){
                        flag = "After";
                    }
                    else if(flag.equals("After"))
                    	{
                        if(ligne.equals("<name>accountValue1</name>")){
                    	
                        String price = sortie.nextLine();
                        int last = price.indexOf("</string></value>");
                        details.put("balanceAfter", Long.parseLong(price.substring(15, last)));

                        flag = "-";
                        }
                    }
                    else if(ligne.equals("<name>accountBeforeRefill</name>")){
                        flag = "Before";
                    }
                    else if(flag.equals("Before")){ 
                    	if(ligne.equals("<name>accountValue1</name>")){
                        String price = sortie.nextLine();
                        int last=price.indexOf("</string></value>");
                        details.put("balanceBefore", Long.parseLong(price.substring(15, last)));
                        flag="-";
                    	}   
                    }
                    else if(ligne.equals("<name>responseCode</name>")){
                        String price = sortie.nextLine();
                        int last = price.indexOf("</i4></value>");
                        details.put("responseCode", Long.parseLong(price.substring(11, last)));
                    }
                    else if(ligne.equals("<name>transactionAmount</name>")){
                        String price = sortie.nextLine();
                        int last = price.indexOf("</string></value>");
                        details.put("transactionAmount", Long.parseLong(price.substring(15, last)));
                    }
                }
                
                responseCode = details.get("responseCode") == 0;   		
    	}
    }
    catch(NoSuchElementException ex){
    	
    }
    catch(Exception ex){

    } finally {
      	air.fermer();

      }
    
	return responseCode;
}
    
}
