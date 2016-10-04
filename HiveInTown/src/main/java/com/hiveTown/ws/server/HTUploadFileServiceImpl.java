package com.hiveTown.ws.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;

import com.hiveTown.Exception.HTClientServicesException;
import com.hiveTown.Exception.HTWebConsoleException;
import com.hiveTown.data.ResidentDetails;
import com.hiveTown.model.Community;
import com.hiveTown.model.RoleType;
import com.hiveTown.model.User;
import com.hiveTown.service.CommunityService;
import com.hiveTown.service.EmailService;
import com.hiveTown.service.LoginService;
import com.hiveTown.service.UserService;
import com.hiveTown.util.HTUtil;
import com.hiveTown.ws.server.data.HTFileUploadParams;

@Path("/file")
public class HTUploadFileServiceImpl {

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(HTUploadFileServiceImpl.class);
	@Context
	UriInfo uriInfo;

	private UserService userService;
	private EmailService emailService;
	private LoginService loginService;
	private CommunityService communityService;
	
	@Autowired
    public void setCommunityService(CommunityService communityService){
        this.communityService = communityService;
    }
	
	@Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }
	
	@Autowired
    public void setEmailService(EmailService emailService){
        this.emailService = emailService;
    }
	
	@Autowired
    public void setLoginService(LoginService loginService){
        this.loginService = loginService;
    }
	
	@POST
	@Path("/upload/{urlkeyword}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@PathParam("urlkeyword") String urlkeyword, @Context HttpServletRequest request) 
					throws HTClientServicesException {
		
		String token = request.getHeader("HTToken");
		LOGGER.debug("token:" + token);
		User user = this.loginService.getUserByAuthToken(token);
		Community community = this.communityService.getCommunityByUrlKeyword(urlkeyword);
		
		if (user == null) {
			LOGGER.error("User not found for token:" + token);
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid auth token. User not found").build();
		}
		
		if (!this.userService.isUserInCommunity(user, community)) {
			LOGGER.error("User not in community");
			return Response.status(Response.Status.UNAUTHORIZED).entity("User not in community").build();
		}
		
		RoleType role = this.userService.getRole(user, community);
		
		if (role != RoleType.ADMIN) {
			LOGGER.error("Not admin");
			return Response.status(Response.Status.UNAUTHORIZED).entity("Role not admin").build();
		}
		
		String path = uriInfo.getAbsolutePath().toString();
		path = path.replaceAll("server/file/upload/comname", "");
		
		String errors = this.readCSV(uploadedInputStream, urlkeyword, path);
		if (errors.length() > 0) {
			this.emailService.sendLog(user.getEmail(), errors, community.getFromAddress(), community.getReplyToAddress());
		}
		return Response.status(Response.Status.OK).entity(errors).build();
	}

	private String readCSV(InputStream csvFile, String urlkeyword, String path) throws HTClientServicesException {
		Map<Integer, String> rejectedId = new HashMap<Integer, String>();
		
		LOGGER.debug("READ CSV for: "+urlkeyword);
		StringBuilder errors = new StringBuilder();
		
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {
			
			Community community = this.communityService.getCommunityByUrlKeyword(urlkeyword);
			
			br = new BufferedReader(new InputStreamReader(csvFile));
			HashSet<HTFileUploadParams> paramsSet = new HashSet<HTFileUploadParams>();
			rejectedId = new HashMap<Integer, String>();
			HTFileUploadParams paramsObj = null;
			int lineNo = 0;
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] paramsInCsv = line.split(cvsSplitBy);

				String emailInSet = null;
				String name = null;
				String contactNo = null;
				String appartment = null;
				String block = null;
				lineNo++;

				// so that all email ids are saved in same format and duplicates
				// are avoided and formatting of other strings

				for (int i = 0; i <= 4; i++) {
					if (paramsInCsv.length >= i + 1 && paramsInCsv[i] != null) {
						switch (i) {
						case 0:
							if (HTUtil.validateEmail(paramsInCsv[i].trim()
									.toLowerCase())) {
								emailInSet = paramsInCsv[i].trim()
										.toLowerCase();
							} else {
								rejectedId.put(lineNo, paramsInCsv[i].trim()
										.toLowerCase());
								i++;
							}
							break;
						case 1:
							name = paramsInCsv[i].toLowerCase().trim();
							break;
						case 2:
							contactNo = paramsInCsv[i].toLowerCase().trim();
							break;

						case 3:
							appartment = paramsInCsv[i].toLowerCase().trim();
							break;
						case 4:
							block = paramsInCsv[i].toLowerCase().trim();
							break;

						default:
							break;
						}
					}
				}
			
				paramsObj = new HTFileUploadParams(emailInSet, name, contactNo,
						appartment, block, lineNo,urlkeyword);

				if (emailInSet != null) {
					Boolean ret = paramsSet.add(paramsObj);
					if (!ret) {
						String str = String.format("DUPLICATE: row#: %s email: %s\n", paramsObj.getLineNo(), paramsObj.getEmail());
						errors.append(str);
						LOGGER.error(str);
					}
				}
			}
			LOGGER.debug("*****************************************");
			for (HTFileUploadParams htFileUploadParams : paramsSet) {
			
				try {
					
					ResidentDetails rd = new ResidentDetails();
					rd.setEmail(htFileUploadParams.getEmail());
					rd.setApartmentNum(htFileUploadParams.getAppartment());
					rd.setBlockName(htFileUploadParams.getBlock());
					rd.setName(htFileUploadParams.getName());
					rd.setContactNum(htFileUploadParams.getContactNo());
					rd.setRole(RoleType.RESIDENT);
					rd.setIsVerified(false);
					
					User user = this.userService.getUserByEmail(rd.getEmail());
					if (user != null) {
						String str = String.format("USER ALREADY EXISTSs: row#: %s email: %s\n", htFileUploadParams.getLineNo(), htFileUploadParams.getEmail());
						errors.append(str);
						LOGGER.error(str);
						continue;
					} 
					int userId = this.communityService.saveResidentDetails(community.getId(), rd);
					if (userId > 0) {
						LOGGER.debug(String.format("SUCCESS: row#: %s email: %s userId: %d", 
												htFileUploadParams.getLineNo(), htFileUploadParams.getEmail(), userId));
						//user = this.userService.getUserById(userId);
						//this.emailService.sendInvite(user.getEmail(), user.getConfirmationCode(), 
						//							path, Integer.toString(rd.getRole().getId()), urlkeyword);
					}
				} catch (HTWebConsoleException e) {
					// TODO Auto-generated catch block
					String str = String.format("FAILED: row#: %s email: %s\n", htFileUploadParams.getLineNo(), htFileUploadParams.getEmail());
					errors.append(str);
					LOGGER.error(str);
					LOGGER.error(e);
				}
			}

			LOGGER.debug("*******************ArrayList ends here***********************");
			LOGGER.debug("************************Rejected Ones..********************");
			for (Map.Entry<Integer, String> entry : rejectedId.entrySet()) {
				String str = ("Invalid row: " + entry.getValue() + " Row#:" + entry.getKey() + "\n");
				LOGGER.info(str);
				errors.append(str);
			}

		} catch (FileNotFoundException e) {
			LOGGER.error(e);
		} catch (IOException e) {
			LOGGER.error(e);
		} 
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					LOGGER.error(e);
				}
			}
		}
		return errors.toString();
	}
	// save uploaded file to new location
	private void writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}
