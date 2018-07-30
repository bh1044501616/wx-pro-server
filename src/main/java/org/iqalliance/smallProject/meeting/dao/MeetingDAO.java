package org.iqalliance.smallProject.meeting.dao;

import java.util.List;

import org.iqalliance.smallProject.common.web.PageObject;
import org.iqalliance.smallProject.meeting.entity.Meeting;

public interface MeetingDAO {
	//��meeting���в���һ��һ�δ�������
	public int saveObject(Meeting meeting);
	//��ȡmeeting���е���Ϣ
	public List<Meeting> getPageObjects(PageObject pageObject);
	//�ϴ��ļ�ʱ������֤���
	public String checkForm(String account);
	//��������
	public int updateObject(Meeting meeting);
	//�����ݽ�����Ϣ
	public int setLecInfo(Meeting meeting);
}
