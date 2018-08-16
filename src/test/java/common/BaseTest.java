package common;

import java.util.HashMap;

import org.iqalliance.smallProject.common.dao.ImageDAO;
import org.iqalliance.smallProject.common.entity.Download;
import org.iqalliance.smallProject.common.entity.Image;
import org.iqalliance.smallProject.common.service.DownloadService;
import org.iqalliance.smallProject.common.web.HttpRequestUtil;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.Assert;

public class BaseTest {
	
	protected ApplicationContext ac;
	

	public BaseTest(){
		ac = new ClassPathXmlApplicationContext("spring-webmvc.xml","spring-mybatis.xml","spring-pool.xml");
	}

	@Test
	public void testHttpRequest() {
		String response = HttpRequestUtil.sendPostRequest("http://localhost:8080/smallProject1.0/sponsor/load.do", new HashMap());
		System.out.println(response);
	}
	
	
	@Test
	/*
	 * ≤‚ ‘imageDAO
	 * 
	 */
	public void testImageDAO() {
		ImageDAO imageDAO = this.ac.getBean("imageDAO",ImageDAO.class);
		
		Image image = new Image();
		image.setPath("C:\\Users\\Airbook\\Desktop\\tomcat\\wtpwebapps\\smallProject1.0\\WEB-INF\\classes\\files\\ticket\\noQrCode.png");
		image.setHashcode(image.hashCode() + "");
		
		int flag = imageDAO.saveObject(image);
		Assert.assertEquals(1,flag);
	}
	
	@Test
	/*
	 * ≤‚ ‘imageDAOµƒgetPath
	 * 
	 */
	public void testImageDAOGetPath() {
		ImageDAO imageDAO = this.ac.getBean("imageDAO",ImageDAO.class);
		
		String hashcode = "46941357";
		
		String path = imageDAO.getPath(hashcode);
		System.out.println(path);
	}
	
	
	@Test
	/*
	 * ≤‚ ‘hashcode
	 * 
	 */
	public void test() {
		Image image = new Image();
		System.out.println(image.hashCode());
		image.setHashcode(565645+"");
		System.out.println(image.hashCode());
	}
	
	
	@Test
	/*
	 * ≤‚ ‘¥Ê¥¢download∂‘œÛ
	 */
	public void test1() {
		DownloadService downloadService = ac.getBean("downloadService",DownloadService.class);
		Download d = new Download();
		d.setPath("C:/Response.txt");
		d.setHashcode(d.hashCode() + "");
		
		int flag = downloadService.saveDownloadObj(d);
		Assert.assertEquals(1, flag);
	}
}
