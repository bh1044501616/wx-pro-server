package org.iqalliance.smallProject.meeting.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/download")
public class DownloadController {
	@RequestMapping("/meeting")
	public void doDownloadMeetingFile(HttpServletRequest request,HttpServletResponse response) {
		//获取下载文件
		String fileDate = request.getParameter("date");
		String fileName = request.getParameter("name");
		String pathSuffix = getClass().getResource("/").toString();
		try {
			fileName = new String(fileName.getBytes("ISO8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String path = pathSuffix.substring(6) + "files" + File.separator + "meeting"+ File.separator + fileDate + File.separator + fileName;
		System.out.println("下载数据的路径：" + path);
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
