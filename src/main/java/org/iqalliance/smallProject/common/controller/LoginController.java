package org.iqalliance.smallProject.common.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iqalliance.smallProject.meeting.dao.MeetingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	@Autowired
	private MeetingDAO dao;
	
	@RequestMapping("/admin")
	public String doLogin() {
		return "login";
	}
	
	@RequestMapping("/checkForm")
	public String doCheckForm(HttpServletRequest request,HttpServletResponse response) {
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		
		try {
			String psw = dao.checkForm(account);
			if(psw.equals(password)) {
				response.addCookie(new Cookie("account",account));
				return "admin";
			}
		}catch(RuntimeException e) {
			e.printStackTrace();
			request.setAttribute("message", "用户不存在！");
			try {
				request.getRequestDispatcher("WEB-INF/pages/message.jsp").forward(request, response);
			} catch (ServletException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return "failedLogin";
	}
}
