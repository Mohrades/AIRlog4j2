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
import util.AccumulatorInformation;
import util.DateTime_iso8601;

/**
 *
 * @author TestAppli
 */
public class GetAccumulators {

    public StringBuffer formerRequete(String msisdn,int[][]accumulatorSelection){
    	StringBuffer chaine= new StringBuffer("");
    	if(accumulatorSelection!=null){
    	int a=accumulatorSelection.length;
        chaine.append("<member><name>accumulatorSelection</name><value><array><data><value>");
        for (int j=0;j<a;j++){ 
        	chaine.append("<struct><member><name>accumulatorIDFirst</name><value><int>");
        	chaine.append(accumulatorSelection[j][0]);
        	chaine.append("</int></value></member><member><name>accumulatorIDLast</name><value><int>");
        	chaine.append(accumulatorSelection[j][1]);
        	chaine.append("</int></value></member></struct>");
        }
        chaine.append("</value></data></array></value></member>");
        }
    	
    	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>GetAccumulators</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
    	requete.append((new SimpleDateFormat("yyMMddHHmmssS")).format(new Date()));
    	requete.append("</string></value></member><member><name>originTimeStamp</name><value><dateTime.iso8601>");
    	requete.append((new DateTime_iso8601()).format(new Date()));
    	requete.append("</dateTime.iso8601></value></member><member><name>subscriberNumberNAI</name><value><int>1</int></value></member>");
    	requete.append("<member><name>subscriberNumber</name><value><string>");
    	requete.append(msisdn);
    	requete.append("</string></value></member>");
    	
    	requete.append(chaine);
    			
    	return requete;
    }
    public HashSet<AccumulatorInformation> getData(SocketConnection air, String msisdn,int[][]accumulatorSelection){
    	HashSet<AccumulatorInformation> liste=new HashSet<AccumulatorInformation>();

    	try{
        	if(air.isOpen()){
            	StringBuffer requete=formerRequete(msisdn,accumulatorSelection);
            	requete.append("</struct></value></param></params></methodCall>");	
                String reponse=air.execute(requete.toString());
                @SuppressWarnings("resource")
				Scanner sortie= new Scanner(reponse);

              while(true){
                    String ligne=sortie.nextLine(); 
                    if(ligne==null) {
                        break;
                    }
                    else if(ligne.equals("<name>accumulatorInformation</name>")){
                        String check=sortie.nextLine();
                        while(!check.equals("</array>")){
                            if(check.equals("<struct>")){
                                int id = 0,value = 0;
                                Date start = null,end = null;
                                while(!check.equals("</struct>")){
                                if(check.equals("<name>accumulatorEndDate</name>")){
                                    String date=sortie.nextLine();
                                    int last=date.indexOf("</dateTime.iso8601></value>");
                                    date=date.substring(25, last);
                                    end=(new DateTime_iso8601()).parse(date);
                                }
                                else if(check.equals("<name>accumulatorID</name>")){
                                    String chaine=sortie.nextLine();
                                    int last=chaine.indexOf("</i4></value>");
                                    id=Integer.parseInt(chaine.substring(11, last));
                                }
                                else if(check.equals("<name>accumulatorStartDate</name>")){
                                    String date=sortie.nextLine();
                                    int last=date.indexOf("</dateTime.iso8601></value>");
                                    date=date.substring(25, last);
                                    start=(new DateTime_iso8601()).parse(date);
                                }
                                else if(check.equals("<name>accumulatorValue</name>")){
                                    String chaine=sortie.nextLine();
                                    int last=chaine.indexOf("</i4></value>");
                                    value=Integer.parseInt(chaine.substring(11, last));
                                }
                                check=sortie.nextLine();
                                }
                           liste.add(new AccumulatorInformation(id,value,start,end));
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
