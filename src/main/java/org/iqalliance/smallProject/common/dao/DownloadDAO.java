package org.iqalliance.smallProject.common.dao;

import org.iqalliance.smallProject.common.entity.Download;
import org.springframework.stereotype.Repository;

@Repository
public interface DownloadDAO {
	/*
	 * ���ڴ���download����
	 */
	public Integer saveObject(Download download);
	/*
	 * ͨ��hashcode��ѯ�ļ�·��
	 */
	public String getPath(String hashcode);
}
