package com.hiveTown.util;
/*
 * Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import antlr.collections.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.model.Address;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.services.simpleemail.model.Message;

@PropertySource("classpath:aws-ses.properties")
public class AmazonSESUtil {

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(AmazonSESUtil.class);
    
    @Value("${ses.accesskey}")
    public String accessKey;
    
    @Value("${ses.secretkey}")
    public  String secretKey;
    
    /*
     * Before running the code:
     *      Fill in your AWS access credentials in the provided credentials
     *      file template, and be sure to move the file to the default location
     *      (C:\\Users\\appusree\\.aws\\credentials) where the sample code will load the
     *      credentials from.
     *      https://console.aws.amazon.com/iam/home?#security_credential
     *
     * WARNING:
     *      To avoid accidental leakage of your credentials, DO NOT keep
     *      the credentials file in your source directory.
     */

    
    private AmazonSimpleEmailServiceClient getClient() {

        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        
        // Instantiate an Amazon SES client, which will make the service call with the supplied AWS credentials.
        AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);
        Region REGION = Region.getRegion(Regions.US_WEST_2);
        client.setRegion(REGION);
        return client;
    }

    
    public String sendRawEmail(String[] toEmail, String fromAddress, MimeMessage mimeMessage) {
    	try {
	    	 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    	 mimeMessage.writeTo(outputStream);
	    	 RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));
	
	        // Assemble the email.
			SendRawEmailRequest request = new SendRawEmailRequest().withSource(fromAddress).withDestinations(toEmail).withRawMessage(rawMessage);
			
            // Send the email.
            SendRawEmailResult r = this.getClient().sendRawEmail(request);
            
            LOGGER.info(r.toString());
            return r.getMessageId();

        } catch (Exception ex) {
          LOGGER.error(ex.toString());
        }
		return null;
    }
    
    
    public boolean sendEmail(String[] toEmail, String strBody, String strSubject, String fromAddress, String replyToAddress) throws IOException {

        // Construct an object to contain the recipient address.
        Destination destination = new Destination().withToAddresses(toEmail);

        // Create the subject and body of the message.
        Content subject = new Content().withData(strSubject);
        Content textBody = new Content().withData(strBody);
        Body body = new Body().withHtml(textBody);

        // Create a message with the specified subject and body.
        Message message = new Message().withSubject(subject).withBody(body);
       
        // Assemble the email.
        SendEmailRequest request = new SendEmailRequest().withSource(fromAddress).withDestination(destination).withMessage(message).withReplyToAddresses(replyToAddress);
        boolean isEmailSent = false;
        try {
            // Send the email.
            SendEmailResult r = this.getClient().sendEmail(request);
            LOGGER.info(r.toString());
            isEmailSent = true;

        } catch (Exception ex) {
          LOGGER.error(ex.toString());
        }
		return isEmailSent;
    }
    
  
}
