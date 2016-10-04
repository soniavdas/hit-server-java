package com.hiveTown.service;

import java.util.List;

import com.hiveTown.model.User;
import com.hiveTown.model.NoticeBoard.*;

public interface LoginService {
	Integer saveSession(Integer userId, String token);

	void setEmailVerified(Integer userId);

	User getUserByEmail(String email);
	
	User getUserByAuthToken(String token);
	
	void setProfileUrl(Integer userId, String profileUrl);

	void setSocialLogin(Integer userId, String socialLoginType);
}
