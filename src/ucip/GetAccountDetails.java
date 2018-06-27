/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

import connexions.SocketConnection;
import util.AccountDetails;
import util.DateTime_iso8601;

/**
 *
 * @author TestAppli
 */
public class GetAccountDetails {

    public GetAccountDetails() {

    }

    public StringBuffer formerRequete(String msisdn) {
    	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>GetAccountDetails</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
    	requete.append((new SimpleDateFormat("yyMMddHHmmssS")).format(new Date()));
    	requete.append("</string></value></member><member><name>originTimeStamp</name><value><dateTime.iso8601>");
    	requete.append((new DateTime_iso8601()).format(new Date()));
    	requete.append("</dateTime.iso8601></value></member><member><name>subscriberNumberNAI</name><value><int>1</int></value></member>");
    	requete.append("<member><name>subscriberNumber</name><value><string>");
    	requete.append(msisdn);
    	requete.append("</string></value></member>");

    	return requete;
    }

    public AccountDetails getData(SocketConnection air, String msisdn) {
    	HashMap<String,String> details=new HashMap<String,String>();

        try {
        	if(air.isOpen()) {
        		StringBuffer requete = formerRequete(msisdn);
            	requete.append("</struct></value></param></params></methodCall>");
                String reponse=air.execute(requete.toString());
                Scanner sortie= new Scanner(reponse);

                while(true){
                      String ligne=sortie.nextLine(); 
                      if(ligne==null) {
                          break;
                      }
                      else{
                      if(ligne.equals("<name>accountFlags</name>")){
                          decaller(sortie,4);
                          String activationStatusFlag=sortie.nextLine();
                          int last=activationStatusFlag.indexOf("</boolean></value>");
                          details.put("accountFlags",activationStatusFlag.substring(16,last));
                          while(!(sortie.nextLine().equals("</struct>"))){
                              
                          }
                          decaller(sortie,2);
                      }
                      else if(ligne.equals("<name>accountGroupID</name>")){
                          String accountGroupID=sortie.nextLine();
                          int last=accountGroupID.indexOf("</i4></value>");
                          details.put("accountGroupID",accountGroupID.substring(11, last));
                      }
                      else if(ligne.equals("<name>activationDate</name>")){
                          String activationDate=sortie.nextLine();
                          int last=activationDate.indexOf("</dateTime.iso8601></value>");
                          details.put("activationDate",activationDate.substring(25, last));
                      }
                      else if(ligne.equals("<name>communityInformationCurrent</name>")) {
                          String communityIDs="";
                          String check=sortie.nextLine();
                          while(!check.equals("</array>")){
                              if(check.equals("<name>communityID</name>")){
                                  String id=sortie.nextLine();
                                  int last=id.indexOf("</i4></value>");
                                  communityIDs+=id.substring(11, last)+"-";
                              }
                          check=sortie.nextLine();  
                      }
                          int len=communityIDs.length();
                          communityIDs=communityIDs.substring(0, len-1);
                          details.put("communityIDs", communityIDs);
                          decaller(sortie,2);
                  }
                     else if(ligne.equals("<name>languageIDCurrent</name>")){
                          String languageIDCurrent=sortie.nextLine();
                          int last=languageIDCurrent.indexOf("</i4></value>");
                          details.put("languageIDCurrent",languageIDCurrent.substring(11, last));
                      }
                     else if(ligne.equals("<name>serviceClassCurrent</name>")){
                         String serviceClassCurrent=sortie.nextLine();
                         int last=serviceClassCurrent.indexOf("</i4></value>");
                         details.put("serviceClassCurrent",serviceClassCurrent.substring(11, last));
                     }
                     else if(ligne.equals("<name>serviceClassOriginal</name>")){
                         String serviceClassOriginal=sortie.nextLine();
                         int last=serviceClassOriginal.indexOf("</i4></value>");
                         details.put("serviceClassOriginal",serviceClassOriginal.substring(11, last));
                     }
                     else if(ligne.equals("<name>serviceClassTemporaryExpiryDate</name>")){
                         String serviceClassTemporaryExpiryDate=sortie.nextLine();
                          int last=serviceClassTemporaryExpiryDate.indexOf("</dateTime.iso8601></value>");
                          details.put("serviceClassTemporaryExpiryDate",serviceClassTemporaryExpiryDate.substring(25, last));
                     }
                     else if(ligne.equals("<name>supervisionExpiryDate</name>")) {
                          String supervisionExpiryDate=sortie.nextLine();
                          int last=supervisionExpiryDate.indexOf("</dateTime.iso8601></value>");
                          details.put("supervisionExpiryDate",supervisionExpiryDate.substring(25, last));
                     }
                     else if(ligne.equals("<name>serviceOfferings</name>")) {
                          String serviceOfferings="";
                          String check=sortie.nextLine();

                          while(!check.equals("</array>")) {
                              if(check.equals("<name>serviceOfferingActiveFlag</name>")) {
                                  String flag=sortie.nextLine();
                                  int last=flag.indexOf("</boolean></value>");
                                  serviceOfferings+=(flag.substring(16,last)+" ");
                              }
                              else if(check.equals("<name>serviceOfferingID</name>")) {
                                  String id=sortie.nextLine();
                                  int last=id.indexOf("</i4></value>");
                                  serviceOfferings+=(id.substring(11, last)+" ");
                              }

                          check=sortie.nextLine();  
                      }
                          int len=serviceOfferings.length();
                          serviceOfferings=serviceOfferings.substring(0, len-1);
                          details.put("serviceOfferings", serviceOfferings);
                          decaller(sortie,2);
                     }
                  }
                }
        	}

        } catch(NoSuchElementException e){  

        } finally {
        	air.fermer();
        }

        if(details.isEmpty()) return null;
        return new AccountDetails(details);
    }
    
    public void decaller(Scanner sortie,int lignes){
        for (int i=0;i<lignes;i++){
            sortie.nextLine();
        }
    }

}
