package org.iqalliance.smallProject.common.dao;

import org.iqalliance.smallProject.common.entity.Feedback;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonDAO {
	//保存反馈对象
	public void saveObject(Feedback feedback);
}
