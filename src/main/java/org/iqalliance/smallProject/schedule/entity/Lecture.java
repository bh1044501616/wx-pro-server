package org.iqalliance.smallProject.schedule.entity;

public class Lecture {
	/**����*/
	private int id;
	/**��������*/
	private int cid;
	/**�ݽ��˵�����*/
	private int lid;
	/**ppt����������*/
	private String ppt;
	/**ppt������*/
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
