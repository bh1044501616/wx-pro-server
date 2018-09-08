package org.iqalliance.smallProject.ticket.entity;

import org.springframework.stereotype.Component;

@Component
public class Ticket {
	/**������̳*/
	private String forum;
	/**������*/
	private String name;
	/**�����˹�˾*/
	private String company;
	/**������ְλ*/
	private String position;
	/**�ֻ���*/
	private String phone;
	/**����*/
	private String email;
	/**����*/
	private String password;
	/**��ά���ַ*/
	private String qrCode;
	/**֧��״̬*/
	private Integer paid;
	/**΢���˺�*/
	private String wx_account;
	/**����Ա��ʶ*/
	private int admin;
	
	public String getForum() {
		return forum;
	}
	public void setForum(String forum) {
		this.forum = forum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public Integer getPaid() {
		return paid;
	}
	public void setPaid(Integer paid) {
		this.paid = paid;
	}
	public String getWx_account() {
		return wx_account;
	}
	public void setWx_account(String wx_account) {
		this.wx_account = wx_account;
	}
	
	public int getAdmin() {
		return admin;
	}
	public void setAdmin(int admin) {
		this.admin = admin;
	}
	@Override
	public String toString() {
		return "Ticket [forum=" + forum + ", name=" + name + ", company=" + company + ", position=" + position
				+ ", phone=" + phone + ", email=" + email + ", password=" + password + ", qrCode=" + qrCode + ", paid="
				+ paid + ", wx_account=" + wx_account + ", admin=" + admin + "]";
	}
	
}
