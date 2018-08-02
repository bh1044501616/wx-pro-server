package org.iqalliance.smallProject.schedule.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.iqalliance.smallProject.schedule.entity.Conference;
import org.iqalliance.smallProject.schedule.entity.DetailBox;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleDAO {
	/*
	 * ͨ�����ڻ�ô����Ϣ
	 */
	public ArrayList<Conference> getObjectsByDate(@Param("date")Date date);
	/*
	 * ͨ��id��ȡ��Ӧ��schedule�Ľ�ʦ����Ϣ��ppt��������
	 */
	public List<DetailBox> getLecturerInfoById(int[] ids);
}
