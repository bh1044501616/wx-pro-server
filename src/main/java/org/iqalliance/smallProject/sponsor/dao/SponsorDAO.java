package org.iqalliance.smallProject.sponsor.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.iqalliance.smallProject.sponsor.entity.Sponsor;

public interface SponsorDAO {
	/*
	 * ��ѯ�����������ݵ���������Ϣ
	 */
	public List<Sponsor> getObjectsByAntistop(@Param("list")List list);
	/*
	 * ��ȡ���б�ǩ
	 */
	public List<String> getAllAntistops();
}
