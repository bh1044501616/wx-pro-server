package org.iqalliance.smallProject.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iqalliance.smallProject.common.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DownloadController {
	
	@Autowired
	private DownloadService downloadService;
	
	@RequestMapping("/download/{hashcode}")
	public void doDownloadMeetingFile(HttpServletRequest request,HttpServletResponse response,@PathVariable("hashcode")String hashcode) {
		
		//获取下载文件
		String path = downloadService.getFilePath(hashcode);
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
				System.out.println(file.getName());
				response.addHeader("Content-Disposition","attachment;filename=" + file.getName());
				in = new FileInputStream(file);
				size = in.available();
				response.addHeader("Content-length",size+"");
				byte[] data = new byte[size];
				in.read(data);
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
			System.out.println("文件不存在");
		}
	}
}
