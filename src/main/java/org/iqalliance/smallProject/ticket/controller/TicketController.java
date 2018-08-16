package org.iqalliance.smallProject.ticket.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
import org.iqalliance.smallProject.common.web.StaticValue;
import org.iqalliance.smallProject.ticket.entity.Ticket;
import org.iqalliance.smallProject.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@RequestMapping("/ticket")
@Controller
public class TicketController {
	
	private final String LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=wx5815e99cfb706cd3&secret=7f87c49cb08298e9ffa355601b1525a8&grant_type=authorization_code&js_code=";
	
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
	public JsonResult doLoginIn(String phone,String wx_account,String password) {
		if(wx_account != null) {
			Ticket ticket = ticketService.getAccountInfo(wx_account);
			return new JsonResult(ticket);
		}else {
			int flag = ticketService.checkPwd(phone, password);
			if(flag == 1) {
				Ticket ticket = ticketService.getAccountInfo(phone);
				String qrCode = ticket.getQrCode();
				if(qrCode == null || "".equals(qrCode)) {
					ticket.setQrCode(StaticValue.URL + "image/1243171897.png");
				}
				return new JsonResult(ticket);
			}else if(flag == 0) {
				return new JsonResult("密码错误！");
			}else {
				return new JsonResult("账号不存在！");
			}
		}
	}
	
	/*
	 * 检测二维码真实性
	 */
	@RequestMapping("pay")
	@ResponseBody
	public JsonResult doCheckMethod(Ticket ticket,String checkCode) {
		
		int flag = 0;
		String phone = ticket.getPhone();
		if( checkCode != null && !"".equals(checkCode) && "haoshaowen".equals(checkCode)) {
			if(phone != null && !"".equals(phone)) {
				//获得手机号信息
				flag = ticketService.pay(ticket);
			}
		}
		if(flag == 1) {
			return new JsonResult();
		} else{
			return new JsonResult("支付失败，请稍后重试！");
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
	
}
