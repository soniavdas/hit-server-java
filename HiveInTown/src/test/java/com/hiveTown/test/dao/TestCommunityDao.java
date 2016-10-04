package com.hiveTown.test.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.hibernate.Session;

import static org.junit.Assert.*;

import com.hiveTown.model.Address;
import com.hiveTown.model.Apartment;
import com.hiveTown.model.ApartmentUsageTypeEnum;
import com.hiveTown.model.Block;
import com.hiveTown.model.Community;
import com.hiveTown.model.Person;
import com.hiveTown.model.ResidentEnumType;
import com.hiveTown.model.RoleType;
import com.hiveTown.model.User;
import com.hiveTown.model.UserApartment;
import com.hiveTown.model.UserCommunity;
import com.hiveTown.dao.CommunityDao;
import com.hiveTown.data.ResidentDetails;

@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestCommunityDao extends AbstractTransactionalJUnit4SpringContextTests  {

	private SessionFactory sessionFactory;
    private CommunityDao communityDao;
    
    @Autowired
    public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }
    
    @Autowired
    public void setCommunityDao(CommunityDao communityDao) {
        this.communityDao = communityDao;
    }
    
	@Test
    public void testAddCommunity() {
	
		String city = "Bengaluru";
		String commName = "Sterling Residency";
		String urlKeyword = "sterlingres";
		String addressLine1 = "Basavanagudi";
		String blockA = "Block A";
		String blockB = "Block B";
		String apt1 = "3064";
		String apt2 = "T2-4";
		String apt3 = "T3-20A";
		String apt4 = "2001";
		int communityId = 0;
		
		Community community1 = this.createCommunity(commName, urlKeyword, addressLine1, city);
		Session session = this.sessionFactory.getCurrentSession();
		session.save(community1);
		assertNotNull(community1.getId());
		sessionFactory.getCurrentSession().flush();
		communityId = community1.getId();
		
		//Add 2 blocks
		Block block1=new Block();
		block1.setBlockName(blockA);
		block1.setCommunity(community1);
		
		Block block2=new Block();
		block2.setBlockName(blockB);
		block2.setCommunity(community1);
		
		session.save(block1);
		session.save(block2);
		sessionFactory.getCurrentSession().flush();
		
		// Create 4 apartments
		Apartment apartment1=new Apartment();
		apartment1.setApartmentNum(apt1);
		apartment1.setBlock(block1);
		apartment1.setApartmentUsageType(ApartmentUsageTypeEnum.SELF_OCCUPIED);
		
		Apartment apartment2=new Apartment();
		apartment2.setApartmentNum(apt2);
		apartment2.setBlock(block1);
		apartment2.setApartmentUsageType(ApartmentUsageTypeEnum.RENTED);
		
		Apartment apartment3=new Apartment();
		apartment3.setApartmentNum(apt3);
		apartment3.setBlock(block2);
		apartment3.setApartmentUsageType(ApartmentUsageTypeEnum.SELF_OCCUPIED);
		
		Apartment apartment4=new Apartment();
		apartment4.setApartmentNum(apt4);
		apartment4.setBlock(block2);
		apartment4.setApartmentUsageType(ApartmentUsageTypeEnum.COMMERCIAL);
		
		// add apartment to blocks and blocks to community
		/*community1.getBlocks().add(block1);
		community1.getBlocks().add(block2);
		block1.getApartments().add(apartment1);
		block1.getApartments().add(apartment2);
		block2.getApartments().add(apartment3);
		block2.getApartments().add(apartment4); */
		
		session.save(community1);
		sessionFactory.getCurrentSession().flush();
	
		// Read Block A
		Block blockA1 = (Block) session.get(Block.class, block1.getId());
		assertNotNull(blockA1);
		assertEquals(commName, blockA1.getCommunity().getCommunityName());
		assertEquals(2, blockA1.getApartments().size());
		
		// Read and verify community->blocks rel
		Community comm = (Community) session.get(Community.class, communityId);
		//Set<Block> blocks = comm.getBlocks();
		//assertNotNull(blocks);
		//assertEquals(2, blocks.size());
		
		// Read an verify block->apartments rel
		//Iterator<Block> blockIter = blocks.iterator();
		//assertTrue(blockIter.hasNext());
	//	while(blockIter.hasNext()) {
		//	Block b = (Block) blockIter.next();
		//	assertEquals(2, b.getApartments().size());
		//}
		
		// Read apartment and assign to user
		User user = createUser("abc@gmail.com", "sonia", "def");
		Apartment apt5 = (Apartment) session.get(Apartment.class, apartment1.getId());
		UserApartment userApt1 = new UserApartment();
		userApt1.setUser(user);
		//userApt1.setApartment(apt5);
		userApt1.setResidentEnumType(ResidentEnumType.TENANT);
		userApt1.setIsResiding(true);
		//userApt1.setFromDate(Date.parse("2012/01/01");
		//userApt1.setToDate(toDate);
		user.getUserApartments().add(userApt1);
		apt5.getUserApartments().add(userApt1);
		session.save(user);
		sessionFactory.getCurrentSession().flush();
		
		// Read apartment and verify User is assigned
		Apartment apt6 = (Apartment) session.get(Apartment.class, apartment1.getId());
		assertEquals(apt1, apt6.getApartmentNum());
		assertEquals(1, apt6.getUserApartments().size());
		Iterator<UserApartment> iter = apt6.getUserApartments().iterator();
		while(iter.hasNext()) {
			UserApartment ua1 = iter.next();
			assertEquals("sonia", ua1.getUser().getPerson().getFirstName());
			//assertEquals(apt1, ua1.getApartment().getApartmentNum());
		}
	}
	
	@Test
	public void testGetCommunity() {
		String city = "Bengaluru";
		String commName = "Sterling Residency";
		String urlKeyword = "sterlingres";
		String addressLine1 = "Basavanagudi";
		
		Community community1 = this.createCommunity(commName, urlKeyword, addressLine1, city);
		Session session = this.sessionFactory.getCurrentSession();
		session.save(community1);
		assertNotNull(community1.getId());
		sessionFactory.getCurrentSession().flush();
		int communityId = community1.getId();
		
		//Read using urlKeyword
		Community community2 = this.communityDao.getCommunityByUrlKeyword(urlKeyword);
		assertNotNull(community2);
		assertEquals(commName, community2.getCommunityName());
		
		
		Community community3 = this.communityDao.getCommunityById(communityId);
		assertNotNull(community3);
		assertEquals(commName, community3.getCommunityName());
	}
	
	public User createUser(String email, String firstName, String lastName) {
		User user = new User();
		Person p = new Person();
		p.setFirstName(firstName);
		p.setLastName(lastName);
		p.setDisplayName(firstName + " " + lastName);
		Address a = new Address();
		a.setCity("Bengaluru");
		user.setEmail(email);
	//	user.setSessionId("123rsgd7dkk");
		user.setPerson(p);
		p.setCurrentAddress(a);
		return user;
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
	
	@Test
	public void testFindUsers() {
		Session session = this.sessionFactory.getCurrentSession();
		
		Community c = this.createCommunity("Sobha", "sobhac", "thanisandra", "bengaluru");
		session.save(c);
		session.flush();
		
		String email, firstName, lastName;
		User user; 
		UserCommunity uc;
		UserApartment ua;
		Apartment apt;
		
		for(int i = 0; i < 100; i++) {
			firstName = String.format("sonia%d", i);
			lastName = String.format("sharma%d", i);
			email = firstName +"@yahoo.com";
			user = this.createUser(email, firstName, lastName);
			session.save(user);
			session.flush();
			
			uc = new UserCommunity();
	        uc.setUser(user);
	        uc.setCommunity(c);
	        if (i % 2 == 0) {
	        	uc.setRole(RoleType.ADMIN);
	        } 
	        else {
	        	uc.setRole(RoleType.RESIDENT);
	        }
	        
	        c.getUserCommunities().add(uc);
	        user.getUserCommunities().add(uc);
	        session.save(c);
		    
	       /* ua = new UserApartment();
	        apt = new Apartment();
	        apt.setApartmentNum("3310");
	        apt.*/
		}
		for (int i = 0; i  < 10; i++) {
			List<ResidentDetails> list = this.communityDao.findUsers(c.getId(), "soni", i, 10);
			assertEquals(10, list.size());
			//assertEquals(i * 10, list.get(0).getId());
			//assertEquals(String.format("sonia%d@yahoo.com", i*10), list.get(0).getEmail());
			//assertEquals("sonia" + i * 10 + " sharma" + i * 10, list.get(0).getDisplayName());
		}
	}
	
	@Test
	public void testCountUsers() {
		Session session = this.sessionFactory.getCurrentSession();
		
		Community c = this.createCommunity("Sobha", "sobhac", "thanisandra", "bengaluru");
		session.save(c);
		session.flush();
		
		String email, firstName, lastName;
		User user; 
		UserCommunity uc;
		
		for(int i = 0; i < 5; i++) {
			firstName = String.format("sonia%d", i);
			lastName = String.format("sharma%d", i);
			email = firstName +"@yahoo.com";
			user = this.createUser(email, firstName, lastName);
			if (i == 0) {
				user.setIsVerified(true);
			}
			session.save(user);
			session.flush();
			
			uc = new UserCommunity();
	        uc.setUser(user);
	        uc.setCommunity(c);
	        if (i % 2 == 0) {
	        	uc.setRole(RoleType.ADMIN);
	        } 
	        else {
	        	uc.setRole(RoleType.RESIDENT);
	        }
	        c.getUserCommunities().add(uc);
	        user.getUserCommunities().add(uc);
	        session.save(c);
		       
		}
		
		int count = this.communityDao.totalResidents(c.getId());
		assertEquals(5, count);
		
		count = this.communityDao.totalResidentsByVerificationStatus(c.getId(), true);
		assertEquals(1, count);
		
		count = this.communityDao.totalResidentsByRole(c.getId(), RoleType.ADMIN);
		assertEquals(3, count);
	}
}