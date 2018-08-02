package org.iqalliance.smallProject.schedule.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.iqalliance.smallProject.schedule.dao.ScheduleDAO;
import org.iqalliance.smallProject.schedule.entity.Conference;
import org.iqalliance.smallProject.schedule.entity.DetailBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
	
	@Autowired
	private ScheduleDAO scheduleDAO;
	
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
}
