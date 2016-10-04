package com.hiveTown.test.dao;

import java.util.Iterator;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.hibernate.Session;

import static org.junit.Assert.*;

import com.hiveTown.dao.CommunityDao;
import com.hiveTown.dao.UserDao;
import com.hiveTown.model.Address;
import com.hiveTown.model.Community;
import com.hiveTown.model.Person;
import com.hiveTown.model.RoleType;
import com.hiveTown.model.User;
import com.hiveTown.model.UserCommunity;

@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestUserDao extends AbstractTransactionalJUnit4SpringContextTests  {

	private SessionFactory sessionFactory;
    private UserDao userDao;
    private CommunityDao communityDao;
    
    @Autowired
    public void setSessionFactory(SessionFactory sf){
        this.sessionFactory = sf;
    }
    
    @Autowired
    private void setUserDao(UserDao userDao) {
    	this.userDao = userDao;
    }
	
    @Autowired
    private void setCommunityDao(CommunityDao communityDao) {
    	this.communityDao = communityDao;
    }
    
    @Test
    public void testWrite() {
    	
    	String sessionId = "SABC1233ffkfkf";
    	String email = "soniavdas2@yahoo.com";
    	String city = "Bengaluru";
    	String commName = "Sobha Chrysanthemum";
    	String commUrl = "sobhachrys";
    	String firstName = "sonia";
    	
    	Session session = this.sessionFactory.getCurrentSession();
    	
    	// Add a community
    	Community comm = new Community();
    	comm.setCommunityName(commName);
    	comm.setUrlKeyword(commUrl);
    	Address commAddress = new Address();
    	commAddress.setCity("Bengaluru");
    	commAddress.setZipCode("560077");
    	comm.setAddress(commAddress);
    	session.save(comm);
    	sessionFactory.getCurrentSession().flush();
    	
    	
		// Add a user with person info and address
		User user = new User();
		Person p = new Person();
		p.setFirstName(firstName);
		p.setLastName("sharma");
		Address a = new Address();
		a.setCity(city);
		user.setEmail(email);
	//	user.setSessionId(sessionId);
		user.setPerson(p);
		p.setCurrentAddress(a);
		
		session.save(user);
		assertNotNull(user.getId());
		int userId = user.getId();
		sessionFactory.getCurrentSession().flush();
		
		// Read and verify
        User user2 =  (User) session.get(User.class, userId);  
        assertEquals(userId, user2.getId());
   //     assertEquals(sessionId, user2.getSessionId());
        assertEquals(email, user2.getEmail());
        assertNotNull(user2.getPerson());
        assertEquals(city, user2.getPerson().getCurrentAddress().getCity());
        
        // Add user community mapping with Admin role
        UserCommunity uc = new UserCommunity();
        uc.setUser(user2);
        uc.setCommunity(comm);
        uc.setRole(RoleType.ADMIN);
        comm.getUserCommunities().add(uc);
        user2.getUserCommunities().add(uc);
        session.save(comm);
        sessionFactory.getCurrentSession().flush();
        
        // Read and verify userCommunity
        UserCommunity uc1 = (UserCommunity) session.get(UserCommunity.class, uc.getId());
        assertNotNull(uc1);
        assertNotNull(uc1.getCommunity());
        assertNotNull(uc1.getUser());
        assertEquals(RoleType.ADMIN, uc1.getRole());
        assertEquals(firstName, uc1.getUser().getPerson().getFirstName());
        assertEquals(commName, uc1.getCommunity().getCommunityName());
        sessionFactory.getCurrentSession().flush();
        
        // Read and Verify through community
        int commId = comm.getId();
        Community comm2 = (Community) session.get(Community.class, commId);
        assertEquals(comm2.getCommunityName(), commName);
        assertFalse(comm2.getUserCommunities().isEmpty());
        assertEquals(comm2.getUserCommunities().size(), 1);
        Iterator<UserCommunity> iter = comm2.getUserCommunities().iterator();
        assertTrue(iter.hasNext());
        while(iter.hasNext()) {
        	UserCommunity uc2 = iter.next();
        	User user3 = uc2.getUser();
        	assertEquals(userId, user3.getId());
     //   	assertEquals(sessionId, user3.getSessionId());
            assertEquals(email, user3.getEmail());
            assertEquals(RoleType.ADMIN, uc2.getRole());
        } 
        
        // Read and Verify through user
        User user4 = (User) session.get(User.class, user2.getId());
        assertFalse(user4.getUserCommunities().isEmpty());
        assertEquals(1, user4.getUserCommunities().size());
        iter = user4.getUserCommunities().iterator();
        assertTrue(iter.hasNext());
        while(iter.hasNext()) {
        	UserCommunity uc3 = iter.next();
        	Community comm3 = uc3.getCommunity();
        	assertEquals(commName, comm3.getCommunityName());
        	assertEquals(RoleType.ADMIN, uc3.getRole());
        } 
     }
    
    @Test
    public void testAddUserToCommunity() {
    	String email = "soniavdas@gmail.com";
    	String name = "Sonia Sharma";
    	String contactNum = "9740594506";
    	String confirmationCode = "rtjunfbf";
    	RoleType role = RoleType.ADMIN;
    	
    	assertNotNull(this.userDao);
    	assertNotNull(this.communityDao);
    	
    	Community community = this.createCommunity("Sterling Residency", "sterlingres", "Dollars Colony", "Bengaluru");
    	this.communityDao.addCommunity(community);
    	assertTrue(community.getId() > 0);
    	assertEquals("sterlingres", community.getUrlKeyword());
    	
    	//User user = userDao.addUserToCommunity(community, email, name, contactNum, confirmationCode, role);
    	//assertTrue("id has value", user.getId() > 0);
    	//assertEquals(email, user.getEmail());
    	//assertEquals(name, user.getPerson().getDisplayName());
    	//assertEquals(contactNum, user.getPerson().getHomePhone());
    }
    
	public Community createCommunity(String commName, String urlKeyword, String addressLine1, String city) {
		//Add a Community 
		Address communityAdress = new Address();
		communityAdress.setAddressLine1(addressLine1);
		communityAdress.setCity(city);
		Community community1=new Community();
		community1.setCommunityName(commName);
		community1.setUrlKeyword(urlKeyword);
		community1.setAddress(communityAdress);
		return community1;
	}
}