package org.iqalliance.smallProject.common.web;

public class PageObject {
	/**���ݿ�ʼ���±�*/
	private int startIndex;
	/**ÿһ������ȡ����Ϣ����*/
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
