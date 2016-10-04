package com.hiveTown.test.dao;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.hiveTown.model.User;
import com.hiveTown.model.UserCommunity;
import com.hiveTown.model.complaint.ComplaintCategory;
import com.hiveTown.model.complaint.ComplaintRegistry;

@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestComplaintRegistry extends AbstractTransactionalJUnit4SpringContextTests  {

	private SessionFactory sessionFactory;
    
    @Autowired
    public void setSessionFactory(SessionFactory sf){
        this.sessionFactory = sf;
    }

    @Test
    public void testgetUserDetails() {
    	
    	ComplaintRegistry complaint=new ComplaintRegistry();
    	Session session = this.sessionFactory.getCurrentSession();
    	User user = (User) session.get(User.class, 3);
    	complaint.setUser(user);
    	complaint.setCategory(ComplaintCategory.INVALID);
    	Iterator<UserCommunity> commIter = user.getUserCommunities().iterator();
		assertTrue(commIter.hasNext());
		while(commIter.hasNext()) {
			UserCommunity uc = (UserCommunity) commIter.next();
			assertEquals(3, uc.getUser().getId());
			complaint.setCommunity(uc.getCommunity());
		}
    	complaint.setDescription("problem fjgjffffffffg");
    	session.save(complaint);
    //	session.getTransaction().commit();
		sessionFactory.getCurrentSession().flush();
    }
    
   
   
	
}

    
	
