package connexions;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SocketConnection {

	private BufferedInputStream in;
    private OutputStream out;
    private String ip;
    private int port;
    private int sleep, threshold, timeout = 12500;
    private Socket socket;

    private boolean available;

	public SocketConnection(String ip, int port, int sleep, int timeout) {
		try {
			this.ip = ip;
			this.port = port;
			this.sleep = sleep;

			socket = new Socket(ip, port);
        	// Par défaut, un objet DatagramSocket ne possède pas de timeout lors de l'utilisation de la méthode receive().
        	// La méthode bloque donc l'exécution de l'application jusqu'à la réception d'un packet de données.
        	// La méthode setSoTimeout() permet de préciser un timeout en millisecondes.
        	// Une fois ce délai écoulé sans réception d'un paquet de données, la méthode lève une exception du type SocketTimeoutException.
			if(timeout > 0) this.timeout = timeout;
			socket.setSoTimeout(this.timeout);

	        out = socket.getOutputStream();

		} catch (UnknownHostException ex) {

		} catch (SocketTimeoutException ex) {

        } catch (IOException e) {
			// if an I/O error occurs when creating the output stream or if the socket is not connected.

		} catch (Throwable e) {

		}
	}

	public boolean isOpen() {
		return (out == null) ? false : true;
	}

	public void setSleep(int sleep) {
		this.sleep = sleep;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public void fermer() {
		try {
			if(isOpen()){
				socket.close();
	    	}

		} catch (IOException e) {

		} catch (Exception e) {

		} catch (Throwable e) {

		}
	}

	public String execute(String requete) {
		String reponse = null;

        try {
        	String header = "POST /Air HTTP/1.1\n" +
            "Content-Length: " + requete.length() + "\n" +
            "Content-Type: text/xml\n" +
            "User-Agent: UGw Server/4.0/1.0\n" +
            "Host: " + ip + ":" + port + "\n" +
            "Authorization: Basic cHNhcHB1c2VyOnBzYXBwdXNlckAxMjM=\n\n";

            byte data[] = (header + requete).getBytes();
            out.write(data, 0, data.length);
            out.flush();

            if(sleep <= 0) {
            	setAvailable(true);
            	return "";
            }

            in = new BufferedInputStream(socket.getInputStream());
            reponse = "";
            byte[] lecteur = new byte[1024];

            int link_timeout = 0;
            while (in.available() == 0) {
            	link_timeout += sleep;

            	if(link_timeout >= timeout) {
            		log(requete, null);
            		handleTimeoutException(requete, link_timeout);
            		return "";
            	}

            	Thread.sleep(sleep);
            }

            // if link read exceedes threshold
            if((threshold > 0) && (link_timeout >= threshold)) handleTimeThreshold(requete, link_timeout);

            // pause supplementaire pour s'assurer de la réception de l'entièreté de la reponse
            // Thread.sleep(10);
            Thread.sleep(12);

            while (in.available() > 0) {
            	in.read(lecteur);
                reponse += new String(lecteur);
                lecteur = new byte[1024];
            }

            try {
            	reponse = reponse.substring(0, reponse.indexOf(String.valueOf((char) 0)));

            } catch(StringIndexOutOfBoundsException ex) {

            } catch (Throwable th) {

            }

            try {
                // int beginIndex=reponse.indexOf("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    			int beginIndex = reponse.indexOf("<?xml");
                reponse = reponse.substring(beginIndex);

            } catch(StringIndexOutOfBoundsException ex) {

            } catch (Throwable th) {

            }

            setAvailable(true);
            return reponse;

        } catch (InterruptedException ex) {

        } catch (SocketTimeoutException ex) {
    		log(requete, null);
    		handleTimeoutException(requete, timeout);
    		return "";

        } catch (IOException ex) {

        } catch (StringIndexOutOfBoundsException ex) {

        } catch (Throwable th) {

        }

        log(requete, reponse);
        return (reponse == null) ? "" : reponse;
	}

	public void log(String requete, String reponse) {
		if(requete.contains("<methodCall><methodName>Get")) ;
		else {
			Logger logger = LogManager.getLogger("logging.log4j.AirRequestLogger");

			logger.log(Level.WARN, requete);
			if(reponse != null) logger.log(Level.ERROR, reponse);

			// logger.shutdown();
		}
	}

	public void handleTimeThreshold(String requete, int duration) {
    	if(requete.contains("<methodCall><methodName>Get")) ;
    	else {
    		Logger logger = LogManager.getLogger("logging.log4j.AirTimeThresholdLogger");
    		logger.error("HOST = " + ip + ",   PORT = " + port + ",   DURATION = " + duration);
    	}
	}

	public void handleTimeoutException(String requete, int timeout) {
		if(requete.contains("<methodCall><methodName>Get")) ;
		else {
			Logger logger = LogManager.getLogger("logging.log4j.AirAvailabilityLogger");
			logger.error("HOST = " + ip + ",   PORT = " + port + ",   TIMEOUT = " + timeout);
		}
	}

}
