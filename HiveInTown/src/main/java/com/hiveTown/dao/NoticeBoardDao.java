package com.hiveTown.dao;

import java.util.List;
import java.util.Set;

import com.hiveTown.data.NoticeSummary;
import com.hiveTown.model.Community;
import com.hiveTown.model.NoticeBoard.Notice;
import com.hiveTown.model.NoticeBoard.NoticeComment;
import com.hiveTown.model.NoticeBoard.NoticeStatus;

public interface NoticeBoardDao {

	Notice getNotice(Integer id);
	void addNotice(Notice notice);
	int getCountOfNewNoticesForUser(Integer communityId, Integer userId);
	void updateNotice(Notice notice);
	void addComment(NoticeComment comment);
	void deleteComment(NoticeComment comment);
	NoticeSummary getSummary(Community community);
	void deleteNotice(Integer noticeId);
	NoticeComment getComment(int commentId);
	void updateComment(NoticeComment comment);
	List<Notice> getNoticesByStatus(Community community, NoticeStatus status,
			int pageIndex, int maxResults);
}
