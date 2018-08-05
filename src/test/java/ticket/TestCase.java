package ticket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.iqalliance.smallProject.common.service.MyQRCode;
import org.iqalliance.smallProject.ticket.dao.TicketDAO;
import org.iqalliance.smallProject.ticket.entity.Ticket;
import org.iqalliance.smallProject.ticket.service.TicketService;
import org.junit.Assert;
import org.junit.Test;

import com.google.zxing.WriterException;

import common.BaseTest;

public class TestCase extends BaseTest{
	
	@Test
	public void test1() {
		TicketDAO ticketDAO = super.ac.getBean("ticketDAO",TicketDAO.class);
		Ticket ticket = new Ticket();
		
		ticket.setForum("forum");
		ticket.setName("name");
		ticket.setCompany("company");
		ticket.setPosition("position");
		ticket.setPhone("13131464346");
		ticket.setEmail("email");
		ticket.setPassword("password");
		
		int flag = ticketDAO.saveObject(ticket);
		
		System.out.println(flag);
		Assert.assertEquals(flag, 1);
	}
	
	@Test
	public void test2() {
		TicketService ticketService = super.ac.getBean("ticketService",TicketService.class);
		
		String code = ticketService.getIdentifyingCode();
		System.out.println(code);
	}
	
	@Test
	public void test3() {
		TicketDAO ticketDAO = super.ac.getBean("ticketDAO",TicketDAO.class);
		
		int id = ticketDAO.checkPhone("13131464346");
		System.out.println(id);
	}
	
	@Test
	public void test4() {
		TicketDAO ticketDAO = super.ac.getBean("ticketDAO",TicketDAO.class);
		
		int num = ticketDAO.updateObject("13131464346","qrcode");
		System.out.println(num);
	}
	
	
	@Test
	public void  test5() {
		//测试二维码图形形成方法
		TicketService ticketService = new TicketService();
		
		File file = new File("asd.png");
		FileInputStream fos = null;
		try {
			fos = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			String result = MyQRCode.readQrCode(fos);
			System.out.println(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
