package com.hiveTown.model;
 
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User {
  
    private int id;   
    private String email;
    private String confirmationCode;
    private boolean isVerified;
    private Set<UserCommunity> userCommunities = new HashSet<UserCommunity>();
    private Set<UserApartment> userApartments = new HashSet<UserApartment>();
    private Person person; 
    private String socialLoginType;
    private User parentUser;
    private Timestamp createTime;
	private Timestamp updateTime;
	private String profileUrl;
	private UserSettings userSettings;
	
    @Id
    @Column(name="idUser")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getEmail() {
        return this.email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }


    public String getConfirmationCode() {
        return this.confirmationCode;
    }
 
    public void setConfirmationCode(String code) {
        this.confirmationCode = code;
    }
 

    public boolean getIsVerified() {
        return this.isVerified;
    }
 
    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }
   
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade=CascadeType.ALL)
    public Set<UserApartment> getUserApartments() {
    	return this.userApartments;
    }
    
    public void setUserApartments(Set<UserApartment> userApartments) {
    	this.userApartments = userApartments;
    }
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade=CascadeType.ALL)
    public Set<UserCommunity> getUserCommunities() {
    	return this.userCommunities;
    }
    
    public void setUserCommunities(Set<UserCommunity> userCommunities) {
    	this.userCommunities = userCommunities;
    }
    
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "fk_personId")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
    
    @Override
    public String toString(){
        return "id="+id+", email="+ email;
    }

	public String getSocialLoginType() {
		return socialLoginType;
	}

	public void setSocialLoginType(String socialLoginType) {
		this.socialLoginType = socialLoginType;
	}

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "fk_parentUserId")
	public User getParentUser() {
		return this.parentUser;
	}

	public void setParentUser(User parentUser) {
		this.parentUser = parentUser;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	// get user's currently residing apartment in community
	public UserApartment getCurrentUserApartment(int communityId) {
		Iterator<UserApartment> iter = this.userApartments.iterator();
		while(iter.hasNext()) {
			UserApartment ua = iter.next();
			if ((ua.getCommunity().getId() == communityId) && ua.getIsResiding()) {
				return ua;
			}
		}
		return null;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}


	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "fk_userSettingsId")
	public UserSettings getUserSettings() {
		return userSettings;
	}

	public void setUserSettings(UserSettings userSettings) {
		this.userSettings = userSettings;
	}
}