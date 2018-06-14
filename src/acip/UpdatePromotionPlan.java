package acip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

import util.DateTime_iso8601;
import util.PromotionPlanAction;
import util.PromotionPlanInformation;
import connexions.SocketConnection;

public class UpdatePromotionPlan {

    public StringBuffer formerRequete(String msisdn,String promotionPlanAction,PromotionPlanInformation promotionPlanNew,PromotionPlanInformation promotionPlanOld,String originOperatorID){
    	StringBuffer modif=new StringBuffer("");
    	if(promotionPlanOld!=null){
    	if(promotionPlanOld.getPromotionStartDate()!=null){
    		modif.append("<member><name>promotionOldStartDate</name><value><dateTime.iso8601>");
    		modif.append((new DateTime_iso8601()).format(promotionPlanOld.getPromotionStartDate()));
    		modif.append("</dateTime.iso8601></value></member>");
    	}
    	if(promotionPlanOld.getPromotionEndDate()!=null){
    		modif.append("<member><name>promotionOldEndDate</name><value><dateTime.iso8601>");
    		modif.append((new DateTime_iso8601()).format(promotionPlanOld.getPromotionEndDate()));
    		modif.append("</dateTime.iso8601></value></member>");
    	}
    	}
    	if(promotionPlanNew!=null){
    	if(promotionPlanNew.getPromotionStartDate()!=null){
    		modif.append("<member><name>promotionStartDate</name><value><dateTime.iso8601>");
    		modif.append((new DateTime_iso8601()).format(promotionPlanNew.getPromotionStartDate()));
    		modif.append("</dateTime.iso8601></value></member>");
    	}
    	if(promotionPlanNew.getPromotionEndDate()!=null){
    		modif.append("<member><name>promotionEndDate</name><value><dateTime.iso8601>");
    		modif.append((new DateTime_iso8601()).format(promotionPlanNew.getPromotionEndDate()));
    		modif.append("</dateTime.iso8601></value></member>");
    	}
    	}
    	
    	StringBuffer requete=new StringBuffer("<?xml version=\"1.0\"?><methodCall><methodName>UpdatePromotionPlan</methodName><params><param><value><struct><member><name>originNodeType</name><value><string>EXT</string></value></member><member><name>originHostName</name><value><string>SRVPSAPP03mtnlocal</string></value></member><member><name>originTransactionID</name><value><string>");
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
    	
    	requete.append("<member><name>promotionPlanAction</name><value><string>");
    	requete.append(promotionPlanAction);
    	requete.append("</string></value></member>");
    	requete.append(modif);
    	requete.append("<member><name>promotionPlanID</name><value><string>");
    	if(!promotionPlanAction.equals(PromotionPlanAction.DELETE))requete.append(promotionPlanNew.getPromotionPlanID());
    	else requete.append(promotionPlanOld.getPromotionPlanID());
    	requete.append("</string></value></member>");
    	
    	return requete;
    }
    
    public boolean update(SocketConnection air, String msisdn,String promotionPlanAction,PromotionPlanInformation promotionPlanNew,PromotionPlanInformation promotionPlanOld,String originOperatorID){
    	boolean responseCode = false;
    	
    	try{
        	if(air.isOpen()) {
            	StringBuffer requete = formerRequete(msisdn, promotionPlanAction, promotionPlanNew,promotionPlanOld,originOperatorID);
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
