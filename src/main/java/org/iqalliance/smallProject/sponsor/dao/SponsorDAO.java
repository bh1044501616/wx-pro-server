package org.iqalliance.smallProject.sponsor.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.iqalliance.smallProject.sponsor.entity.Sponsor;

public interface SponsorDAO {
	/*
	 * 查询符合请求内容的赞助商信息
	 */
	public List<Sponsor> getObjectsByAntistop(@Param("list")List list);
	/*
	 * 获取所有标签
	 */
	public List<String> getAllAntistops();
}
