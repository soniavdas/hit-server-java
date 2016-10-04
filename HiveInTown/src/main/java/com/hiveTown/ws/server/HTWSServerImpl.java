package com.hiveTown.ws.server;

import java.util.Locale;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import com.hiveTown.Exception.HTClientServicesException;
import com.hiveTown.Exception.HTWebConsoleException;
import com.hiveTown.data.UserDetails;
import com.hiveTown.model.Community;
import com.hiveTown.model.RoleType;
import com.hiveTown.model.User;
import com.hiveTown.model.UserCommunity;
import com.hiveTown.service.CommunityService;
import com.hiveTown.service.EmailService;
import com.hiveTown.service.LoginService;
import com.hiveTown.service.UserService;
import com.hiveTown.util.HTConstant;
import com.hiveTown.util.HTUtil;
import com.hiveTown.ws.server.data.HTErrorResponse;
import com.hiveTown.ws.server.data.HTLoginFirstTimeParam;
import com.hiveTown.ws.server.data.HTLoginParam;
//import com.sun.jersey.api.client.WebResource;

//@Api(value ="HTServer" , description ="APIs for HiveInTown")
@Component
@Path("/HTServer")
public class HTWSServerImpl {

	@Context
	UriInfo uriInfo;

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(HTWSServerImpl.class);

	private UserService userService;
	private LoginService loginService;
	private ResourceBundleMessageSource resourceBundle;
	private EmailService emailService;
	private CommunityService communityService;
	
	@Autowired
    public void setResourceBundle(ResourceBundleMessageSource resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
    
	private ResourceBundleMessageSource getResourceBundle() {
		return this.resourceBundle;
	}
	
	@Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }
	
	@Autowired
    public void setLoginService(LoginService loginService){
        this.loginService = loginService;
    }

	@Autowired
    public void setEmailService(EmailService emailService){
        this.emailService = emailService;
    }
	
	@Autowired
    public void setCommunityService(CommunityService communityService){
        this.communityService = communityService;
    }
	
	public HTErrorResponse getErrorResponse(String code) {
		String msg = "";
		try {
			msg = getResourceBundle().getMessage(code, null, Locale.getDefault());
		} catch(NoSuchMessageException e) {
			LOGGER.error(e + " code");
		}
		return new HTErrorResponse(code, msg);
	}
	
	/**
	 * @param params
	 *            HTLoginParam - emailId, token,loginAPI,urlKeyword
	 * @param response
	 * @return UserDetails - firstName, lastName, userId , roleId
	 * @throws HTClientServicesException
	 */
	@POST
	@Path("/signup")
	@Consumes("application/json")
	@Produces("application/json")
	public Response loginUserFirst(HTLoginFirstTimeParam params,
			@Context HttpServletResponse response)
			throws HTClientServicesException {

		LOGGER.info("user signing up: " + params.toString());
		
		UserDetails userDetails = new UserDetails();
		HTErrorResponse htResp = null;
		try {
			
			User user = this.userService.getUserByEmail(params.emailId);
			Community community = this.communityService.getCommunityByUrlKeyword(params.getUrlKeyword());
			
			if (user == null) {
				htResp = this.getErrorResponse("USER_NOT_FOUND");
				return Response.status(Response.Status.OK).entity(htResp).build();
			}
			
			if (!user.getConfirmationCode().equals(params.ecode)) {
				htResp = this.getErrorResponse("INVALID_ECODE");
				return Response.status(Response.Status.OK).entity(htResp).build();
			} 
			
			
			if (!this.userService.isUserInCommunity(user, community)){
				htResp = this.getErrorResponse("ACCESS_FORBIDDEN");
				return Response.status(Response.Status.OK).entity(htResp).build();
			} 
			
			
			//if Id used to log in is different than the id present in email
			if(!params.emailId.equals(params.socialEmail)) {
				LOGGER.debug("user is signing with different email.."+ params.socialEmail);
				User socialUser = this.userService.getUserByEmail(params.socialEmail);
				if (socialUser == null) {
					LOGGER.debug("save linked social record:" + params.socialEmail);
					this.userService.saveSocialLogin(user, params.socialEmail, params.loginAPI);
				} else {
					//TODO link parent
				}
			}
			
			userDetails = this.userService.getUserDetails(params.getEmailId(), community.getId());
			
			String token = HTUtil.generateToken();
			userDetails.setToken(token);
			response.setHeader(HTConstant.TOKEN, token);
			response.setHeader("Access-Control-Allow-Headers", HTConstant.TOKEN);
			this.loginService.saveSession(userDetails.getUserId(), token);
			this.loginService.setEmailVerified(userDetails.getUserId());
			this.loginService.setSocialLogin(userDetails.getUserId(), params.loginAPI);
			this.loginService.setProfileUrl(userDetails.getUserId(), params.getProfileUrl());
			return Response.status(Response.Status.OK).entity(userDetails).build();
		} catch (HTWebConsoleException e) {
			LOGGER.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
		} catch (AuthenticationException e) {
			LOGGER.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
	}

	/**
	 * @param params
	 *            HTLoginParam - emailId, token,loginAPI,urlKeyword
	 * @param response
	 * @return UserDetails - firstName, lastName, userId , roleId
	 * @throws HTClientServicesException
	 */
	@POST
	@Path("/loginUser")
	@Consumes("application/json")
	@Produces("application/json")
	public  Response logInUser(HTLoginParam params,
			@Context HttpServletResponse response)
			throws HTClientServicesException {
		
		LOGGER.info("user login: " + params.toString());
		
		HTErrorResponse htResp = null;
		UserDetails userDetails = new UserDetails();
		try {
			
			if (params.loginAPI.equals("gplus") && !HTUtil.isValidToken(params.token)) {
				LOGGER.error("invalid google token");
				throw new HTClientServicesException("GOOGLE_TOKEN_INVALID", "Invalid google token.");
			} 
			
			Community community = this.communityService.getCommunityByUrlKeyword(params.urlKeyword);
			User user = this.userService.getUserByEmail(params.getEmailId());
			
			if (user == null) {
				String msg = getResourceBundle().getMessage("LOGIN_USER_NOT_FOUND", null, Locale.getDefault());
				msg = String.format(msg, params.getEmailId());
				htResp = new HTErrorResponse("LOGIN_USER_NOT_FOUND", msg);
				return Response.status(Response.Status.OK).entity(htResp).build();
			} 
			
			if (!user.getIsVerified()) {
				htResp = this.getErrorResponse("EMAIL_NOT_VERIFIED");
				return Response.status(Response.Status.OK).entity(htResp).build();
			} 
			
			// get community for user
			if (community == null) {
				LOGGER.info("getting default community");
				community = this.userService.getDefaultCommunityForUser(user.getId());
			}
			
			if (!this.userService.isUserInCommunity(user, community)){
				String msg = getResourceBundle().getMessage("ACCESS_FORBIDDEN", null, Locale.getDefault());
				msg = String.format(msg, params.getEmailId());
				htResp = new HTErrorResponse("ACCESS_FORBIDDEN", msg);
				return Response.status(Response.Status.OK).entity(htResp).build();
			} 
			
			userDetails = this.userService.getUserDetails(params.getEmailId(), community.getId());	
			userDetails.setCommunityUrlKeyword(community.getUrlKeyword());
			String token = HTUtil.generateToken();
			response.setHeader(HTConstant.TOKEN, token);
			response.setHeader("Access-Control-Allow-Headers", HTConstant.TOKEN);
			userDetails.setToken(token);
			this.loginService.saveSession(userDetails.getUserId(), token);
			return Response.status(Response.Status.OK).entity(userDetails).build();
			
		} catch (HTWebConsoleException e) {
			LOGGER.error(e);
			throw new HTClientServicesException("ERROR_GETTING_USER_DETAILS", e.getMessage());
		} catch (AuthenticationException e) {
			LOGGER.error(e);
			throw new HTClientServicesException("ERROR_GENERATING_TOKEN", e.getMessage());
		} 
	}

	@POST
	@Path("/sendinvite/{urlKeyword}/{email}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response sendInvite(@PathParam("email") String email, @PathParam("urlKeyword") String urlKeyword) throws HTClientServicesException {
		
		LOGGER.info("Sending invite:" + email);
		
		HTErrorResponse htResp;
		String path = uriInfo.getBaseUri().toString();
		if (email.isEmpty()) {
			htResp = new HTErrorResponse("REQUIRED_FIELD", "Email is required to send link.");
			return Response.status(Response.Status.OK).entity(htResp).build();
		} 
		
		Community community = this.communityService.getCommunityByUrlKeyword(urlKeyword);
		User user = this.userService.getUserByEmail(email);
		
		if (user == null) {
			htResp = this.getErrorResponse("USER_NOT_FOUND");
			return Response.status(Response.Status.OK).entity(htResp).build();
		} 
		
		if (!this.userService.isUserInCommunity(user, community)) {
			LOGGER.error("User not in community");
			htResp = this.getErrorResponse("ACCESS_FORBIDDEN");
			return Response.status(Response.Status.OK).entity(htResp).build();
		}
		
		RoleType role = this.userService.getRole(user, community);
		Boolean bSent = this.emailService.sendInvite(email, user.getConfirmationCode(), 
										path, Integer.toString(role.getId()), urlKeyword, community.getFromAddress(), community.getReplyToAddress());
		if (!bSent) {
			htResp = this.getErrorResponse("ERROR_SENDING_EMAIL");
			LOGGER.error("error sending email: " + user.getEmail());
			return Response.status(Response.Status.OK).entity(htResp).build();
		}
		return Response.status(Response.Status.OK).build();
	}
	
	@POST
	@Path("/sendinvites/{urlKeyword}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response sendInvites(@PathParam("urlKeyword") String urlKeyword, @Context HttpServletRequest request) throws HTClientServicesException {
		
		String path = uriInfo.getBaseUri().toString();
		
		String token = request.getHeader(HTConstant.TOKEN);
		LOGGER.debug("token:" + token);
		User user = this.loginService.getUserByAuthToken(token);
		if (user == null) {
			LOGGER.error("User not found for token:" + token);
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid auth token. User not found").build();
		}
		
		this.communityService.sendInvites(urlKeyword, path, user.getEmail());
		return Response.status(Response.Status.OK).build();
	}
	

	@POST
	@Path("/removeuser/{urlKeyword}/{user-id}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response removeUser(@PathParam("urlKeyword") String urlKeyword, @PathParam("user-id") Integer userId, 
							@Context HttpServletRequest request) throws HTClientServicesException {
		
		LOGGER.info(urlKeyword + " userId:" + userId);
		User user = this.userService.getUserById(userId);
		if (user == null) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		Community community = this.communityService.getCommunityByUrlKeyword(urlKeyword);
		
		User loggedUser = this.loginService.getUserByAuthToken(request.getHeader(HTConstant.TOKEN));
		if (this.hasAdminRole(loggedUser, community)) {
		
			Boolean bOK = this.communityService.removeUser(community, user.getId());
			return Response.status(Response.Status.OK).build();
		} else {
			LOGGER.error("no access for deleting user: " + loggedUser.getEmail());
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}
	
	
	@POST
	@Path("/unsubscribe")
	@Consumes("application/json")
	public Response unsubscribe(@QueryParam("code") String code,   
								@QueryParam("unsubscribe_all") boolean unsubscribeAll,
								@QueryParam("unsubscribe_comments") boolean unsubscribeComments ) {
		LOGGER.info(" code" + code);
		User user = this.userService.getUserByCode(code);
		this.userService.setEmailPreference(user.getId(), unsubscribeAll, unsubscribeComments);
		return Response.status(Response.Status.OK).build();
	}
	
	@POST
	@Path("/settings")
	@Consumes("application/json")
	public Response setEmailPreference( 
									   @QueryParam("unsubscribe_all") boolean unsubscribeAll,
									   @QueryParam("unsubscribe_comments") boolean unsubscribeComments, 
									   @Context HttpServletRequest request)  {
		LOGGER.info(" setEmailPreference:" + unsubscribeAll + ":" + unsubscribeComments);
		
		User user = this.loginService.getUserByAuthToken(request.getHeader(HTConstant.TOKEN));
		
		if (user == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid auth token. User not found").build();
		}
	    this.userService.setEmailPreference(user.getId(), unsubscribeAll, unsubscribeComments);
		return Response.status(Response.Status.OK).build();
	}
	
	@GET
	@Path("/settings")
	@Produces("application/json")
	public Response getUserSettings(@Context HttpServletRequest request)  {
		
		User user = this.loginService.getUserByAuthToken(request.getHeader(HTConstant.TOKEN));
		
		if (user == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid auth token. User not found").build();
		}
		return Response.status(Response.Status.OK).entity(user.getUserSettings()).build();
	}
	
	private Boolean hasAdminRole(User user, Community community) {
		if (user != null && this.userService.isUserInCommunity(user, community)) {
			RoleType role = userService.getRole(user, community);
			
			if (role == RoleType.ADMIN) {
				return true;
			}
		}
		
		return false;
	}
}

