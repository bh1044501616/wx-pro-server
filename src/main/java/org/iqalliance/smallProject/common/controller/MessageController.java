package org.iqalliance.smallProject.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MessageController {
	@RequestMapping("/message")
	public String doMeessage() {
		return "message";
	}
}
