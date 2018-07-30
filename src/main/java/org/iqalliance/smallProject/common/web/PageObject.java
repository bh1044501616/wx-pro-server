package org.iqalliance.smallProject.common.web;

public class PageObject {
	/**数据开始的下标*/
	private int startIndex;
	/**每一请求提取的信息数量*/
	private int pageSize = 5;
	
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	@Override
	public String toString() {
		return "PageObject [startIndex=" + startIndex + ", pageSize=" + pageSize + "]";
	}
	
	
}
