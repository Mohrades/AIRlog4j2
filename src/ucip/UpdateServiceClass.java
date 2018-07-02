/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

import connexions.SocketConnection;
import util.DateTime_iso8601;

/**
 *
 * @author TestAppli
 */
public class UpdateServiceClass {

    public StringBuffer formerRequete(String msisdn,String serviceClassAction,int serviceClassNew,int serviceClassTemporaryNew,Date serviceClassTemporaryNewExpiryDate,String originOperatorID){ 
    	StringBuffer date=new StringBuffer("");

        if(serviceClassTemporaryNewExpiryDate == null);
        else {        
            date.append("<member><name>serviceClassTemporaryNewExpiryDate</name><value><dateTime.iso8601>");
            date.append((new DateTime_iso8601()).format(serviceClassTemporaryNewExpiryDate));
            date.append("</dateTime.iso8601></value></member>");
       }
        
        StringBuffer chaine = new StringBuffer("");
        if(serviceClassAction.equals("SetOriginal")){
            chaine.append("<member><name>serviceClassNew</name><value><i4>");
            chaine.append(serviceClassNew);
            chaine.append("</i4></value></member>");;
        }
        else if(serviceClassAction.equals("SetTemporary")){
        	chaine.append("<member><name>serviceClassTemporaryNew</name><value><i4>");
        	chaine.append(serviceClassTemporaryNew);
        	chaine.append("</i4></value></member>");
        	chaine.append(date);                                                                          
        }
        else if(serviceClassAction.equals("Set")){
            if(date.toString().equals("")){
            	chaine.append("<member><name>serviceClassNew</name><value><i4>");
            	chaine.append(serviceClassNew);
            	chaine.append("</i4></value></member>");
            }
            else{
            	chaine.append("<member><name>serviceClassTemporaryNew</name><value><i4>");
            	chaine.append(serviceClassTemporaryNew);
            	chaine.append("</i4></value></member>");
            	chaine.append(date);  
            }
        }
        else if(serviceClassAction.equals("DeleteTemporary")){
        	chaine.append("<member><name>serviceClassTemporary</name><value><i4>");
        	chaine.append(serviceClassTemporaryNew);
        	chaine.append("</i4></value></member>");
        }
       
        StringBuffer requete = new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>UpdateServiceClass</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
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
    	
    	requete.append("<member><name>serviceClassAction</name><value><string>");
    	requete.append(serviceClassAction);
    	requete.append("</string></value></member>");
    	requete.append(chaine);
				
    	return requete;
    }
    
    public boolean update(SocketConnection air, String msisdn,String serviceClassAction,int serviceClassNew,int serviceClassTemporaryNew,Date serviceClassTemporaryNewExpiryDate,String originOperatorID){
    	boolean responseCode = false;
    	
    	try{
	    	if(air.isOpen()){
	        	StringBuffer requete = formerRequete(msisdn,serviceClassAction,serviceClassNew,serviceClassTemporaryNew,serviceClassTemporaryNewExpiryDate,originOperatorID);
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
    } catch(NoSuchElementException ex){

    } finally {
       	air.fermer();
    }

	return responseCode;
}
    
}