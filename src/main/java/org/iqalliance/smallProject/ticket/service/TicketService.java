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
	 * �洢ticket�������ݿ���
	 */
	public int saveTicket(Ticket ticket) {
		String company = ticket.getCompany();
		String position = ticket.getPosition();
		//���ǰ�˴����������й�˾��ְλδ��д������Ϊnullֵ����ʱ��ֵ��Ϊ���մ���
		if(company == null) {
			ticket.setCompany("");
		}
		if(position == null) {
			ticket.setPosition("");
		}
		//����ֻ���������� 11 λ�������
		String phone = ticket.getPhone();
		try {
			Long.parseLong(phone);
		}catch(NumberFormatException n) {
			return 0;
		}
		Integer flag = ticketDAO.checkPhone(phone);
		if(flag != null && flag > 0) {
			//����-1˵�����ֻ����Ѿ���ע��
			return -1;
		}
		return ticketDAO.saveObject(ticket);
	}
	
	/*
	 * ��ȡһ��6λ���������֤��
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
	 * Ϊ�û����з���֧����ά����񣬲��ұ���û��Ѿ�֧��
	 */
	public int pay(Ticket ticket) {
		String phone = ticket.getPhone();
		if( ticketDAO.checkPhone(phone) == null) {
			//�޴��û�
			return -1;
		}
		//���ɶ�ά��
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

		//�洢��ά����Ϣ
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
	 * ���û���֧��״̬����Ϊ0��Ȼ���Ϳ��Էַ��볡���Ƶ���Ϣ
	 */
	public int consumePay(String phone) {
		int flag = 0;
		
		//��֤ͨ��
		flag = ticketDAO.checkObject(phone);
		
		//�����½��Ϊ1ʱ��˵�������ݸ��£��û����ڲ�����֤�������
		return flag;
	}
	
	
	/*
	 * ��֤����
	 */
	public int checkPwd(String phone,String password) {
		Ticket ticket = ticketDAO.getObject(phone);
		
		if(ticket != null) {
			String pwd = ticket.getPassword();
			if(pwd.equals(password)) {
				//����һ�£���֤ͨ��
				return 1;
			}else {
				//���벻�ԣ���֤ʧ��
				return 0;
			}
		}
		//�˺Ų�����
		return -1;
	}
	
	/*
	 * ͨ����֤����ȡ�˻���Ϣ
	 * 	
	 * 		�����ص�ֵΪnull����˵����΢�ŵ�½�Ļ�δ����ע��
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
