package org.iqalliance.smallProject.ticket.dao;

import org.apache.ibatis.annotations.Param;
import org.iqalliance.smallProject.ticket.entity.Ticket;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketDAO {
	/*
	 * 保存申请票务信息
	 */
	public int saveObject(Ticket ticket);
	/*
	 * 查询数据库中是否已经有相关电话的信息
	 */
	public int checkPhone(String phone);
	/*
	 * 为数据库中的paid字段和qrCode字段填充值
	 */
	public int updateObject(@Param("phone")String phone,@Param("qrCode")String qrCode);
	/*
	 * 将符合信息要求的pay字段设置为0
	 */
	public int checkObject(@Param("phone")String phone);
}
