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
	 * 通过 前端所选择的标签 来筛选 符合标签内容的赞助商
	 */
	public List getSponsorsByRequest(List list) {
		List sponsors = sponsorDAO.getObjectsByAntistop(list);
		return sponsors;
	}
}
