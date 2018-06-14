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
import util.BalanceAndDate;
import util.DateTime_iso8601;
import util.DedicatedAccount;

/**
 *
 * @author TestAppli
 */
public class UpdateBalanceAndDate {

public StringBuffer formerRequete(String msisdn,HashSet<BalanceAndDate> balancesAndDates,String transactionType,String transactionCode,String originOperatorID){
	StringBuffer mainAccountValue=new StringBuffer("");
	StringBuffer DAsValueNew=new StringBuffer("");

	for(BalanceAndDate balanceAndDate:balancesAndDates){
		long value=balanceAndDate.getAccountValue();
		int id=balanceAndDate.getAccountID();
		Object expirydate=balanceAndDate.getExpiryDate();
		boolean relative=balanceAndDate.isRelative();

		if(id==0) {
			mainAccountValue=new StringBuffer("");
			if(relative){
				mainAccountValue.append("<member><name>adjustmentAmountRelative</name><value><string>");
				mainAccountValue.append(value);
				mainAccountValue.append("</string></value></member>");
			}
			else{
				mainAccountValue.append("<member><name>mainAccountValueNew</name><value><string>");
				mainAccountValue.append(value);
				mainAccountValue.append("</string></value></member>");
			}	
			if(expirydate==null);
			else if(expirydate instanceof Integer){
				mainAccountValue.append("<member><name>supervisionExpiryDateRelative</name><value><i4>");
				mainAccountValue.append(((Integer)expirydate));
				mainAccountValue.append("</i4></value></member>");
			}
			else if(expirydate instanceof Date){
				mainAccountValue.append("<member><name>supervisionExpiryDate</name><value><dateTime.iso8601>");
				mainAccountValue.append((new DateTime_iso8601()).format(((Date)expirydate)));
				mainAccountValue.append("</dateTime.iso8601></value></member>");
			}
			Object serviceFee = balanceAndDate.getServiceFee();
			if(serviceFee==null);
			else if(serviceFee instanceof Integer){
				mainAccountValue.append("<member><name>serviceFeeExpiryDateRelative</name><value><i4>");
				mainAccountValue.append(((Integer)serviceFee));
				mainAccountValue.append("</i4></value></member>");
			}
			else if(serviceFee instanceof Date){
				mainAccountValue.append("<member><name> serviceFeeExpiryDate</name><value><dateTime.iso8601>");
				mainAccountValue.append((new DateTime_iso8601()).format(((Date)serviceFee)));
				mainAccountValue.append("</dateTime.iso8601></value></member>");
			}
		}

		else{
			if(relative){
				DAsValueNew.append("<value><struct><member><name>dedicatedAccountID</name><value><i4>");
				DAsValueNew.append(id);
				DAsValueNew.append("</i4></value></member><member><name>adjustmentAmountRelative</name><value><string>");
				DAsValueNew.append(value);
				DAsValueNew.append("</string></value></member>");;
			}
			else{
				DAsValueNew.append("<value><struct><member><name>dedicatedAccountID</name><value><i4>");
				DAsValueNew.append(id);
				DAsValueNew.append("</i4></value></member><member><name>dedicatedAccountValueNew</name><value><string>");
				DAsValueNew.append(value);
				DAsValueNew.append("</string></value></member>");
			}
			if(expirydate==null);
			else if(expirydate instanceof Integer){
				DAsValueNew.append("<member><name>adjustmentDateRelative</name><value><i4>");
				DAsValueNew.append(((Integer)expirydate));
				DAsValueNew.append("</i4></value></member>");
			}
			else if(expirydate instanceof Date){
				DAsValueNew.append("<member><name>expiryDate</name><value><dateTime.iso8601>");
				DAsValueNew.append((new DateTime_iso8601()).format(((Date)expirydate)));
				DAsValueNew.append("</dateTime.iso8601></value></member>");
			}
			Object startdate=((DedicatedAccount)balanceAndDate).getStartDate();
			if(startdate==null);
			else if(startdate instanceof Integer){
				DAsValueNew.append("<member><name>adjustmentStartDateRelative</name><value><i4>");
				DAsValueNew.append(((Integer)startdate));
				DAsValueNew.append("</i4></value></member>");
			}
			else if(startdate instanceof Date){
				DAsValueNew.append("<member><name>startDate</name><value><dateTime.iso8601>");
				DAsValueNew.append((new DateTime_iso8601()).format(((Date)startdate)));
				DAsValueNew.append("</dateTime.iso8601></value></member>");
			}
			
			DAsValueNew.append("</struct></value>");
		}
	}

	if(DAsValueNew.length()!=0){
		StringBuffer s1=new StringBuffer("<member><name>dedicatedAccountUpdateInformation</name><value><array><data>");
		s1.append(DAsValueNew);
		s1.append("</data></array></value></member>"); 	
		DAsValueNew=new StringBuffer(s1);
	}
	if(transactionType!=null){
		mainAccountValue.append("<member><name>transactionType</name><value><string>");
		mainAccountValue.append(transactionType);
		mainAccountValue.append("</string></value></member>");
	}
	if(transactionCode!=null){
		mainAccountValue.append("<member><name>transactionCode</name><value><string>");
		mainAccountValue.append(transactionCode);
		mainAccountValue.append("</string></value></member>");
	}

	StringBuffer modif=new StringBuffer("");
	modif.append(mainAccountValue);
	modif.append(DAsValueNew);
	
	return formerRequete(modif,msisdn,originOperatorID);
}

public boolean update(SocketConnection air, String msisdn,HashSet<BalanceAndDate> balancesAndDates,String transactionType,String transactionCode,String originOperatorID){
	boolean responseCode = false;
	
	try{
    	if(air.isOpen()){
        	StringBuffer requete = formerRequete(msisdn,balancesAndDates,transactionType,transactionCode,originOperatorID);
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
                        responseCode = Integer.parseInt(code_reponse.substring(11, last)) == 0;

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


public StringBuffer formerRequete(StringBuffer modifications,String msisdn,String originOperatorID){
	
	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>UpdateBalanceAndDate</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
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
	
	requete.append("<member><name>transactionCurrency</name><value><string>XOF</string></value></member>");
	requete.append(modifications);
		
	return requete;
}

}
