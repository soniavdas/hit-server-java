package com.hiveTown.dao;
 
import java.util.List;
 










import com.hiveTown.Exception.HTWebConsoleException;
import com.hiveTown.data.ResidentDetails;
import com.hiveTown.model.Community;
import com.hiveTown.model.ResidentEnumType;
import com.hiveTown.model.RoleType;
import com.hiveTown.model.User;
 
public interface CommunityDao {
 
    public void addCommunity(Community c);
    public void updateCommunity(Community c);
    public Community getCommunityById(int id);
    public void removeCommunity(int id);
    Community getCommunityByUrlKeyword(String keyword);
    List<User> getAdmins(Community c);
    List<User> getResidents(Community c);
	List<ResidentDetails> findUsers(int communityId, String searchText,
			int pageIndex, int numPerPage);
	int totalResidents(int communityId);
	int totalResidentsByVerificationStatus(int communityId, Boolean isVerified);
	int totalResidentsByRole(int communityId, RoleType roleType);
	ResidentDetails getResidentDetails(int communityId, int userId);
	int saveResidentDetails(Community community,
			ResidentDetails residentDetails, User user) throws HTWebConsoleException;
	List<Community> getCommunities();
	List<String> getEmailList(Community community);
	void removeUser(Community community, User user)
			throws HTWebConsoleException;
}