package org.iqalliance.smallProject.meeting.dao;

import org.iqalliance.smallProject.schedule.entity.Lecturer;
import org.springframework.stereotype.Repository;

@Repository
public interface LecturerDAO {
	public void saveObject(Lecturer lecturer);
}
