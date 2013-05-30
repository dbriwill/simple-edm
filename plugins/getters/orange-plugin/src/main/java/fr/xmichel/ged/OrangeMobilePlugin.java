package fr.xmichel.ged;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.connector.plugins.feedback.SimpleGedPluginException;
import com.simple.ged.connector.plugins.getter.SimpleGedGetterPlugin;

public class OrangeMobilePlugin extends SimpleGedGetterPlugin {

	private static final Logger logger = LoggerFactory.getLogger(OrangeMobilePlugin.class);
	
	@Override
	public void doGet() throws SimpleGedPluginException {

		logger.info("Starting OrangeMobilePlugin...");
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			httpclient = WebClientDevWrapper.wrapClient(httpclient);

			
			// ------------------------------
			// Getting vars for connection
			// ------------------------------
			
			HttpGet getVarsRequest = new HttpGet("http://id.orange.fr/auth_user/bin/auth0user.cgi");
			HttpResponse getVarsResponse = httpclient.execute(getVarsRequest);
			
			HttpEntity getVarsEntity = getVarsResponse.getEntity();
			
			String varsPageContent = "";
			
			if (getVarsEntity != null) {
			   BufferedReader reader = new BufferedReader(new InputStreamReader(getVarsEntity.getContent()));

			   String line;
			   while((line = reader.readLine()) != null){
				   varsPageContent += line;
			   }
			}


			Pattern p = Pattern.compile("<form action=\"(.*)\" method=\"post\" name=\"form1\">");
			Matcher m = p.matcher(varsPageContent);
			
			String targetURL = "";
			while(m.find()) {
				targetURL = m.group(1);
			}
			
			
			if (targetURL.isEmpty()) {
				throw new SimpleGedPluginException("Impossible d'obtenir la page de connexion");
			}
			
			logger.debug("Target URL => {}", targetURL);
			
			// a little wait
			Thread.sleep(2000);
			
			
			
			// ------------------------------
			// Sending connection request
			// ------------------------------
			
			HttpPost connectionRequest = new HttpPost(targetURL);
			
			
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			qparams.add(new BasicNameValuePair("valider", ""));
			qparams.add(new BasicNameValuePair("credential", getPropertyValue("phone_number")));
			qparams.add(new BasicNameValuePair("pwd", getPropertyValue("secret_code")));
			
			connectionRequest.setEntity(new UrlEncodedFormEntity(qparams));
			
			httpclient.execute(connectionRequest).getEntity().getContent().close();

			
			// a little wait
			Thread.sleep(2000);
			

			
			// ------------------------------
			// Getting facture link
			// ------------------------------
			
			HttpGet gotoClientSpaceRequest = new HttpGet("https://ecaremobile.orange.fr/EcareAsGenerator/servlet/ecareweb.servlet.redirector.AppServerRedirectorServlet?rubrique=F");
			HttpResponse gotoClientSpaceResponse = httpclient.execute(gotoClientSpaceRequest);
			
			HttpEntity gotoClientSpaceEntity = gotoClientSpaceResponse.getEntity();
			
			String clientSpacePageContent = "";
			
			if (gotoClientSpaceEntity != null) {
				logger.debug("Going to facturation page");
			   BufferedReader reader = new BufferedReader(new InputStreamReader(gotoClientSpaceEntity.getContent()));

			   String line;
			   while((line = reader.readLine()) != null){
				   clientSpacePageContent += line;
			   }
			}
			
			Pattern p2 = Pattern.compile("servlet/ecareweb.servlet.itiola.facturation.FacturationPageServletVisualiser\\?(.*)\" onclick=");
			Matcher m2 = p2.matcher(clientSpacePageContent);
			
			String factureFile = "";
			while(m2.find()) {
				factureFile = m2.group(1).split(" ")[0].replace("\"", "");
				break;
			}
			
			
			if (targetURL.isEmpty()) {
				throw new SimpleGedPluginException("Impossible de lire l'adresse de la facture");
			}
			
			logger.debug("Fichier facture => {}", factureFile);
			
			// a little wait
			Thread.sleep(2000);
			
			
			
			// ------------------------------
			// Getting the PDF file
			// ------------------------------
			
			HttpGet pdfFileRequest = new HttpGet("https://ecaremobile.orange.fr/EcareAsGenerator/servlet/ecareweb.servlet.itiola.facturation.FacturationPageServletVisualiser?" + factureFile);
			HttpResponse pdfFileResponse = httpclient.execute(pdfFileRequest);
			
			HttpEntity pdfFileEntity = pdfFileResponse.getEntity();
			
			if (pdfFileEntity != null) {
				logger.debug("Downloading facture...");

				try (FileOutputStream fos = new FileOutputStream(getDestinationFile() + ".pdf")) { // don't forget the file suffix
				
					try (InputStream instream = pdfFileEntity.getContent()) {
						int l;
						byte[] tmp = new byte[2048];
						while ((l = instream.read(tmp)) != -1) {
							fos.write(tmp, 0, l);
							fos.flush();
						}
					}
					catch(IOException e) {
						logger.error("Could not open read source file", e);
						throw new SimpleGedPluginException("La lecture du fichier source a échouée : " + e.getMessage());
					}
						
				}
				catch (IOException e) {
					logger.error("Could not open target file", e);
					throw new SimpleGedPluginException("L'écriture dans le fichier de destination a échouée : " + e.getMessage());
				}

			}

		}
		catch (Exception e) {
			logger.error("Failed to execute recuperation", e);
			throw new SimpleGedPluginException("La récupération de la facture s'est soldée par un échec : " + e.getMessage());
		}
		
		logger.info("End of OrangeMobilePlugin");
	}
}
