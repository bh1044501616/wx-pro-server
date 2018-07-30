package org.iqalliance.smallProject.meeting.dao;

import org.iqalliance.smallProject.meeting.entity.Lecture;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureDAO {
	public void saveObject(Lecture lecture);
}
