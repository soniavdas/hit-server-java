package com.hiveTown.ws.server;
 
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.hiveTown.Exception.HTWebConsoleException;
import com.hiveTown.data.NoticeCommentDetails;
import com.hiveTown.data.NoticeDetails;
import com.hiveTown.data.NoticeSummary;
import com.hiveTown.data.UserData;
import com.hiveTown.model.Community;
import com.hiveTown.model.User;
import com.hiveTown.model.NoticeBoard.*;
import com.hiveTown.service.CommunityService;
import com.hiveTown.service.LoginService;
import com.hiveTown.service.NoticeBoardService;
import com.hiveTown.service.UserService;
import com.hiveTown.util.HTConstant;
import com.hiveTown.ws.server.data.HTErrorResponse;

@Path("/noticeboard")
public class NoticeBoardResource {
	
		private static final Logger LOGGER = Logger.getLogger(NoticeBoardResource.class);
	 	private NoticeBoardService noticeBoardService;
	 	private CommunityService communityService;
	 	private LoginService loginService;
	 	private UserService userService;
	 	
	    @Autowired
	    public void setNoticeBoardService(NoticeBoardService noticeBoardService){
	        this.noticeBoardService = noticeBoardService;
	    }
	    
	    @Autowired
	    public void setUserService(UserService userService){
	        this.userService = userService;
	    }
	    
	    @Autowired
	    public void setCommunityService(CommunityService communityService){
	        this.communityService = communityService;
	    }
	    
	    
	    @Autowired
	    public void setLoginService(LoginService loginService){
	        this.loginService = loginService;
	    }
	
		@GET
	    @Path("/{urlKeyword}/notice/{notice-id}")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response getNotice(@PathParam("urlKeyword") String urlKeyword, @PathParam("notice-id") Integer id,
	    						 @Context HttpServletRequest request) {
			
			String token = request.getHeader(HTConstant.TOKEN);
			Response response = ValidateNoticeAccess(urlKeyword, id, token);
			if (response != null) {
				return response;
			}
			
	    	NoticeDetails data = NoticeDetails.convert(this.noticeBoardService.getNoticeById(id));
	    	return Response.status(Response.Status.OK).entity(data).build();
	    }
		
		@GET
	    @Path("/{urlKeyword}/notices")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response getNoticesByStatus(@PathParam("urlKeyword") String urlKeyword, 
	    								   @QueryParam("status") String strStatus, 
	    								   @QueryParam("page") int pageIndex,
	    								   @QueryParam("limit") int maxResults,
	    								   @Context HttpServletRequest request) {
	        
			String token = request.getHeader(HTConstant.TOKEN);
			Response response = ValidateApiAccess(urlKeyword, token);
			if (response != null) {
				return response;
			}
			
			Community community = this.communityService.getCommunityByUrlKeyword(urlKeyword);
			
		  	List<NoticeDetails> dataList = new ArrayList<NoticeDetails>();
		  	List<Notice> noticesList = null;
		  	
		  	if (strStatus.equalsIgnoreCase("any")) {
		  		noticesList = this.noticeBoardService.getAllNotices(community, pageIndex, maxResults);
		  	} else {
			  	NoticeStatus status = NoticeStatus.valueOf(strStatus.toUpperCase());
		    	noticesList = this.noticeBoardService.getNoticesByStatus(community, status, pageIndex, maxResults);
		  	}
		  	if (noticesList != null) {
			  	for(Notice notice : noticesList) {
		    		dataList.add(NoticeDetails.convert(notice));
		    	}
		  	}
	    	return Response.status(Response.Status.OK).entity(dataList).build();
	    }
		
		@GET
	    @Path("/{urlKeyword}/notices/summary")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response getSummary(@PathParam("urlKeyword") String urlKeyword, @Context HttpServletRequest request) {
	    						 
			String token = request.getHeader(HTConstant.TOKEN);
			Response response = ValidateApiAccess(urlKeyword, token);
			if (response != null) {
				return response;
			}
			
			Community community = this.communityService.getCommunityByUrlKeyword(urlKeyword);
			NoticeSummary summary = this.noticeBoardService.getSummary(community);
			return Response.status(Response.Status.OK).entity(summary).build();
		}
		
		@POST
		@Path("/{urlKeyword}/notice/{notice-id}")
		@Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response updateNotice(@PathParam("urlKeyword") String urlKeyword, 
	    							 @PathParam("notice-id") Integer id,
	    							 NoticeDetails data, @Context HttpServletRequest request) {
			
			LOGGER.info("add/update notice:" + data);
			LOGGER.info("add/update notice Id:" + id);
			
			String token = request.getHeader(HTConstant.TOKEN);
			Response response = ValidateApiAccess(urlKeyword, token);
			if (response != null) {
				return response;
			}
			
		  	if (data.subject == null || data.subject.length() == 0) {
		  		data.subject = "Untitled";
		  	}
		  	
		  	boolean bAdd = false;
		  	Notice notice = this.noticeBoardService.getNoticeById(id);
		  	Community community = this.communityService.getCommunityByUrlKeyword(urlKeyword);
		  	User user = this.loginService.getUserByAuthToken(token);
		  	
		  	if (notice == null) {
		  		notice = new Notice();
		  		bAdd = true;
		  	} 	
		  	
		  	notice.setSubject(data.subject);
	  		notice.setDetails(data.details);
	  		if (notice.getStatus() != null && notice.getStatus().getId() != data.statusId) {
	  			notice.setDateStatusUpdated(Calendar.getInstance().getTime());
	  			notice.setStatusUpdatedBy(user);
	  		}
	  		notice.setStatus(NoticeStatus.get(data.statusId));
	  		notice.setCategory(NoticeCategory.get(data.categoryId));
	  		notice.setFromDate(Calendar.getInstance().getTime());
	  		notice.setToDate(Calendar.getInstance().getTime());
	  		
	  		//TODO update comment
	  		if (bAdd) {
	  			LOGGER.info("adding new notice..");
	  			notice.setCommunity(community);
	  			notice.setCreatedByUser(user);
	  			notice.setDateStatusUpdated(Calendar.getInstance().getTime());
	  			notice.setStatusUpdatedBy(user);
	  			//notice.setUuid(UUID.randomUUID().toString());
	  			notice.setUuid("0");
	  			this.noticeBoardService.addNotice(notice, community);
	  		} else {
	  			response = ValidateNoticeAccess(urlKeyword, id, token);
				if (response != null) {
					return response;
				}
			  	
	  			this.noticeBoardService.updateNotice(notice);
	  		}
	  		
	  		return Response.status(Response.Status.OK).entity(notice.getId()).build();
		}
		
		
		@POST
		@Path("/{urlKeyword}/notice/{notice-id}/publish")
	    public Response publishNotice(@PathParam("urlKeyword") String urlKeyword, 
	    							  @PathParam("notice-id") Integer noticeId, 
	    							  @Context HttpServletRequest request) {
			
			LOGGER.info("publish notice:" + noticeId);
			String token = request.getHeader(HTConstant.TOKEN);
			Response response = this.ValidateNoticeAccess(urlKeyword, noticeId, token);
			
			if (response != null) {
				return response;
			}
			
  			try {	
  				Community community = this.communityService.getCommunityByUrlKeyword(urlKeyword);
  				
  				this.noticeBoardService.publish(noticeId, community.getId());
  			} catch (Exception e) {
  				LOGGER.error(e);
  				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
  			}
  			return Response.status(Response.Status.OK).build();
		}
		
		@POST
		@Path("/{urlKeyword}/notice/{notice-id}/comment")
		@Consumes("text/plain")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response addComment(@PathParam("urlKeyword") String urlKeyword, 
	    		 				   @PathParam("notice-id") Integer noticeId,
	    						   String newComment,  
	    						   @Context HttpServletRequest request)  {
			
			LOGGER.info("add comment:");
			
			if (newComment == null || newComment.length() == 0) {
				HTErrorResponse output = new HTErrorResponse("REQUIRED_FIELD", "Comment is required");
				Response.status(Response.Status.OK).entity(output).build();
			}
			
			String token = request.getHeader(HTConstant.TOKEN);
			Response response = ValidateNoticeAccess(urlKeyword, noticeId, token);
			if (response != null) {
				return response;
			}
			
			User user = this.loginService.getUserByAuthToken(token);
			
			int commentId = 0;
			try {
				commentId = this.noticeBoardService.addComment(noticeId, newComment, user);
			} catch (HTWebConsoleException e) {
				LOGGER.error(e);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
			}
	    	return Response.status(Response.Status.OK).entity(commentId).build();
		}
		
		@POST
		@Path("/{urlKeyword}/comment/{comment-id}")
		@Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response updateComment(@PathParam("urlKeyword") String urlKeyword, 
	    		 				   	  @PathParam("comment-id") Integer commentId,
	    						      String newComment,  
	    						      @Context HttpServletRequest request)  {
			
			LOGGER.info("update comment:");
			
			if (newComment == null || newComment.length() == 0) {
				HTErrorResponse output = new HTErrorResponse("REQUIRED_FIELD", "Comment is required");
				Response.status(Response.Status.OK).entity(output).build();
			}
			
			String token = request.getHeader(HTConstant.TOKEN);
			Response response = ValidateApiAccess(urlKeyword, token);
			if (response != null) {
				return response;
			}
			
			NoticeComment comment = this.noticeBoardService.getComment(commentId);
			User user = this.loginService.getUserByAuthToken(token);
			
			if (comment.getCommentedByUser().getId() != user.getId()) {
				HTErrorResponse output = new HTErrorResponse("ACCESS_DENIED", "Cannot update comment by another user.");
				Response.status(Response.Status.OK).entity(output).build();
			}
			
			this.noticeBoardService.updateComment(commentId, newComment);
	    	return Response.status(Response.Status.OK).entity(commentId).build();
		}
		
		@DELETE
		@Path("/{urlKeyword}/comment/{comment-id}")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response deleteComment(@PathParam("urlKeyword") String urlKeyword, 
	    		 				      @PathParam("comment-id") Integer commentId,
	    		 				      @Context HttpServletRequest request) {
			
			LOGGER.info("delete commentId:" + commentId);
			
			String token = request.getHeader(HTConstant.TOKEN);
			Response response = ValidateApiAccess(urlKeyword, token);
			if (response != null) {
				return response;
			}
			
			try {
				User user = this.loginService.getUserByAuthToken(token);
				this.noticeBoardService.deleteComment(commentId, user, urlKeyword);
			} catch(HTWebConsoleException e) {
				LOGGER.error(e);
				HTErrorResponse error = new HTErrorResponse("NOTICE_COMMENT_DELETE_ERROR", e.getMessage());
		  		return Response.status(Response.Status.OK).entity(error).build();
			}
	    	return Response.status(Response.Status.OK).build();
		}
		
		
		@GET
	    @Path("/{urlKeyword}/comment/{comment-id}")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response getComment(@PathParam("urlKeyword") String urlKeyword, @PathParam("comment-id") Integer commentId, 
	    						   @Context HttpServletRequest request)  {
			LOGGER.info("commentId:" + commentId);
			
			String token = request.getHeader(HTConstant.TOKEN);
			Response response = ValidateApiAccess(urlKeyword, token);
			if (response != null) {
				return response;
			}
			NoticeComment noticeComment = this.noticeBoardService.getComment(commentId);
			if (noticeComment != null) {
				UserData u = new UserData();
				u.setEmail(noticeComment.getCommentedByUser().getEmail());
				u.setName(noticeComment.getCommentedByUser().getPerson().getDisplayName());
				u.setProfileUrl(noticeComment.getCommentedByUser().getProfileUrl());
				
				NoticeCommentDetails details = new NoticeCommentDetails(commentId, noticeComment.getComment(), noticeComment.getDate(), u);
				return Response.status(Response.Status.OK).entity(details).build();
			}
			LOGGER.info("not found commentId:" + commentId);
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		@DELETE
		@Path("/{urlKeyword}/notice/{notice-id}")
	    public Response deleteNotice(@PathParam("urlKeyword") String urlKeyword, 
	    		 				      @PathParam("notice-id") Integer noticeId, @Context HttpServletRequest request) {
			Response response = ValidateNoticeAccess(urlKeyword, noticeId, request.getHeader(HTConstant.TOKEN));
			if (response != null) {
				return response;
			}
			
			Notice notice = this.noticeBoardService.getNoticeById(noticeId);
			if (notice.getStatus() != NoticeStatus.DRAFT) {
				HTErrorResponse error = new HTErrorResponse("NOTICE_DELETE_ERROR", "Notice is not a draft. It cannot be deleted. " );
		  		return Response.status(Response.Status.OK).entity(error).build();
			}
			
			this.noticeBoardService.deleteNotice(noticeId);
	    	return Response.status(Response.Status.OK).build();
		}
		
		
		private Response ValidateApiAccess(String urlKeyword,  String authToken) {
			HTErrorResponse error = null;
			Community community = this.communityService.getCommunityByUrlKeyword(urlKeyword);
		  	if (community == null) {
		  		error = new HTErrorResponse(Response.Status.NOT_FOUND.toString(), urlKeyword + " community is not found." );
		  		LOGGER.error(error.getMessage());
		  		return Response.status(Response.Status.NOT_FOUND).entity(error).build();
	    	}
		  	
		  	if (authToken == null || authToken.length() == 0) {
				error = new HTErrorResponse(Response.Status.FORBIDDEN.toString(), "Authtoken is empty");
				LOGGER.error(error.getMessage());
	    		return Response.status(Response.Status.FORBIDDEN).entity(error).build();
			}
			
			User loggedUser = this.loginService.getUserByAuthToken(authToken);
			if (!this.userService.isUserInCommunity(loggedUser, community)) {
				error = new HTErrorResponse(Response.Status.FORBIDDEN.toString(), "no access to this community");
				LOGGER.error(error.getMessage());
	    		return Response.status(Response.Status.FORBIDDEN).entity(error).build();
			}
		  	return null;
		}
		
		private Response ValidateNoticeAccess(String urlKeyword, Integer noticeId, String authToken) {
			
			HTErrorResponse error = null;
			
			Response response  = this.ValidateApiAccess(urlKeyword, authToken);
			if (response != null) {
	    		return response;
			}
			
		  	Notice notice = this.noticeBoardService.getNoticeById(noticeId);
		  	Community community = this.communityService.getCommunityByUrlKeyword(urlKeyword);
			
	    	if (notice == null) {
	    		error = new HTErrorResponse(Response.Status.NOT_FOUND.toString(), "Notice is not found." );
	    		LOGGER.error(error.getMessage());
	    		return Response.status(Response.Status.NOT_FOUND).entity(error).build();
	    	}
	    	if (notice.getCommunity().getId() != community.getId()) {
	    		error = new HTErrorResponse(Response.Status.FORBIDDEN.toString(), "You don't have access to this notice.");
	    		LOGGER.error(error.getMessage());
	    		return Response.status(Response.Status.FORBIDDEN).entity(error).build();
	    	}
	    	return null;
		}
}
  