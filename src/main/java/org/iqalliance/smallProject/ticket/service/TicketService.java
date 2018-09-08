package org.iqalliance.smallProject.ticket.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.iqalliance.smallProject.common.dao.ImageDAO;
import org.iqalliance.smallProject.common.entity.Image;
import org.iqalliance.smallProject.common.service.MyQRCode;
import org.iqalliance.smallProject.common.web.StaticValue;
import org.iqalliance.smallProject.ticket.dao.TicketDAO;
import org.iqalliance.smallProject.ticket.entity.QrCode;
import org.iqalliance.smallProject.ticket.entity.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.WriterException;

@Service
public class TicketService {
	
	public static int QR_CODE_SIZE = 1000;
	public static String QR_CODE_FORMAT = "png";
	
	@Autowired
	private TicketDAO ticketDAO;
	
	/*
	 * 存储ticket对象到数据库中
	 */
	public int saveTicket(Ticket ticket) {
		String company = ticket.getCompany();
		String position = ticket.getPosition();
		//如果前端传来的数据中公司和职位未填写，可能为null值，此时将值变为“空串”
		if(company == null) {
			ticket.setCompany("");
		}
		if(position == null) {
			ticket.setPosition("");
		}
		//检查手机号码必须由 11 位数字组成
		String phone = ticket.getPhone();
		try {
			Long.parseLong(phone);
		}catch(NumberFormatException n) {
			return 0;
		}
		Integer flag = ticketDAO.checkPhone(phone);
		if(flag != null && flag > 0) {
			//返回-1说明该手机号已经被注册
			return -1;
		}
		return ticketDAO.saveObject(ticket);
	}
	
	/*
	 * 获取一个6位的随机的验证码
	 */
	public String getIdentifyingCode() {
		StringBuffer code = new StringBuffer();
		for(int i=0;i<6;i++) {
			int num = (int) (Math.random() * 10);
			code.append(num);
		}
		return code.toString();
	}
	
	
	/*
	 * 为用户进行分配支付二维码服务，并且标记用户已经支付
	 */
	public int pay(Ticket ticket) {
		String phone = ticket.getPhone();
		if( ticketDAO.checkPhone(phone) == null) {
			//无此用户
			return -1;
		}
		//生成二维码
		String path = getClass().getResource("/files/ticket").toString().substring("file:/".length());
		String qrCodePath = StaticValue.FILE_PATH + "ticket/" + ticket.getPhone() + ".png";
//		String qrCodePath = path + "qrcode/" + ticket.getPhone() + ".png";
		
		File file = new File(qrCodePath);
		FileOutputStream fos = null;
		boolean flag = false;
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fos = new FileOutputStream(file);
			flag = MyQRCode.saveAsQrCode(fos, ticket.getPhone(), QR_CODE_SIZE, QR_CODE_FORMAT);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//存储二维码信息
		QrCode qrcode = new QrCode();
		System.out.println(qrcode.hashCode());
		qrcode.setHashcode(qrcode.hashCode() + "");
		qrcode.setPath(qrCodePath);
		System.out.println(qrcode.hashCode());
		
		int flag_save = 0;
		int flag_update = 0;
		if(flag == true) {
			flag_save = ticketDAO.saveQrCode(qrcode);
			if(flag_save == 1) {
				String qrCode = StaticValue.URL + "ticket/qrcode/" + qrcode.hashCode() + ".png";
				flag_update = ticketDAO.updateObject(ticket.getPhone(), qrCode);
			}
		}
		
		return flag_update;
	}
	
	
	/*
	 * 将用户的支付状态设置为0，然后发送可以分发入场门牌的信息
	 */
	public int consumePay(String phone) {
		int flag = 0;
		
		//验证通过
		flag = ticketDAO.checkObject(phone);
		
		//当更新结果为1时，说明有数据更新，用户存在并且验证操作完成
		return flag;
	}
	
	
	/*
	 * 验证密码
	 */
	public int checkPwd(String phone,String password) {
		Ticket ticket = ticketDAO.getObject(phone);
		
		if(ticket != null) {
			String pwd = ticket.getPassword();
			if(pwd.equals(password)) {
				//密码一致，验证通过
				return 1;
			}else {
				//密码不对，验证失败
				return 0;
			}
		}
		//账号不存在
		return -1;
	}
	
	/*
	 * 通过验证，获取账户信息
	 * 	
	 * 		若返回的值为null，则说明用微信登陆的还未进行注册
	 */
	public Ticket getAccountInfo(String account) {
		return ticketDAO.getObject(account);
	}
	
	public String getFilePath(String hashcode) {
		String path = ticketDAO.getPath(hashcode);
		if( path != null && !"".equals(hashcode)) {
			return path;
		}
		return null;
	}
}
