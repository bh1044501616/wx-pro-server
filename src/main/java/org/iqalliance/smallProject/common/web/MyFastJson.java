package org.iqalliance.smallProject.common.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

//@Component
/**
 * 
 * @author Airbook
 *	自己手动写一个类用于注入到spring容器中，设定responseBody返回数据的类型
 *	
 *	也可以直接在springMvc配置文件中注入
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
