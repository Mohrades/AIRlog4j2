package ucip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

import connexions.SocketConnection;
import util.BalanceAndDate;
import util.DateTime_iso8601;
import util.DedicatedAccount;

public class GetBalanceAndDate {

public StringBuffer formerRequete(String msisdn, int dedicatedAccountID){

    StringBuffer requete = new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>GetBalanceAndDate</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
    requete.append((new SimpleDateFormat("yyMMddHHmmssS")).format(new Date()));
	requete.append("</string></value></member><member><name>originTimeStamp</name><value><dateTime.iso8601>");
	requete.append((new DateTime_iso8601()).format(new Date()));
	requete.append("</dateTime.iso8601></value></member><member><name>subscriberNumberNAI</name><value><int>1</int></value></member>");
	requete.append("<member><name>subscriberNumber</name><value><string>");
	requete.append(msisdn);
	requete.append("</string></value></member>");

	requete.append("<member><name>dedicatedAccountSelection</name><value><array><data><value><struct><member><name>dedicatedAccountIDFirst</name><value><int>");

	if(dedicatedAccountID < 1) requete.append(1);
	else requete.append(dedicatedAccountID);

	requete.append("</int></value></member></struct></value></data></array></value></member>");

	return requete;
}

    public BalanceAndDate getData(SocketConnection air, String msisdn, int dedicatedAccountID){
    	BalanceAndDate balance = null;

    	try {
    	   if(air.isOpen()) {
    		   StringBuffer requete = formerRequete(msisdn, dedicatedAccountID);
    	       requete.append("</struct></value></param></params></methodCall>");   
    	       String reponse=air.execute(requete.toString());
    	       @SuppressWarnings("resource")
    	       Scanner sortie= new Scanner(reponse);

    	       if(dedicatedAccountID < 1) {
    	    	   BalanceAndDate balanceAndDate=new BalanceAndDate();
    	           balanceAndDate.setRelative(false);
    	    	   balanceAndDate.setAccountID(0);
    	            while(true) {
    	                String ligne=sortie.nextLine();
    	                if(ligne==null) {
    	                    break;
    	                }
    	                else {
	    	                if(ligne.equals("<name>accountValue1</name>")) {
	    	                    String price=sortie.nextLine();
	    	                    int last=price.indexOf("</string></value>");
	    	                    balanceAndDate.setAccountValue(Long.parseLong(price.substring(15, last)));
	    	                }
	    	                else if(ligne.equals("<name>accountValue2</name>")) {
	    	                    String price=sortie.nextLine();
	    	                    int last=price.indexOf("</string></value>");
	    	                    balanceAndDate.setAccountValue2(Long.parseLong(price.substring(15, last)));
	    	                }
	    	                else if(ligne.equals("<name>supervisionExpiryDate</name>")) {
	    	                    String date = sortie.nextLine();
	    	                    int last = date.indexOf("</dateTime.iso8601></value>");
	    	                    date = date.substring(25, last);
	    	                    balanceAndDate.setExpiryDate((new DateTime_iso8601()).parse(date));
	
	    	                    balance = balanceAndDate;
	    	                }
	    	                else if(ligne.equals("<name>serviceFeeExpiryDate</name>")) {
	    	                    String date = sortie.nextLine();
	    	                    int last = date.indexOf("</dateTime.iso8601></value>");
	    	                    date = date.substring(25, last);
	    	                    balanceAndDate.setServiceFee((new DateTime_iso8601()).parse(date));

	    	                    balance = balanceAndDate;
	    	                }
    	                }
    	            }
    	       }
    	       else {
    	    	   while(true) {
    	                String ligne=sortie.nextLine();

    	                if(ligne == null) {
    	                    break;
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
	                                        id=Integer.parseInt(chaine.substring(11, last));
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

                                    if(dedicatedAccountID == id) {
                	                    DedicatedAccount DA = new DedicatedAccount(dedicatedAccountID, val, end);
                	                    DA.setAccountValue2(val2); DA.setStartDate(start);
                	                    DA.setRelative(false);

                	                    balance = DA;
                	                    break;
                                    }
                                }

                             check=sortie.nextLine();
                           }

                            break;
                        }
    	            } 
    	       	}
    	   }
    	}
    	catch(NoSuchElementException e) {

       } finally {
       		air.fermer();
       }

    	if((dedicatedAccountID == 0) && (balance != null) && (balance.getExpiryDate() == null)) balance = null;
    	return balance;
}

}
