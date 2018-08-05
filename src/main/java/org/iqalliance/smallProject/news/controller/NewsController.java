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
		
		//选择加载信息的种类
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
		
		//从前端获取信息
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
		
		//封装成对象
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
			message = "信息保存失败";
			state = ERROR;
		}
		
		news.setDigest(digest);
		news.setTheme(theme);
		news.setKind(kind);
		
		System.out.println(news);
		
		boolean b = newsService.saveNews(news);
		
		if(b) {
			message = "提交成功";
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
			System.out.println("添加信息获赞请求中未获取id");
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
			System.out.println("添加信息查看次数请求中未获取id");
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
			System.out.println("添加信息查看次数请求中未获取id");
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
			System.out.println("添加信息查看次数请求中未获取id");
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
            //使用Apache文件上传组件处理文件上传步骤：
            //1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //设置工厂的缓冲区的大小，当上传的文件大小超过缓冲区的大小时，就会生成一个临时文件存放到指定的临时目录当中。
            factory.setSizeThreshold(1024*100);//设置缓冲区的大小为100KB，如果不指定，那么缓冲区的大小默认是10KB
            //设置上传时生成的临时文件的保存目录
//            factory.setRepository(fileDir);
            //2、创建一个文件上传解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
            //监听文件上传进度
            upload.setProgressListener(new ProgressListener(){
                public void update(long pBytesRead, long pContentLength, int arg2) {
                    System.out.println("文件大小为：" + pContentLength + ",当前已处理：" + pBytesRead);
         /**
          * 文件大小为：14608,当前已处理：4096
                        文件大小为：14608,当前已处理：7367
                        文件大小为：14608,当前已处理：11419
                        文件大小为：14608,当前已处理：14608
          */
                }
            });
             //解决上传文件名的中文乱码
            upload.setHeaderEncoding("UTF-8"); 
            //3、判断提交上来的数据是否是上传表单的数据
            if(!ServletFileUpload.isMultipartContent(request)){
                //按照传统方式获取数据
                return;
            }
            
            //设置上传单个文件的大小的最大值，目前是设置为1024*1024字节，也就是1MB
            upload.setFileSizeMax(1024*1024*1024);
            //设置上传文件总量的最大值，最大值=同时上传的多个文件的大小的最大值的和，目前设置为10MB
            upload.setSizeMax(1024*1024*1024);
            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = upload.parseRequest(request);
            for(FileItem item : list){
            	//为文件起名
            	Date date = new Date();
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            	String filename = sdf.format(date);
            	
            	File file = new File(absolutePath + filename + ".txt");
            	int index = 1;
            	while(file.exists()) {
            		file = new File(absolutePath + filename + "(" + index +")" + ".txt");
            		index++;
            	}
                //获取item中的上传文件的输入流
                InputStream in = item.getInputStream();
                //创建一个文件输出流
                FileOutputStream out = new FileOutputStream(file);
                //创建一个缓冲区
                byte buffer[] = new byte[1024];
                //判断输入流中的数据是否已经读完的标识
                int len = 0;
                //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                while((len=in.read(buffer))>0){
                    //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                    out.write(buffer, 0, len);
                }
                //关闭输入流
                in.close();
                //关闭输出流
                out.close();
                //删除处理文件上传时生成的临时文件
                //item.delete();
                
                message = "文件上传成功！";
                state = SUCCESS;
                
                //存储数据到数据库
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
            request.setAttribute("message", "单个文件超出最大值！！！");
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
            request.setAttribute("message", "上传文件的总的大小超出限制的最大值！！！");
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
        	message= "数据写入失败！";
            state = ERROR;
			e.printStackTrace();
		}catch (FileUploadException e) {
			// TODO Auto-generated catch block
			message= "网络异常！";
            state = ERROR;
			e.printStackTrace();
		} catch (Exception e) {
            message= "文件上传失败！";
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
