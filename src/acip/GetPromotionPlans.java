package acip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

import util.DateTime_iso8601;
import util.PromotionPlanInformation;
import connexions.SocketConnection;

public class GetPromotionPlans {

    public StringBuffer formerRequete(String msisdn,String originOperatorID){
        
    	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>GetPromotionPlans</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
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
   
    public HashSet<PromotionPlanInformation> getData(SocketConnection air, String msisdn,String originOperatorID){
        HashSet<PromotionPlanInformation> liste=new HashSet<PromotionPlanInformation>();
        
        try {
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
                    else if(ligne.equals("<name>promotionPlanInformation</name>")){
                        String check=sortie.nextLine();
                        while(!check.equals("</array>")){
                            if(check.equals("<struct>")){
                                String id = "";
                                Date start = null,end = null;
                                while(!check.equals("</struct>")){
                                if(check.equals("<name>promotionStartDate</name>")){
                                    String date=sortie.nextLine();
                                    int last=date.indexOf("</dateTime.iso8601></value>");
                                    date=date.substring(25, last);
                                    start=(new DateTime_iso8601()).parse(date);
                                }
                                else if(check.equals("<name>promotionPlanID</name>")){
                                    String chaine=sortie.nextLine();
                                    int last=chaine.indexOf("</string></value>");
                                    id=chaine.substring(15, last);
                                }
                                else if(check.equals("<name>promotionEndDate</name>")){
                                    String date=sortie.nextLine();
                                    int last=date.indexOf("</dateTime.iso8601></value>");
                                    date=date.substring(25, last);
                                    end=(new DateTime_iso8601()).parse(date);
                                }
                                check=sortie.nextLine();
                                }
                           liste.add(new PromotionPlanInformation(id,start,end));
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
        
		return liste;
}
    
}
