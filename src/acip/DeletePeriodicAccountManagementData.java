package acip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

import connexions.SocketConnection;
import util.DateTime_iso8601;
import util.PamInformation;
import util.PamInformationList;

public class DeletePeriodicAccountManagementData {

	public StringBuffer formerRequete(String msisdn, PamInformationList pamInformationList, String originOperatorID) {
    	StringBuffer pamInformations = new StringBuffer("");

    	HashSet<PamInformation> list = pamInformationList.getList();

    	if(list.size() > 0) {
    		pamInformations = new StringBuffer("<member><name>pamInformationList</name><value><array><data>");

    		for(PamInformation pam : list) {
    			pamInformations.append("<value><struct><member><name>pamClassID</name><value><i4>");
    			pamInformations.append(pam.getPamClassID());
    			pamInformations.append("</i4></value></member><member><name>pamServiceID</name><value><i4>");
    			pamInformations.append(pam.getPamServiceID());
    			pamInformations.append("</i4></value></member><member><name>scheduleID</name><value><i4>");
    			pamInformations.append(pam.getScheduleID());
    			pamInformations.append("</i4></value></member>");

    			if(pam.getCurrentPamPeriod() != null) {
    				pamInformations.append("<member><name>currentPamPeriod</name><value><string>");
    				pamInformations.append(pam.getCurrentPamPeriod());
    				pamInformations.append("</string></value></member>");
    			}

    			pamInformations.append("</struct></value>");
    		}

        	/*for(PamInformation pam : list) {
    			pamInformations.append("<value><struct><member><name>pamInformation</name><value><struct><member><name>pamClassID</name><value><i4>");
    			pamInformations.append(pam.getPamClassID());
    			pamInformations.append("</i4></value></member><member><name>pamServiceID</name><value><i4>");
    			pamInformations.append(pam.getPamServiceID());
    			pamInformations.append("</i4></value></member><member><name>scheduleID</name><value><i4>");
    			pamInformations.append(pam.getScheduleID());
    			pamInformations.append("</i4></value></member>");
    			
    			if(pam.getCurrentPamPeriod() != null) {
    				pamInformations.append("<member><name>currentPamPeriod</name><value><string>");
    				pamInformations.append(pam.getCurrentPamPeriod());
    				pamInformations.append("</string></value></member>");
    			}

    			pamInformations.append("</struct></value></member></struct></value>");
    		}*/

    		pamInformations.append("</data></array></value></member>");
    	}

    	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>DeletePeriodicAccountManagementData</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
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

public boolean delete(SocketConnection air, String msisdn, PamInformationList pamInformationList, String originOperatorID, boolean acceptServiceIDNotExist){
	boolean responseCode = false;
	// new PamInformationList(pamServiceID, pamClassID, pamScheduleID)
	
	try{
		if(air.isOpen()) {
			StringBuffer requete = formerRequete(msisdn, pamInformationList, originOperatorID);
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
		                responseCode = (Integer.parseInt(code_reponse.substring(11, last)) == 0) || ((acceptServiceIDNotExist) && (Integer.parseInt(code_reponse.substring(11, last)) == 191));
		                
		                break;
		            }
		        }		
		}
	}
	catch(NoSuchElementException ex) {
	
	} finally {
		air.fermer();
	}
	
		return responseCode;
	}

}
