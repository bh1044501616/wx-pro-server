package org.iqalliance.smallProject.common.entity;

import org.springframework.stereotype.Component;

@Component
public class Feedback {
	private int id;
	/**�ύ�������˵���ϵ��ʽ*/
	private String contact;
	/**������Ϣ*/
	private String feedback;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	
	
}
