package org.iqalliance.smallProject.sponsor.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.iqalliance.smallProject.common.web.JsonResult;
import org.iqalliance.smallProject.sponsor.service.SponsorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@Controller
@RequestMapping("/sponsor")
public class SponsorController {
	
	@Autowired
	private SponsorService sponsorService;
	
	@ResponseBody
	@RequestMapping("/load")
	public JsonResult getObjectsByAntistops(String antistopsStr) {
		List list = null;
		if(antistopsStr != null && !"".equals(antistopsStr)) {
			String[] antistops = antistopsStr.split(",");
			list = Arrays.asList(antistops);
		}
		List data = null;
		if(list == null) {
			data = sponsorService.getSponsorsByRequest(new ArrayList());
		}else {
			data = sponsorService.getSponsorsByRequest(list);
		}
		return new JsonResult(data);
	}
	
	@RequestMapping("/antistop")
	@ResponseBody
	public org.iqalliance.smallProject.common.web.JsonResult JsonResult() {
		List<Map<String,String>> list = sponsorService.getAllAntistops();
		if(list == null) {
			return new JsonResult(new Exception());
		}else {
			return new JsonResult(list);
		}
	}
}
