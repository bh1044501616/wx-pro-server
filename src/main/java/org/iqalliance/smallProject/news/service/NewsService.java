package org.iqalliance.smallProject.news.service;

import java.util.List;

import org.iqalliance.smallProject.common.web.PageObject;
import org.iqalliance.smallProject.news.entity.News;

public interface NewsService {
	//����News
	public void saveNews(List<News> list);
	/*
	 * �洢һ��News����
	 * 
	 * ����ֵΪboolean����
	 * 
	 * trueΪ�洢�ɹ�
	 * 
	 * falseΪ�洢ʧ��
	 */
	public boolean saveNews(News news);
//	public void saveNews();
	
	//����page��ȡ��̬����
	public List<News> getPageNews(PageObject pageObject,Integer kind);
	
	
	//�����Ϣ��õ�����
	public void addPraise(int id);
	//�����Ϣ���鿴�Ĵ���
	public void addWatch(int id);
	//��ȡ��Ϣ��õ�����
	public int getPraise(int id);
	//��ȡ��Ϣ���鿴�Ĵ���
	public int getWatch(int id);
}
