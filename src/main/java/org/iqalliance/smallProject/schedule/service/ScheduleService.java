package org.iqalliance.smallProject.schedule.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.iqalliance.smallProject.common.dao.DownloadDAO;
import org.iqalliance.smallProject.common.dao.ImageDAO;
import org.iqalliance.smallProject.common.entity.Download;
import org.iqalliance.smallProject.common.entity.Image;
import org.iqalliance.smallProject.common.web.StaticValue;
import org.iqalliance.smallProject.schedule.dao.ScheduleDAO;
import org.iqalliance.smallProject.schedule.entity.Conference;
import org.iqalliance.smallProject.schedule.entity.DetailBox;
import org.iqalliance.smallProject.schedule.entity.Lecture;
import org.iqalliance.smallProject.schedule.entity.Lecturer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
	
	@Autowired
	private ScheduleDAO scheduleDAO;
	@Autowired
	private DownloadDAO downloadDAO;
	@Autowired
	private ImageDAO imageDAO;
	
	public ArrayList<HashMap<String,Object>> getSchedulesByDate(Date date) {
		List list = scheduleDAO.getObjectsByDate(date);
		
		Map<String,Object> schedules = new HashMap<String,Object>();
		Iterator<Conference> it = list.iterator();
		while(it.hasNext()) {
			Conference conference = it.next();
			
			int id = conference.getId();
			
			String loc = conference.getLoc();
			String domain = conference.getDomain();
			String topic = conference.getTopic();
			Date begintime = conference.getBegintime();
			Date endtime = conference.getEndtime();
			
			Map<String,Object> detail = new HashMap<String,Object>();
			detail.put("topic",topic);
			detail.put("begintime",begintime);
			detail.put("endtime",endtime);
			detail.put("id", id);
			
			if(schedules.containsKey(loc)) {
				//数据中存储了该key
				Map<String,Object> schedule = (Map<String, Object>) schedules.get(loc);
				List<Map<String,Object>> topics = (List<Map<String, Object>>) schedule.get("topics");
				topics.add(detail);
				schedule.put("topics", topics);
				schedules.put(loc, schedule);
			}else {
				//数据中未保存同schedule信息
				List<Map<String,Object>> topics = new ArrayList<Map<String,Object>>();
				topics.add(detail);
				
				Map<String,Object> schedule = new HashMap<String,Object>();
				schedule.put("domain", domain);
				schedule.put("topics", topics);
				
				schedules.put(loc, schedule);
			}
		}
		
		
		//将map数据转换为list形式
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		for(Iterator<String> set = schedules.keySet().iterator();set.hasNext();) {
			String key = set.next();
			
			Map<String,Object> schedule = (Map<String,Object>) schedules.get(key);
			schedule.put("loc", key);
			data.add((HashMap<String, Object>) schedule);
		}
		return data;
	}
	
	
	public Map<String,Object> getLecturerInfoById(int[] ids){
		List<DetailBox> details = scheduleDAO.getLecturerInfoById(ids);
		
		Map<String,Object> map = new HashMap<String,Object>();
		for(int i=0;i<details.size();i++) {
			DetailBox detailBox = details.get(i);
			
			
			int cid = detailBox.getCid();
			List detail = detailBox.getDetails();
			
			map.put(cid+"", detail);
		}
		return map;
	}
	
	/*
	 *	先将download对象存储到数据库中，如果存储完成则存储ppt数据到数据库中
	 *
	 *		返回值：整型			0--两个数据都没存储成功
	 *							1--download对象存储成功
	 *							2--两条数据都存储成功
	 */
	public int savePPT(Download download,Lecture lecture) {
		int index = 0;
		//先将download对象存储进数据库
		int flag_download = downloadDAO.saveObject(download);
		index = flag_download;
		if(flag_download == 1) {
			lecture.setPpt(StaticValue.URL + "download/" + download.getHashcode() + ".do");
			//如果存储成功，则将ppt链接存储进lecture表中
			int flag_schecdule = scheduleDAO.savePPT(lecture);
			index += flag_schecdule;
		}
		return index;
	}
	
	/*
	 *	先将image对象存储到数据库中，如果存储完成则存储pic数据到数据库中
	 *
	 *		返回值：整型			0--两个数据都没存储成功
	 *							1--image对象存储成功
	 *							2--两条数据都存储成功
	 */
	public int savePic(Image image,Lecturer lecturer) {
		int index = 0;
		//先将image对象存储进数据库
		int flag_image = imageDAO.saveObject(image);
		index = flag_image;
		if(flag_image == 1) {
			lecturer.setPic(StaticValue.URL + "image/" + image.getHashcode() + ".jpg");
			//如果存储成功，则将ppt链接存储进lecture表中
			int flag_schecdule = scheduleDAO.savePic(lecturer);
			index += flag_schecdule;
		}
		return index;
	}
	
	/*
	 * 获取所有的演讲嘉宾
	 * 
	 * 		返回值：null-说明未获取到
	 * 			   list-获取到的值
	 */
	public List<Lecturer> getAllLecturer() {
		return scheduleDAO.getLecturers();
	}
}
