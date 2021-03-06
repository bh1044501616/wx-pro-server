package org.iqalliance.smallProject.schedule.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.iqalliance.smallProject.schedule.entity.Conference;
import org.iqalliance.smallProject.schedule.entity.DetailBox;
import org.iqalliance.smallProject.schedule.entity.Lecture;
import org.iqalliance.smallProject.schedule.entity.Lecturer;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleDAO {
	/*
	 * 通过日期获得大会信息
	 */
	public ArrayList<Conference> getObjectsByDate(@Param("date")Date date);
	/*
	 * 通过id获取相应的schedule的讲师的信息和ppt下载链接
	 */
	public List<DetailBox> getLecturerInfoById(int[] ids);
	/*
	 * 存储演讲的ppt文件的链接
	 */
	public int savePPT(Lecture lecture) ;
	/*
	 * 储存演讲人的照片
	 */
	public int savePic(Lecturer lecturer);
	/*
	 * 获取所有演讲嘉宾
	 */
	public List<Lecturer> getLecturers();
}
