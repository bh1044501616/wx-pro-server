package org.iqalliance.smallProject.meeting.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.iqalliance.smallProject.common.web.JsonResult;
import org.iqalliance.smallProject.common.web.PageObject;
import org.iqalliance.smallProject.common.web.StaticValue;
import org.iqalliance.smallProject.meeting.entity.Meeting;
import org.iqalliance.smallProject.meeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/meeting")
public class MeetingController {
	
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	
	@Autowired
	private MeetingService meetingService;
	
	
//	@RequestMapping("/save")
//	@ResponseBody
//	public String doSaveObject() {
//		Meeting meeting = new Meeting();
//		meeting.setLecturerName("��ۿ��");
//		meeting.setMeetingTime(new Date());
//		meeting.setMeetingTheme("���Ǵ�����⣬������������");
//		meeting.setImageUrl("http://www.baidu.com");
//		meeting.setPptUrl("http://www.baidu.com");
//		meeting.setVideoUrl("http://www.baidu.com");
//		meetingService.saveMeetingInfo(meeting);
//		return JSON.toJSONString(new JsonResult());
//	}
	
	@RequestMapping("/load")
	@ResponseBody
	public JsonResult doLoadMeetingInfo(PageObject pageObject,HttpServletResponse response) {
		response.setContentType("application/json;charset=UTF-8");
		if(pageObject == null) {
			pageObject = new PageObject();
		}
		Map<String,Object> data = meetingService.loadMeetingInfo(pageObject);
		if(data.isEmpty()){
//			return JSON.toJSONString(new JsonResult(new RuntimeException("��ȡ��Ϣʧ��")));
			return new JsonResult(new RuntimeException("��ȡ��Ϣʧ��"));
		}
//		return JSON.toJSONString(new JsonResult(data));
		return new JsonResult(data);
	}
	
	@RequestMapping("/download")
	@ResponseBody
	public JsonResult doLoadDownloadList(HttpServletRequest request,PageObject pageObject) {
		
		String id = request.getParameter("id");
		String date = request.getParameter("date");
		//���¼���meetingInfov 
		pageObject.setPageSize(pageObject.getPageSize()%5==0?(pageObject.getPageSize()-5):(pageObject.getPageSize()/5)*5);
		Map<String,Object> data = meetingService.loadPPTS(pageObject,date,id);
		//��������json��ʽ���ظ�С����
		System.out.println(data);
		return new JsonResult(data);
	}
	
	@RequestMapping("/upload")
	public String uploadPage(HttpServletRequest request) {
		//��֤�Ƿ��½
		Cookie[] cookies = request.getCookies();
		if(cookies != null)
			for(int i=0;i<cookies.length;i++) {
				String name = cookies[i].getName();
				if("account".equals(name)) {
					return "upload";
				}
			}
		return "message";
	}
	
	@RequestMapping("/save")
	public String doUpload(HttpServletRequest request,HttpServletResponse response,String meetingTime) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		URL absolutePathUrl = getClass().getResource("/files"+ File.separator + "meeting");
		if(absolutePathUrl == null) {
			String absolutePath_ = getClass().getResource("/").toString().substring(6) + "files";
			System.out.println(absolutePath_);
			File files = new File(absolutePath_);
			if(!files.exists()) {
				files.mkdir();
			}
			File meeting = new File(absolutePath_ + File.separator + "meeting");
			if(!meeting.exists()) {
				meeting.mkdir();
			}
		}
		absolutePathUrl = getClass().getResource("/files"+ File.separator + "meeting");
		String absolutePath = absolutePathUrl.toString().substring(6);
		
		
		
		
		//��Ϣ��ʾ
        String message = "";
        String state = "";
        
		//����files/meeting�ļ����Ƿ���ڣ��������˵���ļ���û�Ҵ�
		File dir = new File(absolutePath);
		if(!(dir.exists()&&dir.isDirectory())) {
			message = "ϵͳ�����ļ��ϴ�ʧ��";
			state = ERROR;
		}
		//��ȡ�ϴ���Ϣ������    �����ļ���
//		String date = request.getParameter("meetingTime");
		
		File fileDir = null;
		
		//�洢����Ϣ�����ڴ������ݿ�
		Map<String,Object> data = new HashMap<String,Object>();
		
        try{
            //ʹ��Apache�ļ��ϴ���������ļ��ϴ����裺
            //1���� ��һ��DiskFileItemFactory����
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //���ù����Ļ������Ĵ�С�����ϴ����ļ���С�����������Ĵ�Сʱ���ͻ�����һ����ʱ�ļ���ŵ�ָ������ʱĿ¼���С�
            factory.setSizeThreshold(1024*100);//���û������Ĵ�СΪ100KB�������ָ������ô�������Ĵ�СĬ����10KB
            //�����ϴ�ʱ���ɵ���ʱ�ļ��ı���Ŀ¼ 
            factory.setRepository(fileDir);
            //2������һ���ļ��ϴ�������
            ServletFileUpload upload = new ServletFileUpload(factory);
            //�����ļ��ϴ�����
            upload.setProgressListener(new ProgressListener(){
                public void update(long pBytesRead, long pContentLength, int arg2) {
                    System.out.println("�ļ���СΪ��" + pContentLength + ",��ǰ�Ѵ���" + pBytesRead);
         /**
          * �ļ���СΪ��14608,��ǰ�Ѵ���4096
                        �ļ���СΪ��14608,��ǰ�Ѵ���7367
                        �ļ���СΪ��14608,��ǰ�Ѵ���11419
                        �ļ���СΪ��14608,��ǰ�Ѵ���14608
          */
                }
            });
             //����ϴ��ļ�������������
            upload.setHeaderEncoding("UTF-8"); 
            //3���ж��ύ�����������Ƿ����ϴ���������
            if(!ServletFileUpload.isMultipartContent(request)){
                //���մ�ͳ��ʽ��ȡ����
                return null;
            }
            
            //�����ϴ������ļ��Ĵ�С�����ֵ��Ŀǰ������Ϊ1024*1024*1024�ֽڣ�Ҳ����1GB
            upload.setFileSizeMax(1024*1024*1024);
            //�����ϴ��ļ����������ֵ�����ֵ=ͬʱ�ϴ��Ķ���ļ��Ĵ�С�����ֵ�ĺͣ�Ŀǰ����Ϊ10MB
            upload.setSizeMax(1024*1024*50);
            //4��ʹ��ServletFileUpload�����������ϴ����ݣ�����������ص���һ��List<FileItem>���ϣ�ÿһ��FileItem��Ӧһ��Form����������
            List<FileItem> list = upload.parseRequest(request);
            for(FileItem item : list){
                //���fileitem�з�װ������ͨ�����������
                if(item.isFormField()){
                    String name = item.getFieldName();
                  //�����ͨ����������ݵ�������������
                    String value = item.getString("UTF-8");
                    //value = new String(value.getBytes("iso8859-1"),"UTF-8");
                    if(name.equals("meetingTime")) {
                    	fileDir = new File(absolutePath + File.separator + value);
                    	if(!fileDir.exists()) {
                    		fileDir.mkdirs();
                    	}
                    }
                    data.put(name, value);
                    System.out.println(name + "=" + value);
                }else{//���fileitem�з�װ�����ϴ��ļ�
                    //�õ��ϴ����ļ����ƣ�
                	String fieldName = item.getFieldName();
                    String filename = item.getName();
                    System.out.println(filename);
                    if(filename==null || filename.trim().equals("")){
                        continue;
                    }
                    //ע�⣺��ͬ��������ύ���ļ����ǲ�һ���ģ���Щ������ύ�������ļ����Ǵ���·���ģ��磺  c:\a\b\1.txt������Щֻ�ǵ������ļ������磺1.txt
                    //�����ȡ�����ϴ��ļ����ļ�����·�����֣�ֻ�����ļ�������
                    String filePrimeName = filename.substring(filename.lastIndexOf("\\")+1,filename.lastIndexOf("."));
                    //�õ��ϴ��ļ�����չ��
                    String fileExtName = filename.substring(filename.lastIndexOf("."));
                    if("image".equals(fieldName)) {
                    	filePrimeName = "lecturer";
                    	fileExtName = ".jpg";
                    }
                    //��ȡ�洢�����ݵ��ļ���
                    String savedFileName = filePrimeName + fileExtName;
                    //�����Ҫ�����ϴ����ļ����ͣ���ô����ͨ���ļ�����չ�����ж��ϴ����ļ������Ƿ�Ϸ�
                    System.out.println("�ϴ����ļ�����չ���ǣ�"+fileExtName);
                    //��ȡitem�е��ϴ��ļ���������
                    InputStream in = item.getInputStream();
                    //����һ���ļ������
                    FileOutputStream out = new FileOutputStream(fileDir + File.separator + savedFileName);
                    //����һ��������
                    byte buffer[] = new byte[1024];
                    //�ж��������е������Ƿ��Ѿ�����ı�ʶ
                    int len = 0;
                    //ѭ�������������뵽���������У�(len=in.read(buffer))>0�ͱ�ʾin���滹������
                    while((len=in.read(buffer))>0){
                        //ʹ��FileOutputStream�������������������д�뵽ָ����Ŀ¼(savePath + "\\" + filename)����
                        out.write(buffer, 0, len);
                    }
                    //�ر�������
                    in.close();
                    //�ر������
                    out.close();
                    //ɾ�������ļ��ϴ�ʱ���ɵ���ʱ�ļ�
                    //item.delete();
                    
                    message = "�ļ��ϴ��ɹ���";
                    state = SUCCESS;
                }
            }
            //����Ϣ�������ݿ�
            Meeting meeting = new Meeting();
            meeting.setMeetingTheme((String)(data.get("meetingTheme")));
            meeting.setLecturerName((String)data.get("lecturerName"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateStr = (String)data.get("meetingTime");
            if(dateStr!=null&&!"".equals(dateStr)) {
            	Date date = sdf.parse(dateStr);
            	meeting.setMeetingTime(date);
            }
            meeting.setImageUrl(StaticValue.URL + "image/meeting.do?date="+dateStr+"&data=lecturer");
            meeting.setVideoUrl(StaticValue.URL + "image/meeting.do?date="+dateStr+"&data=lecturer");
            int i = meetingService.saveMeetingInfo(meeting);
            if(i != 1) {
            	state = "error";
            	message = "���ݴ洢ʧ��";
            }
        }catch (FileUploadBase.FileSizeLimitExceededException e) {
            e.printStackTrace();
            request.setAttribute("message", "�����ļ��������ֵ������");
            request.setAttribute("state",ERROR);
            try {
				request.getRequestDispatcher("/WEB-INF/pages/message.jsp").forward(request, response);
			} catch (ServletException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            return null;
        }catch (FileUploadBase.SizeLimitExceededException e) {
            e.printStackTrace();
            request.setAttribute("message", "�ϴ��ļ����ܵĴ�С�������Ƶ����ֵ������");
            request.setAttribute("state",ERROR);
            try {
				request.getRequestDispatcher("/WEB-INF/pages/message.jsp").forward(request, response);
			} catch (ServletException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            return null;
        }
         catch (IOException e) {
			// TODO Auto-generated catch block
        	message= "ϵͳ�쳣��";
            state = ERROR;
			e.printStackTrace();
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			message= "�����쳣��";
            state = ERROR;
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			message= "�ļ����ʹ���";
            state = ERROR;
			e.printStackTrace();
		}catch (Exception e) {
            message= "�ļ��ϴ�ʧ�ܣ�";
            state = ERROR;
            e.printStackTrace();
        }
        request.setAttribute("message",message);
        request.setAttribute("state",state);
        try {
			request.getRequestDispatcher("/WEB-INF/pages/message.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/video")
	/*
	 * ���ڸ�ģ������Ƶ���Ų���
	 * 
	 * ��ͬ�������ļ�������Դ���ù��ܵ�����url��ǰ׺Ϊmeeting�����ڸ��ĵĵ�����
	 * 
	 * ����url��ʽΪ��http://localhost:8080/smallProject/meeting/video?date=(date)
	 * 	��Ϊÿ���е���Ƶֻ���ܳ���һ����������Ƶ��Ϣ��Ҫһ��date�Ϳ���
	 */
	public void doPlayVideo(HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Content-Type","video/mp4");
		
		//��ȡ����·��
		String absolutePath = getClass().getResource("/files/meeting").toString().substring(6);
		//��ȡ��������
		String date = request.getParameter("date");
		//��ȡ�ļ����ڵ��ļ���
		String path = absolutePath + date;
		System.out.println(path);
		//�ļ��е�ʵ�����
		File dir = new File(path);
		File files[] = null; 
		if(dir.exists()&&dir.isDirectory()) {
			//���ļ��д���ʱ������ʱ�ļ���ʱ������Ƶ�Ĳ���
			files = dir.listFiles(new FileFilter() {
				
				public boolean accept(File pathname) {
					//��ȡ�ļ�������
					String fileName = pathname.getName();
					//��ȡ�ļ������ͣ���׺����
					String fileExtName = fileName.substring(fileName.lastIndexOf(".")+1);
					
					if(fileExtName.matches("mp4")) {
						return true;
					}
					return false;
				}
			});
		}
		
		if(files != null&&files.length == 1) {
			System.out.println("���ļ�");
			//�жϲ��ҵ�����Ƶ�ļ��Ƿ�Ϊ1��,��Ϊһ��������ȷ����
			
			  
			//������
			FileInputStream in = null;
			OutputStream out = null;
			
			try {
				in = new FileInputStream(files[0]);
				out = response.getOutputStream();
				
				//��������
				byte[] data = new byte[1024];
				while(in.read(data) != -1) {
					System.out.println(data);
					out.write(data);
					out.flush();
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					if(in != null)
						in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(out != null)
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}
}
