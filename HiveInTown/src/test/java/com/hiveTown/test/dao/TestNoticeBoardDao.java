package com.hiveTown.test.dao;

import java.util.Calendar;
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
import com.hiveTown.dao.NoticeBoardDao;
import com.hiveTown.model.NoticeBoard.*;
import com.hiveTown.model.Address;
import com.hiveTown.model.Community;
import com.hiveTown.model.Person;
import com.hiveTown.model.RoleType;
import com.hiveTown.model.User;
import com.hiveTown.model.UserCommunity;

@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestNoticeBoardDao extends AbstractTransactionalJUnit4SpringContextTests  {

	private SessionFactory sessionFactory;
    private UserDao userDao;
    private CommunityDao communityDao;
    private NoticeBoardDao noticeBoardDao;
    
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
    
    @Autowired
    private void setNoticeBoardDao(NoticeBoardDao noticeBoardDao) {
    	this.noticeBoardDao = noticeBoardDao;
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
        
        
        String subject = "For Your Attention";
        String details = "Dear Residents, this is to inform that there is going to be a lift maintenance coming sunday. Lifts will not be working. We aplogize for your inconvenience.";
		// Add a notice
		Notice notice = new Notice();
		notice.setSubject(subject);
		notice.setDetails(details);
		notice.setCategory(NoticeCategory.MAINTENANCE);
		notice.setStatus(NoticeStatus.DRAFT);
		notice.setFromDate(Calendar.getInstance().getTime());
		notice.setToDate(Calendar.getInstance().getTime());
		notice.setCommunity(comm);
		notice.setCreatedByUser(user2);
		comm.getNotices().add(notice);
		session.save(comm);
	    sessionFactory.getCurrentSession().flush();
	    
	    // read community and check notice count
		Community comm2 =  (Community) session.get(Community.class, comm.getId());  
		assertEquals(1, comm2.getNotices().size());
		
		// get the notice and compare
		Notice notice2 = comm2.getNotices().iterator().next();
		assertEquals(subject, notice2.getSubject());
		assertEquals(details, notice2.getDetails());
		assertEquals(NoticeStatus.DRAFT, notice2.getStatus());
		assertEquals(NoticeCategory.MAINTENANCE, notice2.getCategory());
		assertEquals(comm2.getId(), notice2.getCommunity().getId());
		assertEquals(user2.getId(), notice2.getCreatedByUser().getId());
		
		
  	}
}
    
