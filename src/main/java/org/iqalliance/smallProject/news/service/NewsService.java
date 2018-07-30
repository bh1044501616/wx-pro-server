package org.iqalliance.smallProject.news.service;

import java.util.List;

import org.iqalliance.smallProject.common.web.PageObject;
import org.iqalliance.smallProject.news.entity.News;

public interface NewsService {
	//保存News
	public void saveNews(List<News> list);
	/*
	 * 存储一个News对象
	 * 
	 * 返回值为boolean类型
	 * 
	 * true为存储成功
	 * 
	 * false为存储失败
	 */
	public boolean saveNews(News news);
//	public void saveNews();
	
	//加载page获取动态数据
	public List<News> getPageNews(PageObject pageObject,Integer kind);
	
	
	//添加信息获得的赞数
	public void addPraise(int id);
	//添加信息被查看的次数
	public void addWatch(int id);
	//获取信息获得的赞数
	public int getPraise(int id);
	//获取信息被查看的次数
	public int getWatch(int id);
}
