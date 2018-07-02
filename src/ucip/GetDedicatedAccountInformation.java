package ucip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

import connexions.SocketConnection;
import util.BalanceAndDate;
import util.DateTime_iso8601;
import util.DedicatedAccount;

public class GetDedicatedAccountInformation {

public StringBuffer formerRequete(String msisdn, int[][] dedicatedAccountSelection) {
	StringBuffer chaine= new StringBuffer("");

	if((dedicatedAccountSelection != null) && (dedicatedAccountSelection.length > 0)) {
		int a = dedicatedAccountSelection.length;
	    chaine.append("<member><name>dedicatedAccountSelection</name><value><array><data>");

	    for (int j = 0; j < a; j++) {
	    	if((dedicatedAccountSelection[j][0] <= 0) || (dedicatedAccountSelection[j][1] <= 0));
	    	else {
		    	chaine.append("<value><struct><member><name>dedicatedAccountIDFirst</name><value><int>");
		    	chaine.append(dedicatedAccountSelection[j][0]);
		    	chaine.append("</int></value></member><member><name>dedicatedAccountIDLast</name><value><int>");
		    	chaine.append(dedicatedAccountSelection[j][1]);
		    	chaine.append("</int></value></member></struct></value>");
	    	}
	    }

	    chaine.append("</data></array></value></member>");
    }

    StringBuffer requete = new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>GetBalanceAndDate</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
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

    public HashSet<BalanceAndDate> getData(SocketConnection air, String msisdn, int[][] dedicatedAccountSelection) {
    	HashSet<BalanceAndDate> liste = new HashSet<BalanceAndDate>();
    	BalanceAndDate mainAccount = new BalanceAndDate(0, 0, null);
 	   	mainAccount.setRelative(false);

    	try {
    	   if(air.isOpen()) {
    		   StringBuffer requete = formerRequete(msisdn, dedicatedAccountSelection);
    	       requete.append("</struct></value></param></params></methodCall>");   
    	       String reponse=air.execute(requete.toString());
    	       @SuppressWarnings("resource")
    	       Scanner sortie= new Scanner(reponse);

	    	   while(true) {
	                String ligne = sortie.nextLine();

	                if(ligne == null) {
	                    break;
	                }
	                else if(ligne.equals("<name>accountValue1</name>")) {
	                    String price=sortie.nextLine();
	                    int last = price.indexOf("</string></value>");
	                    mainAccount.setAccountValue(Long.parseLong(price.substring(15, last)));
	                }
	                else if(ligne.equals("<name>accountValue2</name>")) {
	                    String price=sortie.nextLine();
	                    int last = price.indexOf("</string></value>");
	                    mainAccount.setAccountValue2(Long.parseLong(price.substring(15, last)));
	                }
	                else if(ligne.equals("<name>supervisionExpiryDate</name>")) {
	                    String date = sortie.nextLine();
	                    int last=date.indexOf("</dateTime.iso8601></value>");
	                    date = date.substring(25, last);
	                    mainAccount.setExpiryDate((new DateTime_iso8601()).parse(date));
	                }
	                else if(ligne.equals("<name>serviceFeeExpiryDate</name>")) {
	                    String date = sortie.nextLine();
	                    int last = date.indexOf("</dateTime.iso8601></value>");
	                    date = date.substring(25, last);
	                    mainAccount.setServiceFee((new DateTime_iso8601()).parse(date));
	                }
                    else if(ligne.equals("<name>dedicatedAccountInformation</name>")) {
                        String check=sortie.nextLine();

                        while(!check.equals("</array>")) {
                            if(check.equals("<struct>")) {
                                int id = 0; long val = 0; long val2 = 0;
                                Date start = null, end = null;

                                while(!check.equals("</struct>")) {

	                                if(check.equals("<name>expiryDate</name>")) {
	                                    String date = sortie.nextLine();
	                                    int last = date.indexOf("</dateTime.iso8601></value>");
	                                    date = date.substring(25, last);
	                                    end = (new DateTime_iso8601()).parse(date);
	                                }
	                                else if(check.equals("<name>dedicatedAccountID</name>")) {
	                                    String chaine = sortie.nextLine();
	                                    int last = chaine.indexOf("</i4></value>");
	                                    id = Integer.parseInt(chaine.substring(11, last));
	                                }
	                                else if(check.equals("<name>startDate</name>")) {
	                                    String date = sortie.nextLine();
	                                    int last = date.indexOf("</dateTime.iso8601></value>");
	                                    date = date.substring(25, last);
	                                    start = (new DateTime_iso8601()).parse(date);
	                                }
	                                else if(check.equals("<name>dedicatedAccountValue1</name>")) {
	            	                    String price = sortie.nextLine();
	            	                    int last = price.indexOf("</string></value>");
	            	                    val = Long.parseLong(price.substring(15, last));
	                                }
	                                else if(check.equals("<name>dedicatedAccountValue2</name>")) {
	            	                    String price = sortie.nextLine();
	            	                    int last = price.indexOf("</string></value>");
	            	                    val2 = Long.parseLong(price.substring(15, last));
	                                }

	                                check = sortie.nextLine();
                                }

        	                    DedicatedAccount DA = new DedicatedAccount(id, val, end);
        	                    DA.setAccountValue2(val2); DA.setStartDate(start);
        	                    DA.setRelative(false);
        	                    liste.add(DA);
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

    	if((mainAccount != null) && (mainAccount.getExpiryDate() == null));
    	else if (mainAccount != null) liste.add(mainAccount);

    	if(liste.isEmpty()) return null;
    	else return liste;
    }
}
