package com.hiveTown.model;
 
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.hiveTown.model.NoticeBoard.Notice;

@Entity
@Table(name="community")
public class Community {
 
    private int id;
    private String communityName;
    private String urlKeyword;
    private Address address;
    private Set<UserCommunity> userCommunity = new HashSet<UserCommunity>();
 //   private Set<Block> blocks=new HashSet<Block>();
    private List<Notice> notices;

    private Set<UserApartment> userApartments = new HashSet<UserApartment>();
    private Timestamp createTime;
 	private Timestamp updateTime;
 	
 	private String fromAddress;
 	private String fromDisplayName;
 	private String replyToNoticeAddress;
 	private String replyToAddress;
 	
    @Id
    @Column(name="idCommunity")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
    
    public String getCommunityName() {
    	return this.communityName;
    }
    
    public void setCommunityName(String communityName) {
    	this.communityName = communityName;
    }
  
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "fk_addressId")
    public Address getAddress() {
    	return this.address;
    }
    
    public void setAddress(Address address) {
    	this.address =  address;
    }
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "community", cascade=CascadeType.ALL)
    public Set<UserCommunity> getUserCommunities() {
    	return this.userCommunity;
    }
    
    public void setUserCommunities(Set<UserCommunity> userCommunity) {
    	this.userCommunity = userCommunity;
    }

	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "community", cascade=CascadeType.ALL)
	public Set<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(Set<Block> blocks) {
		this.blocks = blocks;
	} */

	public String getUrlKeyword() {
		return urlKeyword;
	}

	public void setUrlKeyword(String urlKeyword) {
		this.urlKeyword = urlKeyword;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "community", cascade=CascadeType.ALL)
	@OrderBy("dateStatusUpdated desc")
	public List<Notice> getNotices() {
		return notices;
	}

	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "community", cascade=CascadeType.ALL)
	public Set<UserApartment> getUserApartments() {
		return userApartments;
	}
	public void setUserApartments(Set<UserApartment> userApartments) {
		this.userApartments = userApartments;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getReplyToNoticeAddress() {
		return replyToNoticeAddress;
	}

	public void setReplyToNoticeAddress(String replyToNoticeAddress) {
		this.replyToNoticeAddress = replyToNoticeAddress;
	}

	public String getReplyToAddress() {
		return replyToAddress;
	}

	public void setReplyToAddress(String replyToAddress) {
		this.replyToAddress = replyToAddress;
	}

	public String getFromDisplayName() {
		return fromDisplayName;
	}

	public void setFromDisplayName(String fromDisplayName) {
		this.fromDisplayName = fromDisplayName;
	}

}