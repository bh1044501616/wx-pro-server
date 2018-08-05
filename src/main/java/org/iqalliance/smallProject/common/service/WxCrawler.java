package org.iqalliance.smallProject.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iqalliance.smallProject.news.dao.NewsDAO;
import org.iqalliance.smallProject.news.entity.News;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class WxCrawler {
	
	private ApplicationContext ac;
	private static NewsDAO newsDAO;
	
	public WxCrawler(){
		ac = new ClassPathXmlApplicationContext(new String[]{"spring-webmvc.xml","spring-pool.xml","spring-mybatis.xml"});
		newsDAO = ac.getBean("newsDAO",NewsDAO.class);
	}
	
	public String getJsonList(String filepath) throws IOException {
		File file = new File(filepath);
		if(!file.exists()) {
			throw new FileNotFoundException();
		}
		FileInputStream fis = new FileInputStream(file);
		
		byte[] data = new byte[1024];
		int flag = -1;
		String str = "";
		while((flag = fis.read(data)) != -1) {
			str += new String(data,"utf-8");
		}
		//获取数据
		String prefixStr0 = "msgList = '";
		String suffixStr0 = "endOfContent";
		String prefixStr1 = "Response code: 200\r\n" + "Response body: {";
		String suffixStr1 = "real_type\":0}";
		
		//截取获取的最新的数据
		String latest = WxCrawler.formatUrl(str.substring(str.indexOf(prefixStr0) + prefixStr0.length(), str.indexOf(suffixStr0)));
		//json格式化
		String replace[] = new String[] {"&#39;", "'", "&quot;", "\"", "&nbsp;", " ", "&gt;", ">", "&lt;", "<", "&amp;", "&", "&yen;", "¥"};
		for (int i=0;i< replace.length;i+= 2) {
			latest=latest.replaceAll(replace[i],replace[i+1]);
		}
		//获取有用的值
		JSONObject latestJson = JSON.parseObject(latest);
		JSONArray latestJsonArray = latestJson.getJSONArray("list");
		List<Map> list = new ArrayList<Map>();
		for(int i=0;i<latestJsonArray.size();i++) {
			JSONObject jsonObject1 = latestJsonArray.getJSONObject(i).getJSONObject("app_msg_ext_info");
			JSONObject jsonObject2 = latestJsonArray.getJSONObject(i).getJSONObject("comm_msg_info");
			
			String detailUrl = jsonObject1.getString("content_url");
			String cover = jsonObject1.getString("cover");
			String theme = jsonObject1.getString("title");
			String digest = jsonObject1.getString("digest");
			String timeStr = jsonObject2.getString("datetime");
			long timeLong = Long.parseLong(timeStr) * 1000;
			Date time = new Date(timeLong);

			Map<String,Object> map = new HashMap<String,Object>();
			
			map.put("detailUrl", detailUrl);
			map.put("cover", cover);
			map.put("theme", theme);
			map.put("digest", digest);
			map.put("time", time);
			
			if(theme.contains("TiD")) {
				map.put("kind", 2);
			}else if(theme.contains("智联")) {
				map.put("kind", 1);
			}
			
			list.add(map);
		}
		String[] beforeStr = str.split("Response code: 200\\sResponse body:\\s");
		for(int i=0;i<beforeStr.length;i++) {
			String before = WxCrawler.formatUrl(beforeStr[i].trim().replaceAll("\\\\\"", "\"").replaceAll("\"general_msg_list\":\"", "\"general_msg_list\":").replaceAll("}}]}\"", "}}]}"));
			int end = before.indexOf(suffixStr1);
			if(end > 0) {
				before = before.substring(0,end + suffixStr1.length());
			}
			else {
				continue;
			}
			System.out.println(before);
			JSONObject refreshObject = JSON.parseObject(before);
			if(refreshObject == null) {
				continue;
			}
			System.out.println(refreshObject);
			JSONObject general_msg_list = refreshObject.getJSONObject("general_msg_list");
			JSONArray articleObjectList = general_msg_list.getJSONArray("list");
			for(int j=0;j<articleObjectList.size();j++) {
				JSONObject jsonItem = articleObjectList.getJSONObject(j);
				
				JSONObject comm_msg_info = jsonItem.getJSONObject("comm_msg_info");
				JSONObject app_msg_ext_info = jsonItem.getJSONObject("app_msg_ext_info");
				
				String detailUrl = app_msg_ext_info.getString("content_url");
				String cover = app_msg_ext_info.getString("cover");
				String theme = app_msg_ext_info.getString("title");
				String digest = app_msg_ext_info.getString("digest");
				String timeStr = comm_msg_info.getString("datetime");
				long timeLong = Long.parseLong(timeStr) * 1000;
				Date time = new Date(timeLong);
				
				Map<String,Object> map = new HashMap<String,Object>();
				
				map.put("detailUrl", detailUrl);
				map.put("cover", cover);
				map.put("theme", theme);
				map.put("digest", digest);
				map.put("time", time);
				
				if(theme.contains("TiD")) {
					map.put("kind", 2);
				}else if(theme.contains("智联")) {
					map.put("kind", 1);
				}
				
				list.add(map);
			}
		}
		String jsonList = JSON.toJSONString(list);
		return jsonList;
	}
	
	
	public static String formatUrl(String str) {
		//格式化url(复原腾讯所添加的干扰因素)
		String formatedStr = null;
		formatedStr = str/*.replaceAll("^[\\s\\uFEFF\\xA0]+|[\\s\\uFEFF\\xA0]+$","")*/.replaceAll("\\\\\\\\/", "/");
		return formatedStr;
	}
	
	public String getJsonList(File file) throws IOException {
		return this.getJsonList(file.getAbsolutePath());
	}
	
	
	public static void main(String[] args) {
		WxCrawler wc = new WxCrawler();
		try {
			String list = wc.getJsonList("/home/Response1.txt");
			
			JSONArray ja = JSON.parseArray(list);
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
					newsDAO.saveObject(news);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
