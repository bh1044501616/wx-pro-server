package common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaseTest {
	
	protected ApplicationContext ac;
	

	public BaseTest(){
		ac = new ClassPathXmlApplicationContext("spring-webmvc.xml","spring-mybatis.xml","spring-pool.xml");
	}

}
