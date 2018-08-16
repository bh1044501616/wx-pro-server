package org.iqalliance.smallProject.schedule.entity;

public class Lecture {
	/**主键*/
	private int id;
	/**大会的主键*/
	private int cid;
	/**演讲人的主键*/
	private int lid;
	/**ppt的下载链接*/
	private String ppt;
	/**ppt的名字*/
	private String pptName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getLid() {
		return lid;
	}
	public void setLid(int lid) {
		this.lid = lid;
	}
	public String getPpt() {
		return ppt;
	}
	public void setPpt(String ppt) {
		this.ppt = ppt;
	}
	public String getPptName() {
		return pptName;
	}
	public void setPptName(String pptName) {
		this.pptName = pptName;
	}
	@Override
	public String toString() {
		return "Lecture [id=" + id + ", cid=" + cid + ", lid=" + lid + ", ppt=" + ppt + ", pptName=" + pptName + "]";
	}
	
	
}
