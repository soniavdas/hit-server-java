package com.hiveTown.service;

import java.util.List;

import com.hiveTown.Exception.HTWebConsoleException;
import com.hiveTown.data.ResidentDetails;
import com.hiveTown.model.Community;
import com.hiveTown.model.ResidentEnumType;
import com.hiveTown.model.RoleType;
import com.hiveTown.model.User;

public interface CommunityService {
    void addCommunity(Community community);
    void updateCommunity(Community community);
    Community getCommunityById(int id);
    Community getCommunityByUrlKeyword(String urlKeyword);
    void removeCommunity(int id);
	List<ResidentDetails> findResidents(String urlKeyword, String searchText, int pageIndex, int numPerPage);
	int totalResidentsByRole(String urlKeyword, RoleType role);
	int totalResidentsByVerificationStatus(String urlKeyword, Boolean isVerified);
	int totalResidents(String urlKeyword);
	
	ResidentDetails getResidentDetails(Integer communityId, Integer userId);
	int saveResidentDetails(Integer communityId,
			ResidentDetails residentDetails) throws HTWebConsoleException;
	void sendInvites(String urlKeyword, String path, String adminEmail);
	List<Community> getCommunities();
	Boolean removeUser(Community community, Integer userId);
}
