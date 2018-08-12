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
	 * ͨ�� ǰ����ѡ��ı�ǩ ��ɸѡ ���ϱ�ǩ���ݵ�������
	 */
	public List getSponsorsByRequest(List list) {
		List sponsors = sponsorDAO.getObjectsByAntistop(list);
		return sponsors;
	}
	
	/*
	 * ���Ͷ�����֤�룬������֤�ֻ�
	 */
	public String sendIdenMsg(String phone,String content) {
		return null;
	}
	
	/*
	 * ��ȡ���б�ǩ
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
