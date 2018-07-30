package org.iqalliance.smallProject.news.service.impl;

import java.util.List;

import org.iqalliance.smallProject.common.web.PageObject;
import org.iqalliance.smallProject.news.dao.NewsDAO;
import org.iqalliance.smallProject.news.entity.News;
import org.iqalliance.smallProject.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl implements NewsService{
	
	@Autowired
	private NewsDAO newsDAO;
	
//	@Autowired
//	private WxAccountCrawler wxAccountCrawler;

	public void saveNews(List<News> list) {
		for(int i = 0;i<list.size();i++){
			newsDAO.saveObject(list.get(i));
		}
	}
	
	public boolean saveNews(News news) {
		// TODO Auto-generated method stub
		if(!news.hasNull()) {
			newsDAO.saveObject(news);
			return true;
		}
		return false;
	}
//	public void saveNews() {
//		try {
//			wxAccountCrawler.start(10);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		List<News> list = wxAccountCrawler.getList();
//		for(int i = 0;i<list.size();i++){
//			newsDAO.saveObject(list.get(i));
//		}
//	}
	
	public List<News> getPageNews(PageObject pageObject,Integer kind) {
		List<News> list = newsDAO.getPageObjects(pageObject,kind);
		return list;
	}

	public void addPraise(int id) {
		News news = new News();
		news.setId(id);
		newsDAO.addPraise(news);
	}

	public void addWatch(int id) {
		News news = new News();
		news.setId(id);
		newsDAO.addWatch(news);
	}

	public int getPraise(int id) {
		// TODO Auto-generated method stub
		return newsDAO.getPraise(id);
	}

	public int getWatch(int id) {
		// TODO Auto-generated method stub
		return newsDAO.getWatch(id);
	}

	
}
