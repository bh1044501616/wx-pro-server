package org.iqalliance.smallProject.common.dao;

import org.iqalliance.smallProject.common.entity.Feedback;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonDAO {
	//���淴������
	public void saveObject(Feedback feedback);
}
