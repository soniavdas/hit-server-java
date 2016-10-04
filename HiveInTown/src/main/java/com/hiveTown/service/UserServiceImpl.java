package com.hiveTown.service;

import java.io.IOException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hiveTown.Exception.HTWebConsoleException;
import com.hiveTown.dao.UserDao;
import com.hiveTown.data.UserDetails;
import com.hiveTown.model.Community;
import com.hiveTown.model.RoleType;
import com.hiveTown.model.User;
import com.hiveTown.model.UserSettings;

public class UserServiceImpl implements UserService {
	
	
	private UserDao userDao;
	private static final Logger LOGGER = Logger.getLogger(UserService.class);
	
	@Autowired
	public void setUserDao(UserDao userDAO) {
		this.userDao = userDAO;
	}


	@Override
	@Transactional(readOnly=true)
	public User getUserById(Integer userId) {
		return this.userDao.getUserById(userId);
	}


	@Override
	@Transactional
	public Integer saveSession(Integer userId, String token) {
		return this.userDao.saveSession(userId, token);
	}


	@Override
	@Transactional
	public void saveUser(User user) {
		 this.userDao.saveUser(user);
	}


	@Override
	@Transactional(readOnly=true)
	public User getUserByEmail(String email) {
		return this.userDao.getUserByEmail(email);
	}


	@Override
	@Transactional(readOnly=true)
	public User getUserBySessionToken(String token) {
		return this.userDao.getUserBySessionToken(token);
	}


	@Override
	@Transactional(readOnly=true)
	public UserDetails getUserDetails(String email, Integer communityId) throws HTWebConsoleException {
		return this.userDao.getUserDetails(email, communityId);
	}


	@Override
	@Transactional(readOnly=true)
	public Boolean isUserInCommunity(User user, Community community) {
		return this.userDao.isUserInCommunity(user, community);
	}


	@Override
	@Transactional(readOnly=true)
	public RoleType getRole(User user, Community community) {
		return this.userDao.getRole(user, community);
	}


	@Override
	@Transactional
	public Boolean saveSocialLogin(User user, String socialEmail,
			String socialLoginType) throws HTWebConsoleException {
		return this.userDao.saveSocialLogin(user, socialEmail, socialLoginType);
	}

	@Override
	@Transactional
	public void setEmailPreference(Integer userId, boolean unsubscribeAll, boolean unsubscribeComments) {
		User user = this.userDao.getUserById(userId);
		if (user != null) {
			UserSettings us = user.getUserSettings();
			us.setUnsubscribedAll(unsubscribeAll);
			us.setUnsubscribedComments(unsubscribeComments);
			us.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			this.userDao.saveUser(user);
			LOGGER.info(" email:" + user.getEmail() + " unsubscribe all:" + unsubscribeAll + " unsubscribe comments: " + unsubscribeComments);
		}
	}


	@Override
	@Transactional(readOnly=true)
	public User getUserByCode(String code) {
		return this.userDao.getUserByCode(code);
	}


	@Override
	@Transactional(readOnly=true)
	public Community getDefaultCommunityForUser(Integer userId) {
		return this.userDao.getDefaultCommunityForUser(userId);
	};
	
}
