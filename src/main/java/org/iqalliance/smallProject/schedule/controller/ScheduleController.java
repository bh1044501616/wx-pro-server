package org.iqalliance.smallProject.schedule.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.iqalliance.smallProject.common.entity.Download;
import org.iqalliance.smallProject.common.entity.Image;
import org.iqalliance.smallProject.common.web.JsonResult;
import org.iqalliance.smallProject.common.web.StaticValue;
import org.iqalliance.smallProject.schedule.entity.Lecture;
import org.iqalliance.smallProject.schedule.entity.Lecturer;
import org.iqalliance.smallProject.schedule.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	public static Logger LOGGER = LoggerFactory.getLogger(ScheduleController.class.getName());
	
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
		
		HttpSession session = request.getSession(false);
		if(session == null) {
			return new JsonResult("ҳ�����");
		}
		
		String message = "";
		
		//�����ļ���������
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//���û�������С���������ô�С���ļ��ͻᱻ�洢��һ����ʱ��ŵ�ַ��
		factory.setSizeThreshold(1024000000);
		//����һ���ļ��ϴ�������
		ServletFileUpload upload = new ServletFileUpload(factory);
		//����ϴ��ļ����ֵ�������������
		upload.setHeaderEncoding("UTF-8");
		
		//���õ����ļ�������С
		upload.setSizeMax(1024000000);
		//���������ļ�������С
		upload.setFileSizeMax(1024000000);
		
		//���ڴ洢��ͨ����Ϣ������
		Map<String,Object> map = new HashMap<String,Object>();
		//��֤�ź�
		int flag = -1;
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
					
					if(name == null || "".equals(name)) {
						return new JsonResult("δ�����ļ���");
					}
					int lid = Integer.parseInt(name.substring(2,3));
					int cid = Integer.parseInt(name.substring(0,1));
					
					
					//�����ļ��Ĵ洢λ��
					String absolutePath = getClass().getResource("/files/schedule/ppt").toString().substring(6);
					//���ļ��洢������
					InputStream in = item.getInputStream();
					byte[] data = new byte[1024*1024];
					
					absolutePath = absolutePath + cid;
					File dir = new File(StaticValue.FILE_PATH + "schedule/ppt/" + cid);
					LOGGER.info("��ȡ�洢Ŀ¼�ľ��Ե�ַ" + dir.getAbsolutePath());
					if(!dir.exists() || !dir.isDirectory()) {
						boolean dir_created = dir.mkdir();
						if(dir_created) {
							LOGGER.info("�ļ����Ѵ����ɹ�");
						}else {
							LOGGER.info("�ļ��д���ʧ��");
						}
					}
					//���ļ��洢·����������ĿĿ¼�⣬Ϊ��ֹ���²�����Ŀ�����ļ����ļ���ʧ������Ĵ���pic�ķ���һ�£�
					File file = new File(StaticValue.FILE_PATH + "schedule/ppt/" + cid + File.separator + name.substring(3));
					LOGGER.info("��ȡ�洢Ŀ¼�ľ��Ե�ַ" + file.getAbsolutePath());
					if(!file.exists()) {
						boolean file_created = file.createNewFile();
						if(file_created) {
							LOGGER.info("�ļ��Ѵ����ɹ�");
						}else {
							LOGGER.info("�ļ�����ʧ��");
						}
					}
					FileOutputStream fos = new FileOutputStream(file);
					
					int index = -1;
					while((index = in.read(data)) != -1) {
						fos.write(data);
					}
					
					//�����浽���ص����ݵĵ�ַ�浽���ݿ�
					Download download = new Download();
					download.setPath(file.getAbsolutePath());
					download.setHashcode(download.hashCode() + "");
					
					Lecture lecture = new Lecture();
					lecture.setCid(cid);
					lecture.setLid(lid);
					lecture.setPptName(name.substring(3));
					
					flag = scheduleService.savePPT(download, lecture);
					
					if(flag == -1) {
						return new JsonResult("����δ������Ϣ��");
					}
					if(flag == 0) {
						return new JsonResult("�����ļ���Ϣ�洢ʧ�ܣ�");
					}
					if(flag == 1) {
						return new JsonResult("ppt��Ϣ�洢ʧ�ܣ�");
					}
				}
			}
			
		} catch (FileUploadException e) {
			message = "������ֹ���";
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw); 
			e.printStackTrace(pw);
			LOGGER.error(sw.toString());
		} catch (UnsupportedEncodingException e) {
			message = "�������";
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw); 
			e.printStackTrace(pw);
			LOGGER.error(sw.toString());
		} catch (IOException e) {
			message = "������ֹ���";
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw); 
			e.printStackTrace(pw);
			LOGGER.error(sw.toString());
		} catch(NumberFormatException n) {
			message = "�ļ����Ƹ�ʽ����ȷ��";
			LOGGER.error(message);
		}
		
		if("".equals(message)) {
			return new JsonResult();
		}else {
			return new JsonResult(message);
		}
	}
	
	@RequestMapping("/saveppt")
	public String doSavePPT(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session != null) {
			return "uploadPPT";
		}else {
			return "message";
		}
	}
	
	@RequestMapping("/savePic")
	@ResponseBody
	/*
	 * ���ݽ���ͼƬ�ϴ������أ�Ȼ��ͼƬ�ŵ�������ͼƬ�����ϡ�
	 * ����lecturer����Ƭ��Ϣ
	 */
	public JsonResult doSaveImageObject(HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		if(session == null) {
			return new JsonResult("ҳ�����");
		}
		
		String message = "";
		
		//�����ļ���������
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//���û�������С���������ô�С���ļ��ͻᱻ�洢��һ����ʱ��ŵ�ַ��
		factory.setSizeThreshold(1024000000);
		//����һ���ļ��ϴ�������
		ServletFileUpload upload = new ServletFileUpload(factory);
		//����ϴ��ļ����ֵ�������������
		upload.setHeaderEncoding("UTF-8");
		
		//���õ����ļ�������С
		upload.setSizeMax(1024000000);
		//���������ļ�������С
		upload.setFileSizeMax(1024000000);
		
		//���ڴ洢��ͨ����Ϣ������
		Map<String,Object> map = new HashMap<String,Object>();
		//��֤�ź�
		int flag = -1;
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
					
					if(name == null || "".equals(name)) {
						return new JsonResult("δ�����ļ���");
					}
					
					
					//�����ļ��Ĵ洢λ��
					String absolutePath = getClass().getResource("/files/schedule/pic").toString().substring(6);
					//���ļ��洢������
					InputStream in = item.getInputStream();
					byte[] data = new byte[1024*1024];
					
					File dir = new File(StaticValue.FILE_PATH + "schedule/pic");
					if(!dir.exists() || !dir.isDirectory()) {
						dir.mkdir();
					}
					File file = new File(StaticValue.FILE_PATH + "schedule/pic" + File.separator + name);
					if(!file.exists()) {
						file.createNewFile();
					}
					FileOutputStream fos = new FileOutputStream(file);
					
					int index = -1;
					while((index = in.read(data)) != -1) {
						fos.write(data);
					}
					
					//�����浽���ص����ݵĵ�ַ�浽���ݿ�
					Image image = new Image();
					image.setPath(file.getAbsolutePath());
					image.setHashcode(image.hashCode() + "");
					
					Lecturer lecturer = new Lecturer();
					lecturer.setName(name.substring(0, name.lastIndexOf(".")));
					
					flag = scheduleService.savePic(image, lecturer);
					
					if(flag == -1) {
						return new JsonResult("����δ������Ϣ��");
					}
					if(flag == 0) {
						return new JsonResult("�����ļ���Ϣ�洢ʧ�ܣ�");
					}
					if(flag == 1) {
						return new JsonResult("pic��Ϣ�洢ʧ�ܣ�");
					}
				}
			}
			
		} catch (FileUploadException e) {
			message = "������ֹ���";
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			message = "�������";
			e.printStackTrace();
		} catch (IOException e) {
			message = "������ֹ���";
			e.printStackTrace();
		} catch(NumberFormatException n) {
			message = "�ļ����Ƹ�ʽ����ȷ��";
		}
		
		if("".equals(message)) {
			return new JsonResult();
		}else {
			return new JsonResult(message);
		}
	}
	
	@RequestMapping("/savepic")
	public String doSavePic(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session != null) {
				return "uploadPic";
		}else {
			return "message";
		}
	}
	
	@RequestMapping("/load")
	public String doLoadOnBrower() {
		return "downloadPPT";
	}
}
