package org.iqalliance.smallProject.news.entity;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.annotation.JSONField;

@Component
public class News {
	/**ʵ��������*/
	private int id;
	/**������̬������*/
	private String theme;
	/**��̬����ʱ��*/
	//���ڽ�jspǰ��ҳ�洫������������ʽ�ַ���ʱ��ת��ΪDate����
	@DateTimeFormat(pattern="yyyy/MM/dd")
	@JSONField(format="yyyy-MM-dd")
	private Date time;
	/**��������*/
	private String detailUrl;
	/**��ϸ����*/
	private String digest;
	/**ͼƬ����*/
	private String cover;
	/**������Ϣ�鿴����*/
	private int watch;
	/**������Ϣ���޴���*/
	private int praise;
	/**��Ϣ����*/
	private int kind;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}


	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	
	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}
	
	

	public int getWatch() {
		return watch;
	}

	public void setWatch(int watch) {
		this.watch = watch;
	}

	public int getPraise() {
		return praise;
	}

	public void setPraise(int praise) {
		this.praise = praise;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	@Override
	public String toString() {
		return "News [id=" + id + ", theme=" + theme + ", time=" + time + ", detailUrl=" + detailUrl + ", digest="
				+ digest + ", cover=" + cover + ", watch=" + watch + ", praise=" + praise + ", kind=" + kind + "]";
	}

	public boolean hasNull() {
		if(this.theme == null || this.time == null || this.cover == null || this.detailUrl == null || this.digest == null 
				|| "".equals(this.theme) || "".equals(this.cover) || "".equals(this.detailUrl) || "".equals(this.digest)) {
			return true;
		}
		return false;
	}
	
	
}
