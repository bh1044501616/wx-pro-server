package org.iqalliance.smallProject.meeting.dao;

import java.util.List;

import org.iqalliance.smallProject.common.web.PageObject;
import org.iqalliance.smallProject.meeting.entity.Meeting;

public interface MeetingDAO {
	//向meeting表中插入一条一次大会的数据
	public int saveObject(Meeting meeting);
	//获取meeting表中的信息
	public List<Meeting> getPageObjects(PageObject pageObject);
	//上传文件时用于验证身份
	public String checkForm(String account);
	//更新数据
	public int updateObject(Meeting meeting);
	//设置演讲人信息
	public int setLecInfo(Meeting meeting);
}
