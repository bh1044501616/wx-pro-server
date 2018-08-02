package org.iqalliance.smallProject.schedule.entity;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.annotation.JSONField;

@Component
public class Conference {
	private int id;
	private String loc;
	@DateTimeFormat(pattern="yyyyMMdd")
	@JSONField(format="yyyy-MM-dd")
	private Date begintime; 
	private Date endtime;
	private String domain;
	private String topic;
	
	
	public int getId() {
		return id;
	}
	
	public Date getBegintime() {
		return begintime;
	}
	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public String toString() {
		return "Conference [id=" + id + ", loc=" + loc + ", begintime=" + begintime + ", endtime=" + endtime
				+ ", domain=" + domain + ", topic=" + topic + "]";
	}
	
	
}
