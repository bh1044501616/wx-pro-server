package org.iqalliance.smallProject.meeting.dao;

import org.iqalliance.smallProject.schedule.entity.Conference;
import org.springframework.stereotype.Repository;

@Repository
public interface ConferenceDAO {
	public void saveObject(Conference conference);
}
