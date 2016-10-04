package com.hiveTown.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiveTown.Exception.HTWebConsoleException;
import com.hiveTown.dao.CommunityDao;
import com.hiveTown.dao.UserDao;
import com.hiveTown.model.Community;
import com.hiveTown.model.ResidentEnumType;
import com.hiveTown.model.RoleType;
import com.hiveTown.model.User;
import com.hiveTown.model.UserCommunity;
import com.hiveTown.data.ResidentDetails;

@Service
public class CommunityServiceImpl implements CommunityService {

	private static final Logger LOGGER = Logger.getLogger(CommunityService.class);
    private CommunityDao communityDao;
    private UserDao userDao;
    private EmailService emailService;
    
    @Autowired
    public void setCommunityDao(CommunityDao communityDao) {
        this.communityDao = communityDao;
    }
	    
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    
    @Autowired
    public void setEmailService(EmailService emailService){
        this.emailService = emailService;
    }
    
	@Override
	@Transactional
	public void addCommunity(Community community) {
		this.communityDao.addCommunity(community);
	}

	@Override
	@Transactional
	public void updateCommunity(Community community) {
		this.communityDao.updateCommunity(community);
	}

	@Override
	@Transactional
	public Community getCommunityById(int id) {
		return this.communityDao.getCommunityById(id);
	}

	@Override
	@Transactional
	public Community getCommunityByUrlKeyword(String urlKeyword) {
		return this.communityDao.getCommunityByUrlKeyword(urlKeyword);
	}

	@Override
	@Transactional
	public void removeCommunity(int id) {
		this.communityDao.removeCommunity(id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<ResidentDetails> findResidents(String urlKeyword, String searchText, int pageIndex, int numPerPage) {
		Community c = this.communityDao.getCommunityByUrlKeyword(urlKeyword);
		if (c != null) {
			return this.communityDao.findUsers(c.getId(), searchText, pageIndex, numPerPage);
		}
		return new ArrayList<ResidentDetails>();
	}
	
	@Override
	@Transactional(readOnly=true)
	public int totalResidents(String urlKeyword) {
		Community c = this.communityDao.getCommunityByUrlKeyword(urlKeyword);
		if (c != null) {
			return this.communityDao.totalResidents(c.getId());
		}
		return 0;
	}
	
	@Override
	@Transactional(readOnly=true)
	public int totalResidentsByVerificationStatus(String urlKeyword, Boolean isVerified) {
		Community c = this.communityDao.getCommunityByUrlKeyword(urlKeyword);
		if (c != null) {
			return this.communityDao.totalResidentsByVerificationStatus(c.getId(), isVerified);
		}
		return 0;
	}
	
	@Override
	@Transactional(readOnly=true)
	public int totalResidentsByRole(String urlKeyword, RoleType role) {
		Community c = this.communityDao.getCommunityByUrlKeyword(urlKeyword);
		if (c != null) {
			return this.communityDao.totalResidentsByRole(c.getId(), role);
		}
		return 0;
	}

	@Override
	@Transactional(readOnly=true)
	public ResidentDetails getResidentDetails(Integer communityId, Integer userId) {
		try {
			return this.communityDao.getResidentDetails(communityId, userId);
		} catch (Exception e) {
			LOGGER.error(e);
			LOGGER.error(String.format("CommunityId %d userId %d", communityId, userId));
			throw e;
		}
	}

	@Override
	@Transactional(readOnly=false)
	public int saveResidentDetails(Integer communityId, ResidentDetails residentDetails) throws HTWebConsoleException {
		try {
			Community community = this.communityDao.getCommunityById(communityId);
			User user = this.userDao.getUserByEmail(residentDetails.getEmail());
			return this.communityDao.saveResidentDetails(community, residentDetails, user);
		} catch (HTWebConsoleException e) {
			LOGGER.error(e);
			LOGGER.error(String.format("CommunityId %d userId %d", communityId, residentDetails.getUserId()));
			throw e;
		}
	}
	
	@Override
	@Transactional
	public void sendInvites(String urlKeyword, String path, String adminEmail) {
		StringBuilder errors = new StringBuilder();
		
		Community community = this.communityDao.getCommunityByUrlKeyword(urlKeyword);
		
		errors.append("ERROR sending emails to:\n");
		for(UserCommunity uc : community.getUserCommunities()) {
			User u = uc.getUser();
			if (!u.getIsVerified()) {
				Boolean bSent = this.emailService.sendInvite(u.getEmail(), u.getConfirmationCode(), path, 
															Integer.toString(uc.getRole().getId()), urlKeyword, 
															community.getFromAddress(), community.getReplyToAddress());
				if (!bSent) {
					LOGGER.error("error sending email: " + u.getEmail());
					errors.append(u.getEmail() + "\n");
				}
			}
		}
		
		this.emailService.sendLog(adminEmail, errors.toString(), community.getFromAddress(), community.getReplyToAddress());
	}
	
	@Override
	@Transactional
	public Boolean removeUser(Community community, Integer userId) {
		try {
			User user = this.userDao.getUserById(userId);
			this.communityDao.removeUser(community, user);
			return true;
		} catch (HTWebConsoleException e) {
			LOGGER.error(e);
		}
		return false;
	}

	@Override
	@Transactional
	public List<Community> getCommunities() {
		return this.communityDao.getCommunities();
	}
}
