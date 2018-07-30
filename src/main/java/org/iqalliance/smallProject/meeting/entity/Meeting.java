package org.iqalliance.smallProject.meeting.entity;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.annotation.JSONField;

@Component
public class Meeting {
	/**实体类主键*/
	private int id;
	/**大会主讲师姓名*/
	private String lecturerName;
	/**本次大会主题*/
	private String meetingTheme;
	/**大会时间*/
	//用于将jsp前端页面传过来的以下形式字符串时间转换为Date类型
	@DateTimeFormat(pattern="yyyy/MM/dd")
	@JSONField(format="yyyy-MM-dd")
	private Date meetingTime;
	/**ppt下载链接*/
	private String pptUrl;
	/**大会录像链接*/
	private String videoUrl;
	/**大会讲师照片*/
	private String imageUrl;
	/**ppt目录*/
	private List ppts;
	/**演讲人介绍*/
	private String introduction;
	
	
	
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getLecturerName() {
		return lecturerName;
	}
	public void setLecturerName(String lecturerName) {
		this.lecturerName = lecturerName;
	}
	public String getMeetingTheme() {
		return meetingTheme;
	}
	public void setMeetingTheme(String meetingTheme) {
		this.meetingTheme = meetingTheme;
	}
	public Date getMeetingTime() {
		return meetingTime;
	}
	public void setMeetingTime(Date meetingTime) {
		this.meetingTime = meetingTime;
	}
	public String getPptUrl() {
		return pptUrl;
	}
	public void setPptUrl(String pptUrl) {
		this.pptUrl = pptUrl;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public List getPpts() {
		return ppts;
	}
	public void setPpts(List ppts) {
		this.ppts = ppts;
	}
	@Override
	public String toString() {
		return "Meeting [lecturerName=" + lecturerName + ", meetingTheme=" + meetingTheme + ", meetingTime="
				+ meetingTime + ", pptUrl=" + pptUrl + ", videoUrl=" + videoUrl + ", imageUrl=" + imageUrl + ", ppts="
				+ ppts + "]";
	}
	
	
}
