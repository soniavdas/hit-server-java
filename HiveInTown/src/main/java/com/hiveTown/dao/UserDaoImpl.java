package com.hiveTown.dao;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiveTown.Exception.HTWebConsoleException;
import com.hiveTown.data.UserDetails;
import com.hiveTown.model.Community;
import com.hiveTown.model.HTSession;
import com.hiveTown.model.Person;
import com.hiveTown.model.RoleType;
import com.hiveTown.model.User;
import com.hiveTown.model.UserCommunity;

@Repository
public class UserDaoImpl implements UserDao {

	private static final Logger LOGGER = Logger.getLogger(UserDao.class);

	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	@Override
	public User getUserById(Integer userId) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = (Criteria) session.createCriteria(User.class);
		criteria.add(Restrictions.eq("id", userId));
		User user = (User) criteria.uniqueResult();
		return user;
	}
	
	@Override
	public User getUserByEmail(String email) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = (Criteria) session.createCriteria(User.class);
		criteria.add(Restrictions.eq("email", email));
		User user = (User) criteria.uniqueResult();
		return user;
	}   
	
	@Override
	public Integer saveSession(Integer userId, String token) {
		Session session = this.sessionFactory.getCurrentSession();
		
		Criteria criteria = (Criteria) session.createCriteria(HTSession.class);
		criteria.add(Restrictions.eq("userIdFk", userId));
		HTSession htSession = (HTSession) criteria.uniqueResult();
		
		if (htSession == null) {
			htSession = new HTSession();
			htSession.setUserIdFk(userId);
			htSession.setCreated(new Timestamp(System.currentTimeMillis()));
		}
		htSession.setToken(token);
		htSession.setLast_Updated(new Timestamp(System.currentTimeMillis()));
		return (Integer) session.save(htSession);
	}

	@Override
	public void saveUser(User user) {
		Session session = this.sessionFactory.getCurrentSession();
		user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		session.update(user);
	}

	@Override
	public User getUserBySessionToken(String token) {
		Session session = this.sessionFactory.getCurrentSession();
		
		try {
			Criteria criteria = (Criteria) session.createCriteria(HTSession.class);
			criteria.add(Restrictions.eq("token", token));
			HTSession htSession = (HTSession) criteria.uniqueResult();
			if (htSession == null) {
				LOGGER.info("HTSession not found for : " + token);
				return null;
			}
			int userId = htSession.getUserIdFk();
			
			criteria = (Criteria) session.createCriteria(User.class);
			criteria.add(Restrictions.eq("id", userId));
			return (User) criteria.uniqueResult();
		} catch(HibernateException e) {
			LOGGER.error(e);
			return null;
		}
	}

	@Override
	public UserDetails getUserDetails(String email, Integer communityId) throws HTWebConsoleException {
		UserDetails userDetails = null;
		
		try {
			User user = this.getUserByEmail(email);
			if (user != null) {
				userDetails = new UserDetails();
				userDetails.setEmail(email);
				userDetails.setIsVerified(user.getIsVerified());
				userDetails.setUserId(user.getId());
				userDetails.setUnsubscribedAll(user.getUserSettings().getUnsubscribedAll());
				userDetails.setUnsubscribedComments(user.getUserSettings().getUnsubscribedComments());
				
				Iterator<UserCommunity> iter = (Iterator<UserCommunity>) user.getUserCommunities().iterator();
				while (iter.hasNext()) {
					UserCommunity uc = (UserCommunity) iter.next();
					if (uc.getCommunity().getId() == communityId) {
						userDetails.setRole(uc.getRole());
						userDetails.setCommunityUrlKeyword(uc.getCommunity().getUrlKeyword());
					}
				}
				
				Person p = user.getPerson();
				if (p != null) {
					userDetails.setDisplayName(p.getDisplayName());
					userDetails.setFirstName(p.getFirstName());
					userDetails.setLastName(p.getLastName());
				}
			}
		} catch(Exception e) {
			LOGGER.error(e);
			throw new HTWebConsoleException("ERROR_GETTING_USER_DETAILS", e.getMessage());
		}
		return userDetails;
	}

	@Override
	public Boolean isUserInCommunity(User user, Community community) {
		UserCommunity uc = this.getUserCommunity(user, community);
		return (uc != null);
	}

	@Override
	public RoleType getRole(User user, Community community) {
		UserCommunity uc = this.getUserCommunity(user, community);
		return uc.getRole();
	}
	
	private UserCommunity getUserCommunity(User user, Community community) {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			Criteria criteria = (Criteria) session.createCriteria(UserCommunity.class);
			criteria.add(Restrictions.eq("community", community));
			criteria.add(Restrictions.eq("user", user));
			
			UserCommunity uc = (UserCommunity) criteria.uniqueResult();
			return uc;
		} catch(HibernateException e) {
			LOGGER.error(e);
			return null;
		}
	}

	@Override
	public Boolean saveSocialLogin(User user, String socialEmail, String socialLoginType) throws HTWebConsoleException {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			User socialUser = new User();
			socialUser.setEmail(socialEmail);
			socialUser.setIsVerified(true);
			socialUser.setConfirmationCode(user.getConfirmationCode());
			socialUser.setParentUser(user);
			socialUser.setSocialLoginType(socialLoginType);
			socialUser.setPerson(user.getPerson());
			
			session.save(socialUser);
			return socialUser.getId() > 0;
		} catch(HibernateException e) {
			LOGGER.error(e);
			throw new HTWebConsoleException("ERROR_SAVING_SOCIAL_LOGIN", e.getMessage());
		}
	}

	@Override
	public User getUserByCode(String code) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = (Criteria) session.createCriteria(User.class);
		criteria.add(Restrictions.eq("confirmationCode", code));
		User user = (User) criteria.uniqueResult();
		return user;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Community getDefaultCommunityForUser(Integer userId) {
		
		try {
			Session session = this.sessionFactory.getCurrentSession();
			User user = this.getUserById(userId);
			Criteria criteria = (Criteria) session.createCriteria(UserCommunity.class);
			criteria.add(Restrictions.eq("user", user));
			
			List<UserCommunity> uc = (List<UserCommunity>) criteria.list();
			if (uc != null && uc.get(0) != null) {
				return uc.get(0).getCommunity();
			} else {
				LOGGER.error("No community for user: " + userId);
			}
		} catch(HibernateException e) {
			LOGGER.error(e);
		}
		return null;
	}

}