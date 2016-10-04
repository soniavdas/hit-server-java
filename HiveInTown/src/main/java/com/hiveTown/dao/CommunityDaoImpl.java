package com.hiveTown.dao;
 
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
 









import com.hiveTown.Exception.HTWebConsoleException;
import com.hiveTown.data.ResidentDetails;
import com.hiveTown.model.Address;
import com.hiveTown.model.Community;
import com.hiveTown.model.Person;
import com.hiveTown.model.ResidentEnumType;
import com.hiveTown.model.RoleType;
import com.hiveTown.model.User;
import com.hiveTown.model.UserApartment;
import com.hiveTown.model.UserCommunity;
import com.hiveTown.model.UserSettings;
import com.hiveTown.util.HTUtil;
 
@Repository
public class CommunityDaoImpl implements CommunityDao {
	private static final Log logger = LogFactory.getLog(CommunityDaoImpl.class);
	private static final String TOTAL_QUERY = //"select count(*) from User as u inner join u.userCommunities as uc where uc.community.id=:communityId ";
	"select count(*) from UserCommunity as uc inner join uc.user as u where uc.community.id=:communityId ";
	
    private SessionFactory sessionFactory;
    
    private static final Logger LOGGER = Logger.getLogger(CommunityDao.class);

    @Autowired
    public void setSessionFactory(SessionFactory sf){
        this.sessionFactory = sf;
    }

	@Override
	public void addCommunity(Community c) {
		Session session = this.sessionFactory.getCurrentSession();
        session.persist(c);
        logger.info("Community saved successfully, Community Details="+c);
	}

	@Override
	public void updateCommunity(Community c) {
		Session session = this.sessionFactory.getCurrentSession();
        session.update(c);
        logger.info("Community updated successfully, Community Details="+c);
	}

	@Override
	public Community getCommunityById(int id) {
	    Session session = this.sessionFactory.getCurrentSession();   
        Community c = (Community) session.get(Community.class, new Integer(id));
        return c;
	}
	
	@Override
	public Community getCommunityByUrlKeyword(String keyword) {
		Community c = null;
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = (Criteria) session.createCriteria(Community.class);
		criteria.add(Restrictions.eq("urlKeyword", keyword));
		c = (Community) criteria.uniqueResult();
        return c;
	}

	@Override
	public void removeCommunity(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Community p = (Community) session.get(Community.class, new Integer(id));
        if(null != p){
            session.delete(p);
        }
        logger.info("User deleted successfully, Community details="+p);
	}

	@Override
	public List<User> getAdmins(Community c) {
		return getUsersByRole(RoleType.ADMIN, c);
	}

	@Override
	public List<User> getResidents(Community c) {
		return getUsersByRole(RoleType.RESIDENT, c);
	}
 
	private List<User> getUsersByRole(RoleType role, Community c) {
		List<User> userList = new ArrayList<User>();
		for ( UserCommunity uc : c.getUserCommunities() ) {
			if (uc.getRole() == role) {
				userList.add(uc.getUser());
			}
		}
		return userList;
	}
	
	@Override
	 @SuppressWarnings("unchecked")
	public List<ResidentDetails> findUsers(int communityId, String searchText, int pageIndex, int maxResults) {
		
		LOGGER.info("pageIndex:" + pageIndex);
		List<ResidentDetails> resultList = null;
		
		try {
		    Criteria criteria = this.getResidentCriteria(communityId);
		    
		    criteria.addOrder(Order.asc("p.displayName"))
		    .setFirstResult(pageIndex * maxResults)
		    .setMaxResults(maxResults);
	       
	        resultList = (List<ResidentDetails>) criteria.list();   
	        if (resultList != null) {
	        	LOGGER.info(resultList.size());
	        }
		}
		catch(HibernateException e) {
			LOGGER.error(e);
			throw e;
		}
        return resultList;
        
	}
    
	@Override
	public ResidentDetails getResidentDetails(int communityId, int userId) {
		Criteria criteria = this.getResidentCriteria(communityId);
		criteria.add(Restrictions.eq("uc.user.id", userId));
		
		return (ResidentDetails) criteria.uniqueResult();
	}
	
	private Criteria getResidentCriteria(int communityId) {
		
		Session session = this.sessionFactory.getCurrentSession();
		
		  Criteria criteria = session.createCriteria(User.class, "u")
					.createAlias("userCommunities", "uc", Criteria.INNER_JOIN, Restrictions.eq("uc.community.id", communityId))
					.createAlias("userApartments", "ua", Criteria.LEFT_JOIN, Restrictions.and(Restrictions.eq("ua.isResiding", true), Restrictions.eq("ua.community.id", communityId)))
					.createAlias("person", "p", Criteria.INNER_JOIN) //, Restrictions.ilike("p.displayName", searchText, MatchMode.ANYWHERE))
				    .setProjection(Projections.projectionList()
		    				.add(Property.forName("u.id").as("userId"))
		    				.add(Property.forName("u.email").as("email"))
		    				.add(Property.forName("u.profileUrl").as("profileUrl"))
		    				.add(Property.forName("p.displayName").as("name"))
		    				.add(Property.forName("p.mobile").as("contactNum"))
		    				.add(Property.forName("uc.role").as("role"))
		    				.add(Property.forName("ua.apartmentNum").as("apartmentNum"))
		    				.add(Property.forName("ua.id").as("userApartmentId"))
		    				.add(Property.forName("u.isVerified").as("isVerified"))
		    				.add(Property.forName("ua.blockName").as("blockName"))
		    				.add(Property.forName("uc.id").as("userCommunityId")))
		    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		    .setResultTransformer(Transformers.aliasToBean(ResidentDetails.class));
		  
		  return criteria;
	}
	
	@Override
	public int totalResidents(int communityId) {
		Session session = this.sessionFactory.getCurrentSession();
	
		int numResidents = ((Long) session.createQuery(TOTAL_QUERY)
								.setInteger("communityId", communityId)
								.iterate().next()).intValue();
		logger.info("total Residents query, Community ="+communityId);
		logger.info("total Residents ="+numResidents);
		return numResidents;
	}
	
	@Override
	public int totalResidentsByVerificationStatus(int communityId, Boolean isVerified) {
		Session session = this.sessionFactory.getCurrentSession();
		String query = TOTAL_QUERY + " and u.isVerified = :isVerified";
		int numResidents = ((Long) session.createQuery(query)
									.setInteger("communityId", communityId)
									.setBoolean("isVerified", isVerified)
									.iterate().next()).intValue();
		logger.info("total Active Residents query, Community ="+communityId);
		logger.info("total Active Residents ="+numResidents);
		return numResidents;
	}
	
	@Override
	public int totalResidentsByRole(int communityId, RoleType roleType) {
		Session session = this.sessionFactory.getCurrentSession();
		String query = TOTAL_QUERY + " and uc.role=:roleType";
		int numResidents =  ((Long) session.createQuery(query)
									.setInteger("communityId", communityId)
									.setInteger("roleType", roleType.getId())
									.iterate().next()).intValue();
		logger.info("total Residents By role query, Community ="+communityId);
		logger.info("total Residents By role="+numResidents);
		return numResidents;
	}
	
	@Override
	public int saveResidentDetails(Community community, ResidentDetails residentDetails, User user) throws HTWebConsoleException {
		
		Session session = this.sessionFactory.getCurrentSession();
		
	/*	Block block = this.getBlockByBlockName(community, residentDetails.getBlockName());
		if (block == null) {
			String message = "Adding Block " + residentDetails.getBlockName();
			LOGGER.info(message);
			block = new Block();
			block.setBlockName(residentDetails.getBlockName());
			block.setCommunity(community);
			community.getBlocks().add(block);
			session.save(community);
		}
		
		Apartment apt = this.getApartmentByNumber(block, residentDetails.getApartmentNum());
		if (apt == null) {
			String message = "Adding apartment " + residentDetails.getApartmentNum();
			LOGGER.info(message);
			apt = new Apartment();
			apt.setBlock(block);
			block.getApartments().add(apt);
			session.save(community);
		}*/
		
		String name = residentDetails.getName();
		String blockName = residentDetails.getBlockName();
		String apartmentNum = residentDetails.getApartmentNum();
		String contactNum = residentDetails.getContactNum();
		String email = residentDetails.getEmail();
		RoleType role = residentDetails.getRole();

		if (user == null) {	
			LOGGER.info("adding new user + person + address:" + email);
			Person person = new Person();
			person.setDisplayName(name);
			person.setMobile(contactNum);
			person.setCreateTime(new Timestamp(System.currentTimeMillis()));
			person.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			Address address = new Address();
			address.setCity(community.getAddress().getCity());
			person.setCurrentAddress(address);

			UserSettings userSettings = new UserSettings();
			
			user = new User();
			user.setEmail(email);
			user.setConfirmationCode(HTUtil.uniqueKey(residentDetails.getEmail()));
			user.setPerson(person);
			user.setUserSettings(userSettings);
			user.setCreateTime(new Timestamp(System.currentTimeMillis()));
			user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			session.save(user);
		}
		
		Criteria criteria = (Criteria) session.createCriteria(UserCommunity.class);
		criteria.add(Restrictions.eq("community", community));
		criteria.add(Restrictions.eq("user", user));
		
		UserCommunity uc = (UserCommunity) criteria.uniqueResult();
		if (uc == null) {
			LOGGER.info("adding new user community: " + email);
			uc = new UserCommunity();
			uc.setUser(user);
			uc.setCommunity(community);
			uc.setRole(role);
			uc.setCreateTime(new Timestamp(System.currentTimeMillis()));
			uc.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			community.getUserCommunities().add(uc);
			user.getUserCommunities().add(uc);
			
			LOGGER.info("adding new user apartment: " + email);
			UserApartment ua = new UserApartment();
			ua.setUser(user);
			ua.setBlockName(blockName);
			ua.setApartmentNum(apartmentNum);
			ua.setIsResiding(true);
			ua.setResidentEnumType(ResidentEnumType.OTHER);
			ua.setCommunity(community);
			ua.setCreateTime(new Timestamp(System.currentTimeMillis()));
			ua.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			user.getUserApartments().add(ua);
			community.getUserApartments().add(ua);
			
		} else  {
			
			LOGGER.info("saving person record");
			Person p = user.getPerson();
			p.setDisplayName(name);
			p.setMobile(contactNum);
			
			LOGGER.info("update role to: " + role.toString());
			if (uc.getRole().getId() != role.getId()) {
				uc.setRole(role);
				uc.setCreateTime(new Timestamp(System.currentTimeMillis()));
				uc.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			}
			
			LOGGER.info("getting current user apartment: " + email);
			UserApartment ua = user.getCurrentUserApartment(community.getId());
			if (ua == null) {
				LOGGER.info("User apartment not found. adding new user apartment: " + email);
				ua = new UserApartment();
				ua.setUser(user);
				ua.setBlockName(blockName);
				ua.setApartmentNum(apartmentNum);
				ua.setIsResiding(true);
				ua.setResidentEnumType(ResidentEnumType.OTHER);
				ua.setFromDate(Calendar.getInstance().getTime());
				ua.setCreateTime(new Timestamp(System.currentTimeMillis()));
				ua.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				ua.setCommunity(community);
				community.getUserApartments().add(ua);
			} else  { 
				ua.setBlockName(blockName);
				ua.setApartmentNum(apartmentNum);
				ua.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			} 
		}
		try {
			session.save(community);
		} catch(Exception e) {
			LOGGER.error(e);
			throw new HTWebConsoleException("SAVE_RESIDENT_SAVE_FAILED", e.getMessage());
		}
		return ((user != null) ? user.getId() :  0) ;
	}

	@Override
	public void removeUser(Community community, User user) throws HTWebConsoleException {
		LOGGER.info("removing user:" + user.getEmail());
		Session session = this.sessionFactory.getCurrentSession();
				
		Criteria criteria = (Criteria) session.createCriteria(UserCommunity.class);
		criteria.add(Restrictions.eq("community", community));
		criteria.add(Restrictions.eq("user", user));
		UserCommunity uc = (UserCommunity) criteria.uniqueResult();
		
		if (uc == null) {
			throw new HTWebConsoleException("USER_NOT_FOUND", "user not found in the community");
		}
		
		try {
			UserApartment ua = user.getCurrentUserApartment(community.getId());
			ua.setIsResiding(false);
			ua.setToDate(Calendar.getInstance().getTime());
			ua.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			user.getUserCommunities().remove(uc);
			session.save(user);
			session.delete(uc);
			
			
			LOGGER.info("User community record deleted:" + uc.getId());
		}
		catch(Exception e) {
			LOGGER.error(e);
			throw new HTWebConsoleException("REMOVE_RESIDENT_FAILED", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Community> getCommunities() {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			Criteria criteria = (Criteria) session.createCriteria(Community.class);
			return (List<Community>) criteria.list();
		} catch(HibernateException e) {
			LOGGER.error(e);
			return null;
		}
	}
	
	@Override
	public List<String> getEmailList(Community community) {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			
			Criteria criteria = (Criteria) session.createCriteria(UserCommunity.class, "uc")
						.createAlias("uc.user", "u")
						.createAlias("uc.community", "c")
						.createAlias("u.userSettings", "us")
						.add(Restrictions.eq("c.id", community.getId()))
						//.add(Restrictions.eq("u.isVerified", true))
						.add(Restrictions.eq("us.unsubscribedAll", false));
						criteria.setProjection(Projections.property("u.email"));
			List list = criteria.list();
			List<String> emailList = new ArrayList<String>(list.size());
			for(Iterator it=list.iterator(); it.hasNext(); ) {
				Object o = it.next();
				emailList.add(o.toString());
			}
			return emailList;
		} catch(HibernateException e) {
			LOGGER.error(e);
		}
		return new ArrayList<String>();
	}
	
} 