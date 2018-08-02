package schedule;


import java.util.Date;
import java.util.List;

import org.iqalliance.smallProject.schedule.dao.ScheduleDAO;
import org.iqalliance.smallProject.schedule.entity.Conference;
import org.junit.Test;
import java.util.Map;

import common.BaseTest;

public class TestCase extends BaseTest{
	
	@Test
	public void testGetObjectsByDate() {
		ScheduleDAO dao = super.ac.getBean("scheduleDAO",ScheduleDAO.class);
		long secs = System.currentTimeMillis()-24*60*60*1000*2;
		Date now = new Date(secs);
		List<Conference> c = dao.getObjectsByDate(new Date());
		System.out.println(c);
	}
	
	@Test
	public void testGetDetail(){
		ScheduleDAO dao = super.ac.getBean("scheduleDAO",ScheduleDAO.class);
		
		int[] ids = new int[]{1,2};
		List map = dao.getLecturerInfoById(ids);
		System.out.println(map);
	}
}
