package org.iqalliance.smallProject.ticket.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.iqalliance.smallProject.common.service.MyQRCode;
import org.iqalliance.smallProject.common.web.JsonResult;
import org.iqalliance.smallProject.ticket.entity.Ticket;
import org.iqalliance.smallProject.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/ticket")
@Controller
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	
	/*
	 * ע����Ʊ����Ϣ
	 */
	@RequestMapping("/signin")
	@ResponseBody
	public JsonResult saveObject(Ticket ticket) {
		System.out.println(ticket);
		int flag = ticketService.saveTicket(ticket);
		if(flag == 0) {
			return new JsonResult(new Exception());
		}else if(flag == -1){
			return new JsonResult("�ֻ����Ѿ���ע�ᣡ");
		}else {
			return new JsonResult();
		}
	}
	
	
	/*
	 * ����ά����ʵ��
	 */
	@RequestMapping("pay")
	@ResponseBody
	public JsonResult doCheckMethod(Ticket ticket,String checkCode) {
		
		int flag = 0;
		String phone = ticket.getPhone();
		if( checkCode != null && !"".equals(checkCode) && "haoshaowen".equals(checkCode)) {
			if(phone != null && !"".equals(phone)) {
				//����ֻ�����Ϣ
				flag = ticketService.pay(ticket);
			}
		}
		if(flag == 1) {
			return new JsonResult();
		} else {
			return new JsonResult("֧��ʧ�ܣ����Ժ����ԣ�");
		}
	}
	
	
	@RequestMapping("/check")
	@ResponseBody
	public JsonResult doUpload(HttpServletRequest request) {
		
		String message = "";
		
		//�����ļ���������
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//���û�������С���������ô�С���ļ��ͻᱻ�洢��һ����ʱ��ŵ�ַ��
		factory.setSizeThreshold(1024*10);
		//����һ���ļ��ϴ�������
		ServletFileUpload upload = new ServletFileUpload(factory);
		//����ϴ��ļ����ֵ�������������
		upload.setHeaderEncoding("UTF-8");
		
		//���õ����ļ�������С
		upload.setSizeMax(1024*10);
		//���������ļ�������С
		upload.setFileSizeMax(1024*10);
		
		//���ڴ洢��ͨ����Ϣ������
		Map<String,Object> map = new HashMap<String,Object>();
		//��֤�ź�
		int flag = 0;
		try {
			//��ȡ������Ϣͷ��ȡ���� �����б�
			List<FileItem> items = upload.parseRequest(request);
			for(FileItem item:items) {
				if(item.isFormField()) {
					//������з�װ������ͨ������
					String name = item.getName();
					String value = item.getString("UTF-8");
					
					map.put(name, value);
				}else {
					//��Ϊ�ļ���ʱ
					String name = item.getName();
					if("qrCode".equals(name)) {
						InputStream in = item.getInputStream();
						String result = MyQRCode.readQrCode(in);
						
						map.put(name, result);
					}
				}
			}
			
			//��֤��ά����Ϣ�Ƿ���ȷ
			 flag = ticketService.consumePay(map);
		} catch (FileUploadException e) {
			message = "������ֹ���";
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			message = "�������";
			e.printStackTrace();
		} catch (IOException e) {
			message = "������ֹ���";
			e.printStackTrace();
		}
		if("".equals(message)) {
			return new JsonResult();
		}else {
			return new JsonResult(message);
		}
	}
}
