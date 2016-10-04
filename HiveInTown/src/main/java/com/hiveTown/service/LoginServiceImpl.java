package com.hiveTown.service;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hiveTown.dao.UserDao;
import com.hiveTown.model.User;

@Transactional(readOnly=true, rollbackFor = Exception.class)
public class LoginServiceImpl implements LoginService {
   
	private SessionFactory sessionFactory;
	private UserDao userDao;
 	
 	@Autowired
    public void setSessionFactory(SessionFactory sf){
        this.sessionFactory = sf;
    }
 	
    @Autowired
    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    @Override
	@Transactional(readOnly=false)
	public Integer saveSession(Integer userId, String token) {
		return this.userDao.saveSession(userId, token);
	}
	
    @Override
	@Transactional(readOnly=false)
	public void setEmailVerified(Integer userId) {
    	User user = this.userDao.getUserById(userId);
    	user.setIsVerified(true);
		this.userDao.saveUser(user);
	}
    
    @Override
	@Transactional(readOnly=false)
	public void setSocialLogin(Integer userId, String socialLoginType) {
    	User user = this.userDao.getUserById(userId);
    	user.setSocialLoginType(socialLoginType);
		this.userDao.saveUser(user);
	}
    
    @Override
	@Transactional
	public User getUserByEmail(String email) {
		return this.userDao.getUserByEmail(email);
	}

	@Override
	@Transactional
	public User getUserByAuthToken(String token) {
		if (token == null || token.length() == 0) {
			return null;
		}
		return this.userDao.getUserBySessionToken(token);
	}

	@Override
	@Transactional(readOnly=false)
	public void setProfileUrl(Integer userId, String profileUrl) {
		User user = this.userDao.getUserById(userId);
    	user.setProfileUrl(profileUrl);
		this.userDao.saveUser(user);
	}
}
