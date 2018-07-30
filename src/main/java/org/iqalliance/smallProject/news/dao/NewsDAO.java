package org.iqalliance.smallProject.news.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.iqalliance.smallProject.common.web.PageObject;
import org.iqalliance.smallProject.news.entity.News;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsDAO {
	//存储新的动态
	public void saveObject(News news);
	
	//获取pageObjects
	public List<News> getPageObjects(@Param("pageObject")PageObject pageObject,@Param("kind")Integer kind);
	
	//添加信息获得的赞数
	public void addPraise(News news);
	//添加信息被查看的次数
	public void addWatch(News news);
	//获取信息获得的赞数
	public int getPraise(int id);
	//获取信息被查看的次数
	public int getWatch(int id);
}
