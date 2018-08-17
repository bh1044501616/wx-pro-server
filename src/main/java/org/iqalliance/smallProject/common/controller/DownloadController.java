package org.iqalliance.smallProject.common.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.iqalliance.smallProject.common.service.DownloadService;
import org.iqalliance.smallProject.common.service.MyQRCode;
import org.iqalliance.smallProject.common.web.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
public class DownloadController {
	
	@Autowired
	private DownloadService downloadService;
	
	@RequestMapping("/download/{hashcode}")
	@ResponseBody
	public JsonResult doDownloadMeetingFile(HttpServletRequest request,HttpServletResponse response,@PathVariable("hashcode")String hashcode) {
		
		String message = "";
		
		//获取下载文件
		String path = downloadService.getFilePath(hashcode);
		System.out.println(path);
		if(path == null) {
			path = "";
		}
		File file = new File(path);
		//发送文件
		if(file.exists()) {
			int size = 0;
			FileInputStream in = null;
			OutputStream out = null;
			try {
				out = response.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				response.addHeader("Content-Disposition","attachment;filename=" + file.getName());
				response.addHeader("Content-type","application/json");
				in = new FileInputStream(file);
				size = in.available();
				response.addHeader("Content-length",size+"");
				byte[] data = new byte[size];
				in.read(data);
				out.write(data);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				message = "请求数据错误";
			} catch (IOException e) {
				e.printStackTrace();
				message = "网络繁忙，请稍后重试！";
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
						e.printStackTrace();
					}
				}
				if(!"".equals(message)) {
					return new JsonResult(message);
				}
				return new JsonResult();
			}
		}else {
			return new JsonResult("文件不存在");
		}
	}

	
}
