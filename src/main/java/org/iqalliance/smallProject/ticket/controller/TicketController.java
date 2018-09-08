package org.iqalliance.smallProject.ticket.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.iqalliance.smallProject.common.web.JsonResult;
import org.iqalliance.smallProject.common.web.StaticValue;
import org.iqalliance.smallProject.schedule.controller.ScheduleController;
import org.iqalliance.smallProject.ticket.entity.Ticket;
import org.iqalliance.smallProject.ticket.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/ticket")
@Controller
public class TicketController {
	
	private final String LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=wx5815e99cfb706cd3&secret=7f87c49cb08298e9ffa355601b1525a8&grant_type=authorization_code&js_code=";
	
	public static Logger LOGGER = LoggerFactory.getLogger(TicketController.class.getName());
	
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
			return new JsonResult("���緱æ�����Ժ�");
		}else if(flag == -1){
			return new JsonResult("�ֻ����Ѿ���ע�ᣡ");
		}else {
			return new JsonResult();
		}
	}
	
	/*
	 * ��½�˺�
	 */
	@RequestMapping("/loginin")
	@ResponseBody
	public JsonResult doLoginIn(String phone,String wx_account,String password,HttpServletRequest request) {
		LOGGER.info("��½ticket�˻�����");
		if(wx_account != null) {
			LOGGER.info("΢�ŵ�½");
			Ticket ticket = ticketService.getAccountInfo(wx_account);
			return new JsonResult(ticket);
		}else {
			LOGGER.info("�˺������½");
			int flag = ticketService.checkPwd(phone, password);
			if(flag == 1) {
				LOGGER.info("�˺�������֤�ɹ�");
				
				Ticket ticket = ticketService.getAccountInfo(phone);
				String qrCode = ticket.getQrCode();
				if(qrCode == null || "".equals(qrCode)) {
					ticket.setQrCode(StaticValue.URL + "image/1243171897.png");
				}
				return new JsonResult(ticket);
			}else if(flag == 0) {
				LOGGER.info("��֤ʧ�ܣ��������");
				return new JsonResult("�������");
			}else {
				LOGGER.info("��֤ʧ�ܣ��˺Ų�����");
				return new JsonResult("�˺Ų����ڣ�");
			}
		}
	}
	
	/*
	 * ����ά����ʵ��
	 */
	@RequestMapping("pay")
	@ResponseBody
	public JsonResult doCheckMethod(Ticket ticket,String checkCode,HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		if(session != null) {
			int flag = 0;
			String phone = ticket.getPhone();
			flag = ticketService.checkPwd(phone, "nopwd");
			if(flag == -1) {
				return new JsonResult("�˺Ų����ڣ�");
			}
			if( checkCode != null && !"".equals(checkCode) && "haoshaowen".equals(checkCode)) {
				if(phone != null && !"".equals(phone)) {
					//����ֻ�����Ϣ
					flag = ticketService.pay(ticket);
				}
			}
			if(flag == 1) {
				return new JsonResult();
			}else {
				return new JsonResult("�˺���֧������ϵͳ�������Ժ����ԣ�");
			}
		}else {
			return new JsonResult("ҳ�����!");
		}
	}
	
	
	@RequestMapping("/check")
	@ResponseBody
	public JsonResult doUpload(String phone) {
		
		String message = "";
		int flag = 0;
		
		if(phone != null && !"".equals(phone)) {
			flag = ticketService.consumePay(phone);
		}
		if(flag == 0) {
			return new JsonResult("��ά����Ч��");
		}
		return new JsonResult();
		/*//�����ļ���������
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
	
	
	@RequestMapping("/login")
	@ResponseBody
	public JsonResult doLoginIn(String code) {
		URL url;
		OutputStream out = null;
		BufferedReader in = null;
		try {
			url = new URL(LOGIN_URL + code);
			URLConnection connection = url.openConnection();
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
            
			out = connection.getOutputStream();
			
			
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String response = "";
			String flag = null;
			while((flag = in.readLine()) != null) {
				response += flag;
			}
			
			JSONObject jsonObject = (JSONObject) JSON.parse(response);
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}*/
		
	}
	
	@RequestMapping("/qrcode/{hashcode}")
	@ResponseBody
	public void doLoadMeetingImage(HttpServletRequest request,HttpServletResponse response,@PathVariable("hashcode")String hashcode) {
		LOGGER.info("��ȡ��ά�����");
		//��֤��½״̬
		
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		
		LOGGER.info("��֤�˺�����");
		int flag = -1;
		if(account != null && !"".equals(account) && password != null && !"".equals(password)) {
			flag = ticketService.checkPwd(account, password);
		}
		if(flag == 1) {
			LOGGER.info("��֤����ɹ�������ͼƬ��Ϣ");
			String path = ticketService.getFilePath(hashcode);
			if( path == null) {
				String prefixPath = getClass().getResource("/").toString().substring(6);
				path = prefixPath + File.separator + "files" + File.separator + "404.png";
			}
			String uri = request.getRequestURI();
			FileInputStream in = null;
			OutputStream out = null;
			if(new File(path).exists()) {
				try {
					 in = new FileInputStream(path);
					 int size = in.available();
					 byte[] data = new byte[size];
					 in.read(data);
					 //����ͼƬ��Ϣ
					 response.setContentType("image/jpg");
					 out = response.getOutputStream();
					 out.write(data);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					if(in != null) {
						try {
							in.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(out != null) {
						try {
							out.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}else {
				
			}
			System.out.println(path);
		}
		}
				
		
	
}
