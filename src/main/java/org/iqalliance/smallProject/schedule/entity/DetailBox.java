package org.iqalliance.smallProject.schedule.entity;

import java.util.List;

public class DetailBox {
	private int cid;
	private List<Detail> details;
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public List<Detail> getDetails() {
		return details;
	}
	public void setDetails(List<Detail> details) {
		this.details = details;
	}
	@Override
	public String toString() {
		return "DetailBox [cid=" + cid + ", details=" + details + "]";
	}
	
	
}
