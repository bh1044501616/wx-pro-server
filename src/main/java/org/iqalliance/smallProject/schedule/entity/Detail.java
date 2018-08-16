package org.iqalliance.smallProject.schedule.entity;

public class Detail {
	private String name;
	private String introduction;
	private String pic;
	private String ppt;
	private String pptName;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
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
		return "Detail [name=" + name + ", introduction=" + introduction + ", pic=" + pic + ", ppt=" + ppt
				+ ", pptName=" + pptName + "]";
	}
	
	
}
