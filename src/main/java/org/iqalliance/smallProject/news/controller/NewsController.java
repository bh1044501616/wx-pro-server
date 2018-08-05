package org.iqalliance.smallProject.news.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.iqalliance.smallProject.common.service.WxCrawler;
import org.iqalliance.smallProject.common.web.JsonResult;
import org.iqalliance.smallProject.common.web.PageObject;
import org.iqalliance.smallProject.common.web.StaticValue;
import org.iqalliance.smallProject.meeting.controller.MeetingController;
import org.iqalliance.smallProject.meeting.entity.Meeting;
import org.iqalliance.smallProject.news.entity.News;
import org.iqalliance.smallProject.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/news")
public class NewsController {
	
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	
	@Autowired
	private NewsService newsService;
	
	@RequestMapping("/load")
	public void doLoad(PageObject pageObject,HttpServletResponse response,HttpServletRequest request) {
		response.setContentType("application/json;charset=UTF-8");
		
		//ѡ�������Ϣ������
		String idStr = request.getParameter("id");
		Integer kind = null;
		try {
			kind = Integer.parseInt(idStr);
		}catch(NumberFormatException n) {
			kind = 0;
		}
		List<News> list = newsService.getPageNews(pageObject,kind);
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("pageObject", pageObject);
		data.put("list", list);
		
		JsonResult jr = new JsonResult(data);
		
		JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd";
		String jsonStr = JSON.toJSONStringWithDateFormat(jr, "yyyy-MM-dd");
		
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.write(jsonStr);
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(pw != null) {
				pw.close();
			}
		}
		
	}
	
	@RequestMapping("/upload")
	public String doUpload(News news) {
		return "submit";
	}
	
	@RequestMapping("/save")
	public String doSaveObjects(News news,HttpServletRequest request,HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		final String SUCCESS = "success";
		final String ERROR = "error";
		
		String message = "";
		String state = "";
		
		//��ǰ�˻�ȡ��Ϣ
		String theme = null;
		String time = null;
		String cover = null;
		String detailUrl = null;
		String digest = null;
		int kind = 0;
		try {
			theme = new String(request.getParameter("theme").getBytes("ISO8859-1"),"UTF-8");
			time = new String(request.getParameter("time").getBytes("ISO8859-1"),"UTF-8");
			cover = new String(request.getParameter("cover").getBytes("ISO8859-1"),"UTF-8");
			detailUrl = new String(request.getParameter("detailUrl").getBytes("ISO8859-1"),"UTF-8");
			digest = new String(request.getParameter("digest").getBytes("ISO8859-1"),"UTF-8");
			String kindStr = new String(request.getParameter("kind").getBytes("ISO8859-1"),"UTF-8");
			kind = Integer.parseInt(kindStr);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//��װ�ɶ���
		news.setCover(cover);
		news.setDetailUrl(detailUrl);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); 
		System.out.println(sdf);
		try {
			Date date = sdf.parse(time);
			news.setTime(date);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			message = "��Ϣ����ʧ��";
			state = ERROR;
		}
		
		news.setDigest(digest);
		news.setTheme(theme);
		news.setKind(kind);
		
		System.out.println(news);
		
		boolean b = newsService.saveNews(news);
		
		if(b) {
			message = "�ύ�ɹ�";
			state = SUCCESS;
		}
		
		request.setAttribute("message", message);
		request.setAttribute("state", state);
		
		try {
			request.getRequestDispatcher("/WEB-INF/pages/submitResult.jsp").forward(request,response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RequestMapping("/addPraise")
	@ResponseBody
	public JsonResult doAddPraise(HttpServletRequest request) {
		String idStr = request.getParameter("id");
		int id = 0;
		try {
			id = Integer.parseInt(idStr);
			newsService.addPraise(id);
		}catch(NumberFormatException n) {
			System.out.println("�����Ϣ����������δ��ȡid");
		}
		return new JsonResult();
	}
	
	@RequestMapping("/addWatch")
	@ResponseBody
	public JsonResult doAddWatch(HttpServletRequest request) {
		String idStr = request.getParameter("id");
		int id = 0;
		try {
			id = Integer.parseInt(idStr);
			newsService.addWatch(id);
		}catch(NumberFormatException n) {
			System.out.println("�����Ϣ�鿴����������δ��ȡid");
		}
		return new JsonResult();
	}
	
	@RequestMapping("/getWatch")
	@ResponseBody
	public int doGetWatch(HttpServletRequest request) {
		String idStr = request.getParameter("id");
		int id = 0;
		int watch = 0;
		try {
			id = Integer.parseInt(idStr);
			watch = newsService.getWatch(id);
		}catch(NumberFormatException n) {
			System.out.println("�����Ϣ�鿴����������δ��ȡid");
		}
		return watch;
	}
	@RequestMapping("/getPraise")
	@ResponseBody
	public int doGetPraise(HttpServletRequest request) {
		String idStr = request.getParameter("id");
		int id = 0;
		int praise = 0;
		try {
			id = Integer.parseInt(idStr);
			praise = newsService.getPraise(id);
		}catch(NumberFormatException n) {
			System.out.println("�����Ϣ�鿴����������δ��ȡid");
		}
		return praise;
	}
	
	
	@RequestMapping("uploadCrawler")
	public void doUploadCrawler(HttpServletRequest request,HttpServletResponse response) {
		
		String message = "";
		String state = "";
		
		String absolutePath = getClass().getResource("/files"+File.separator+"news").toString().substring(6);
		System.out.println(absolutePath);
		File fileDir = new File(absolutePath);
		
		try{
            //ʹ��Apache�ļ��ϴ���������ļ��ϴ����裺
            //1������һ��DiskFileItemFactory����
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //���ù����Ļ������Ĵ�С�����ϴ����ļ���С�����������Ĵ�Сʱ���ͻ�����һ����ʱ�ļ���ŵ�ָ������ʱĿ¼���С�
            factory.setSizeThreshold(1024*100);//���û������Ĵ�СΪ100KB�������ָ������ô�������Ĵ�СĬ����10KB
            //�����ϴ�ʱ���ɵ���ʱ�ļ��ı���Ŀ¼
//            factory.setRepository(fileDir);
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
                return;
            }
            
            //�����ϴ������ļ��Ĵ�С�����ֵ��Ŀǰ������Ϊ1024*1024�ֽڣ�Ҳ����1MB
            upload.setFileSizeMax(1024*1024*1024);
            //�����ϴ��ļ����������ֵ�����ֵ=ͬʱ�ϴ��Ķ���ļ��Ĵ�С�����ֵ�ĺͣ�Ŀǰ����Ϊ10MB
            upload.setSizeMax(1024*1024*1024);
            //4��ʹ��ServletFileUpload�����������ϴ����ݣ�����������ص���һ��List<FileItem>���ϣ�ÿһ��FileItem��Ӧһ��Form����������
            List<FileItem> list = upload.parseRequest(request);
            for(FileItem item : list){
            	//Ϊ�ļ�����
            	Date date = new Date();
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            	String filename = sdf.format(date);
            	
            	File file = new File(absolutePath + filename + ".txt");
            	int index = 1;
            	while(file.exists()) {
            		file = new File(absolutePath + filename + "(" + index +")" + ".txt");
            		index++;
            	}
                //��ȡitem�е��ϴ��ļ���������
                InputStream in = item.getInputStream();
                //����һ���ļ������
                FileOutputStream out = new FileOutputStream(file);
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
                
                //�洢���ݵ����ݿ�
                WxCrawler wxCrawler = new WxCrawler();
                String list1 = wxCrawler.getJsonList(file);
                
                JSONArray ja = JSON.parseArray(list1);
    			for(int i=0;i<ja.size();i++) {
    				JSONObject jo = ja.getJSONObject(i);
    				
    				News news = new News();
    				
    				news.setCover(jo.getString("cover"));
    				news.setDetailUrl(jo.getString("detailUrl"));
    				news.setDigest(jo.getString("digest"));
    				news.setTheme(jo.getString("theme"));
    				news.setTime(jo.getDate("time"));
    				
    				Integer kind = jo.getIntValue("kind");
    				if(kind != null) {
    					news.setKind(kind);
    				}
    				
    				if(! news.hasNull()) {
    					newsService.saveNews(news);
    				}
    			}
    			
    			
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
        }catch (IOException e) {
			// TODO Auto-generated catch block
        	message= "����д��ʧ�ܣ�";
            state = ERROR;
			e.printStackTrace();
		}catch (FileUploadException e) {
			// TODO Auto-generated catch block
			message= "�����쳣��";
            state = ERROR;
			e.printStackTrace();
		} catch (Exception e) {
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
	}
	
	
}
