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
	 * 注册门票的信息
	 */
	@RequestMapping("/signin")
	@ResponseBody
	public JsonResult saveObject(Ticket ticket) {
		System.out.println(ticket);
		int flag = ticketService.saveTicket(ticket);
		if(flag == 0) {
			return new JsonResult("网络繁忙，请稍后");
		}else if(flag == -1){
			return new JsonResult("手机号已经被注册！");
		}else {
			return new JsonResult();
		}
	}
	
	/*
	 * 登陆账号
	 */
	@RequestMapping("/loginin")
	@ResponseBody
	public JsonResult doLoginIn(String phone,String wx_account,String password,HttpServletRequest request) {
		LOGGER.info("登陆ticket账户服务");
		if(wx_account != null) {
			LOGGER.info("微信登陆");
			Ticket ticket = ticketService.getAccountInfo(wx_account);
			return new JsonResult(ticket);
		}else {
			LOGGER.info("账号密码登陆");
			int flag = ticketService.checkPwd(phone, password);
			if(flag == 1) {
				LOGGER.info("账号密码验证成功");
				
				Ticket ticket = ticketService.getAccountInfo(phone);
				String qrCode = ticket.getQrCode();
				if(qrCode == null || "".equals(qrCode)) {
					ticket.setQrCode(StaticValue.URL + "image/1243171897.png");
				}
				return new JsonResult(ticket);
			}else if(flag == 0) {
				LOGGER.info("验证失败：密码错误");
				return new JsonResult("密码错误！");
			}else {
				LOGGER.info("验证失败：账号不存在");
				return new JsonResult("账号不存在！");
			}
		}
	}
	
	/*
	 * 检测二维码真实性
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
				return new JsonResult("账号不存在！");
			}
			if( checkCode != null && !"".equals(checkCode) && "haoshaowen".equals(checkCode)) {
				if(phone != null && !"".equals(phone)) {
					//获得手机号信息
					flag = ticketService.pay(ticket);
				}
			}
			if(flag == 1) {
				return new JsonResult();
			}else {
				return new JsonResult("账号已支付或者系统出错，请稍后重试！");
			}
		}else {
			return new JsonResult("页面错误!");
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
			return new JsonResult("二维码无效！");
		}
		return new JsonResult();
		/*//创建文件工厂对象
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//设置缓冲流大小（当超过该大小后，文件就会被存储到一个临时存放地址）
		factory.setSizeThreshold(1024*10);
		//创建一个文件上传解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		//解决上传文件名字的中文乱码问题
		upload.setHeaderEncoding("UTF-8");
		
		//设置单个文件的最大大小
		upload.setSizeMax(1024*10);
		//设置所有文件的最大大小
		upload.setFileSizeMax(1024*10);
		
		//用于存储普通表单信息的数据
		Map<String,Object> map = new HashMap<String,Object>();
		//验证信号
		int flag = 0;
		try {
			//获取请求消息头获取到的 参数列表
			List<FileItem> items = upload.parseRequest(request);
			for(FileItem item:items) {
				if(item.isFormField()) {
					//如果表单中封装的是普通数据项
					String name = item.getName();
					String value = item.getString("UTF-8");
					
					map.put(name, value);
				}else {
					//当为文件项时
					String name = item.getName();
					if("qrCode".equals(name)) {
						InputStream in = item.getInputStream();
						String result = MyQRCode.readQrCode(in);
						
						map.put(name, result);
					}
				}
			}
			
			//验证二维码信息是否正确
			 flag = ticketService.consumePay(map);
		} catch (FileUploadException e) {
			message = "网络出现故障";
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			message = "服务出错";
			e.printStackTrace();
		} catch (IOException e) {
			message = "网络出现故障";
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
		LOGGER.info("获取二维码服务");
		//验证登陆状态
		
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		
		LOGGER.info("验证账号密码");
		int flag = -1;
		if(account != null && !"".equals(account) && password != null && !"".equals(password)) {
			flag = ticketService.checkPwd(account, password);
		}
		if(flag == 1) {
			LOGGER.info("验证密码成功，返回图片信息");
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
					 //发送图片信息
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
