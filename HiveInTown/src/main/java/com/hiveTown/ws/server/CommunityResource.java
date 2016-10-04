package com.hiveTown.ws.server;
 
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.hiveTown.Exception.HTWebConsoleException;
import com.hiveTown.data.ResidentDetails;
import com.hiveTown.data.ResidentSummary;
import com.hiveTown.model.Community;
import com.hiveTown.model.ResidentEnumType;
import com.hiveTown.model.RoleType;
import com.hiveTown.model.User;
import com.hiveTown.service.CommunityService;
import com.hiveTown.service.UserService;
import com.hiveTown.ws.server.data.HTErrorResponse;
import com.hiveTown.ws.server.data.HTSaveResidentDetailParam;

@Path("/community")
public class CommunityResource {
	
	private static final Logger LOGGER = Logger.getLogger(CommunityResource.class);
    private CommunityService communityService;
    private UserService userService;
    
    @Autowired
    public void setCommunityService(CommunityService cs){
        this.communityService = cs;
    }
    
    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }
 
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommunities() {
         
    	List<Community> list = this.communityService.getCommunities();
    	ArrayList<com.hiveTown.data.Community> commList = new ArrayList<com.hiveTown.data.Community>(10);
    	if (list != null) {
	    	for (Community c  : list) {
	    		com.hiveTown.data.Community comm = new com.hiveTown.data.Community();
	    		comm.setCommunityId(c.getId());
	    		comm.setName(c.getCommunityName());
	    		comm.setUrlKeyword(c.getUrlKeyword());
	    		commList.add(comm);
	    	}
    	}
    	return Response.status(Response.Status.OK).entity(commList).build();
    }
    
    @GET
    @Path("/{urlKeyword}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCommunityName(@PathParam("urlKeyword") String urlKeyword) {
         
    	Community c = this.communityService.getCommunityByUrlKeyword(urlKeyword);
    	if (c == null) {
    		throw new WebApplicationException(Response.Status.NOT_FOUND);
    	}
    	return c.getCommunityName();
    }
    
    @GET
    @Path("/{urlKeyword}/residents")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@PathParam("urlKeyword") String urlKeyword, 
    										//@QueryParam("search") String searchText,
    										@QueryParam("page") int pageIndex,
    										@QueryParam("limit") int maxResults) {
    	Community c = this.communityService.getCommunityByUrlKeyword(urlKeyword);
    	if (c == null) {
    		throw new WebApplicationException(Response.Status.NOT_FOUND);
    	}
    	List<ResidentDetails> list =  this.communityService.findResidents(urlKeyword, null, pageIndex, maxResults); 
    	return Response.status(Response.Status.OK).entity(list).build();
    }
    
    @GET
    @Path("/{urlKeyword}/residents/count")
    @Produces(MediaType.APPLICATION_JSON)
    public ResidentSummary totalResidents(@PathParam("urlKeyword") String urlKeyword) {
    	Community c = this.communityService.getCommunityByUrlKeyword(urlKeyword);
    	if (c == null) {
    		throw new WebApplicationException(Response.Status.NOT_FOUND);
    	}
    	ResidentSummary summary = new ResidentSummary();
    	
    	summary.setTotalVerified(this.communityService.totalResidentsByVerificationStatus(urlKeyword, true));
    	summary.setTotalUnVerified(this.communityService.totalResidentsByVerificationStatus(urlKeyword, false));
    	summary.setTotalAdmins(this.communityService.totalResidentsByRole(urlKeyword, RoleType.ADMIN));
    	summary.setTotalResidents(this.communityService.totalResidentsByRole(urlKeyword, RoleType.RESIDENT));
    	summary.setTotal(this.communityService.totalResidents(urlKeyword));
    	return summary;
    }
    
    @GET
    @Path("/{urlKeyword}/resident/{user-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResidentDetails(@PathParam("urlKeyword") String urlKeyword,
    								   @PathParam("user-id") Integer userId) {
		
    	Community c = this.communityService.getCommunityByUrlKeyword(urlKeyword);
    	if (c == null) {
    		throw new WebApplicationException(Response.Status.NOT_FOUND);
    	}
    	// do error checking and session token
    	
    	ResidentDetails details = this.communityService.getResidentDetails(c.getId(), userId);
    	
    	return Response.status(Response.Status.OK).entity(details).build();
    }
    
    @POST
    @Path("/{urlKeyword}/resident")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveResidentDetails(@PathParam("urlKeyword") String urlKeyword,
			   							HTSaveResidentDetailParam param) {
    	
    	Community c = this.communityService.getCommunityByUrlKeyword(urlKeyword);
    	if (c == null) {
    		throw new WebApplicationException(Response.Status.NOT_FOUND);
    	}
    	try {
    		
    		ResidentDetails residentDetails = param.getResidentDetails();
    		LOGGER.info("setting role:" + residentDetails.getRole());
    		
    		//if trying to add an existing user
    		if (residentDetails.getUserId() == 0) { //add
    			User user = this.userService.getUserByEmail(residentDetails.getEmail());
    			if (this.userService.isUserInCommunity(user, c)) {
    				HTErrorResponse error = new HTErrorResponse("SAVE_RESIDENT_FAILED", "User by that email already exists in this community");
    				return Response.status(Response.Status.OK).entity(error).build();
    			}
    		}
    		
    		
    		int userId = this.communityService.saveResidentDetails(c.getId(), param.getResidentDetails());
    		return Response.status(Response.Status.OK).entity(userId).build();
    	} catch (HTWebConsoleException e) {
    		LOGGER.info(param);
    		HTErrorResponse error = new HTErrorResponse("SAVE_RESIDENT_FAILED", "Save failed.");
    		return Response.status(Response.Status.OK).entity(error).build();
    	}
    }
}