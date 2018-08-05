package org.iqalliance.smallProject.sponsor.service;

import java.util.List;

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
}
