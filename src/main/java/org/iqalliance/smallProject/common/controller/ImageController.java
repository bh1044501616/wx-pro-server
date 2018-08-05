package org.iqalliance.smallProject.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iqalliance.smallProject.common.service.ImageService;
import org.iqalliance.smallProject.common.web.StaticValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ImageController {
	
	@Autowired
	private ImageService imageService;
	
	@RequestMapping("/image/{hashcode}")
	//向meeting模块传输图片信息
	public void doLoadMeetingImage(HttpServletRequest request,HttpServletResponse response,@PathVariable("hashcode")String hashcode) {
		
		String path = imageService.getFilePath(hashcode);
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
