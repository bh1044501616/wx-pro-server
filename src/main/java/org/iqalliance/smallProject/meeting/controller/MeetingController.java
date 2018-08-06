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
//		meeting.setLecturerName("郝劭文");
//		meeting.setMeetingTime(new Date());
//		meeting.setMeetingTheme("这是大会主题，哈哈哈哈哈哈");
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
//			return JSON.toJSONString(new JsonResult(new RuntimeException("获取信息失败")));
			return new JsonResult(new RuntimeException("获取信息失败"));
		}
//		return JSON.toJSONString(new JsonResult(data));
		return new JsonResult(data);
	}
	
	@RequestMapping("/download")
	@ResponseBody
	public JsonResult doLoadDownloadList(HttpServletRequest request,PageObject pageObject) {
		
		String id = request.getParameter("id");
		String date = request.getParameter("date");
		//重新加载meetingInfov 
		pageObject.setPageSize(pageObject.getPageSize()%5==0?(pageObject.getPageSize()-5):(pageObject.getPageSize()/5)*5);
		Map<String,Object> data = meetingService.loadPPTS(pageObject,date,id);
		//将数据以json形式返回给小程序
		System.out.println(data);
		return new JsonResult(data);
	}
	
	@RequestMapping("/upload")
	public String uploadPage(HttpServletRequest request) {
		//验证是否登陆
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
		
		
		
		
		//消息提示
        String message = "";
        String state = "";
        
		//查找files/meeting文件夹是否存在，如果存在说明文件夹没找错
		File dir = new File(absolutePath);
		if(!(dir.exists()&&dir.isDirectory())) {
			message = "系统错误，文件上传失败";
			state = ERROR;
		}
		//获取上传信息的日期    创建文件夹
//		String date = request.getParameter("meetingTime");
		
		File fileDir = null;
		
		//存储表单信息，用于存入数据库
		Map<String,Object> data = new HashMap<String,Object>();
		
        try{
            //使用Apache文件上传组件处理文件上传步骤：
            //1、创 建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //设置工厂的缓冲区的大小，当上传的文件大小超过缓冲区的大小时，就会生成一个临时文件存放到指定的临时目录当中。
            factory.setSizeThreshold(1024*100);//设置缓冲区的大小为100KB，如果不指定，那么缓冲区的大小默认是10KB
            //设置上传时生成的临时文件的保存目录 
            factory.setRepository(fileDir);
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
                return null;
            }
            
            //设置上传单个文件的大小的最大值，目前是设置为1024*1024*1024字节，也就是1GB
            upload.setFileSizeMax(1024*1024*1024);
            //设置上传文件总量的最大值，最大值=同时上传的多个文件的大小的最大值的和，目前设置为10MB
            upload.setSizeMax(1024*1024*50);
            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = upload.parseRequest(request);
            for(FileItem item : list){
                //如果fileitem中封装的是普通输入项的数据
                if(item.isFormField()){
                    String name = item.getFieldName();
                  //解决普通输入项的数据的中文乱码问题
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
                }else{//如果fileitem中封装的是上传文件
                    //得到上传的文件名称，
                	String fieldName = item.getFieldName();
                    String filename = item.getName();
                    System.out.println(filename);
                    if(filename==null || filename.trim().equals("")){
                        continue;
                    }
                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    String filePrimeName = filename.substring(filename.lastIndexOf("\\")+1,filename.lastIndexOf("."));
                    //得到上传文件的扩展名
                    String fileExtName = filename.substring(filename.lastIndexOf("."));
                    if("image".equals(fieldName)) {
                    	filePrimeName = "lecturer";
                    	fileExtName = ".jpg";
                    }
                    //获取存储的数据的文件名
                    String savedFileName = filePrimeName + fileExtName;
                    //如果需要限制上传的文件类型，那么可以通过文件的扩展名来判断上传的文件类型是否合法
                    System.out.println("上传的文件的扩展名是："+fileExtName);
                    //获取item中的上传文件的输入流
                    InputStream in = item.getInputStream();
                    //创建一个文件输出流
                    FileOutputStream out = new FileOutputStream(fileDir + File.separator + savedFileName);
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
                }
            }
            //将信息存入数据库
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
            	message = "数据存储失败";
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
            return null;
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
            return null;
        }
         catch (IOException e) {
			// TODO Auto-generated catch block
        	message= "系统异常！";
            state = ERROR;
			e.printStackTrace();
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			message= "网络异常！";
            state = ERROR;
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			message= "文件类型错误！";
            state = ERROR;
			e.printStackTrace();
		}catch (Exception e) {
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
		return null;
	}
	
	@RequestMapping("/video")
	/*
	 * 用于该模块中视频播放部分
	 * 
	 * 不同于其他文件请求资源，该功能的请求url的前缀为meeting（后期更改的调整）
	 * 
	 * 请求url格式为：http://localhost:8080/smallProject/meeting/video?date=(date)
	 * 	因为每天中的视频只可能出现一个，所以视频信息需要一个date就可以
	 */
	public void doPlayVideo(HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Content-Type","video/mp4");
		
		//获取请求路径
		String absolutePath = getClass().getResource("/files/meeting").toString().substring(6);
		//获取请求日期
		String date = request.getParameter("date");
		//获取文件所在的文件夹
		String path = absolutePath + date;
		System.out.println(path);
		//文件夹的实体对象
		File dir = new File(path);
		File files[] = null; 
		if(dir.exists()&&dir.isDirectory()) {
			//当文件夹存在时，并且时文件夹时进行视频的查找
			files = dir.listFiles(new FileFilter() {
				
				public boolean accept(File pathname) {
					//获取文件的名字
					String fileName = pathname.getName();
					//获取文件的类型（后缀名）
					String fileExtName = fileName.substring(fileName.lastIndexOf(".")+1);
					
					if(fileExtName.matches("mp4")) {
						return true;
					}
					return false;
				}
			});
		}
		
		if(files != null&&files.length == 1) {
			System.out.println("有文件");
			//判断查找到的视频文件是否为1个,若为一个，则正确播放
			
			  
			//建立流
			FileInputStream in = null;
			OutputStream out = null;
			
			try {
				in = new FileInputStream(files[0]);
				out = response.getOutputStream();
				
				//建立缓存
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
