package sponsor;

import java.util.ArrayList;
import java.util.List;

import org.iqalliance.smallProject.sponsor.dao.SponsorDAO;
import org.junit.Test;

import common.BaseTest;

public class TestCase extends BaseTest{
	
	@Test
	public void test1() {
		SponsorDAO sponsorDAO = ac.getBean("sponsorDAO",SponsorDAO.class);
		
		List list = new ArrayList();
		list.add("助力研发效率");
		
		List result = sponsorDAO.getObjectsByAntistop(list);
		System.out.println(result);
	}
}
