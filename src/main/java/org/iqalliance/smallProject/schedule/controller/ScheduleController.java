package org.iqalliance.smallProject.schedule.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iqalliance.smallProject.common.web.JsonResult;
import org.iqalliance.smallProject.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {
	
	@Autowired
	private ScheduleService scheduleService;
	
	@RequestMapping("/info/{dateStr}")
	public void doGetObjectsByDate(@PathVariable String dateStr,HttpServletRequest request,HttpServletResponse response) {
		response.setContentType("application/json;charset=utf-8");
		List data = null;
		JsonResult jsonResult = null;
		try {
			Date date = new SimpleDateFormat("yyyyMMdd").parse(dateStr);
			data = scheduleService.getSchedulesByDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
			jsonResult = new JsonResult(e);
		}
		if(data == null)
			jsonResult = new JsonResult(new Exception());
		jsonResult = new JsonResult(data);
		String json = JSON.toJSONStringWithDateFormat(jsonResult, "yyyy-MM-dd");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.println(json);
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(pw != null)
				pw.close();
		}
	}
	
	
	@RequestMapping("/detail")
	@ResponseBody
	public JsonResult doGetDetailById(HttpServletRequest request) {
		String idsStr = request.getParameter("ids");
		
		if(idsStr == null) {
			return new JsonResult(new Exception());
		}
		JSONArray jsonObj = JSON.parseArray(idsStr);
		int[] ids = new int[jsonObj.size()];
		for(int i=0;i<ids.length;i++) {
			ids[i] = jsonObj.getInteger(i);
		}
		Map data = scheduleService.getLecturerInfoById(ids);
		return new JsonResult(data);  
	}
}
