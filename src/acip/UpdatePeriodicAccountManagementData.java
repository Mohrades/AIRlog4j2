package acip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

import connexions.SocketConnection;
import util.DateTime_iso8601;
import util.PamUpdateInformation;
import util.PamUpdateInformationList;

public class UpdatePeriodicAccountManagementData {

	public StringBuffer formerRequete(String msisdn, PamUpdateInformationList pamUpdateInformationList, String originOperatorID){
    	StringBuffer pamInformations = new StringBuffer("");

    	HashSet<PamUpdateInformation> list = pamUpdateInformationList.getList();
    	
    	if(list.size() > 0) {
        	pamInformations = new StringBuffer("<member><name>pamUpdateInformationList</name><value><array><data>");

    		for(PamUpdateInformation pam : list) {
    			pamInformations.append("<value><struct>");

    			if(pam.getPamClassIDOld() >= 0) {
        			pamInformations.append("<member><name>pamClassIDOld</name><value><i4>");
        			pamInformations.append(pam.getPamClassIDOld());
        			pamInformations.append("</i4></value></member>");    				
    			}
    			if(pam.getPamClassIDNew() >= 0) {
    				pamInformations.append("<member><name>pamClassIDNew</name><value><i4>");
    				pamInformations.append(pam.getPamClassIDNew());
    				pamInformations.append("</i4></value></member>");   				
    			}

				pamInformations.append("<member><name>pamServiceID</name><value><i4>");
    			pamInformations.append(pam.getPamServiceID());
    			pamInformations.append("</i4></value></member>");

    			if(pam.getScheduleIDOld() >= 0) {
        			pamInformations.append("<member><name>scheduleIDOld</name><value><i4>");
        			pamInformations.append(pam.getScheduleIDOld());
        			pamInformations.append("</i4></value></member>");
    			}
    			if(pam.getScheduleIDNew() >= 0) {
    				pamInformations.append("<member><name>scheduleIDNew</name><value><i4>");
    				pamInformations.append(pam.getScheduleIDNew());
    				pamInformations.append("</i4></value></member>");    				
    			}

    			if(pam.getCurrentPamPeriod() != null) {
    				pamInformations.append("<member><name>currentPamPeriod</name><value><string>");
    				pamInformations.append(pam.getCurrentPamPeriod());
    				pamInformations.append("</string></value></member>");
    			}

    			if(pam.getPamServicePriorityOld() >= 0) {
    				pamInformations.append("<member><name>pamServicePriorityOld</name><value><i4>");
    				pamInformations.append(pam.getCurrentPamPeriod());
    				pamInformations.append("</i4></value></member>");
    			}

    			if(pam.getPamServicePriorityNew() >= 0) {
    				pamInformations.append("<member><name>pamServicePriorityNew</name><value><i4>");
    				pamInformations.append(pam.getCurrentPamPeriod());
    				pamInformations.append("</i4></value></member>");
    			}

    			if(pam.getDeferredToDate() != null) {
    				pamInformations.append("<member><name>deferredToDate</name><value><dateTime.iso8601>");
    				pamInformations.append((new DateTime_iso8601()).format(pam.getDeferredToDate()));
    				pamInformations.append("</dateTime.iso8601></value></member>");
    			}

				pamInformations.append("</struct></value>");
    		}

    		/*for(PamUpdateInformation pam : list) {
    			pamInformations.append("<value><member><name>pamUpdateInformation</name><value><struct>");

    			pamInformations.append("<member><name>pamClassIDOld</name><value><i4>");
    			pamInformations.append(pam.getPamClassIDOld());
    			pamInformations.append("</i4></value></member>");
				pamInformations.append("<member><name>pamClassIDNew</name><value><i4>");
				pamInformations.append(pam.getPamClassIDNew());
				pamInformations.append("</i4></value></member>");

				pamInformations.append("<member><name>pamServiceID</name><value><i4>");
    			pamInformations.append(pam.getPamServiceID());
    			pamInformations.append("</i4></value></member>");

    			pamInformations.append("<member><name>scheduleIDOld</name><value><i4>");
    			pamInformations.append(pam.getScheduleIDOld());
    			pamInformations.append("</i4></value></member>");
				pamInformations.append("<member><name>scheduleIDNew</name><value><i4>");
				pamInformations.append(pam.getScheduleIDNew());
				pamInformations.append("</i4></value></member>");

    			if(pam.getCurrentPamPeriod() != null) {
    				pamInformations.append("<member><name>currentPamPeriod</name><value><string>");
    				pamInformations.append(pam.getCurrentPamPeriod());
    				pamInformations.append("</string></value></member>");
    			}

    			if(pam.getPamServicePriorityOld() != -1) {
    				pamInformations.append("<member><name>pamServicePriorityOld</name><value><i4>");
    				pamInformations.append(pam.getCurrentPamPeriod());
    				pamInformations.append("</i4></value></member>");
    			}

    			if(pam.getPamServicePriorityNew() != -1) {
    				pamInformations.append("<member><name>pamServicePriorityNew</name><value><i4>");
    				pamInformations.append(pam.getCurrentPamPeriod());
    				pamInformations.append("</i4></value></member>");
    			}

    			if(pam.getDeferredToDate() != null) {
    				pamInformations.append("<member><name>deferredToDate</name><value><dateTime.iso8601>");
    				pamInformations.append((new DateTime_iso8601()).format(pam.getDeferredToDate()));
    				pamInformations.append("</dateTime.iso8601></value></member>");
    			}

				pamInformations.append("</struct></value></member></value>");
    		}*/

    		pamInformations.append("</data></array></value></member>");    		
    	}

    	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>UpdatePeriodicAccountManagementData</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
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

    	requete.append(pamInformations); 	
    	return requete;
} 

public boolean update(SocketConnection air, String msisdn, PamUpdateInformationList pamUpdateInformationList, String originOperatorID){
	boolean responseCode = false;
	
	try{
	if(air.isOpen()) {
		StringBuffer requete = formerRequete(msisdn, pamUpdateInformationList, originOperatorID);
		requete.append("</struct></value></param></params></methodCall>");
		String reponse=air.execute(requete.toString());
	    @SuppressWarnings("resource")
		Scanner sortie = new Scanner(reponse);
	        while(true) {
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

}
