package com.hiveTown.util;
 
import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
 
/**
 * Google OAuth2.0 
 * Released under the same license as scribe (MIT License)
 * @author yincrash
 * 
 */
public class Google2Api  {
	private static final String NETWORK_NAME = "Google";
private static final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";
private static final String SCOPE = "https://mail.google.com/ https://www.googleapis.com/auth/userinfo.email";
private static final Token EMPTY_TOKEN = null;

public static void main(String[] args)
{}
}