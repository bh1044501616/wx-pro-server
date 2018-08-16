package org.iqalliance.smallProject.common.dao;

import org.iqalliance.smallProject.common.entity.Download;
import org.springframework.stereotype.Repository;

@Repository
public interface DownloadDAO {
	/*
	 * 用于储存download对象
	 */
	public Integer saveObject(Download download);
	/*
	 * 通过hashcode查询文件路径
	 */
	public String getPath(String hashcode);
}
