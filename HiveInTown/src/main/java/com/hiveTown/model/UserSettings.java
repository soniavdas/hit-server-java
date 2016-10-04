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
@Table(name="user_settings")
public class UserSettings {
  
    private int id;   
    private boolean unsubscribedAll;
    private boolean unsubscribedComments;
    private Timestamp createTime;
	private Timestamp updateTime;
	private User user;
	
    @Id
    @Column(name="idUserSettings")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
    
    @OneToOne(mappedBy="userSettings")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
    @Override
    public String toString(){
        return "id="+id;
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

	@Column(name="unsubscribed_all")
	public boolean getUnsubscribedAll() {
		return unsubscribedAll;
	}

	public void setUnsubscribedAll(boolean unsubscribedAll) {
		this.unsubscribedAll = unsubscribedAll;
	}

	@Column(name="unsubscribed_nb_comments")
	public boolean getUnsubscribedComments() {
		return unsubscribedComments;
	}

	public void setUnsubscribedComments(boolean unsubscribedComments) {
		this.unsubscribedComments = unsubscribedComments;
	}
	 
}