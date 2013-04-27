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

import com.simple.ged.connector.plugins.SimpleGedPluginProperty;
import com.simple.ged.connector.plugins.feedback.SimpleGedPluginException;
import com.simple.ged.connector.plugins.getter.SimpleGedGetterPlugin;

public class SFRMobilePlugin extends SimpleGedGetterPlugin {

	private static final Logger logger = LoggerFactory.getLogger(SFRMobilePlugin.class);
	
	@Override
	public void doGet() throws SimpleGedPluginException {

		logger.info("Starting SFRMobilePlugin...");
		
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
			qparams.add(new BasicNameValuePair("username", getPropertyValue("phone_number")));
			qparams.add(new BasicNameValuePair("password", getPropertyValue("secret_code")));
			qparams.add(new BasicNameValuePair("identifier", ""));
			
			connectionRequest.setEntity(new UrlEncodedFormEntity(qparams));
			
			httpclient.execute(connectionRequest).getEntity().getContent().close();

			
			// a little wait
			Thread.sleep(2000);
			
			
			// ------------------------------
			// Getting facture link
			// ------------------------------
			
			HttpGet gotoClientSpaceRequest = new HttpGet("https://www.sfr.fr/espace-client/consulter-factures-mobile/consultation/index.html?sl=Mjk6ODU3ODY5Ng==#sfrintid=EC_telecom_mob-abo_mob-facture");
			HttpResponse gotoClientSpaceResponse = httpclient.execute(gotoClientSpaceRequest);
			
			HttpEntity gotoClientSpaceEntity = gotoClientSpaceResponse.getEntity();
			

			if (gotoClientSpaceEntity != null) {
				logger.debug("Going to facturation page");
				BufferedReader reader = new BufferedReader(new InputStreamReader(gotoClientSpaceEntity.getContent()));

				String line;
				while((line = reader.readLine()) != null){
				}
			}
			
			// a little wait
			Thread.sleep(2000);
			
			
			
			// ------------------------------
			// Getting the PDF file "synthese"
			// ------------------------------
			
			HttpGet pdfFileRequestSynthese = new HttpGet("https://www.sfr.fr/espace-client/consulter-factures-mobile/consultation/lireFactureABOPDF.html");
			HttpResponse pdfFileResponseSynthese = httpclient.execute(pdfFileRequestSynthese);
			
			HttpEntity pdfFileEntitySynthese = pdfFileResponseSynthese.getEntity();
			
			if (pdfFileEntitySynthese != null) {
				logger.debug("Downloading facture...");

				try (FileOutputStream fos = new FileOutputStream(getDestinationFile() + " - synthese.pdf")) { // don't forget the file suffix
				
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

				
				
			// ------------------------------
			// Getting the PDF file "details"
			// ------------------------------
				
			HttpGet pdfFileRequestDetails = new HttpGet("https://www.sfr.fr/espace-client/consulter-factures-mobile/consultation/lireFadetPDF.html?ligne=");
			HttpResponse pdfFileResponseDetails = httpclient.execute(pdfFileRequestDetails);
			
			HttpEntity pdfFileEntityDetails = pdfFileResponseDetails.getEntity();
			
			if (pdfFileEntityDetails != null) {
				logger.debug("Downloading facture...");

				try (FileOutputStream fos = new FileOutputStream(getDestinationFile() + " - details.pdf")) { // don't forget the file suffix
				
					try (InputStream instream = pdfFileEntityDetails.getContent()) {
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
		
		logger.info("End of SFRMobilePlugin");
	}
	
	
	
	
	// for testing
	public static void main(String[] arg) {
		
		// Instantiate our plugin
		SimpleGedGetterPlugin p = new SFRMobilePlugin();
		
		// create properties list 
		List<SimpleGedPluginProperty> properties = new ArrayList<>();
		
		// create the required properties
		SimpleGedPluginProperty phoneNumber  = new SimpleGedPluginProperty();
		phoneNumber.setPropertyKey("phone_number");
		phoneNumber.setPropertyValue("06XXXXXXXX");
		
		SimpleGedPluginProperty secretCode  = new SimpleGedPluginProperty();
		secretCode.setPropertyKey("secret_code");
		secretCode.setPropertyValue("XXXXXXX");
		
		// add the property in list
		properties.add(phoneNumber);
		properties.add(secretCode);
		
		// set properties list to our plugin
		p.setProperties(properties);
		
		// define destination file for the try
		p.setDestinationFile("sfrMobilePluginTest");
		
		// finally, try our plugin
		try {
			p.doGet();
		} catch (SimpleGedPluginException e) {
			logger.error("Epic fail :", e);
		}
	}
	
}
