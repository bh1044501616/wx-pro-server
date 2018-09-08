package org.iqalliance.smallProject.ticket.entity;

public class QrCode {
	/**对应数据库中的存储对象的主键*/
	private int id;
	/**hashcode字段*/
	private String hashcode;
	/**图片保存路径*/
	private String path;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHashcode() {
		return hashcode;
	}
	public void setHashcode(String hashcode) {
		this.hashcode = hashcode;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
}
