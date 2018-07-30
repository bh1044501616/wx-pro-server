package org.iqalliance.smallProject.meeting.dao;

import org.iqalliance.smallProject.meeting.entity.Topic;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicDAO {
	public void saveObject(Topic topic);
}
