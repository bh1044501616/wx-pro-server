package org.iqalliance.smallProject.sponsor.entity;

import java.util.List;

public class Sponsor {
	/**����*/
	private int id;
	/**����������*/
	private String name;
	/**������������˾*/
	private String company;
	/**������logo*/
	private String pic;
	/**��������������*/
	private String detail;
	/**�����̱�ǩ*/
	private List<String> antistops;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	
	
	public List<String> getAntistops() {
		return antistops;
	}
	public void setAntistops(List<String> antistops) {
		this.antistops = antistops;
	}
	
	
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	@Override
	public String toString() {
		return "Sponsor [id=" + id + ", name=" + name + ", company=" + company + ", pic=" + pic + ", detail=" + detail
				+ ", antistops=" + antistops + "]";
	}
	
	
}
