package org.iqalliance.smallProject.common.web;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iqalliance.smallProject.common.service.MyCrawler;
import org.iqalliance.smallProject.meeting.dao.LecturerDAO;
import org.iqalliance.smallProject.meeting.dao.MeetingDAO;
import org.iqalliance.smallProject.meeting.dao.ThemeDAO;
import org.iqalliance.smallProject.meeting.dao.TopicDAO;
import org.iqalliance.smallProject.meeting.entity.Meeting;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SaveMeeting {
	public Map saveMeetingInfoByCrawler() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("spring-webmvc.xml","spring-pool.xml","spring-mybatis.xml");
		ThemeDAO themeDAO = ac.getBean("themeDAO",ThemeDAO.class);
		TopicDAO topicDAO = ac.getBean("topicDAO",TopicDAO.class);
		LecturerDAO lecturerDAO = ac.getBean("lecturerDAO",LecturerDAO.class);
		
				
		MyCrawler myCrawler = new MyCrawler();
		
		final String URL = "http://web1806060001.gz01.bdysite.com/category/";
		final String URL1 = "http://web1806060001.gz01.bdysite.com";
		int n = 17;
		Document[] documents = new Document[4];
		
		for(int i=0;i<documents.length;i++) {
			String url = URL + (n++) + ".html";
			String indexPageHtml = myCrawler.getHtmlStr(url);
			documents[i] = Jsoup.parse(indexPageHtml);
		}
		
		
		Map<String,Object> meetingMap = new HashMap<String,Object>();
		List<Object> lecturerList = new ArrayList<Object>();
		Calendar beginDate = Calendar.getInstance();
		try {
			beginDate.setTime(new SimpleDateFormat("yyyy-MM-dd").parse("2018-07-15"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<documents.length;i++) {
			Elements elements = documents[i].select("div[class=box]");
			for(int j=0;j<elements.size();j++) {
				Element box = elements.get(j);
				
				String theme = box.select("span[class=title]").text();
				
//				Theme themeObj = new Theme();
//				themeObj.setTheme(theme);
//				themeDAO.saveObject(themeObj);
				
				String url1 = box.select("a[class=text mb20]").attr("href");
				System.out.println(url1);
				String htmlStr = null;
				try {
					htmlStr = myCrawler.getHtmlStr(URL1 + url1);
				}catch(Exception e) {
					System.out.println(i+","+j);
				}
				Document detailDocument = Jsoup.parse(htmlStr);
				
				Elements boxes = detailDocument.select("div[class=box]");
				
				for(int k=0;k<boxes.size();k++) {
					Element box__k = boxes.get(k);
					
					String topic = box__k.attr("tit").trim();
					
//					Pattern pattern = Pattern.compile("(\\w+:\\w{2}-\\w{2}:\\w{2})*");
//					Matcher matcher = pattern.matcher(topicStr);
//					matcher.find();
//					String timeStr = matcher.group();
//					String topic = topicStr.substring(topicStr.indexOf(timeStr) + timeStr.length()).trim();
					
//					Topic topicObj = new Topic();
//					topicObj.setTopic(topic);
//					topicDAO.saveObject(topicObj);
					
					Elements lecturers = box__k.select("div[class=content fix]");
					for(int l=0;l<lecturers.size();l++) {
						Element lecture = lecturers.get(l);
						
						String pic = lecture.select("img").attr("src").trim();
						String name = lecture.select("div[class=c-1]").text().trim();
						
						Element text = lecture.select("div[class=text]").first();
						String introduction = null;
						if(text != null) {
							introduction = text.select("div").last().text();
						}
						
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("pic", pic);
						map.put("name", name);
						map.put("introduction", introduction);
						
						lecturerList.add(map);
//						Lecturer lecturerObj = new Lecturer();
//						lecturerObj.setIntroduction(introduction);
//						lecturerObj.setName(name);
//						lecturerObj.setPic(pic);
//						
//						lecturerDAO.saveObject(lecturerObj);
					}
				}
			}
		}
		meetingMap.put(new SimpleDateFormat("yyyy-MM-dd").format(beginDate.getTime()), lecturerList);
		beginDate.add(Calendar.DAY_OF_MONTH, 1);
		
		return meetingMap;
	}
	
	public void saveNamesAndPPTNames() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("spring-webmvc.xml","spring-pool.xml","spring-mybatis.xml");
		MeetingDAO meetingDAO = ac.getBean("meetingDAO",MeetingDAO.class);
		
		File majorDir = new File("C:" + File.separator + File.separator + "ppts");
		if(!majorDir.exists()) {
			System.out.println("文件夹不存在");
			return;
		}
		File[] dirs = majorDir.listFiles();
		for(int i=0;i<dirs.length;i++) {
			if(!dirs[i].isDirectory()) {
				System.out.println("不是文件夹");
				continue;
			}
			File[] files = dirs[i].listFiles();
			for(int j=0;j<files.length;j++) {
				if(files[j].isDirectory()) {
					File[] filesInDir = files[j].listFiles();
					for(int k=0;k<filesInDir.length;k++) {
						String name = filesInDir[k].getName();
						
						int flag = name.lastIndexOf("-");
						Meeting meeting = new Meeting();
						if(flag < 0) {
							String theme = name.substring(0, name.lastIndexOf("."));
							meeting.setMeetingTheme(theme);
						}else {
							String lecName = name.substring(flag + 1,name.lastIndexOf(".")).trim();
							String theme = name.substring(0,flag);
							
							meeting.setMeetingTheme(theme);
							meeting.setLecturerName(lecName);
						}
						System.out.println(name);
						System.out.println(flag);
						meetingDAO.saveObject(meeting);
					}
				}
				if(files[j].isFile()) {
					String name = files[j].getName();
					
					if(name.matches("TiD2017")) {
						System.out.println("1");
						name = name.substring(name.indexOf("TiD2017-") + "TiD2017-".length());
					}
					
					int flag = name.lastIndexOf("-");
					Meeting meeting = new Meeting();
					if(flag < 0) {
						String theme = name.substring(0, name.lastIndexOf("."));
						meeting.setMeetingTheme(theme);
					}else {
						String lecName = name.substring(flag + 1,name.lastIndexOf(".")).trim();
						String theme = name.substring(0,flag);
						
						meeting.setMeetingTheme(theme);
						meeting.setLecturerName(lecName);
					}
					System.out.println(flag);
					System.out.println(name);
					meetingDAO.saveObject(meeting);
				}
			}
		}
	}
	
	public void saveInfo() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("spring-webmvc.xml","spring-pool.xml","spring-mybatis.xml");
		MeetingDAO meetingDAO = ac.getBean("meetingDAO",MeetingDAO.class);
		
		Map<String,Object> map = this.saveMeetingInfoByCrawler();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String beginDateStr = "2018-7-15";
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(beginDateStr));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = "http://web1806060001.gz01.bdysite.com";
		for(int i=0;i<4;i++) {
			String key = sdf.format(c.getTime());
			
			List<Object> list = (List<Object>) map.get(key);
			for(int j=0;j<list.size();j++) {
				Map lecInfo = (Map) list.get(j);
				
				String name = ((String) lecInfo.get("name")).trim();
				String pic = url + lecInfo.get("pic");
				String intro = (String) lecInfo.get("introduction");
				
				Meeting meeting = new Meeting();
				meeting.setLecturerName(name);
				meeting.setImageUrl(pic);
				meeting.setIntroduction(intro);
				
				System.out.println(name);
				meetingDAO.setLecInfo(meeting);
			}
		}
	}
	
	
	public static void main(String[] args) {
		SaveMeeting meetingService = new SaveMeeting();
		Map map = meetingService.saveMeetingInfoByCrawler();
		System.out.println(map);
//		meetingService.saveNamesAndPPTNames();
//		meetingService.saveInfo();
	}
	
	public void set() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("spring-webmvc.xml","spring-pool.xml","spring-mybatis.xml");
		MeetingDAO meetingDAO = ac.getBean("meetingDAO",MeetingDAO.class);
		
		String name = "";
		String pic = "";
		String intro = "";
		
		
		Meeting meeting = new Meeting();
		meeting.setLecturerName(name);
		meeting.setImageUrl(pic);
		meeting.setIntroduction(intro);
		
		meetingDAO.saveObject(meeting);
		
	}
}
