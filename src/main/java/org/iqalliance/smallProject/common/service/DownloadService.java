package org.iqalliance.smallProject.common.service;

import org.iqalliance.smallProject.common.dao.DownloadDAO;
import org.iqalliance.smallProject.common.entity.Download;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DownloadService {
	
	@Autowired
	private DownloadDAO downloadDAO;
	
	public String getFilePath(String hashcode) {
		String path = downloadDAO.getPath(hashcode);
		if( path != null && !"".equals(hashcode)) {
			return path;
		}
		return null;
	}
	
	public int saveDownloadObj(Download download) {
		return downloadDAO.saveObject(download);
	}
}
