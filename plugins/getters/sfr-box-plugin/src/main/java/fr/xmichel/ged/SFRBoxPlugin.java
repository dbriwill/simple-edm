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

public class SFRBoxPlugin extends SimpleGedGetterPlugin {

	private static final Logger logger = LoggerFactory.getLogger(SFRBoxPlugin.class);
	
	@Override
	public void doGet() throws SimpleGedPluginException {

		logger.info("Starting SFRBoxPlugin...");
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			httpclient = WebClientDevWrapper.wrapClient(httpclient);

			
			// ------------------------------
			// Getting vars for connection
			// ------------------------------
			
			HttpGet getVarsRequest = new HttpGet("https://www.sfr.fr/cas/login");
			HttpResponse getVarsResponse = httpclient.execute(getVarsRequest);
			
			HttpEntity getVarsEntity = getVarsResponse.getEntity();
			
			
			Pattern patternSecurityToken = Pattern.compile("<input type=\"hidden\" name=\"lt\" value=\"(.*)\" />");
			String securityToken = "";
			
			Pattern patternConnectionTarget = Pattern.compile("<form action=\"(.*)\" name=\"loginForm\" id=\"loginForm\" method=\"post\">");
			String targetURL = "";
			
			
			if (getVarsEntity != null) {
			   BufferedReader reader = new BufferedReader(new InputStreamReader(getVarsEntity.getContent()));

			   String line;
			   while((line = reader.readLine()) != null){
				   
				   if (line.contains("input type=\"hidden\" name=\"lt\"")) {
					   Matcher matcherSecurityToken = patternSecurityToken.matcher(line);
						while(matcherSecurityToken.find()) {
							securityToken = matcherSecurityToken.group(1);
						}
				   }
				   
				   if (line.contains("name=\"loginForm\" id=\"loginForm\"")) {
					   Matcher matcherConnectionTarget = patternConnectionTarget.matcher(line);
						while(matcherConnectionTarget.find()) {
							targetURL = "https://www.sfr.fr" + matcherConnectionTarget.group(1);
						}
				   }
				   
			   }
			}

			
			if (targetURL.isEmpty()) {
				throw new SimpleGedPluginException("Impossible d'obtenir la page de connexion");
			}
			
			if (securityToken.isEmpty()) {
				throw new SimpleGedPluginException("Impossible d'obtenir un token de connexion");
			}
			
			logger.debug("Target URL     => {}", targetURL);
			logger.debug("Security token => {}", securityToken);
			
			// a little wait
			Thread.sleep(2000);
			
			
			// ------------------------------
			// Sending connection request
			// ------------------------------
			
			HttpPost connectionRequest = new HttpPost(targetURL);
			
			
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			qparams.add(new BasicNameValuePair("lt", securityToken));
			qparams.add(new BasicNameValuePair("_eventId", "submit"));
			qparams.add(new BasicNameValuePair("username", getPropertyValue("sfr_mail")));
			qparams.add(new BasicNameValuePair("password", getPropertyValue("secret_code")));
			qparams.add(new BasicNameValuePair("identifier", ""));
			
			connectionRequest.setEntity(new UrlEncodedFormEntity(qparams));
			
			httpclient.execute(connectionRequest).getEntity().getContent().close();

			
			// a little wait
			Thread.sleep(2000);
			
			
			// ------------------------------
			// Getting facture link
			// ------------------------------
			HttpGet gotoClientSpaceRequest = new HttpGet("https://espace-client.sfr.fr/moncompte-webapp/moncompte/espaceClient/consofacture.action?sl=MjY0ODM6NDc7NQ==#sfrintid=EC_telecom_adsl_fixe-consofacture");
			HttpResponse gotoClientSpaceResponse = httpclient.execute(gotoClientSpaceRequest);
			
			HttpEntity gotoClientSpaceEntity = gotoClientSpaceResponse.getEntity();
			

			if (gotoClientSpaceEntity != null) {
				logger.debug("Going to facturation page");
				BufferedReader reader = new BufferedReader(new InputStreamReader(gotoClientSpaceEntity.getContent()));

				@SuppressWarnings("unused")
				String line;
				while((line = reader.readLine()) != null){
				}
			}
			
			// a little wait
			Thread.sleep(2000);
			
			
			
			// ---------------------
			// Getting the PDF file 
			// ---------------------
			
			HttpGet pdfFileRequestSynthese = new HttpGet("https://espace-client.sfr.fr/moncompte-webapp/moncompte/factures/affichageFacturePdf.action?id=0");
			HttpResponse pdfFileResponseSynthese = httpclient.execute(pdfFileRequestSynthese);
			
			HttpEntity pdfFileEntitySynthese = pdfFileResponseSynthese.getEntity();
			
			if (pdfFileEntitySynthese != null) {
				logger.debug("Downloading facture...");

				try (FileOutputStream fos = new FileOutputStream(getDestinationFile() + ".pdf")) { // don't forget the file suffix
				
					try (InputStream instream = pdfFileEntitySynthese.getContent()) {
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
		
			
		logger.info("End of SFRBoxPlugin");
	}
	
}
