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
import util.FafInformationList;
import util.FafInformation;

/**
 *
 * @author TestAppli
 */
public class GetFaFList {

    public StringBuffer formerRequete(String msisdn,int requestedOwner){
    	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>GetFaFList</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
    	requete.append((new SimpleDateFormat("yyMMddHHmmssS")).format(new Date()));
    	requete.append("</string></value></member><member><name>originTimeStamp</name><value><dateTime.iso8601>");
    	requete.append((new DateTime_iso8601()).format(new Date()));
    	requete.append("</dateTime.iso8601></value></member><member><name>subscriberNumberNAI</name><value><int>1</int></value></member>");
    	requete.append("<member><name>subscriberNumber</name><value><string>");
    	requete.append(msisdn);
    	requete.append("</string></value></member>");
    	
    	requete.append("<member><name>requestedOwner</name><value><int>");
    	requete.append(requestedOwner);
    	requete.append("</int></value></member>");
                      
    	return requete;
}
    public FafInformationList getData(SocketConnection air, String msisdn,int requestedOwner) {
        HashSet<FafInformation> fafInformationList=new HashSet<FafInformation>();
        Date fafChangeUnbarDate = null;
        boolean fafMaxAllowedNumbersReachedFlag = false;

        try {
        	if(air.isOpen()) {
            	StringBuffer requete = formerRequete(msisdn,requestedOwner);
            	requete.append("</struct></value></param></params></methodCall>");    
                String reponse=air.execute(requete.toString());
                @SuppressWarnings("resource")
				Scanner sortie= new Scanner(reponse);
                
              while(true){
                    String ligne=sortie.nextLine(); 
                    if(ligne==null) {
                        break;
                    }
                    else if(ligne.equals("<name>fafChangeUnbarDate</name>")){
                         String date=sortie.nextLine();
                         int last=date.indexOf("</dateTime.iso8601></value>");
                         date=date.substring(25, last);
                         fafChangeUnbarDate=(new DateTime_iso8601()).parse(date);
                    }
                   else if(ligne.equals("<name>fafMaxAllowedNumbersReachedFlag</name>")){
                         String flag=sortie.nextLine();
                         int last=flag.indexOf("</boolean></value>");
                         fafMaxAllowedNumbersReachedFlag=flag.substring(16,last).equals("1")?true:false;
                    } 
                    else if(ligne.equals("<name>fafInformationList</name>")){
                        String check=sortie.nextLine();               
                       while(!check.equals("</array>")){
                    	   if(check.equals("<struct>")){
                    		   String fafNumber = null;
                    		   int fafIndicator = 0;
                    		   while(!check.equals("</struct>")){
                    			   if(check.equals("<name>fafNumber</name>")){
                    				   String number=sortie.nextLine();
                    				   int last=number.indexOf("</string></value>");
                    				   fafNumber=number.substring(15, last);
                    			   }
                    			   else if(check.equals("<name>fafIndicator</name>")){
                    				   String indic=sortie.nextLine();
                    				   int last=indic.indexOf("</i4></value>");
                    				   fafIndicator=Integer.parseInt(indic.substring(11, last));
                    			   }
                    			   check=sortie.nextLine();  
                    		   }
                    		   fafInformationList.add(new FafInformation(fafNumber,fafIndicator));
                    		   check=sortie.nextLine();  
                    		   }
                    	   check=sortie.nextLine();
                    	   }
                    } 
              }        		
        	}

        }
        catch(NoSuchElementException e){
        	
        } finally {
          	air.fermer();

          }

        FafInformationList fafList = new FafInformationList(fafInformationList);
        fafList.setFafChangeUnbarDate(fafChangeUnbarDate);
        fafList.setFafMaxAllowedNumbersReachedFlag(fafMaxAllowedNumbersReachedFlag);
        return fafList;
}
    
}
