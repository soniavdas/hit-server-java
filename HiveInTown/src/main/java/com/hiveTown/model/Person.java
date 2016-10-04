package com.hiveTown.model;
 
import java.sql.Blob;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="person")
public class Person {
  
    private int id;   
//    private String email;
    private String firstName;
    private String lastName;
    private String displayName;
    private String gender;
    
   private Date dateOfBirth;
   private String aadharID;
   private String voterID;
   private String driversLicenseID;
   private String occupation;
   private String homePhone;
   private String mobile;
   private Address currentAddress;
   private Blob photo;
   private Blob documentProof1;
   private Blob documentProof2;
   private Timestamp createTime;
   private Timestamp updateTime;
    
    
    @Id
    @Column(name="idPerson")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
	/*public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
*/
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
    
	  
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "fk_currentAddressId")
    public Address getCurrentAddress() {
    	return this.currentAddress;
    }
    
    public void setCurrentAddress(Address address) {
    	this.currentAddress=  address;
    }

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAadharID() {
		return aadharID;
	}

	public void setAadharID(String aadharID) {
		this.aadharID = aadharID;
	}

	public String getVoterID() {
		return voterID;
	}

	public void setVoterID(String voterID) {
		this.voterID = voterID;
	}

	public String getDriversLicenseID() {
		return driversLicenseID;
	}

	public void setDriversLicenseID(String driversLicenseID) {
		this.driversLicenseID = driversLicenseID;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Blob getPhoto() {
		return photo;
	}

	public void setPhoto(Blob photo) {
		this.photo = photo;
	}

	public Blob getDocumentProof1() {
		return documentProof1;
	}

	public void setDocumentProof1(Blob documentProof1) {
		this.documentProof1 = documentProof1;
	}

	public Blob getDocumentProof2() {
		return documentProof2;
	}

	public void setDocumentProof2(Blob documentProof2) {
		this.documentProof2 = documentProof2;
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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
    
}