package meeting;

import java.util.Date;

import org.iqalliance.smallProject.meeting.dao.ConferenceDAO;
//import org.iqalliance.smallProject.meeting.dao.LectureDAO;
import org.iqalliance.smallProject.meeting.dao.LecturerDAO;
import org.iqalliance.smallProject.meeting.dao.ThemeDAO;
import org.iqalliance.smallProject.meeting.dao.TopicDAO;
//import org.iqalliance.smallProject.meeting.entity.Lecture;
import org.iqalliance.smallProject.meeting.entity.Theme;
import org.iqalliance.smallProject.meeting.entity.Topic;
import org.iqalliance.smallProject.schedule.entity.Conference;
import org.iqalliance.smallProject.schedule.entity.Lecturer;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestNewMeeting {
	private ApplicationContext ac;
	
	public TestNewMeeting(){
		ac = new ClassPathXmlApplicationContext("spring-webmvc.xml","spring-mybatis.xml","spring-pool.xml");
	}
	
//	@Test
//	public void test() {
//		ThemeDAO themeDAO = ac.getBean("themeDAO",ThemeDAO.class);
//		TopicDAO topicDAO = ac.getBean("topicDAO",TopicDAO.class);
//		LecturerDAO lecturerDAO = ac.getBean("lecturerDAO",LecturerDAO.class);
//		ConferenceDAO conferenceDAO = ac.getBean("conferenceDAO",ConferenceDAO.class);
//		LectureDAO lectureDAO = ac.getBean("lectureDAO",LectureDAO.class);
//		
//		
//		Theme theme = new Theme();
//		theme.setTheme("这是主题");
//		Topic topic = new Topic();
//		topic.setTopic("演讲题目");
//		Lecturer lecturer = new Lecturer();
//		lecturer.setName("郝劭文");
//		lecturer.setIntroduction("是个大好人");
//		lecturer.setPic("http://www.baidu.com");
//		
//		Conference conference = new Conference();
//		conference.setLoc("丰宁");
//		conference.setTopic(topic.getTopic());
//		
//		themeDAO.saveObject(theme);
//		topicDAO.saveObject(topic);
//		lecturerDAO.saveObject(lecturer);
//		conferenceDAO.saveObject(conference);
//	}
	
//	@Test
//	public void test1() {
//		LectureDAO lectureDAO = ac.getBean("lectureDAO",LectureDAO.class);
//		
//		Lecture lecture = new Lecture();
//		lecture.setCid(1);
//		lecture.setLid(1);
//		
//		lectureDAO.saveObject(lecture);
//	}
}
