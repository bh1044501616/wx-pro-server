package org.iqalliance.smallProject.ticket.dao;

import org.apache.ibatis.annotations.Param;
import org.iqalliance.smallProject.ticket.entity.Ticket;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketDAO {
	/*
	 * ��������Ʊ����Ϣ
	 */
	public int saveObject(Ticket ticket);
	/*
	 * ��ѯ���ݿ����Ƿ��Ѿ�����ص绰����Ϣ
	 */
	public Integer checkPhone(String phone);
	/*
	 * Ϊ���ݿ��е�paid�ֶκ�qrCode�ֶ����ֵ
	 */
	public int updateObject(@Param("phone")String phone,@Param("qrCode")String qrCode);
	/*
	 * ��������ϢҪ���pay�ֶ�����Ϊ0
	 */
	public Integer checkObject(@Param("phone")String phone);
	/*
	 * ͨ���ֻ��Ż���΢���˺Ż�ȡ�˺�������Ϣ
	 */
	public Ticket getObject(@Param("account")String account);
}
