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
public class UpdateCommunityList {

    public StringBuffer formerRequete(String msisdn, int[] communityOldIDs, int[] communityNewIDs, String originOperatorID){
    	StringBuffer communityOld=new StringBuffer("<member><name>communityInformationCurrent</name><value><array><data>");
        int nbre = communityOldIDs.length;

        for (int i = 0; i < nbre; i++) {
        	communityOld.append("<value><struct>");

            communityOld.append("<member><name>communityID</name><value><i4>");
            communityOld.append(communityOldIDs[i]);
            communityOld.append("</i4></value></member>");

            communityOld.append("</struct></value>");
        }
        communityOld.append("</data></array></value></member>");


        StringBuffer communityNew = new StringBuffer("<member><name>communityInformationNew</name><value><array><data>");
        nbre = communityNewIDs.length;

        for (int i = 0; i < nbre; i++) {
        	communityNew.append("<value><struct>");

        	communityNew.append("<member><name>communityID</name><value><i4>");
        	communityNew.append(communityNewIDs[i]);
        	communityNew.append("</i4></value></member>");

        	communityNew.append("</struct></value>");
        }
        communityNew.append("</data></array></value></member>");


        StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>UpdateCommunityList</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
    	requete.append((new SimpleDateFormat("yyMMddHHmmssS")).format(new Date()));
    	requete.append("</string></value></member><member><name>originTimeStamp</name><value><dateTime.iso8601>");
    	requete.append((new DateTime_iso8601()).format(new Date()));
    	requete.append("</dateTime.iso8601></value></member><member><name>subscriberNumberNAI</name><value><int>1</int></value></member>");
    	requete.append("<member><name>subscriberNumber</name><value><string>");
    	requete.append(msisdn);
    	requete.append("</string></value></member>");

    	if(originOperatorID != null) {
        	requete.append("<member><name>originOperatorID</name><value><string>");
        	requete.append(originOperatorID);
        	requete.append("</string></value></member>");
        	}
    	
    	requete.append(communityOld);
    	requete.append(communityNew);
    	
    	return requete;
            										
    }

    public boolean update(SocketConnection air, String msisdn,int[] communityOldIDs,int[] communityNewIDs,String originOperatorID){
    	boolean responseCode = false;
    	
    	try{
    	if(air.isOpen()){
    		StringBuffer requete = formerRequete(msisdn,communityOldIDs,communityNewIDs,originOperatorID);
    		
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
