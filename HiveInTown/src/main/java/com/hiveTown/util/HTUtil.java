package com.hiveTown.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.sasl.AuthenticationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.jboss.logging.Logger;
import org.json.JSONObject;

public class HTUtil {

	private static final Logger LOGGER = Logger.getLogger(HTUtil.class);

	/**
	 * Check if Google token from server is valid
	 * 
	 * @param token
	 * @return
	 */
	/**
	 * @param token
	 * @return
	 */
	public static boolean isValidToken(String token) {

		// String connection1;
		boolean isvalidToken = false;
		try {
			String connection = connectionGoogle(HTConstant.GOOGLEOAUTHURL,
					HTConstant.ID_TOKEN + token);
			LOGGER.debug("connection:" +connection);

			JSONObject obj = new JSONObject(connection);
			String emailId = obj.getString("email");
			boolean verifiedEmail = obj.getBoolean("email_verified");

			// if email id is verified
			if (verifiedEmail) {
				LOGGER.info("google auth verified:" + emailId);
				isvalidToken = true;
			}

			// return isvalidToken;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		// }
		return isvalidToken;

	}

	/**
	 * Check if the connection to google API is valid
	 * 
	 * @param url
	 * @param parameter
	 * @return
	 * @throws MalformedURLException
	 * @throws ProtocolException
	 * @throws IOException
	 */
	public static String connectionGoogle(String url, String parameter)
			throws MalformedURLException, ProtocolException, IOException {

		URL url1 = new URL(url);
		HttpURLConnection request1 = (HttpURLConnection) url1.openConnection();
		request1.setRequestMethod("GET");
		request1.setDoOutput(true);
		request1.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		OutputStreamWriter wr = new OutputStreamWriter(
				request1.getOutputStream());
		wr.write(parameter);
		wr.flush();
		request1.connect();
		String responseBody = convertStreamToString(request1.getInputStream());
		wr.close();
		return responseBody;
	}

	/**
	 * Generates Token for the client
	 * 
	 * @return
	 * @throws AuthenticationException
	 */
	public static String generateToken() throws AuthenticationException {
		try {
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			byte[] temp = new byte[100];
			sr.nextBytes(temp);
			String n = new String(Hex.encodeHex(temp));
			return n;
		} catch (Exception e) {
			throw new AuthenticationException(e.getMessage(), e);
		}
	}

	/**
	 * @param is
	 * @return
	 */
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}

		return sb.toString();
	}

	

	/**
	 * Read the uploaded CSV file
	 * 
	 * @param csvFile
	 * @throws HTClientServicesException
	 */
	

	/**
	 * @param email
	 * @return
	 */
	public static boolean validateEmail(final String email) {

		Matcher matcher;

		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);

		matcher = pattern.matcher(email);
		return matcher.matches();

	}
	
	/**
	 * @param email
	 * @return
	 */
	public static String uniqueKey(final String email){
		Random randomGenerator = new Random();
		 int randomInt = randomGenerator.nextInt(999999999);
		byte[] encodedBytes = Base64.encodeBase64((email+randomInt).getBytes());
		return  new String(encodedBytes);
	}
}
