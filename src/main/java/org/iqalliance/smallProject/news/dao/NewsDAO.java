package org.iqalliance.smallProject.news.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.iqalliance.smallProject.common.web.PageObject;
import org.iqalliance.smallProject.news.entity.News;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsDAO {
	//�洢�µĶ�̬
	public void saveObject(News news);
	
	//��ȡpageObjects
	public List<News> getPageObjects(@Param("pageObject")PageObject pageObject,@Param("kind")Integer kind);
	
	//�����Ϣ��õ�����
	public void addPraise(News news);
	//�����Ϣ���鿴�Ĵ���
	public void addWatch(News news);
	//��ȡ��Ϣ��õ�����
	public int getPraise(int id);
	//��ȡ��Ϣ���鿴�Ĵ���
	public int getWatch(int id);
}
