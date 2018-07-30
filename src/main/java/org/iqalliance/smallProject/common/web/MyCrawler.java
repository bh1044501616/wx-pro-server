package org.iqalliance.smallProject.common.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MyCrawler {
	
	/*
	 * 通过网址url获取页面
	 */
	public String getHtmlStr(String urlStr) {
		
		String htmlStr = "";
		
		URL url = null;
		HttpURLConnection connect = null;
		try {
			//创建一个网络地址对象
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(url == null) {
			return null;
		}
		System.setProperty("sun.net.client.defaultConnectTimeout", "50000");
		System.setProperty("sun.net.client.defaultReadTimeout", "50000");
		try {
			connect = (HttpURLConnection)url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(connect == null) {
			return null;
		}
		InputStream in = null;
		try {
			in = connect.getInputStream();
			
			byte[] data = new byte[1024];
			int flag = -1;
			
			while((flag = in.read(data)) != -1) {
				String str = new String(data,"UTF-8");
				htmlStr += str;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return htmlStr;
	}
	
	
	public static void main(String[] args) {
		MyCrawler my = new MyCrawler();
		String htmlStr = my.getHtmlStr("http://web1806060001.gz01.bdysite.com/category/2.html");
		System.out.println(htmlStr);
	}
}
