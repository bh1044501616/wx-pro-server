package org.iqalliance.smallProject.common.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author Airbook
 *	用于java向外界发起请求的类
 *
 *		有两个方法：
 *			get：发起get请求
 *
 *			post：发起post请求
 */
public class HttpRequestUtil {
	
	public static String sendPostRequest(String url,Map<String,Object> params) {
		
		//将URL进行编码
		/*try {
		 * 	//由于是post请求，请求参数都在包中，所以不需要此步骤
			url = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		//创建 http请求客户端（模拟浏览器）
		HttpClient httpClient =  new HttpClient();
		
		//创建post请求对象
		PostMethod postMethod = new PostMethod(url);
		
		//为post请求添加消息头
		postMethod.addRequestHeader("Connection","close");
		
		//为post请求添加参数 
		for(Entry<String,Object> entry:params.entrySet()) {
			postMethod.addParameter(entry.getKey(), (String) entry.getValue());
		}
		
		//使用系统提供的默认的恢复策略,设置请求重试处理，用的是默认的重试处理：请求三次
        httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        
        //存储结果
        String result = null;
        try {
			httpClient.executeMethod(postMethod);
			
			//获取请求返回信息
			result = postMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
			return "请检查请求链接";
		} catch (IOException e) {
			e.printStackTrace();
			return "网络异常";
		} finally {
			//释放post请求链接
			postMethod.releaseConnection();
			
			//关闭掉httpClient实例
			if(httpClient != null) {
				((SimpleHttpConnectionManager) (httpClient.getHttpConnectionManager())).shutdown();
			}
		}
		return result;
	}
	
	
	public static String sendGetRequest(String url,Map<String,Object> params) {
		
		//创建请求客户端
		HttpClient httpClient = new HttpClient();
		
		String paramsStr = "";
		for(Entry<String,Object> entry:params.entrySet()) {
			//为url添加get请求url中的参数
			paramsStr += "&" + entry.getKey() + "=" + entry.getValue();
		}
		if(paramsStr.length() > 1) {
			//如果有参数
			paramsStr = paramsStr.substring(1);
			url = url + "?" + paramsStr;
		}
		//将请求链接进行url编码
		/*try {
			url = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		//创建GET方法的实例
		System.out.println(url);
        GetMethod method = new GetMethod(url);
 
 
        //接收返回结果
        String result = null;
        try {
            //执行HTTP GET方法请求
        	httpClient.executeMethod(method);
 
 
            //返回处理结果
            result = method.getResponseBodyAsString();
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("请检查输入的URL!");
            e.printStackTrace();
        } catch (IOException e) {
            // 发生网络异常
            System.out.println("发生网络异常!");
            e.printStackTrace();
        } finally {
            //释放链接
            method.releaseConnection();
 
 
            //关闭HttpClient实例
            if (httpClient != null) {
                ((SimpleHttpConnectionManager)httpClient.getHttpConnectionManager()).shutdown();
            }
        }
        return result;
    }
}
