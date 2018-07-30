package org.iqalliance.smallProject.meeting.entity;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.annotation.JSONField;

@Component
public class Meeting {
	/**ʵ��������*/
	private int id;
	/**�������ʦ����*/
	private String lecturerName;
	/**���δ������*/
	private String meetingTheme;
	/**���ʱ��*/
	//���ڽ�jspǰ��ҳ�洫������������ʽ�ַ���ʱ��ת��ΪDate����
	@DateTimeFormat(pattern="yyyy/MM/dd")
	@JSONField(format="yyyy-MM-dd")
	private Date meetingTime;
	/**ppt��������*/
	private String pptUrl;
	/**���¼������*/
	private String videoUrl;
	/**��ὲʦ��Ƭ*/
	private String imageUrl;
	/**pptĿ¼*/
	private List ppts;
	/**�ݽ��˽���*/
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
