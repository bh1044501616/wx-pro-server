package org.iqalliance.smallProject.schedule.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.iqalliance.smallProject.common.entity.Download;
import org.iqalliance.smallProject.common.entity.Image;
import org.iqalliance.smallProject.common.web.JsonResult;
import org.iqalliance.smallProject.schedule.entity.Lecture;
import org.iqalliance.smallProject.schedule.entity.Lecturer;
import org.iqalliance.smallProject.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {
	
	@Autowired
	private ScheduleService scheduleService;
	
	@RequestMapping("/info/{dateStr}")
	public void doGetObjectsByDate(@PathVariable String dateStr,HttpServletRequest request,HttpServletResponse response) {
		response.setContentType("application/json;charset=utf-8");
		List data = null;
		JsonResult jsonResult = null;
		try {
			Date date = new SimpleDateFormat("yyyyMMdd").parse(dateStr);
			data = scheduleService.getSchedulesByDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
			jsonResult = new JsonResult(e);
		}
		if(data == null)
			jsonResult = new JsonResult(new Exception());
		jsonResult = new JsonResult(data);
		String json = JSON.toJSONStringWithDateFormat(jsonResult, "HH:mm");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.println(json);
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(pw != null)
				pw.close();
		}
	}
	
	
	@RequestMapping("/detail")
	@ResponseBody
	public JsonResult doGetDetailById(HttpServletRequest request) {
		String idsStr = request.getParameter("ids");
		
		if(idsStr == null) {
			return new JsonResult(new Exception());
		}
		JSONArray jsonObj = JSON.parseArray(idsStr);
		int[] ids = new int[jsonObj.size()];
		for(int i=0;i<ids.length;i++) {
			ids[i] = jsonObj.getInteger(i);
		}
		Map data = scheduleService.getLecturerInfoById(ids);
		return new JsonResult(data);  
	}
	
	
	@RequestMapping("/savePPT")
	@ResponseBody
	public JsonResult doSaveDownloadObject(HttpServletRequest request) {
		String message = "";
		
		//创建文件工厂对象
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//设置缓冲流大小（当超过该大小后，文件就会被存储到一个临时存放地址）
		factory.setSizeThreshold(1024000000);
		//创建一个文件上传解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		//解决上传文件名字的中文乱码问题
		upload.setHeaderEncoding("UTF-8");
		
		//设置单个文件的最大大小
		upload.setSizeMax(1024000000);
		//设置所有文件的最大大小
		upload.setFileSizeMax(1024000000);
		
		//用于存储普通表单信息的数据
		Map<String,Object> map = new HashMap<String,Object>();
		//验证信号
		int flag = -1;
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
					
					if(name == null || "".equals(name)) {
						return new JsonResult("未传入文件！");
					}
					int lid = Integer.parseInt(name.substring(2,3));
					int cid = Integer.parseInt(name.substring(0,1));
					
					
					//设置文件的存储位置
					String absolutePath = getClass().getResource("/files/schedule/ppt").toString().substring(6);
					//把文件存储到本地
					InputStream in = item.getInputStream();
					byte[] data = new byte[1024*1024];
					
					File dir = new File(absolutePath + cid);
					System.out.println(dir.getAbsolutePath());
					if(!dir.exists() || !dir.isDirectory()) {
						dir.mkdir();
					}
					File file = new File(absolutePath + cid + File.separator + name.substring(3));
					System.out.println(file.getAbsolutePath());
					
					FileOutputStream fos = new FileOutputStream(file);
					
					int index = -1;
					while((index = in.read(data)) != -1) {
						fos.write(data);
					}
					
					//将保存到本地的数据的地址存到数据库
					Download download = new Download();
					download.setPath(file.getAbsolutePath());
					download.setHashcode(download.hashCode() + "");
					
					Lecture lecture = new Lecture();
					lecture.setCid(cid);
					lecture.setLid(lid);
					lecture.setPptName(name.substring(3));
					
					flag = scheduleService.savePPT(download, lecture);
					
					if(flag == -1) {
						return new JsonResult("表单中未传入信息！");
					}
					if(flag == 0) {
						return new JsonResult("下载文件信息存储失败！");
					}
					if(flag == 1) {
						return new JsonResult("ppt信息存储失败！");
					}
				}
			}
			
		} catch (FileUploadException e) {
			message = "网络出现故障";
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			message = "服务出错";
			e.printStackTrace();
		} catch (IOException e) {
			message = "网络出现故障";
			e.printStackTrace();
		} catch(NumberFormatException n) {
			message = "文件名称格式不正确！";
		}
		
		if("".equals(message)) {
			return new JsonResult();
		}else {
			return new JsonResult(message);
		}
	}
	
	@RequestMapping("/saveppt")
	public String doSavePPT() {
		return "uploadPPT";
	}
	
	@RequestMapping("/savePic")
	@ResponseBody
	/*
	 * 将演讲人图片上传到本地，然后将图片放到服务器图片服务上。
	 * 更改lecturer的照片信息
	 */
	public JsonResult doSaveImageObject(HttpServletRequest request) {
		String message = "";
		
		//创建文件工厂对象
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//设置缓冲流大小（当超过该大小后，文件就会被存储到一个临时存放地址）
		factory.setSizeThreshold(1024000000);
		//创建一个文件上传解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		//解决上传文件名字的中文乱码问题
		upload.setHeaderEncoding("UTF-8");
		
		//设置单个文件的最大大小
		upload.setSizeMax(1024000000);
		//设置所有文件的最大大小
		upload.setFileSizeMax(1024000000);
		
		//用于存储普通表单信息的数据
		Map<String,Object> map = new HashMap<String,Object>();
		//验证信号
		int flag = -1;
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
					
					if(name == null || "".equals(name)) {
						return new JsonResult("未传入文件！");
					}
					
					
					//设置文件的存储位置
					String absolutePath = getClass().getResource("/files/schedule/pic").toString().substring(6);
					//把文件存储到本地
					InputStream in = item.getInputStream();
					byte[] data = new byte[1024*1024];
					
					File dir = new File(absolutePath);
					if(!dir.exists() || !dir.isDirectory()) {
						dir.mkdir();
					}
					File file = new File(absolutePath + File.separator + name);
					
					FileOutputStream fos = new FileOutputStream(file);
					
					int index = -1;
					while((index = in.read(data)) != -1) {
						fos.write(data);
					}
					
					//将保存到本地的数据的地址存到数据库
					Image image = new Image();
					image.setPath(file.getAbsolutePath());
					image.setHashcode(image.hashCode() + "");
					
					Lecturer lecturer = new Lecturer();
					lecturer.setName(name.substring(0, name.lastIndexOf(".")));
					
					flag = scheduleService.savePic(image, lecturer);
					
					if(flag == -1) {
						return new JsonResult("表单中未传入信息！");
					}
					if(flag == 0) {
						return new JsonResult("下载文件信息存储失败！");
					}
					if(flag == 1) {
						return new JsonResult("pic信息存储失败！");
					}
				}
			}
			
		} catch (FileUploadException e) {
			message = "网络出现故障";
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			message = "服务出错";
			e.printStackTrace();
		} catch (IOException e) {
			message = "网络出现故障";
			e.printStackTrace();
		} catch(NumberFormatException n) {
			message = "文件名称格式不正确！";
		}
		
		if("".equals(message)) {
			return new JsonResult();
		}else {
			return new JsonResult(message);
		}
	}
	
	@RequestMapping("/savepic")
	public String doSavePic() {
		return "uploadPic";
	}
}
