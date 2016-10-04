package com.hiveTown.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hiveTown.dao.UserDao;
import com.hiveTown.util.*;

public class EmailService {

	private static final Logger LOGGER = Logger.getLogger(EmailService.class);
	private static final String TEMPLATE_FILE = "EmailTemplates/emailtemplate.html";
			
	private AmazonSESUtil mailSender;
	
	
	@Autowired
	public void setMailSender(AmazonSESUtil mailSender) {
		this.mailSender = mailSender;
	}

	
	public Boolean sendInvite(String toEmail, String ecode, String path, String roleId, String communityUrlKeyword, String fromAddress, String replyToAddress) {
		
		try {
			LOGGER.info("path:" + path);
			String strSubject = "Verify your email address";
			String strBody = String.format("You are invited to join %s community!\n\n. Please click on following link\n", communityUrlKeyword);
	        path = path.replaceAll("HiveInTown/server/", "");
	        String link ="#/"+communityUrlKeyword+"/signup?r="+roleId+"&e="+toEmail+"&c="+java.net.URLEncoder.encode(ecode, "UTF-8");
	        strBody = strBody + path +link;
	        
	        return this.mailSender.sendEmail(new String[]{toEmail}, strBody, strSubject, fromAddress, replyToAddress);
        	
        } catch(Exception e) {
        	LOGGER.error(e);
        	return false;
        }
	}
	
	public String sendNotice(List<String> toEmails, String subject,  String notice, String fromAddress, String replyToAddress) {
		try {
			LOGGER.info("Sending emails to" + toEmails);
			LOGGER.info("Subject:" + subject);
			LOGGER.info("fromAddress:" + fromAddress);
			LOGGER.info("replyToAddress:" + replyToAddress);
			
			String[] toEmailAarray = (String[]) toEmails.toArray(new String[toEmails.size()]);
			String body = this.getTemplateFile(TEMPLATE_FILE);
			if (body.isEmpty()) {
				body = notice;
			} else {
				body = body.replaceFirst("<%nb_subject%>", subject);
				body = body.replaceFirst("<%nb_details%>", notice);
			}
			MimeMessage mimeMessage = this.composeMessage(toEmailAarray, body, subject, fromAddress, replyToAddress);
			//mimeMessage.addHeader(HTConstant.NOTICE_UUID_KEY, uuid);
			return this.mailSender.sendRawEmail(new String[]{}, fromAddress, mimeMessage);
		} catch(Exception e) {
        	LOGGER.error(e);
        	return null;
        }
	}
	
	public void sendLog(String toEmail, String errors, String fromAddress, String replyToAddress) {
		try {
			this.mailSender.sendEmail(new String[]{toEmail}, "ERROR LOG", errors, fromAddress, replyToAddress);
		} catch(Exception e) {
			LOGGER.error(e);
		}
	}

    
    private MimeMessage composeMessage(String[] toEmail, String strBody, String strSubject, String fromAddress, String replyToAddress) 
    									throws MessagingException {
    	
    	  Session session = Session.getInstance(new Properties()); 
    	
    	  MimeMessage msg = new MimeMessage(session);
          
          // Sender and recipient
          InternetAddress fromAddressObj = new InternetAddress(fromAddress);
          msg.setFrom(fromAddressObj);
          
          int i = 0;
          InternetAddress[] addresses = new  InternetAddress[toEmail.length];
          for( String e : toEmail) {
        	  addresses[i++] = new InternetAddress(e);
          }
          msg.setRecipients(RecipientType.BCC, addresses);
          // Subject
          msg.setSubject(strSubject);
          InternetAddress[] replyTo = new InternetAddress[1];
          replyTo[0] = new InternetAddress(replyToAddress);
          msg.setReplyTo(replyTo);   
          
          // Add a MIME part to the message
          MimeMultipart mp = new MimeMultipart();
   		            
          BodyPart part = new MimeBodyPart();
          part.setContent(strBody, "text/html");
          mp.addBodyPart(part);
   		                        
          msg.setContent(mp);
          return msg;
	}
    
    private String getTemplateFile(String fileName) {
    	StringBuilder result = new StringBuilder("");

    	//Get file from resources folder
    	ClassLoader classLoader = getClass().getClassLoader();
    	InputStreamReader is = new InputStreamReader(classLoader.getResourceAsStream(fileName));
    	BufferedReader br = new BufferedReader(is);
    	try {
	    	String read = br.readLine();
	    	
	    	while(read != null) {
	    	    //System.out.println(read);
	    		result.append(read);
	    	    read =br.readLine();
	    	}
    	} catch(IOException ex) {
    		LOGGER.error(ex.toString());
    	}
    	return result.toString();

      }

}
