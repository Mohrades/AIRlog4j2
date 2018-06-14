package ucip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

import connexions.SocketConnection;
import util.DateTime_iso8601;
import util.UsageCounterUsageThresholdInformation;

public class GetUsageThresholdsAndCounters {

    public StringBuffer formerRequete(String msisdn,String originOperatorID){
    	
    	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>GetUsageThresholdsAndCounters</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
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
    	
    	return requete;
    }
    
    public HashSet<UsageCounterUsageThresholdInformation> getData(SocketConnection air, String msisdn,String originOperatorID){
        HashSet<UsageCounterUsageThresholdInformation>counters=new HashSet<UsageCounterUsageThresholdInformation>();
        
        try{
        	if(air.isOpen()) {
            	StringBuffer requete = formerRequete(msisdn,originOperatorID);
            	requete.append("</struct></value></param></params></methodCall>");
            	String reponse=air.execute(requete.toString());
                @SuppressWarnings("resource")
				Scanner sortie= new Scanner(reponse);
                
                while(true){
                    String ligne=sortie.nextLine(); 
                    if(ligne==null) {
                        break;
                    }
                    else if(ligne.equals("<name>usageCounterUsageThresholdInformation</name>")){
                  	  String check=sortie.nextLine();
                        while(!check.equals("</array>")){
                            if(check.equals("<struct>")){
                          	  UsageCounterUsageThresholdInformation counter=new UsageCounterUsageThresholdInformation();
                          	  while(!check.equals("</struct>")){ 
                          		  if(check.equals("<name>usageCounterID</name>")){
                          			  String chaine=sortie.nextLine();
                                        int last=chaine.indexOf("</i4></value>");
                                        counter.setUsageCounterID(Integer.parseInt(chaine.substring(11, last)));
                          		  }
                          		  else if(check.equals("<name>usageCounterValue</name>")){
                          			  String chaine=sortie.nextLine();
                                        int last=chaine.indexOf("</string></value>");
                                        counter.setUsageCounterValue(Long.parseLong(chaine.substring(15, last)));
                          		  }
                          		  else if(check.equals("<name>usageThresholdInformation</name>")){
                          			  sortie:while(!check.equals("</array>")){
                          			  String usageThresholdID = null;
                          			  while(!check.equals("</struct>")){ 
                                  		  if(check.equals("<name>usageThresholdID</name>")){
                                  			  String chaine=sortie.nextLine();
                                                int last=chaine.indexOf("</i4></value>");
                                                usageThresholdID=chaine.substring(11, last);
                                  			  }
                                  		  else if(check.equals("<name>usageThresholdValue</name>")){
                                  			  if(usageThresholdID.equals(counter.getUsageCounterID()+"100")){
                                  				  String chaine=sortie.nextLine();
                                  				  int last=chaine.indexOf("</string></value>");
                                  				  counter.setUsageThresholdValue100(Long.parseLong(chaine.substring(15, last)));
                                  				  while(!sortie.nextLine().equals("</array>"));
                                  				  break sortie;
                                  			  } 
                                  			  }
                                  		  check=sortie.nextLine();
                                  		  }
                          			  check=sortie.nextLine();
                          			  }
                          		  }
                          		  check=sortie.nextLine(); 
                          	  }
                          	  counters.add(counter);
                          	  }
                            check=sortie.nextLine(); 
                        }
                        break;
                    }
            }
              
        	}
}
        catch(NoSuchElementException e){  
        	
       } finally {
          	air.fermer();

          }
        return counters;
    }
    
}
