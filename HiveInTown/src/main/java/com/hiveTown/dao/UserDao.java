package com.hiveTown.dao;
 
import java.util.List;
 











import com.hiveTown.Exception.HTWebConsoleException;
import com.hiveTown.data.UserDetails;
import com.hiveTown.model.Community;
import com.hiveTown.model.RoleType;
import com.hiveTown.model.HTSession;
import com.hiveTown.model.User;
 
public interface UserDao {
 
    User getUserById(Integer userId);
	Integer saveSession(Integer userId, String token);
	
	void saveUser(User user);
	User getUserByEmail(String email);
	User getUserBySessionToken(String token);
	UserDetails getUserDetails(String email, Integer communityId) throws HTWebConsoleException;
	Boolean isUserInCommunity(User user, Community community);
	RoleType getRole(User user, Community community);
	Boolean saveSocialLogin(User user, String socialEmail,
			String socialLoginType) throws HTWebConsoleException;
	User getUserByCode(String code);
	Community getDefaultCommunityForUser(Integer userId);
}
