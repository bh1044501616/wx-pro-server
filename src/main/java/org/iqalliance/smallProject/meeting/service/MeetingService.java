package org.iqalliance.smallProject.meeting.service;

import java.util.Date;
import java.util.Map;

import org.iqalliance.smallProject.common.web.PageObject;
import org.iqalliance.smallProject.meeting.entity.Meeting;

public interface MeetingService {
	public int saveMeetingInfo(Meeting meeting);
	public Map<String, Object> loadMeetingInfo(PageObject pageObject);
	public Map<String,Object> loadPPTS(PageObject pageObject,String date,String id);
	
	//ͨ�����ڻ�ȡ�������ٰ�Ĵ����Ϣ
	public Map<String,Object> getMeetingInfoByDate(Date date);
}
