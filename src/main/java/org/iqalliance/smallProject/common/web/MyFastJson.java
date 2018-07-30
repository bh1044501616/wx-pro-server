package org.iqalliance.smallProject.common.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

//@Component
/**
 * 
 * @author Airbook
 *	�Լ��ֶ�дһ��������ע�뵽spring�����У��趨responseBody�������ݵ�����
 *	
 *	Ҳ����ֱ����springMvc�����ļ���ע��
 *
 */ 
public class MyFastJson extends FastJsonHttpMessageConverter{
	
	public MyFastJson(){
		List list = new ArrayList();
		list.add("text/html;charset=UTF-8");
		list.add("application/json;charset=UTF-8");
		this.setSupportedMediaTypes(list);
	}
}
