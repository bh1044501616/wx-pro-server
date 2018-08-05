package org.iqalliance.smallProject.ticket.controller;

import org.iqalliance.smallProject.common.web.JsonResult;
import org.iqalliance.smallProject.ticket.entity.Ticket;
import org.iqalliance.smallProject.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/ticket")
@Controller
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	
	/*
	 * ע����Ʊ����Ϣ
	 */
	@RequestMapping("/signin")
	@ResponseBody
	public JsonResult saveObject(Ticket ticket) {
		System.out.println(ticket);
		int flag = ticketService.saveTicket(ticket);
		if(flag == 0) {
			return new JsonResult(new Exception());
		}else if(flag == -1){
			return new JsonResult("�ֻ����Ѿ���ע�ᣡ");
		}else {
			return new JsonResult();
		}
	}
	
	
	/*
	 * �û�֧�������Ʊ����
	 */
	@RequestMapping("pay")
	@ResponseBody
	public JsonResult doPayMethod(Ticket ticket,String checkCode) {
		int flag = 0;
		String phone = ticket.getPhone();
		if( checkCode != null && !"".equals(checkCode) && "haoshaowen".equals(checkCode)) {
			if(phone != null && !"".equals(phone)) {
				//����ֻ�����Ϣ
				flag = ticketService.pay(ticket);
			}
		}
		if(flag == 1) {
			return new JsonResult();
		} else {
			return new JsonResult("֧��ʧ�ܣ����Ժ����ԣ�");
		}
	}
}
