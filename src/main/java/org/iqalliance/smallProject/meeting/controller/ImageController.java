package org.iqalliance.smallProject.meeting.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/image")
public class ImageController {
	
	
	@RequestMapping("/meeting")
	//向meeting模块传输图片信息
	public void doLoadMeetingImage(HttpServletRequest request,HttpServletResponse response) {
		String uri = request.getRequestURI();
		String requestPath = request.getParameter("date");
		String requestData = request.getParameter("data");
		String absolutePath = getClass().getResource("/").toString();
		String path = absolutePath.substring(6) + "files" + File.separator + "meeting"+ File.separator + requestPath + File.separator + requestData + ".jpg";
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
