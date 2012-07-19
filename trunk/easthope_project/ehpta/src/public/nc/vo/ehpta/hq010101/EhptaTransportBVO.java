/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.ehpta.hq010101;

import java.util.ArrayList;
import nc.vo.pub.*;
import nc.vo.pub.lang.*;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * <p>
 * �ڴ˴����Ӵ����������Ϣ
 * </p>
 * 
 * ��������:2012-7-2
 * 
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
public class EhptaTransportBVO extends SuperVO {
	public String shipaddr;
	public String arriveaddr;
	public String pk_corp;
	public String pk_send;
	public String pk_transport_b;
	public String pk_arrive;
	public String remarks;
	public String pk_transport;
	public String pk_yunshu;
	public UFDouble carprice;
	public Integer dr;
	public UFDouble shippingprice_a;
	public UFDouble actualshippingprice;
	public UFDouble rkscprice;
	public UFDouble rkscprice_c;
	public UFDouble mtzbfprice;
	public UFDateTime ts;
	public UFDouble sjfee;
	public UFDouble shippingprice;
	public UFDouble nhcyprice;
	public UFDouble dieselprice;
	public String sendpk;
	public String arrivepk;

	public static final String PK_CORP = "pk_corp";
	public static final String PK_SEND = "pk_send";
	public static final String PK_TRANSPORT_B = "pk_transport_b";
	public static final String PK_ARRIVE = "pk_arrive";
	public static final String REMARKS = "remarks";
	public static final String PK_YUNSHU = "pk_yunshu";
	public static final String CARPRICE = "carprice";
	public static final String DR = "dr";
	public static final String SHIPPINGPRICE_A = "shippingprice_a";
	public static final String ACTUALSHIPPINGPRICE = "actualshippingprice";
	public static final String RKSCPRICE = "rkscprice";
	public static final String RKSCPRICE_C = "rkscprice_c";
	public static final String MTZBFPRICE = "mtzbfprice";
	public static final String TS = "ts";
	public static final String SJFEE = "sjfee";
	public static final String SHIPPINGPRICE = "shippingprice";
	public static final String NHCYPRICE = "nhcyprice";
	public static final String DIESELPRICE = "dieselprice";
	public static final String PK_TRANSPORT = "pk_transport";
	public static final String SHIPADDR = "shipaddr";
	public static final String ARRIVEADDR = "arriveaddr";
	
	public String getSendpk() {
		return sendpk;
	}

	public void setSendpk(String sendpk) {
		this.sendpk = sendpk;
	}

	public String getArrivepk() {
		return arrivepk;
	}

	public void setArrivepk(String arrivepk) {
		this.arrivepk = arrivepk;
	}

	public String getShipaddr() {
		return shipaddr;
	}

	public void setShipaddr(String shipaddr) {
		this.shipaddr = shipaddr;
	}

	public String getArriveaddr() {
		return arriveaddr;
	}

	public void setArriveaddr(String arriveaddr) {
		this.arriveaddr = arriveaddr;
	}

	public void setPk_yunshu(String pk_yunshu) {
		this.pk_yunshu = pk_yunshu;
	}

	/**
	 * ����pk_transport��Getter����
	 * 
	 * �������ڣ�2012-7-2
	 * 
	 * @return String
	 */
	public String getPk_transport() {
		return pk_transport;
	}

	/**
	 * ����pk_transport��Setter����
	 * 
	 * �������ڣ�2012-7-2
	 * 
	 * @param pk_transport
	 */
	public void setPk_transport(String newpk_transport) {
		pk_transport = newpk_transport;
	}

	/**
	 * ����pk_corp��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return String
	 */
	public String getPk_corp() {
		return pk_corp;
	}

	/**
	 * ����pk_corp��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newPk_corp
	 *            String
	 */
	public void setPk_corp(String newPk_corp) {

		pk_corp = newPk_corp;
	}

	/**
	 * ����pk_send��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return String
	 */
	public String getPk_send() {
		return pk_send;
	}

	/**
	 * ����pk_send��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newPk_send
	 *            String
	 */
	public void setPk_send(String newPk_send) {

		pk_send = newPk_send;
	}

	/**
	 * ����pk_transport_b��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return String
	 */
	public String getPk_transport_b() {
		return pk_transport_b;
	}

	/**
	 * ����pk_transport_b��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newPk_transport_b
	 *            String
	 */
	public void setPk_transport_b(String newPk_transport_b) {

		pk_transport_b = newPk_transport_b;
	}

	/**
	 * ����pk_arrive��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return String
	 */
	public String getPk_arrive() {
		return pk_arrive;
	}

	/**
	 * ����pk_arrive��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newPk_arrive
	 *            String
	 */
	public void setPk_arrive(String newPk_arrive) {

		pk_arrive = newPk_arrive;
	}

	/**
	 * ����remarks��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return String
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * ����remarks��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newRemarks
	 *            String
	 */
	public void setRemarks(String newRemarks) {

		remarks = newRemarks;
	}

	/**
	 * ����pk_transport��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return String
	 */
	public String getPk_yunshu() {
		return pk_yunshu;
	}

	/**
	 * ����pk_transport��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newPk_transport
	 *            String
	 */
	public void setpk_yunshu(String newpk_yunshu) {

		pk_yunshu = newpk_yunshu;
	}

	/**
	 * ����carprice��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getCarprice() {
		return carprice;
	}

	/**
	 * ����carprice��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newCarprice
	 *            UFDouble
	 */
	public void setCarprice(UFDouble newCarprice) {

		carprice = newCarprice;
	}

	/**
	 * ����dr��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * ����dr��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newDr
	 *            Integer
	 */
	public void setDr(Integer newDr) {

		dr = newDr;
	}

	/**
	 * ����shippingprice_a��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getShippingprice_a() {
		return shippingprice_a;
	}

	/**
	 * ����shippingprice_a��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newShippingprice_a
	 *            UFDouble
	 */
	public void setShippingprice_a(UFDouble newShippingprice_a) {

		shippingprice_a = newShippingprice_a;
	}

	/**
	 * ����actualshippingprice��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getActualshippingprice() {
		return actualshippingprice;
	}

	/**
	 * ����actualshippingprice��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newActualshippingprice
	 *            UFDouble
	 */
	public void setActualshippingprice(UFDouble newActualshippingprice) {

		actualshippingprice = newActualshippingprice;
	}

	/**
	 * ����rkscprice��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getRkscprice() {
		return rkscprice;
	}

	/**
	 * ����rkscprice��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newRkscprice
	 *            UFDouble
	 */
	public void setRkscprice(UFDouble newRkscprice) {

		rkscprice = newRkscprice;
	}

	/**
	 * ����rkscprice_c��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getRkscprice_c() {
		return rkscprice_c;
	}

	/**
	 * ����rkscprice_c��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newRkscprice_c
	 *            UFDouble
	 */
	public void setRkscprice_c(UFDouble newRkscprice_c) {

		rkscprice_c = newRkscprice_c;
	}

	/**
	 * ����mtzbfprice��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getMtzbfprice() {
		return mtzbfprice;
	}

	/**
	 * ����mtzbfprice��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newMtzbfprice
	 *            UFDouble
	 */
	public void setMtzbfprice(UFDouble newMtzbfprice) {

		mtzbfprice = newMtzbfprice;
	}

	/**
	 * ����ts��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return UFDateTime
	 */
	public UFDateTime getTs() {
		return ts;
	}

	/**
	 * ����ts��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newTs
	 *            UFDateTime
	 */
	public void setTs(UFDateTime newTs) {

		ts = newTs;
	}

	/**
	 * ����sjfee��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getSjfee() {
		return sjfee;
	}

	/**
	 * ����sjfee��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newSjfee
	 *            UFDouble
	 */
	public void setSjfee(UFDouble newSjfee) {

		sjfee = newSjfee;
	}

	/**
	 * ����shippingprice��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getShippingprice() {
		return shippingprice;
	}

	/**
	 * ����shippingprice��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newShippingprice
	 *            UFDouble
	 */
	public void setShippingprice(UFDouble newShippingprice) {

		shippingprice = newShippingprice;
	}

	/**
	 * ����nhcyprice��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNhcyprice() {
		return nhcyprice;
	}

	/**
	 * ����nhcyprice��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newNhcyprice
	 *            UFDouble
	 */
	public void setNhcyprice(UFDouble newNhcyprice) {

		nhcyprice = newNhcyprice;
	}

	/**
	 * ����dieselprice��Getter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getDieselprice() {
		return dieselprice;
	}

	/**
	 * ����dieselprice��Setter����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newDieselprice
	 *            UFDouble
	 */
	public void setDieselprice(UFDouble newDieselprice) {

		dieselprice = newDieselprice;
	}

	/**
	 * ��֤���������֮��������߼���ȷ��.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @exception nc.vo.pub.ValidationException
	 *                �����֤ʧ��,�׳� ValidationException,�Դ�����н���.
	 */
	public void validate() throws ValidationException {

		ArrayList errFields = new ArrayList(); // errFields record those null

		// fields that cannot be null.
		// ����Ƿ�Ϊ�������յ��ֶθ��˿�ֵ,�������Ҫ�޸��������ʾ��Ϣ:

		if (pk_transport_b == null) {
			errFields.add(new String("pk_transport_b"));
		}

		StringBuffer message = new StringBuffer();
		message.append("�����ֶβ���Ϊ��:");
		if (errFields.size() > 0) {
			String[] temp = (String[]) errFields.toArray(new String[0]);
			message.append(temp[0]);
			for (int i = 1; i < temp.length; i++) {
				message.append(",");
				message.append(temp[i]);
			}
			throw new NullFieldException(message.toString());
		}
	}

	/**
	 * <p>
	 * ȡ�ø�VO�����ֶ�.
	 * <p>
	 * ��������:2012-7-2
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return "pk_transport";

	}

	/**
	 * <p>
	 * ȡ�ñ�����.
	 * <p>
	 * ��������:2012-7-2
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_transport_b";
	}

	/**
	 * <p>
	 * ���ر�����.
	 * <p>
	 * ��������:2012-7-2
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "ehpta_transport_b";
	}

	/**
	 * ����Ĭ�Ϸ�ʽ����������.
	 * 
	 * ��������:2012-7-2
	 */
	public EhptaTransportBVO() {

		super();
	}

	/**
	 * ʹ���������г�ʼ���Ĺ�����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newPk_transport_b
	 *            ����ֵ
	 */
	public EhptaTransportBVO(String newPk_transport_b) {

		// Ϊ�����ֶθ�ֵ:
		pk_transport_b = newPk_transport_b;

	}

	/**
	 * ���ض����ʶ,����Ψһ��λ����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_transport_b;

	}

	/**
	 * ���ö����ʶ,����Ψһ��λ����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @param newPk_transport_b
	 *            String
	 */
	public void setPrimaryKey(String newPk_transport_b) {

		pk_transport_b = newPk_transport_b;

	}

	/**
	 * ������ֵ�������ʾ����.
	 * 
	 * ��������:2012-7-2
	 * 
	 * @return java.lang.String ������ֵ�������ʾ����.
	 */
	public String getEntityName() {

		return "ehpta_transport_b";

	}
}