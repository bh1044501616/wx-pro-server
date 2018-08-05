package org.iqalliance.smallProject.meeting.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iqalliance.smallProject.common.service.MyCrawler;
import org.iqalliance.smallProject.common.web.PageObject;
import org.iqalliance.smallProject.common.web.StaticValue;
import org.iqalliance.smallProject.meeting.dao.LecturerDAO;
import org.iqalliance.smallProject.meeting.dao.MeetingDAO;
import org.iqalliance.smallProject.meeting.dao.ThemeDAO;
import org.iqalliance.smallProject.meeting.dao.TopicDAO;
import org.iqalliance.smallProject.meeting.entity.Meeting;
import org.iqalliance.smallProject.meeting.service.MeetingService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;


@Service
public class MeetingServiceImpl implements MeetingService{
	
	@Autowired
	private MeetingDAO meetingDAO;
	
	public int saveMeetingInfo(Meeting meeting) {
		int i = meetingDAO.saveObject(meeting);
		System.out.println("第一次插入数据"+i);
		if(i == 1) {
			int id = meeting.getId();
			Date date = meeting.getMeetingTime();
			String dateStr = (new SimpleDateFormat("yyyyMMdd")).format(date);
			String pptUrl = StaticValue.URL + "meeting/download.do?date=" + dateStr + "&id=" + id;
			meeting.setPptUrl(pptUrl);
			i = meetingDAO.updateObject(meeting);
		}
		return i;
	}

	public Map<String,Object> loadMeetingInfo(PageObject pageObject) {
		if(pageObject != null) {
			Map<String,Object> map = new HashMap<String,Object>();
			List<Meeting> list = meetingDAO.getPageObjects(pageObject);
			map.put("list", list);
			map.put("pageObject", pageObject);
			return map;
		}
		return null;
	}

	public Map<String,Object> loadPPTS(PageObject pageObject,String date,String id){
		if(pageObject != null) {
			Map<String,Object> map = new HashMap<String,Object>();
			List<Meeting> list = meetingDAO.getPageObjects(pageObject);
			//获取请求数据的文件夹
			String absolutePath = getClass().getResource("/files/meeting").toString();
			String dirPath = absolutePath.substring(6) + File.separator + date;
			File dir = new File(dirPath);
			//找出该文件夹下符合的ppt/pptx文件存入数组中
			File[] files = null;
			if(dir.exists() && dir.isDirectory()) {
				files = dir.listFiles(new FileFilter() {
					
					public boolean accept(File file) {
						String fileType = file.getName().substring(file.getName().indexOf(".")+1);
						if(fileType.matches("ppt|pptx|pdf|doc|xls|docx|xlsx")) {
							return true;
						}
						return false;
					}
				});
			}
			//将ppt文件的名字存入集合
			List<String> ppts = new ArrayList<String>();
			if(files != null)
				for(File file:files) {
					ppts.add(file.getName());
				}
			//将查找到的文件信息存入实体类中
			for(int i=0;i<list.size();i++) {
				System.out.println(list.get(i));
				if((list.get(i).getId()+"").equals(id)) {
					Meeting meeting = list.get(i);
					meeting.setPpts(ppts);
					list.set(i, meeting);
					break;
				}
			}
			map.put("list", list);
			map.put("pageObject", pageObject);
			return map;
		}
		return null;
	}

	public Map<String, Object> getMeetingInfoByDate(Date date) {
		return null;
	}

}
