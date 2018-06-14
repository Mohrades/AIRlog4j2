package ucip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

import connexions.SocketConnection;
import util.DateTime_iso8601;
import util.OfferInformation;

public class GetOffers {
    
    public StringBuffer formerRequete(String msisdn,int[][] offerSelection,boolean requestInactiveOffersFlag,String offerRequestedTypeFlag,boolean requestDedicatedAccountDetailsFlag) {
    	StringBuffer offerIDs=new StringBuffer("");

    	if(offerSelection!=null) {
	    	int nbre=offerSelection.length;
	    	offerIDs.append("<member><name>offerSelection</name><value><array><data>");
	
	        for (int i=0;i<nbre;i++) {
	        	int[]range=offerSelection[i];
	        	offerIDs.append("<value><struct><member><name>offerIDFirst</name><value><i4>");
	        	offerIDs.append(range[0]);
	        	offerIDs.append("</i4></value></member><member><name>offerIDLast</name><value><i4>");
	        	offerIDs.append(range[1]);
	        	offerIDs.append("</i4></value></member></struct></value>");
	        }
	
	        offerIDs.append("</data></array></value></member>");
        }

    	StringBuffer RequestedTypeFlag=new StringBuffer("");
    	if(offerRequestedTypeFlag!=null){
    		RequestedTypeFlag.append("<member><name>offerRequestedTypeFlag</name><value><string>");
    		RequestedTypeFlag.append(offerRequestedTypeFlag);
    		RequestedTypeFlag.append("</string></value></member>");
    	}
    	
    	StringBuffer DedicatedAccountDeatilsFlag=new StringBuffer("");
    	if(!requestDedicatedAccountDetailsFlag);
    	else{
    		DedicatedAccountDeatilsFlag.append("<member><name>requestDedicatedAccountDetailsFlag</name><value><boolean>1</boolean></value></member>");
    	}
    	
    	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>GetOffers</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
    	requete.append((new SimpleDateFormat("yyMMddHHmmssS")).format(new Date()));
    	requete.append("</string></value></member><member><name>originTimeStamp</name><value><dateTime.iso8601>");
    	requete.append((new DateTime_iso8601()).format(new Date()));
    	requete.append("</dateTime.iso8601></value></member><member><name>subscriberNumberNAI</name><value><int>1</int></value></member>");
    	requete.append("<member><name>subscriberNumber</name><value><string>");
    	requete.append(msisdn);
    	requete.append("</string></value></member>");

    	if(requestInactiveOffersFlag){
    		requete.append("<member><name>requestInactiveOffersFlag</name><value><boolean>1</boolean></value></member>");
    	}

    	requete.append(offerIDs);
    	requete.append(RequestedTypeFlag);
    	requete.append(DedicatedAccountDeatilsFlag);

    	return requete;
    }
    
    public HashSet<OfferInformation> getData(SocketConnection air, String msisdn,int[][] offerSelection,boolean requestInactiveOffersFlag,String offerRequestedTypeFlag,boolean requestDedicatedAccountDetailsFlag) {
    	HashSet<OfferInformation> data=new HashSet<OfferInformation>();

    	try {
    		if(air.isOpen()) {
    	    	StringBuffer requete = formerRequete(msisdn, offerSelection,requestInactiveOffersFlag,offerRequestedTypeFlag, requestDedicatedAccountDetailsFlag);
    	    	requete.append("</struct></value></param></params></methodCall>");
    	    	String reponse=air.execute(requete.toString());
    	    	@SuppressWarnings("resource")
				Scanner sortie= new Scanner(reponse);

                while(true) {
                    String ligne=sortie.nextLine();
                    if(ligne==null) {
                        break;
                    }
                    else if(ligne.equals("<name>responseCode</name>")){
                        String code_reponse=sortie.nextLine();
                        int last=code_reponse.indexOf("</i4></value>");
                        
                        if(Integer.parseInt(code_reponse.substring(11, last)) == 165) {
                        	data=new HashSet<OfferInformation>();
                        	break;
                        }

                    }
                    else if(ligne.equals("<name>offerInformation</name>")){
                        String check=sortie.nextLine();
                        while(!check.equals("</array>")){
                            if(check.equals("<struct>")){
                                int id = 0,type = 0;
                                Date start = null,end = null;
                                while(!check.equals("</struct>")){
                                if(check.equals("<name>expiryDate</name>")){
                                    String date=sortie.nextLine();
                                    int last=date.indexOf("</dateTime.iso8601></value>");
                                    date=date.substring(25, last);
                                    end=(new DateTime_iso8601()).parse(date);
                                }
                                else if(check.equals("<name>offerID</name>")){
                                    String chaine=sortie.nextLine();
                                    int last=chaine.indexOf("</i4></value>");
                                    id=Integer.parseInt(chaine.substring(11, last));
                                }
                                else if(check.equals("<name>startDate</name>")){
                                    String date=sortie.nextLine();
                                    int last=date.indexOf("</dateTime.iso8601></value>");
                                    date=date.substring(25, last);
                                    start=(new DateTime_iso8601()).parse(date);
                                }
                                else if(check.equals("<name>offerType</name>")){
                                    String chaine=sortie.nextLine();
                                    int last=chaine.indexOf("</i4></value>");
                                    type=Integer.parseInt(chaine.substring(11, last));
                                }
                                check=sortie.nextLine();
                                }
                           data.add(new OfferInformation(id,type,start,end,null));
                        }
                         check=sortie.nextLine();   
                       }
                         break;
                   }
                }
    		}

        } catch(NoSuchElementException ex) {

        } finally {
           	air.fermer();

        }

    	return data;
    }
    
}