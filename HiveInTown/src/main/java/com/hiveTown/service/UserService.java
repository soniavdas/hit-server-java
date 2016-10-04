package com.hiveTown.service;

import java.io.IOException;

import com.hiveTown.Exception.HTWebConsoleException;
import com.hiveTown.data.ResidentDetails;
import com.hiveTown.data.UserDetails;
import com.hiveTown.model.Community;
import com.hiveTown.model.RoleType;
import com.hiveTown.model.User;
import com.hiveTown.ws.server.data.HTFileUploadParams;

public interface UserService {

	User getUserById(Integer userId);
	Integer saveSession(Integer userId, String token);
	
	void saveUser(User user);
	User getUserByEmail(String email);
	User getUserBySessionToken(String token);
	User getUserByCode(String code);
	UserDetails getUserDetails(String email, Integer communityId) throws HTWebConsoleException;
	Boolean isUserInCommunity(User user, Community community);
	RoleType getRole(User user, Community community);
	Boolean saveSocialLogin(User user, String socialEmail,
			String socialLoginType) throws HTWebConsoleException;
	void setEmailPreference(Integer userId, boolean unsubscribeAll, boolean unsubscribeComments);
	
	Community getDefaultCommunityForUser(Integer userId);
}
