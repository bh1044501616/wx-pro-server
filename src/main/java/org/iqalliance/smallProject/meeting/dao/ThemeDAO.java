package org.iqalliance.smallProject.meeting.dao;

import org.iqalliance.smallProject.meeting.entity.Theme;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeDAO {
	public void saveObject(Theme theme);
}
