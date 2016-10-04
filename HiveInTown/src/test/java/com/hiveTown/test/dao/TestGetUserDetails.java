package com.hiveTown.test.dao;


import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.hiveTown.model.User;
import com.hiveTown.model.UserCommunity;

@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestGetUserDetails extends AbstractTransactionalJUnit4SpringContextTests  {

	private SessionFactory sessionFactory;
    
    @Autowired
    public void setSessionFactory(SessionFactory sf){
        this.sessionFactory = sf;
    }

    @Test
    public void testgetUserDetails() {
    	String emailId = "sri@gmail.com";
    	Session session = this.sessionFactory.getCurrentSession();
        //session.beginTransaction();
    	Criteria cr =   session.createCriteria(User.class);  
    	cr.add(Restrictions.eq("email", emailId));
    	List<User> users = cr.list();
    	System.out.println("USers list"+users.size());
    	
    	if(users!=null){
    		for(User user:users)
    		{
    			System.out.println("user id: "+user.getId());
    			System.out.println("firstname: "+user.getPerson().getFirstName());
    			System.out.println("last name: "+user.getPerson().getLastName());
    			Set<UserCommunity> usercom=user.getUserCommunities();
    			
    			if(usercom!=null)
    			{
    				System.out.println("commu size"+usercom.size());
    				Object[] usercomarry=usercom.toArray();
    				UserCommunity uc1=(UserCommunity)usercomarry[0];
    				System.out.println("role :"+uc1.getRole().name());
    			}
    		}	
        			
    	}
    }
    
    
    
	
}