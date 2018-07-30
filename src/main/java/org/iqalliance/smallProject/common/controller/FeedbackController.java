package org.iqalliance.smallProject.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.iqalliance.smallProject.common.dao.CommonDAO;
import org.iqalliance.smallProject.common.entity.Feedback;
import org.iqalliance.smallProject.common.web.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FeedbackController {
	
	@Autowired
	private CommonDAO commonDAO;
	
	@RequestMapping("getFeedback")
	@ResponseBody
	public JsonResult getFeedback(HttpServletRequest request) {
		String contact = request.getParameter("contact");
		String feedbackstr = request.getParameter("feedback");
		
		Feedback feedback = new Feedback();
		feedback.setFeedback(feedbackstr);
		feedback.setContact(contact);
		
		commonDAO.saveObject(feedback);
		
		return new JsonResult();
	}
}
