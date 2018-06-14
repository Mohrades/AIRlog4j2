/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author TestAppli
 */
public class DateTime_iso8601 {

    public String format(Date date) {
        // TODO Auto-generated method stub
    	String timestamp = (new SimpleDateFormat("yyyyMMdd'T'HH:mm:ssZ")).format(date);

    	// date only
    	if(timestamp.endsWith("23:59:59+0100")) {
    		timestamp = timestamp.replace("23:59:59+0100", "12:00:00+0000");
    	}

		return timestamp;
    }

    public Date parse(String date) {
    	// date only
        if(date.endsWith("12:00:00+0000")) {
        	date = date.replace("12:00:00+0000", "23:59:59+0100");
        }

        try {
            return (new SimpleDateFormat("yyyyMMdd'T'HH:mm:ssZ")).parse(date);

        } catch (ParseException ex) {
            return null;
        }
    }
}
