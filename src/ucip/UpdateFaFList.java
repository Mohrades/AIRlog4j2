package ucip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

import util.DateTime_iso8601;
import util.FafInformationList;
import util.FafInformation;
import connexions.SocketConnection;

public class UpdateFaFList {

    public StringBuffer formerRequete(String msisdn,String fafAction,FafInformationList fafInformation,String originOperatorID){
    	StringBuffer fafInformations = new StringBuffer("");

    	HashSet<FafInformation> fafInformationList = fafInformation.getList();
    	int nbre=fafInformationList.size();

    	if(nbre==1) {
    		for(FafInformation faf:fafInformationList) {
	    		fafInformations.append("<member><name>fafInformation</name><value><struct><member><name>fafNumber</name><value><string>");
	    		fafInformations.append(faf.getFafNumber());
	    		fafInformations.append("</string></value></member><member><name>owner</name><value><string>");
	    		fafInformations.append(faf.getOwner());
	    		fafInformations.append("</string></value></member><member><name>fafIndicator</name><value><i4>");
	    		fafInformations.append(faf.getFafIndicator());
	    		fafInformations.append("</i4></value></member></struct></value></member>");
    		}
    	}
    	else {
    		fafInformations=new StringBuffer("<member><name>fafInformationList</name><value><array><data>");

    		for(FafInformation faf:fafInformationList){
    			fafInformations.append("<value><member><name>fafInformation</name><value><struct>");

    			fafInformations.append("<member><name>fafNumber</name><value><string>");
    			fafInformations.append(faf.getFafNumber());
    			fafInformations.append("</string></value></member><member><name>owner</name><value><string>");
    			fafInformations.append(faf.getOwner());
    			fafInformations.append("</string></value></member><member><name>fafIndicatorList</name><value><array><data><value><struct><member><name>fafIndicator</name><value><i4>");
    			fafInformations.append(faf.getFafIndicator());
    			fafInformations.append("</i4></value></member></struct></value></data></array></value></member>");

    			fafInformations.append("</struct></value></member></value>");
    		}

    		fafInformations.append("</data></array></value></member>");
    	}
    	
    	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>UpdateFaFList</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
    	requete.append((new SimpleDateFormat("yyMMddHHmmssS")).format(new Date()));
    	requete.append("</string></value></member><member><name>originTimeStamp</name><value><dateTime.iso8601>");
    	requete.append((new DateTime_iso8601()).format(new Date()));
    	requete.append("</dateTime.iso8601></value></member><member><name>subscriberNumberNAI</name><value><int>1</int></value></member>");
    	requete.append("<member><name>subscriberNumber</name><value><string>");
    	requete.append(msisdn);
    	requete.append("</string></value></member>");

    	if(originOperatorID!=null) {
        	requete.append("<member><name>originOperatorID</name><value><string>");
        	requete.append(originOperatorID);
        	requete.append("</string></value></member>");
        }
    	
    	requete.append("<member><name>fafAction</name><value><string>");
    	requete.append(fafAction);
    	requete.append("</string></value></member>");
    	requete.append(fafInformations);
    	
    	return requete;				
    }
    
    public boolean update(SocketConnection air, String msisdn,String fafAction,FafInformationList fafInformation,String originOperatorID){
    	boolean responseCode = false;

    	try{
        	if(air.isOpen()) {
            	StringBuffer requete = formerRequete(msisdn, fafAction, fafInformation,originOperatorID);
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
    
}
