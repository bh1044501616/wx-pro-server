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
 *	����java����緢���������
 *
 *		������������
 *			get������get����
 *
 *			post������post����
 */
public class HttpRequestUtil {
	
	public static String sendPostRequest(String url,Map<String,Object> params) {
		
		//��URL���б���
		/*try {
		 * 	//������post��������������ڰ��У����Բ���Ҫ�˲���
			url = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		//���� http����ͻ��ˣ�ģ���������
		HttpClient httpClient =  new HttpClient();
		
		//����post�������
		PostMethod postMethod = new PostMethod(url);
		
		//Ϊpost���������Ϣͷ
		postMethod.addRequestHeader("Connection","close");
		
		//Ϊpost������Ӳ��� 
		for(Entry<String,Object> entry:params.entrySet()) {
			postMethod.addParameter(entry.getKey(), (String) entry.getValue());
		}
		
		//ʹ��ϵͳ�ṩ��Ĭ�ϵĻָ�����,�����������Դ����õ���Ĭ�ϵ����Դ�����������
        httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        
        //�洢���
        String result = null;
        try {
			httpClient.executeMethod(postMethod);
			
			//��ȡ���󷵻���Ϣ
			result = postMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
			return "������������";
		} catch (IOException e) {
			e.printStackTrace();
			return "�����쳣";
		} finally {
			//�ͷ�post��������
			postMethod.releaseConnection();
			
			//�رյ�httpClientʵ��
			if(httpClient != null) {
				((SimpleHttpConnectionManager) (httpClient.getHttpConnectionManager())).shutdown();
			}
		}
		return result;
	}
	
	
	public static String sendGetRequest(String url,Map<String,Object> params) {
		
		//��������ͻ���
		HttpClient httpClient = new HttpClient();
		
		String paramsStr = "";
		for(Entry<String,Object> entry:params.entrySet()) {
			//Ϊurl���get����url�еĲ���
			paramsStr += "&" + entry.getKey() + "=" + entry.getValue();
		}
		if(paramsStr.length() > 1) {
			//����в���
			paramsStr = paramsStr.substring(1);
			url = url + "?" + paramsStr;
		}
		//���������ӽ���url����
		/*try {
			url = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		//����GET������ʵ��
		System.out.println(url);
        GetMethod method = new GetMethod(url);
 
 
        //���շ��ؽ��
        String result = null;
        try {
            //ִ��HTTP GET��������
        	httpClient.executeMethod(method);
 
 
            //���ش�����
            result = method.getResponseBodyAsString();
        } catch (HttpException e) {
            // �����������쳣��������Э�鲻�Ի��߷��ص�����������
            System.out.println("���������URL!");
            e.printStackTrace();
        } catch (IOException e) {
            // ���������쳣
            System.out.println("���������쳣!");
            e.printStackTrace();
        } finally {
            //�ͷ�����
            method.releaseConnection();
 
 
            //�ر�HttpClientʵ��
            if (httpClient != null) {
                ((SimpleHttpConnectionManager)httpClient.getHttpConnectionManager()).shutdown();
            }
        }
        return result;
    }
}
