package org.iqalliance.smallProject.common.service;

import org.iqalliance.smallProject.common.dao.ImageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
	
	@Autowired
	private ImageDAO imageDAO;
	
	public String getFilePath(String hashcode) {
		String path = imageDAO.getPath(hashcode);
		if( path != null && !"".equals(hashcode)) {
			return path;
		}
		return null;
	}
}
