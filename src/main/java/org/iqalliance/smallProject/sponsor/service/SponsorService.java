package org.iqalliance.smallProject.sponsor.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iqalliance.smallProject.sponsor.dao.SponsorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SponsorService {
	
	@Autowired
	private SponsorDAO sponsorDAO;
	
	/*
	 * 通过 前端所选择的标签 来筛选 符合标签内容的赞助商
	 */
	public List getSponsorsByRequest(List list) {
		List sponsors = sponsorDAO.getObjectsByAntistop(list);
		return sponsors;
	}
	
	/*
	 * 发送短信验证码，用以验证手机
	 */
	public String sendIdenMsg(String phone,String content) {
		return null;
	}
	
	/*
	 * 获取所有标签
	 */
	public List<Map<String,String>> getAllAntistops(){
		List<String> list = sponsorDAO.getAllAntistops();
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		for(String str:list) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("str", str);
			result.add(map);
		}
		return result;
	}
}
