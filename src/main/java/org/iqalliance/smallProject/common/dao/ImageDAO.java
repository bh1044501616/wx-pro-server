package org.iqalliance.smallProject.common.dao;

import org.iqalliance.smallProject.common.entity.Image;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageDAO {
	/*
	 * 用于储存image对象
	 */
	public int saveObject(Image image);
	/*
	 * 通过hashcode查询文件路径
	 */
	public String getPath(String hashcode);
}
