package org.iqalliance.smallProject.common.dao;

import org.iqalliance.smallProject.common.entity.Image;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageDAO {
	/*
	 * ���ڴ���image����
	 */
	public int saveObject(Image image);
	/*
	 * ͨ��hashcode��ѯ�ļ�·��
	 */
	public String getPath(String hashcode);
}
