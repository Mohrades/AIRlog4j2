package acip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

import connexions.SocketConnection;
import util.DateTime_iso8601;

public class GetPromotionCounters {

    public StringBuffer formerRequete(String msisdn){
        
    	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>GetPromotionCounters</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
    	requete.append((new SimpleDateFormat("yyMMddHHmmssS")).format(new Date()));
    	requete.append("</string></value></member><member><name>originTimeStamp</name><value><dateTime.iso8601>");
    	requete.append((new DateTime_iso8601()).format(new Date()));
    	requete.append("</dateTime.iso8601></value></member><member><name>subscriberNumberNAI</name><value><int>1</int></value></member>");
    	requete.append("<member><name>subscriberNumber</name><value><string>");
    	requete.append(msisdn);
    	requete.append("</string></value></member>");
    	
    	return requete;
    } 
    
    public void getData(SocketConnection air, String msisdn){
        try{
        	if(air.isOpen()) {
            	StringBuffer requete = formerRequete(msisdn);
            	requete.append("</struct></value></param></params></methodCall>");
                String reponse=air.execute(requete.toString());
                @SuppressWarnings("resource")
				Scanner sortie= new Scanner(reponse);  
            	
                while(true){
                      String ligne=sortie.nextLine(); 
                      if(ligne==null) {
                          break;
                      }
                }
        	}
        }
        catch(NoSuchElementException e){
        	
       } finally {
          	air.fermer();

          }
}
    
}
