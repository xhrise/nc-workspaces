/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.ehpta.hq010101;

import java.util.ArrayList;
import nc.vo.pub.*;
import nc.vo.pub.lang.*;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 创建日期:2012-7-2
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
	 * 属性pk_transport的Getter方法
	 * 
	 * 创建日期：2012-7-2
	 * 
	 * @return String
	 */
	public String getPk_transport() {
		return pk_transport;
	}

	/**
	 * 属性pk_transport的Setter方法
	 * 
	 * 创建日期：2012-7-2
	 * 
	 * @param pk_transport
	 */
	public void setPk_transport(String newpk_transport) {
		pk_transport = newpk_transport;
	}

	/**
	 * 属性pk_corp的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return String
	 */
	public String getPk_corp() {
		return pk_corp;
	}

	/**
	 * 属性pk_corp的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newPk_corp
	 *            String
	 */
	public void setPk_corp(String newPk_corp) {

		pk_corp = newPk_corp;
	}

	/**
	 * 属性pk_send的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return String
	 */
	public String getPk_send() {
		return pk_send;
	}

	/**
	 * 属性pk_send的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newPk_send
	 *            String
	 */
	public void setPk_send(String newPk_send) {

		pk_send = newPk_send;
	}

	/**
	 * 属性pk_transport_b的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return String
	 */
	public String getPk_transport_b() {
		return pk_transport_b;
	}

	/**
	 * 属性pk_transport_b的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newPk_transport_b
	 *            String
	 */
	public void setPk_transport_b(String newPk_transport_b) {

		pk_transport_b = newPk_transport_b;
	}

	/**
	 * 属性pk_arrive的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return String
	 */
	public String getPk_arrive() {
		return pk_arrive;
	}

	/**
	 * 属性pk_arrive的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newPk_arrive
	 *            String
	 */
	public void setPk_arrive(String newPk_arrive) {

		pk_arrive = newPk_arrive;
	}

	/**
	 * 属性remarks的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return String
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * 属性remarks的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newRemarks
	 *            String
	 */
	public void setRemarks(String newRemarks) {

		remarks = newRemarks;
	}

	/**
	 * 属性pk_transport的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return String
	 */
	public String getPk_yunshu() {
		return pk_yunshu;
	}

	/**
	 * 属性pk_transport的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newPk_transport
	 *            String
	 */
	public void setpk_yunshu(String newpk_yunshu) {

		pk_yunshu = newpk_yunshu;
	}

	/**
	 * 属性carprice的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getCarprice() {
		return carprice;
	}

	/**
	 * 属性carprice的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newCarprice
	 *            UFDouble
	 */
	public void setCarprice(UFDouble newCarprice) {

		carprice = newCarprice;
	}

	/**
	 * 属性dr的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * 属性dr的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newDr
	 *            Integer
	 */
	public void setDr(Integer newDr) {

		dr = newDr;
	}

	/**
	 * 属性shippingprice_a的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getShippingprice_a() {
		return shippingprice_a;
	}

	/**
	 * 属性shippingprice_a的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newShippingprice_a
	 *            UFDouble
	 */
	public void setShippingprice_a(UFDouble newShippingprice_a) {

		shippingprice_a = newShippingprice_a;
	}

	/**
	 * 属性actualshippingprice的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getActualshippingprice() {
		return actualshippingprice;
	}

	/**
	 * 属性actualshippingprice的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newActualshippingprice
	 *            UFDouble
	 */
	public void setActualshippingprice(UFDouble newActualshippingprice) {

		actualshippingprice = newActualshippingprice;
	}

	/**
	 * 属性rkscprice的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getRkscprice() {
		return rkscprice;
	}

	/**
	 * 属性rkscprice的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newRkscprice
	 *            UFDouble
	 */
	public void setRkscprice(UFDouble newRkscprice) {

		rkscprice = newRkscprice;
	}

	/**
	 * 属性rkscprice_c的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getRkscprice_c() {
		return rkscprice_c;
	}

	/**
	 * 属性rkscprice_c的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newRkscprice_c
	 *            UFDouble
	 */
	public void setRkscprice_c(UFDouble newRkscprice_c) {

		rkscprice_c = newRkscprice_c;
	}

	/**
	 * 属性mtzbfprice的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getMtzbfprice() {
		return mtzbfprice;
	}

	/**
	 * 属性mtzbfprice的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newMtzbfprice
	 *            UFDouble
	 */
	public void setMtzbfprice(UFDouble newMtzbfprice) {

		mtzbfprice = newMtzbfprice;
	}

	/**
	 * 属性ts的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return UFDateTime
	 */
	public UFDateTime getTs() {
		return ts;
	}

	/**
	 * 属性ts的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newTs
	 *            UFDateTime
	 */
	public void setTs(UFDateTime newTs) {

		ts = newTs;
	}

	/**
	 * 属性sjfee的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getSjfee() {
		return sjfee;
	}

	/**
	 * 属性sjfee的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newSjfee
	 *            UFDouble
	 */
	public void setSjfee(UFDouble newSjfee) {

		sjfee = newSjfee;
	}

	/**
	 * 属性shippingprice的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getShippingprice() {
		return shippingprice;
	}

	/**
	 * 属性shippingprice的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newShippingprice
	 *            UFDouble
	 */
	public void setShippingprice(UFDouble newShippingprice) {

		shippingprice = newShippingprice;
	}

	/**
	 * 属性nhcyprice的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNhcyprice() {
		return nhcyprice;
	}

	/**
	 * 属性nhcyprice的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newNhcyprice
	 *            UFDouble
	 */
	public void setNhcyprice(UFDouble newNhcyprice) {

		nhcyprice = newNhcyprice;
	}

	/**
	 * 属性dieselprice的Getter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return UFDouble
	 */
	public UFDouble getDieselprice() {
		return dieselprice;
	}

	/**
	 * 属性dieselprice的Setter方法.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newDieselprice
	 *            UFDouble
	 */
	public void setDieselprice(UFDouble newDieselprice) {

		dieselprice = newDieselprice;
	}

	/**
	 * 验证对象各属性之间的数据逻辑正确性.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @exception nc.vo.pub.ValidationException
	 *                如果验证失败,抛出 ValidationException,对错误进行解释.
	 */
	public void validate() throws ValidationException {

		ArrayList errFields = new ArrayList(); // errFields record those null

		// fields that cannot be null.
		// 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:

		if (pk_transport_b == null) {
			errFields.add(new String("pk_transport_b"));
		}

		StringBuffer message = new StringBuffer();
		message.append("下列字段不能为空:");
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
	 * 取得父VO主键字段.
	 * <p>
	 * 创建日期:2012-7-2
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return "pk_transport";

	}

	/**
	 * <p>
	 * 取得表主键.
	 * <p>
	 * 创建日期:2012-7-2
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_transport_b";
	}

	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:2012-7-2
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "ehpta_transport_b";
	}

	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:2012-7-2
	 */
	public EhptaTransportBVO() {

		super();
	}

	/**
	 * 使用主键进行初始化的构造子.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newPk_transport_b
	 *            主键值
	 */
	public EhptaTransportBVO(String newPk_transport_b) {

		// 为主键字段赋值:
		pk_transport_b = newPk_transport_b;

	}

	/**
	 * 返回对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_transport_b;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @param newPk_transport_b
	 *            String
	 */
	public void setPrimaryKey(String newPk_transport_b) {

		pk_transport_b = newPk_transport_b;

	}

	/**
	 * 返回数值对象的显示名称.
	 * 
	 * 创建日期:2012-7-2
	 * 
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "ehpta_transport_b";

	}
}
