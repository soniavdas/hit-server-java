package com.hiveTown.service;

import java.util.List;
import java.util.Set;

import com.hiveTown.Exception.HTWebConsoleException;
import com.hiveTown.data.NoticeSummary;
import com.hiveTown.model.Community;
import com.hiveTown.model.User;
import com.hiveTown.model.NoticeBoard.*;

public interface NoticeBoardService {
   void updateNotice(Notice notice);
   Notice getNoticeById(Integer id);
   int addComment(Integer noticeId, String comment, User user) throws HTWebConsoleException;
   NoticeSummary getSummary(Community community);
   void addNotice(Notice notice, Community community);
   void deleteNotice(Integer noticeId);
   void publish(Integer noticeId, Integer communityId) throws HTWebConsoleException;
   NoticeComment getComment(Integer commentId);
   void updateComment(Integer commentId, String commentText);
   void deleteComment(Integer commentId, User user, String urlKeyword)
		throws HTWebConsoleException;
   List<Notice> getNoticesByStatus(Community community, NoticeStatus status,
		int pageIndex, int maxResults);
   List<Notice> getAllNotices(Community community, int pageIndex, int maxResults);
}
