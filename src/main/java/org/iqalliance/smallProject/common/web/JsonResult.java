package org.iqalliance.smallProject.common.web;

public class JsonResult {
	
	public static int SUCCESS = 1;
	public static int ERROR = 0;
	
	private int state;
	private Object data;
	private String message;

	public JsonResult() {
		this.state = SUCCESS;
	}
	
	public JsonResult(Object data) {
		this.state = SUCCESS;
		this.data = data;
	}

	public JsonResult(String message) {
		super();
		this.state = 0;
		this.message = message;
	}
	

	public JsonResult(int state, String message) {
		super();
		this.state = state;
		this.message = message;
	}

	public JsonResult(Throwable t) {
		this.state = ERROR;
		this.data = t.getMessage();
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
}
