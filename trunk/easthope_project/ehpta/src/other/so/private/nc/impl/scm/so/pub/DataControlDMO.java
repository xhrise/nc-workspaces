package nc.impl.scm.so.pub;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.bs.uap.lock.PKLock;

import nc.impl.ehpta.pub.UAPServQueryBS;
import nc.impl.ehpta.pub.UifService;
import nc.impl.scm.so.so002.SaleinvoiceDMO;
import nc.impl.scm.so.so016.SOToolsDMO;
import nc.itf.ic.service.IICToSO;
import nc.itf.scm.so.pub.IDataControlDMO;
import nc.itf.scm.so.pub.ISOInventoryDiscount;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.itf.uap.sf.IOperateLogService;
import nc.itf.uap.sf.IServiceProviderSerivce;

import nc.ui.scm.so.SaleBillType;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.IPFConfigInfo;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.pub.GeneralSqlString;
import nc.vo.so.pub.Operlog;
import nc.vo.so.so001.ISaleOrderAction;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.soreceive.SaleReceiveVO;

/**
 * ����(��д)������ 
 * 
 * �������ڣ�(2001-6-22 8:40:22)
 * 
 * @author���ν�
 * 
 */
public class DataControlDMO extends nc.bs.pub.DataManageObject implements
		IDataControlDMO {
	/**
	 * DataControlDMO ������ע�⡣
	 * 
	 * @exception javax.naming.NamingException
	 *                �쳣˵����
	 * @exception nc.bs.pub.SystemException
	 *                �쳣˵����
	 */
	public DataControlDMO() throws javax.naming.NamingException,
			nc.bs.pub.SystemException {
		super();
	}

	/**
	 * DataControlDMO ������ע�⡣
	 * 
	 * @param dbName
	 *            java.lang.String
	 * @exception javax.naming.NamingException
	 *                �쳣˵����
	 * @exception nc.bs.pub.SystemException
	 *                �쳣˵����
	 */
	public DataControlDMO(String dbName) throws javax.naming.NamingException,
			nc.bs.pub.SystemException {
		super(dbName);
	}

	/**
	 * �Զ�ȡ����������״̬��
	 * 
	 * �������ڣ�(2001-6-21 14:00:35)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param strDepartmentID
	 *            java.lang.String
	 */
	public void autoSetInventoryCancelFinish(AggregatedValueObject bill)
			throws Exception {
		/** ********************************************************** */
		// ������ϵͳ����ӿڣ�
		beforeCallMethod("nc.bs.so.pub.DataControlDMO",
				"autoSetnventoryFinish", new Object[] { bill });
		/** ********************************************************** */

		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
		CircularlyAccessibleValueObject head = bill.getParentVO();
		Object headid = head.getAttributeValue("cgeneralhid");
		if (headid == null || headid.toString().trim().length() == 0) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sopub", "UPPsopub-000075")/*
																			 * @res
																			 * "�ӿڲ�������idΪ�գ�"
																			 */);
		}

		if (billbody == null || billbody.length <= 0)
			return;

		Object type = billbody[0].getAttributeValue("cfirsttype");
		if (type == null)
			return;
		if (type.equals("30") || type.equals("3A")) {
		} else
			return;

		ArrayList alist = new ArrayList();

		for (int i = 0; i < billbody.length; i++) {
			Object id = billbody[i].getAttributeValue("cfirstbillbid");
			if (id != null && !alist.contains(id)) {
				alist.add(id);
			}
		}

		if (alist.size() <= 0)
			return;

		// ��ѯ��������Ϣ
		nc.impl.scm.so.so001.SaleOrderDMO saledmo = new nc.impl.scm.so.so001.SaleOrderDMO();
		nc.vo.so.so001.SaleorderBVO[] oldordbvos = (nc.vo.so.so001.SaleorderBVO[]) saledmo
				.queryBodyDataForUpdateStatus((String[]) alist
						.toArray(new String[alist.size()]));
		if (oldordbvos == null || oldordbvos.length <= 0)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sopub", "UPPsopub-000076")/*
																			 * @res
																			 * "��ѯ��������Ϣʧ��"
																			 */);

		// �������״̬�߼�
		saledmo.processOutState(oldordbvos);

		// �������һ�γ�������
		saledmo.setOrdLastDate("dlastoutdate", oldordbvos, null);

		/** ********************************************************** */
		// ������ϵͳ����ӿڣ�
		afterCallMethod("nc.bs.so.pub.DataControlDMO", "autoSetnventoryFinish",
				new Object[] { bill });
		/** ********************************************************** */
	}

	public void autoSetInventoryFinish(AggregatedValueObject bill)
			throws Exception {
		/** ********************************************************** */
		// ������ϵͳ����ӿڣ�
		beforeCallMethod("nc.bs.so.pub.DataControlDMO",
				"autoSetnventoryFinish", new Object[] { bill });
		/** ********************************************************** */

		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
		CircularlyAccessibleValueObject head = bill.getParentVO();
		Object headid = head.getAttributeValue("cgeneralhid");
		if (headid == null || headid.toString().trim().length() == 0) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sopub", "UPPsopub-000075")/*
																			 * @res
																			 * "�ӿڲ�������idΪ�գ�"
																			 */);
		}

		if (billbody == null || billbody.length <= 0)
			return;

		Object type = billbody[0].getAttributeValue("cfirsttype");
		if (type == null)
			return;
		if (type.equals("30") || type.equals("3A")) {
		} else
			return;

		ArrayList alist = new ArrayList();

		for (int i = 0; i < billbody.length; i++) {
			Object id = billbody[i].getAttributeValue("cfirstbillbid");
			if (id != null && !alist.contains(id)) {
				alist.add(id);
			}
		}

		if (alist.size() <= 0)
			return;

		// ��ѯ��������Ϣ
		nc.impl.scm.so.so001.SaleOrderDMO saledmo = new nc.impl.scm.so.so001.SaleOrderDMO();
		nc.vo.so.so001.SaleorderBVO[] oldordbvos = (nc.vo.so.so001.SaleorderBVO[]) saledmo
				.queryBodyDataForUpdateStatus((String[]) alist
						.toArray(new String[alist.size()]));
		if (oldordbvos == null || oldordbvos.length <= 0)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sopub", "UPPsopub-000076")/*
																			 * @res
																			 * "��ѯ��������Ϣʧ��"
																			 */);

		// �������״̬�߼�
		saledmo.processOutState(oldordbvos);

		// �������һ�γ�������
		saledmo.setOrdLastDate("dlastoutdate", oldordbvos, (UFDate) head
				.getAttributeValue("dbilldate"));

		/** ********************************************************** */
		// ������ϵͳ����ӿڣ�
		afterCallMethod("nc.bs.so.pub.DataControlDMO", "autoSetnventoryFinish",
				new Object[] { bill });
		/** ********************************************************** */
	}
	
	/**
	 * �Զ�������Ʊ״̬�� �������ڣ�(2001-6-21 14:00:35)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param strDepartmentID
	 *            java.lang.String
	 */
	public void autoSetInvoicetFinish(AggregatedValueObject bill)
			throws SQLException, BusinessException,
			javax.naming.NamingException, nc.bs.pub.SystemException {
		//TODO pub_function
	}

	/**
	 * ���ߣ���־ƽ ���ܣ��ͷŵ����� ������AggregatedValueObject ���أ�void ���⣺ �������ڣ�(2002-4-15
	 * 10:27:42) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void freePkForVo(nc.vo.pub.AggregatedValueObject vo)
			throws BusinessException, SQLException,
			javax.naming.NamingException, javax.ejb.RemoveException {
		try {
			// ����Ա
			String sCuser = null;
			sCuser = (String) vo.getParentVO().getAttributeValue("coperatorid");
			if (sCuser == null || sCuser.trim().equals(""))
				throw new BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"sopub", "UPPsopub-000077")/*
															 * @res
															 * "��ȡ����Ա�������ܽ�����ز�����"
															 */);
			String[] saPk = getPksForVoFree(vo);
			// nc.vo.scm.pub.SCMEnv.out("------------------------------------------");
			// nc.vo.scm.pub.SCMEnv.out("=====================USER:" + sCuser);
			// for(int i = 0;i<saPk.length;i++){
			// nc.vo.scm.pub.SCMEnv.out("=====================ID:" + saPk[i]);
			// }
			// nc.vo.scm.pub.SCMEnv.out("------------------------------------------");
			PKLock lock = PKLock.getInstance();
			if (saPk != null) {
				lock.releaseBatchLock(saPk, sCuser, "");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * ���ԭʼ�� �������ڣ�(2001-9-3 9:30:24)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private UFDouble getOldMoney(String strBillType, String strID)
			throws SQLException {
		UFDouble nmoney = new UFDouble(0.0);
		// String strSQL = "Select nmny From ";
		String strSQL = "Select nsummny From ";
		String strField = null;

		// ����
		if (strBillType.equals(SaleBillType.SaleOrder)) {
			strSQL = strSQL + " so_saleorder_b ";
			strField = "corder_bid";
		}
		// ��Ʊ
		if (strBillType.equals(SaleBillType.SaleInvoice)) {
			strSQL = strSQL + " so_saleinvoice_b ";
			strField = "cinvoice_bid";
		}
		
		/**v5.5������ԭ���߼�������*/
		// ������
		/*if (strBillType.equals(SaleBillType.SaleReceipt)) {
			strSQL = strSQL + " so_salereceipt_b ";
			strField = "creceipt_bid";
		}*/

		strSQL = strSQL + " Where " + strField + " =  ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			// pk
			stmt.setString(1, strID);

			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				BigDecimal n = (BigDecimal) rs.getObject(1);
				nmoney = (n == null ? new UFDouble(0) : new UFDouble(n));
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return nmoney;
	}

	/**
	 * ���ԭʼ������ �������ڣ�(2001-9-3 9:30:24)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private UFDouble getOldNumber(String strBillType, String strID)
			throws SQLException {
		UFDouble nnumber = new UFDouble(0.0);
		String strSQL = "Select nnumber From ";
		String strField = null;

		// ����
		if (strBillType.equals(SaleBillType.SaleOrder)) {
			strSQL = strSQL + " so_saleorder_b ";
			strField = "corder_bid";
		}
		// ��Ʊ
		if (strBillType.equals(SaleBillType.SaleInvoice)) {
			strSQL = strSQL + " so_saleinvoice_b ";
			strField = "cinvoice_bid";
		}
		// ������
		if (strBillType.equals(SaleBillType.SaleReceive)) {
			strSQL = strSQL + " so_salereceive_b ";
			strField = "csalereceiveid_bid";
		}

		strSQL = strSQL + " Where " + strField + " =  ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			// pk
			stmt.setString(1, strID);

			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				BigDecimal n = (BigDecimal) rs.getObject(1);
				nnumber = (n == null ? new UFDouble(0) : new UFDouble(n));
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return nnumber;
	}

	/**
	 * ���ߣ���־ƽ ���ܣ������㷨,�����ݺ��ͷŵ�����ʱ����ȡ������Ҫ������ID�� ������AggregatedValueObject
	 * ���أ�String[] ���⣺ �������ڣ�(2002-4-15 10:27:42) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return String[]
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	private String[] getPksForVo(nc.vo.pub.AggregatedValueObject vo)
			throws BusinessException {
		String[] saRslt = null;
		// �Ϸ��Լ��
		if (vo == null)
			return null;
		if (vo.getParentVO() == null
				&& (vo.getChildrenVO() == null || vo.getChildrenVO().length <= 0))
			return null;
		// Vector vTmp = new Vector();
		if (vo.getParentVO() == null) {
			nc.vo.scm.pub.SCMEnv.out("���ݴ����޵��ݱ�ͷ����,����ʧ��");
			return null;
		}
		// ����ID����
		Vector vPk = new Vector();
		// ����������־
		boolean bIsNew = (vo.getParentVO().getPrimaryKey() == null || vo
				.getParentVO().getPrimaryKey().trim().equals(""));
		String sTmp = null;
		// �������������
		if (bIsNew) {
			// �Ƿ��в��յ��ϲ�����
			boolean bIsRef = false;
			if (vo.getChildrenVO() == null || vo.getChildrenVO().length <= 0) {
				throw new BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"sopub", "UPPsopub-000078")/*
															 * @res
															 * "���ݴ��������ĵ��ݱ���Ϊ��,����ʧ��"
															 */);
			}
			for (int i = 0; i < vo.getChildrenVO().length; i++) {
				if (vo.getClass() == nc.vo.so.so001.SaleOrderVO.class
						|| vo.getClass() == nc.vo.so.soreceive.SaleReceiveVO.class)
					sTmp = (String) vo.getChildrenVO()[i]
							.getAttributeValue("csourcebillbodyid");
				else
					sTmp = (String) vo.getChildrenVO()[i]
							.getAttributeValue("cupsourcebillbodyid");
				// sTmp = (String)
				// vo.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid");
				if (sTmp != null && !sTmp.trim().equals("")) {
					bIsRef = true;
					break;
				}
			}
			// ������е����о�Ϊ����
			if (!bIsRef) {
				nc.vo.scm.pub.SCMEnv.out("������ȫ�����ƣ����ؼ���");
				return null;
			}
			for (int i = 0; i < vo.getChildrenVO().length; i++) {
				if (vo.getClass() == nc.vo.so.so001.SaleOrderVO.class
						|| vo.getClass() == nc.vo.so.soreceive.SaleReceiveVO.class)
					sTmp = (String) vo.getChildrenVO()[i]
							.getAttributeValue("csourcebillbodyid");
				else
					sTmp = (String) vo.getChildrenVO()[i]
							.getAttributeValue("cupsourcebillbodyid");
				// sTmp = (String)
				// vo.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid");
				if (sTmp != null && !sTmp.trim().equals("")) {
					if (!vPk.contains(sTmp))
						vPk.addElement(sTmp);
					if (vo.getClass() == nc.vo.so.so001.SaleOrderVO.class
							|| vo.getClass() == nc.vo.so.soreceive.SaleReceiveVO.class)
						sTmp = (String) vo.getChildrenVO()[i]
								.getAttributeValue("csourcebillid");
					else
						sTmp = (String) vo.getChildrenVO()[i]
								.getAttributeValue("cupsourcebillid");
					// sTmp = (String)
					// vo.getChildrenVO()[i].getAttributeValue("cupsourcebillid");
					if (sTmp == null || sTmp.trim().equals("")) {
						nc.vo.scm.pub.SCMEnv
								.out("�������ݴ��󣺲��յĵ����ϲ���ID�Ҳ���ƥ����ϲ�ͷID,����ʧ��");
						return null;
					}
					if (!vPk.contains(sTmp))
						vPk.addElement(sTmp);
				}
			}
		}
		// ������޸ĵ���
		else {
			sTmp = vo.getParentVO().getPrimaryKey();
			if (sTmp == null || sTmp.trim().equals("")) {
				nc.vo.scm.pub.SCMEnv.out("���ݴ����Ҳ�����ͷID,����ʧ��");
				return null;
			}
			vPk.addElement(sTmp);
			if (vo.getChildrenVO() != null && vo.getChildrenVO().length > 0) {
				for (int i = 0; i < vo.getChildrenVO().length; i++) {
					sTmp = vo.getChildrenVO()[i].getPrimaryKey();
					if (sTmp != null && sTmp.trim().length() > 0
							&& !vPk.contains(sTmp))
						vPk.addElement(sTmp);
				}
			}
		}
		if (vPk != null && vPk.size() > 0) {
			saRslt = new String[vPk.size()];
			vPk.copyInto(saRslt);
		}
		// �������������
		return saRslt;

	}

	/**
	 * ���ߣ���־ƽ ���ܣ������㷨,�����ݺ��ͷŵ�����ʱ����ȡ������Ҫ������ID�� ������AggregatedValueObject
	 * ���أ�String[] ���⣺ �������ڣ�(2002-4-15 10:27:42) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-17
	 * ��ӡ�� ���ݱ�����ܽ����ϲ���Դ���ݣ��޸�Ϊ��󻯼���
	 * 
	 */
	private String[] getPksForVoFree(nc.vo.pub.AggregatedValueObject vo)
			throws BusinessException, SQLException {
		String[] saRslt = null;

		// �Ϸ��Լ��
		if (vo == null)
			return null;

		// Vector vTmp = new Vector();
		if (vo.getParentVO() == null) {
			nc.vo.scm.pub.SCMEnv.out("���ݴ����޵��ݱ�ͷ����,����ʧ��");
			return null;
		}

		// ����ID����
		Vector vPk = new Vector();
		String sTmp = null;

		sTmp = vo.getParentVO().getPrimaryKey();
		if (sTmp != null && !sTmp.trim().equals("")) {
			vPk.addElement(sTmp);
		}

		if (vo.getChildrenVO() != null && vo.getChildrenVO().length > 0) {
			for (int i = 0; i < vo.getChildrenVO().length; i++) {
				if (vo.getClass() == nc.vo.so.so001.SaleOrderVO.class)
					sTmp = (String) vo.getChildrenVO()[i]
							.getAttributeValue("csourcebillbodyid");
				else
					sTmp = (String) vo.getChildrenVO()[i]
							.getAttributeValue("cupsourcebillbodyid");

				// sTmp = (String)
				// vo.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid");
				if (sTmp != null && !sTmp.trim().equals("")) {
					if (!vPk.contains(sTmp))
						vPk.addElement(sTmp);
					if (vo.getClass() == nc.vo.so.so001.SaleOrderVO.class)
						sTmp = (String) vo.getChildrenVO()[i]
								.getAttributeValue("csourcebillid");
					else
						sTmp = (String) vo.getChildrenVO()[i]
								.getAttributeValue("cupsourcebillid");
					// sTmp = (String)
					// vo.getChildrenVO()[i].getAttributeValue("cupsourcebillid");
					if (sTmp == null || sTmp.trim().equals("")) {
						throw new BusinessException(
								nc.bs.ml.NCLangResOnserver.getInstance()
										.getStrByID("sopub", "UPPsopub-000079")/*
																				 * @res
																				 * "�������ݴ��󣺲��յĵ����ϲ���ID�Ҳ���ƥ����ϲ�ͷID,����ʧ��"
																				 */);

					}
					if (!vPk.contains(sTmp)) {
						vPk.addElement(sTmp);
					}
				}

				sTmp = vo.getChildrenVO()[i].getPrimaryKey();
				if (sTmp != null && sTmp.trim().length() > 0
						&& !vPk.contains(sTmp))
					vPk.addElement(sTmp);

			}
		}

		if (vPk != null && vPk.size() > 0) {
			saRslt = new String[vPk.size()];
			vPk.copyInto(saRslt);
		}
		return saRslt;

	}

	/**
	 * ������Դ����ID��
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 */
	private String getSourceBillTypeID(String strDetailID)
			throws java.sql.SQLException, BusinessException {
		String strSourceID = null;

		String strSQL = "Select creceipttype from so_saleinvoice_b Where cinvoice_bid ";

		strSQL = strSQL + " =  ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			// pk
			stmt.setString(1, strDetailID);

			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {

				strSourceID = rs.getString(1);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return strSourceID;
	}


	/**
	 * ���ߣ���־ƽ ���ܣ������� ������AggregatedValueObject ���أ�UFBoolean ���⣺ �������ڣ�(2002-4-15
	 * 10:27:42) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public UFBoolean lockPkForVo(nc.vo.pub.AggregatedValueObject vo) throws BusinessException {

		String[] saPk = getPksForVo(vo);
		if (saPk == null || saPk.length == 0)
			return UFBoolean.TRUE;

		for (int i = 0; i < saPk.length; i++) {
			String pk = saPk[i];
			if (!PKLock.getInstance().addDynamicLock(pk)) {
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub",
						"UPPsopub-000080")/*
											 * @res "���ڲ������������Ժ�����"
											 */);
			}
		}

		return UFBoolean.TRUE;
	}

//	/**
//	 * �ɱ��� �������ڣ�(2001-6-21 14:00:35)
//	 * 
//	 * @return nc.vo.pub.lang.UFDouble
//	 * @param strDepartmentID
//	 *            java.lang.String
//	 */
//	public void setCostmny(AggregatedValueObject bill) throws SQLException,
//			BusinessException {
//
//		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
//
//		String sql = "update so_saleexecute set ncostmny = ? where csale_bid = ?";
//
//		Connection con = null;
//		PreparedStatement stmt = null;
//
//		try {
//			con = getConnection();
//			stmt = con.prepareStatement(sql);
//
//			for (int i = 0; i < billbody.length; i++) {
//				// monny
//				UFDouble nmny = (UFDouble) billbody[i]
//						.getAttributeValue("nmny");
//				stmt.setBigDecimal(1, nmny.toBigDecimal());
//				// csale_bid
//				stmt.setString(2, billbody[i].getPrimaryKey());
//				if (stmt.executeUpdate() == 0) {
//					BusinessException e = new BusinessException(
//							nc.bs.ml.NCLangResOnserver.getInstance()
//									.getStrByID("sopub", "UPPsopub-000081")/*
//																			 * @res
//																			 * "�ɱ�����дʧ��"
//																			 */);
//					throw e;
//				}
//				;
//			}
//		} finally {
//			try {
//				if (stmt != null) {
//					stmt.close();
//				}
//			} catch (Exception e) {
//			}
//			try {
//				if (con != null) {
//					con.close();
//				}
//			} catch (Exception e) {
//			}
//		}
//
//	}

	/**
	 * �ۼ�(��д)��Ʊ������ �������ڣ�(2001-6-21 14:00:35)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param strDepartmentID
	 *            java.lang.String
	 */
	public void setDecreaseInvoiceMoney(AggregatedValueObject bill)
			throws SQLException, BusinessException {
		/** ********************************************************** */
		// ������ϵͳ����ӿڣ�
		beforeCallMethod("nc.bs.so.pub.DataControlDMO",
				"setTotalInvoiceNumber", new Object[] { bill });
		/** ********************************************************** */

		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
		if (billbody == null || billbody.length <= 0)
			return;

		// �����ԴΪ���۶���ֱ�ӻ�д
		// �����ԴΪ���۳��ⵥ�����ܰ������۷������������۶���
		// �����Դ�ڳ����۳��ⵥ ���ڳ����۶���

		// ��Դ����ID csourcebillhid
		String csourcebillhid = null;
		// ��Դ���ݸ���ID csourcebiibid
		String csourcebiibid = null;

		// String creceipttype = null;

		UFDouble noutnum;
//		String sourceBillID[];

		String SourceBillTypeID;

		String SQLUpdate = "update so_saleexecute set ntotalinvoicemny=isnull(ntotalinvoicemny,0) - ? where csale_bid = ?  and csaleid = ? ";

		Connection con = null;
		PreparedStatement stmt = null;

		String errMsg = null;

		try {

			for (int i = 0; i < bill.getChildrenVO().length; i++) {
				// ���۷�Ʊ���ӱ�VO
				CircularlyAccessibleValueObject bodyVO = bill.getChildrenVO()[i];

				if (bodyVO.getAttributeValue("cupreceipttype") != null) {

					// // ��Դ����type creceipttype
					// creceipttype = bodyVO.getAttributeValue("creceipttype")
					// == null ? ""
					// : bodyVO.getAttributeValue("creceipttype")
					// .toString();

					// ��Դ����ID csourcebillhid
					csourcebillhid = bodyVO
							.getAttributeValue("cupsourcebillid").toString();
					// ��Դ���ݸ���ID csourcebiibid
					csourcebiibid = bodyVO.getAttributeValue(
							"cupsourcebillbodyid").toString();
					// ���� noutnum
					noutnum = (bodyVO.getAttributeValue("nsummny") == null ? new UFDouble(
							"0")
							: new UFDouble(bodyVO.getAttributeValue("nsummny")
									.toString()));

					// �����Դ�����۶���/�ڳ����۶��� ֱ�ӻ�д���۶����Ŀ�Ʊ����
					if (bodyVO.getAttributeValue("cupreceipttype").equals(
							nc.ui.scm.so.SaleBillType.SaleOrder)) {

						con = getConnection();
						stmt = con.prepareStatement(SQLUpdate);
						stmt.setBigDecimal(1, noutnum.toBigDecimal());
						// csale_bid
						stmt.setString(2, csourcebiibid);
						// csaleid
						stmt.setString(3, csourcebillhid);
						stmt.executeUpdate();
						// �ر�����
						stmt.close();
						con.close();

					} else {
						// �����ԴΪ���۳��ⵥ�����ܰ������۷������������۶���

						if (bodyVO.getAttributeValue("cupreceipttype").equals(
								nc.ui.scm.so.SaleBillType.SaleOutStore)) {

							// ��һ�� ���۳��ⵥ�ӱ�
							// �ۼƿ�Ʊ����
							// SQLUpdate =
							// "update ic_general_bb3 set
							// nsignnum=isnull(nsignnum,0) + ? where cgeneralbid
							// = ? and cgeneralhid = ?";

							// con = getConnection();
							// stmt = con.prepareStatement(SQLUpdate);
							// stmt.setBigDecimal(1, noutnum.toBigDecimal());
							// csale_bid
							// stmt.setString(2, csourcebiibid);
							// csaleid
							// stmt.setString(3, csourcebillhid);
							// stmt.executeUpdate();
							// �ر�����
							// stmt.close();
							// con.close();

							// nc.vo.scm.pub.SCMEnv.out("��д���۳��ⵥ���ۼƿ�Ʊ����");
							// �ڶ��� �������۷�Ʊ�����ϲ���Դ

							SourceBillTypeID = getSourceBillTypeID(bodyVO
									.getAttributeValue("cinvoice_bid")
									.toString());

							if (SourceBillTypeID == null) {
								errMsg = nc.bs.ml.NCLangResOnserver
										.getInstance().getStrByID("sopub",
												"UPPsopub-000082")/*
																	 * @res
																	 * "�������۳��ⵥ�ķ�Ʊ�����۳��ⵥ��Դû�д��ݸ���Ʊ"
																	 */;
								BusinessException e = new BusinessException(
										errMsg);
								throw e;

							}

							SQLUpdate = "update so_saleexecute set ntotalinvoicemny=isnull(ntotalinvoicemny,0) - ? where csale_bid = ?  and csaleid = ?";

							// ����������۷�����
							if (SourceBillTypeID
									.equals(nc.ui.scm.so.SaleBillType.SaleReceive)) {
								/**v5.5������ԭ���߼�������*/
								/*
								// �ȸ������۷��������ۼ�ִ������
								// �ӷ�Ʊ�еõ����ϲ�ID

								con = getConnection();
								stmt = con.prepareStatement(SQLUpdate);
								stmt.setBigDecimal(1, noutnum.toBigDecimal());
								// csale_bid
								stmt.setString(2, bodyVO.getAttributeValue(
										"csourcebillbodyid").toString());
								// csaleid
								stmt.setString(3, bodyVO.getAttributeValue(
										"csourcebillid").toString());
								stmt.executeUpdate();
								// �ر�����
								stmt.close();
								con.close();
								nc.vo.scm.pub.SCMEnv
										.out("��д���۳��ⵥ���ϲ㷢�������ۼƿ�Ʊ����");
								// �ٸ������۶������ۼƿ�Ʊ����

								sourceBillID = getSourceID(
										nc.ui.scm.so.SaleBillType.SaleReceipt,
										csourcebiibid);
								con = getConnection();
								stmt = con.prepareStatement(SQLUpdate);
								stmt.setBigDecimal(1, noutnum.toBigDecimal());
								// csale_bid
								stmt.setString(2, sourceBillID[1]);
								// csaleid
								stmt.setString(3, sourceBillID[2]);
								nc.vo.scm.pub.SCMEnv
										.out("��д���۳��ⵥ���ϲ㷢���������۶������ۼƿ�Ʊ����");
							*/} else {
								// �������۶������ۼ�ִ������
								if (SourceBillTypeID
										.equals(nc.ui.scm.so.SaleBillType.SaleOrder)) {
									// �ȸ������۷��������ۼ�ִ������
									// �ӷ�Ʊ�еõ����ϲ�ID

									con = getConnection();
									stmt = con.prepareStatement(SQLUpdate);
									stmt.setBigDecimal(1, noutnum
											.toBigDecimal());
									// csale_bid
									stmt.setString(2, bodyVO.getAttributeValue(
											"csourcebillbodyid").toString());
									// csaleid
									stmt.setString(3, bodyVO.getAttributeValue(
											"csourcebillid").toString());
									stmt.executeUpdate();
									// �ر�����
									stmt.close();
									con.close();
									nc.vo.scm.pub.SCMEnv
											.out("��д���۳��ⵥ���ϲ����۶������ۼƿ�Ʊ����");
								}
							}

						} else {
							// �����Դ�ڳ����۳��ⵥ ���ڳ����۶���

							if (bodyVO
									.getAttributeValue("cupreceipttype")
									.equals(
											nc.ui.scm.so.SaleBillType.SaleInitOutStore)) {

								// //��һ�� ���۳��ⵥ�ӱ�
								// //�ۼƿ�Ʊ����
								// SQLUpdate =
								// "update ic_general_bb3 set
								// nsignnum=isnull(nsignnum,0) - ? where
								// cgeneralbid = ? and cgeneralhid = ?";

								// con = getConnection();
								// stmt = con.prepareStatement(SQLUpdate);
								// stmt.setBigDecimal(1,
								// noutnum.toBigDecimal());
								// //csale_bid
								// stmt.setString(2, csourcebiibid);
								// //csaleid
								// stmt.setString(3, csourcebillhid);
								// stmt.executeUpdate();
								// //�ر�����
								// stmt.close();
								// con.close();

								// nc.vo.scm.pub.SCMEnv.out("��д�ڳ����۳��ⵥ���ۼƿ�Ʊ����");

								SourceBillTypeID = getSourceBillTypeID(bodyVO
										.getAttributeValue("cinvoice_bid")
										.toString());

								if (SourceBillTypeID == null) {
									errMsg = nc.bs.ml.NCLangResOnserver
											.getInstance().getStrByID("sopub",
													"UPPsopub-000083")/*
																		 * @res
																		 * "�����ڳ����۳��ⵥ���ڳ���Ʊ���ڳ����۳��ⵥ��Դû�д��ݸ���Ʊ"
																		 */;
									BusinessException e = new BusinessException(
											errMsg);
									throw e;

								}

								SQLUpdate = "update so_saleexecute set ntotalinvoicemny=isnull(ntotalinvoicemny,0) - ? where csale_bid = ?  and csaleid = ?";

							}

						}

					}

				}
			}

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		/** ********************************************************** */
		// ������ϵͳ����ӿڣ�
		afterCallMethod("nc.bs.so.pub.DataControlDMO", "setTotalInvoiceNumber",
				new Object[] { bill });
		/** ********************************************************** */
	}

	/**
	 * �ۼ�(��д)��Ʊ������ �������ڣ�(2001-6-21 14:00:35)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param strDepartmentID
	 *            java.lang.String
	 */
	public void setDecreaseInvoiceNum(AggregatedValueObject bill)
			throws SQLException, BusinessException{
		/** ********************************************************** */
		// ������ϵͳ����ӿڣ�
		beforeCallMethod("nc.bs.so.pub.DataControlDMO",
				"setTotalInvoiceNumber", new Object[] { bill });
		/** ********************************************************** */

		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
		CircularlyAccessibleValueObject billhead = bill.getParentVO();
		if (billhead == null || billbody == null || billbody.length <= 0)
			return;

		ArrayList alistids = new ArrayList();
		String[] corder_bids = null;

		// �����ԴΪ���۶���ֱ�ӻ�д
		// �����ԴΪ���۳��ⵥ�����ܰ������۷������������۶���
		// �����Դ�ڳ����۳��ⵥ ���ڳ����۶���

		// Դ����ID csourcebillhid
		String csourcebillhid = null;
		// Դ���ݸ���ID csourcebiibid
		String csourcebiibid = null;
		// ��Դ����ID csourcebillhid
		String cupsourcebillhid = null;
		// ��Դ���ݸ���ID csourcebiibid
		String cupsourcebiibid = null;

		String creceipttype = null;

		for (int i = 0, loop = billbody.length; i < loop; i++) {
			creceipttype = billbody[i].getAttributeValue("creceipttype") == null ? ""
					: billbody[i].getAttributeValue("creceipttype").toString();
			if (!"30".equals(creceipttype))
				continue;
			Object id = billbody[i].getAttributeValue("csourcebillbodyid");
			if (id != null && !alistids.contains(id)) {
				alistids.add(id);
			}
		}

		// ����ǰ������Ʊ����������Ʊ״̬
		HashMap hsntotalinvoicenumber = null;
		if (alistids.size() > 0) {
			corder_bids = (String[]) alistids.toArray(new String[alistids
					.size()]);
			hsntotalinvoicenumber = SOToolsDMO.getAnyValueUFDouble(
					"so_saleexecute", "ntotalinvoicenumber", "csale_bid",
					corder_bids, " creceipttype = '30' ");
		}

		// ԭ����
		UFDouble oldnnumber = new UFDouble(0);
		// ԭ����
		UFDouble oldnmny = new UFDouble(0);
		// ԭ����
		UFDouble oldnsubmny = new UFDouble(0);

		Hashtable[] hasdata = getOldData("32", billhead.getPrimaryKey());

		Hashtable hasNumber = hasdata[0];
		Hashtable hasNmny = hasdata[1];
		Hashtable hasSubmny = hasdata[2];

		// String sourceBillID[];
		// String SourceBillTypeID;

		String SQLUpdate = "update so_saleexecute set ntotalinvoicenumber=isnull(ntotalinvoicenumber,0) - ?  ,ntotalinvoicemny=isnull(ntotalinvoicemny,0) - ? ,narsubinvmny=isnull(narsubinvmny,0) - ?  where csale_bid = ?  and csaleid = ? and creceipttype = '30' ";

		Connection con = null;
		PreparedStatement stmt = null;

		// String errMsg = null;

		try {

			for (int i = 0; i < bill.getChildrenVO().length; i++) {
				// ���۷�Ʊ���ӱ�VO

				CircularlyAccessibleValueObject bodyVO = bill.getChildrenVO()[i];

				SaleinvoiceDMO invDMO = new SaleinvoiceDMO();
				if (bodyVO.getAttributeValue("cupreceipttype") != null
						&& bodyVO.getAttributeValue("csourcebillid") != null) {

					// ��Դ����type creceipttype
					creceipttype = (String) bodyVO
							.getAttributeValue("creceipttype");

					// ��Դ����ID csourcebillhid
					cupsourcebillhid = (String) bodyVO
							.getAttributeValue("cupsourcebillid");
					// ��Դ���ݸ���ID csourcebiibid
					cupsourcebiibid = (String) bodyVO
							.getAttributeValue("cupsourcebillbodyid");
					// Դͷ����ID
					csourcebillhid = (String) bodyVO
							.getAttributeValue("csourcebillid");
					// Դͷ���ݸ���ID
					csourcebiibid = (String) bodyVO
							.getAttributeValue("csourcebillbodyid");

					if (hasNumber.containsKey(bodyVO.getPrimaryKey()))
						oldnnumber = (UFDouble) hasNumber.get(bodyVO
								.getPrimaryKey());

					if (hasNmny.containsKey(bodyVO.getPrimaryKey()))
						oldnmny = (UFDouble) hasNmny
								.get(bodyVO.getPrimaryKey());

					if (hasSubmny != null
							&& hasSubmny.containsKey(bodyVO.getPrimaryKey()))
						oldnsubmny = (UFDouble) hasSubmny.get(bodyVO
								.getPrimaryKey());

					String cupreceipttype = (String) bodyVO
							.getAttributeValue("cupreceipttype");
					if (cupreceipttype.equals("32")) {
						SaleinvoiceBVO[] upvos = invDMO
								.queryBodyDataBybid(cupsourcebiibid);
						cupsourcebillhid = (String) upvos[0]
								.getAttributeValue("cupsourcebillid");
						cupsourcebiibid = (String) upvos[0]
								.getAttributeValue("cupsourcebillbodyid");
						cupreceipttype = (String) upvos[0]
								.getAttributeValue("cupreceipttype");
					}

					// �����Դ�����۶���/�ڳ����۶��� ֱ�ӻ�д���۶����Ŀ�Ʊ����
					if (cupreceipttype
							.equals(nc.ui.scm.so.SaleBillType.SaleOrder)) {

						con = getConnection();
						stmt = con.prepareStatement(SQLUpdate);

						// old
						stmt.setBigDecimal(1, oldnnumber.toBigDecimal());
						// old
						stmt.setBigDecimal(2, oldnmny.toBigDecimal());
						// oldsubmny
						stmt.setBigDecimal(3, oldnsubmny.toBigDecimal());
						// csale_bid
						stmt.setString(4, cupsourcebiibid);
						// csaleid
						stmt.setString(5, cupsourcebillhid);
						stmt.executeUpdate();
						// �ر�����
						stmt.close();
						con.close();

					} else {
						// �����ԴΪ���۳��ⵥ�����ܰ������۷������������۶���

						if (cupreceipttype.equals("4C")) {
              // ��һ�� ���۳��ⵥ�ӱ�
              IICToSO icTOso = (IICToSO) NCLocator.getInstance().lookup(IICToSO.class.getName());
              nc.bs.pub.para.SysInitDMO initdmo = new nc.bs.pub.para.SysInitDMO();
              UFDouble so65 = initdmo.getParaDbl((String) billhead.getAttributeValue("pk_corp"),"SO65");
              UFBoolean so64 = initdmo.getParaBoolean((String) billhead.getAttributeValue("pk_corp"),"SO64");
              UFDouble so26 = initdmo.getParaDbl((String) billhead.getAttributeValue("pk_corp"),"SO26");
              UFBoolean so25 = initdmo.getParaBoolean((String) billhead.getAttributeValue("pk_corp"),"SO25");

              // V5���������ⵥ��Ʊ
              if (!so64.booleanValue()) so65 = new UFDouble(0);
              if (!so25.booleanValue()) so26 = new UFDouble(0);
              Hashtable<String, UFDouble> hs = new Hashtable<String, UFDouble>();
              hs.put(cupsourcebiibid, new UFDouble(0).sub(oldnnumber));
              icTOso.reWrite4CInvoiceNum(hs, so65);

							
							// �ۼƿ�Ʊ����
//							String SQLOUTUpdate = "update ic_general_bb3 set nsignnum=isnull(nsignnum,0) - ?  where cgeneralbid = ?  and cgeneralhid = ?";
//
//							con = getConnection();
//							stmt = con.prepareStatement(SQLOUTUpdate);
//
//							stmt.setBigDecimal(1, oldnnumber.toBigDecimal());
//
//							// csale_bid
//							stmt.setString(2, cupsourcebiibid);
//							// csaleid
//							stmt.setString(3, cupsourcebillhid);
//							stmt.executeUpdate();
//							// �ر�����
//							stmt.close();
//							con.close();

							nc.vo.scm.pub.SCMEnv.out("��д���۳��ⵥ���ۼƿ�Ʊ����");

							// �ڶ��� �������۷�Ʊ�����ϲ���Դ

							// �ٸ������۶������ۼƿ�Ʊ����
							if (nc.ui.scm.so.SaleBillType.SaleOrder
									.equals(creceipttype)) {

								con = getConnection();
								stmt = con.prepareStatement(SQLUpdate);
								// old
								stmt
										.setBigDecimal(1, oldnnumber
												.toBigDecimal());
								// old
								stmt.setBigDecimal(2, oldnmny.toBigDecimal());
								// oldsubmny
								stmt
										.setBigDecimal(3, oldnsubmny
												.toBigDecimal());
								// csale_bid
								stmt.setString(4, csourcebiibid);
								// csaleid
								stmt.setString(5, csourcebillhid);
								stmt.executeUpdate();
								// �ر�����
								stmt.close();
								con.close();
								nc.vo.scm.pub.SCMEnv
										.out("��д���۳��ⵥ���ϲ㷢���������۶������ۼƿ�Ʊ����");
							}

						} else {
							// �����Դ�ڳ����۳��ⵥ ���ڳ����۶���

							if (bodyVO
									.getAttributeValue("cupreceipttype")
									.equals(
											nc.ui.scm.so.SaleBillType.SaleInitOutStore)) {

								// ��һ�� ���۳��ⵥ�ӱ�
								// �ۼƿ�Ʊ����
								String SQLOUTUpdate = "update ic_general_bb3 set nsignnum=isnull(nsignnum,0) -?  where cgeneralbid = ?  and cgeneralhid = ?";

								con = getConnection();
								stmt = con.prepareStatement(SQLOUTUpdate);
								stmt
										.setBigDecimal(1, oldnnumber
												.toBigDecimal());
								// csale_bid
								stmt.setString(2, cupsourcebiibid);
								// csaleid
								stmt.setString(3, cupsourcebillhid);
								stmt.executeUpdate();
								// �ر�����
								stmt.close();
								con.close();

								nc.vo.scm.pub.SCMEnv.out("��д�ڳ����۳��ⵥ���ۼƿ�Ʊ����");

								// �ӷ�Ʊ�еõ����ϲ�ID

								if (nc.ui.scm.so.SaleBillType.SaleOrder
										.equals(creceipttype)) {

									con = getConnection();
									stmt = con.prepareStatement(SQLUpdate);
									// old
									stmt.setBigDecimal(1, oldnnumber
											.toBigDecimal());
									// old
									stmt.setBigDecimal(2, oldnmny
											.toBigDecimal());
									// oldsubmny
									stmt.setBigDecimal(3, oldnsubmny
											.toBigDecimal());
									// csale_bid
									stmt.setString(4, csourcebiibid);
									// csaleid
									stmt.setString(5, csourcebillhid);
									stmt.executeUpdate();
									// �ر�����
									stmt.close();
									con.close();
									/*System.out
											.println("��д�ڳ����۳��ⵥ���ϲ��ڳ��ڳ������ۼƿ�Ʊ����");*/
									SCMEnv.out("��д�ڳ����۳��ⵥ���ϲ��ڳ��ڳ������ۼƿ�Ʊ����");
									// �ٸ������۶������ۼƿ�Ʊ����
								}
							}

						}

					}

				}
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		// ����Ʊ״̬
		if (corder_bids != null && corder_bids.length > 0) {

			try {
				// ��ѯ��������Ϣ
				nc.impl.scm.so.so001.SaleOrderDMO saledmo = new nc.impl.scm.so.so001.SaleOrderDMO();
				nc.vo.so.so001.SaleorderBVO[] oldordbvos = (nc.vo.so.so001.SaleorderBVO[]) saledmo
						.queryBodyDataForUpdateStatus(corder_bids);
				if (oldordbvos == null || oldordbvos.length <= 0)
					return;

				if (hsntotalinvoicenumber != null) {
					// �����޸�ǰ�ĳ�������
					for (int i = 0, loop = oldordbvos.length; i < loop; i++) {
						oldordbvos[i]
								.setNtotalinvoicenumber_old((UFDouble) hsntotalinvoicenumber
										.get(oldordbvos[i].getCorder_bid()));
					}
				}

				// modify by river for 2012-08-08
				// �޸Ŀ�Ʊ�ر�״̬������Ʊ��������붩��������ͬʱ��Ʊ�رգ�����ƱΪ����״̬
				for(SaleorderBVO ordb : oldordbvos) {
					if(ordb.getNnumber().sub(ordb.getNtotalinvoicenumber() == null ? new UFDouble("0") : ordb.getNtotalinvoicenumber()).doubleValue() <= 0) {
				
						// ����Ʊ״̬�߼�
						// river
						saledmo.processInvoicendState(new SaleorderBVO[]{ordb});
		
						// �������һ�ο�Ʊ����
						saledmo.setOrdLastDate("dlastinvoicedt", new SaleorderBVO[]{ordb},
								(UFDate) billhead.getAttributeValue("dbilldate"));
					} else {
						try { UAPServQueryBS.iUAPQueryBS.executeQuery("update so_saleexecute set bifinvoicefinish = 'N' where csale_bid = '"+ordb.getCorder_bid()+"'", null); } catch(Exception e) { Logger.error(DataControlDMO.class.getName() + " - 1365 - ����ʱ����ִ�б��¼�Ŀ�Ʊ�ر�״̬"); } ;
					}
				}
				
			} catch (Exception e) { 
				throw new BusinessException(e.getMessage());
			}

		}

		/** ********************************************************** */
		// ������ϵͳ����ӿڣ�
		afterCallMethod("nc.bs.so.pub.DataControlDMO", "setTotalInvoiceNumber",
				new Object[] { bill });
		/** ********************************************************** */
	}

	/**
	 * ����(��д)���������� �������ڣ�(2001-6-21 14:00:35)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param strDepartmentID
	 *            java.lang.String
	 */
	public void setDecreaseReceiptNumber(AggregatedValueObject bill)
			throws SQLException, BusinessException {
		/** ********************************************************** */
		// ������ϵͳ����ӿڣ�
		beforeCallMethod("nc.bs.so.pub.DataControlDMO",
				"setTotalReceiptNumber", new Object[] { bill });
		/** ********************************************************** */
		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
		String sql = "update so_saleexecute set ntotalreceivenumber=isnull(ntotalreceivenumber,0) - ? where csale_bid = ? and csaleid = ?";
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			for (int i = 0; i < billbody.length; i++) {
				// oldnumber
				UFDouble oldnumber = new UFDouble(0);
				if (billbody[i].getStatus() != VOStatus.NEW)
					oldnumber = getOldNumber(bill.getParentVO()
							.getAttributeValue("creceipttype").toString(),
							billbody[i].getPrimaryKey().toString());
				stmt.setBigDecimal(1, oldnumber.toBigDecimal());
				// csale_bid
				stmt.setString(2, billbody[i].getAttributeValue(
						"cupsourcebillbodyid").toString());
				// csaleid
				stmt.setString(3, billbody[i].getAttributeValue(
						"cupsourcebillid").toString());
				stmt.executeUpdate();
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		/** ********************************************************** */
		// ������ϵͳ����ӿڣ�
		afterCallMethod("nc.bs.so.pub.DataControlDMO", "setTotalReceiptNumber",
				new Object[] { bill });
		/** ********************************************************** */
	}

//	/**
//	 * �ۼ�(��д)�ɱ��� �������ڣ�(2001-6-21 14:00:35)
//	 * 
//	 * @return nc.vo.pub.lang.UFDouble
//	 * @param strDepartmentID
//	 *            java.lang.String
//	 */
//	public void setTotalCostmny(AggregatedValueObject bill) throws SQLException {
//
//		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
//
//		String sql = "update so_saleexecute set ntotalcostmny=isnull(ntotalcostmny,0) + ? where csale_bid = ? and csaleid = ?";
//
//		Connection con = null;
//		PreparedStatement stmt = null;
//
//		try {
//			con = getConnection();
//			stmt = con.prepareStatement(sql);
//
//			for (int i = 0; i < billbody.length; i++) {
//				// monny
//				UFDouble nmny = (UFDouble) billbody[i]
//						.getAttributeValue("nmny");
//				if (nmny == null)
//					nmny = new UFDouble(0.0);
//				stmt.setBigDecimal(1, nmny.toBigDecimal());
//				// csale_bid
//				stmt.setString(2, billbody[i].getAttributeValue(
//						"cupsourcebillbodyid").toString());
//				// csaleid
//				stmt.setString(3, billbody[i].getAttributeValue(
//						"cupsourcebillid").toString());
//				stmt.executeUpdate();
//			}
//		} finally {
//			try {
//				if (stmt != null) {
//					stmt.close();
//				}
//			} catch (Exception e) {
//			}
//			try {
//				if (con != null) {
//					con.close();
//				}
//			} catch (Exception e) {
//			}
//		}
//
//	}

	/**
	 * �ۼ�(��д)���������� �������ڣ�(2001-6-21 14:00:35)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param strDepartmentID
	 *            java.lang.String
	 */
	public void setTotalInventoryNumber(AggregatedValueObject bill)
			throws SQLException {
		/** ********************************************************** */
		// ������ϵͳ����ӿڣ�
		beforeCallMethod("nc.bs.so.pub.DataControlDMO",
				"setTotalInventoryNumber", new Object[] { bill });
		/** ********************************************************** */

		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();

		String sql = "update so_saleexecute set ntotalinventorynumber=isnull(ntotalinventorynumber,0) + ? where csale_bid = ? and csaleid = ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);

			for (int i = 0; i < billbody.length; i++) {
				// nc.vo.scm.pub.SCMEnv.out("-----------------------:" +
				// billbody[i].getAttributeValue("nnumber"));
				// nc.vo.scm.pub.SCMEnv.out("-----------------------:" +
				// billbody[i].getAttributeValue("cupsourcebillbodyid"));
				// nc.vo.scm.pub.SCMEnv.out("-----------------------:" +
				// billbody[i].getAttributeValue("cupsourcebillid"));
				if (billbody[i].getAttributeValue("cupsourcebillbodyid") != null) {
					// number
					UFDouble nnumber = (UFDouble) billbody[i]
							.getAttributeValue("nnumber");
					if (nnumber == null)
						nnumber = new UFDouble(0.0);
					stmt.setBigDecimal(1, nnumber.toBigDecimal());
					// csale_bid
					stmt.setString(2, billbody[i].getAttributeValue(
							"cupsourcebillbodyid").toString());
					// csaleid
					stmt.setString(3, billbody[i].getAttributeValue(
							"cupsourcebillid").toString());

					stmt.executeUpdate();
				}
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		/** ********************************************************** */
		// ������ϵͳ����ӿڣ�
		afterCallMethod("nc.bs.so.pub.DataControlDMO",
				"setTotalInventoryNumber", new Object[] { bill });
		/** ********************************************************** */
	}

	/**
	 * �ۼ�(��д)��Ʊ�� �������ڣ�(2001-6-21 14:00:35)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param strDepartmentID
	 *            java.lang.String �޸����ڣ�2003-11-22 �޸��ˣ����� �޸����ݣ���д����Ϊ��˰�ϼ�
	 */
	public void setTotalInvoiceMoney(AggregatedValueObject bill)
			throws SQLException, BusinessException {
		/** ********************************************************** */
		// ������ϵͳ����ӿڣ�
		beforeCallMethod("nc.bs.so.pub.DataControlDMO", "setTotalInvoiceMoney",
				new Object[] { bill });
		/** ********************************************************** */

		// CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
		// �����ԴΪ���۶���ֱ�ӻ�д
		// �����ԴΪ���۳��ⵥ�����ܰ������۷������������۶���
		// �����Դ�ڳ����۳��ⵥ ���ڳ����۶���
		// ��Դ����ID csourcebillhid
		String csourcebillhid = null;
		// ��Դ���ݸ���ID csourcebiibid
		String csourcebiibid = null;

		// ������
		UFDouble nmny = new UFDouble(0.0);
		// ԭ����
		UFDouble oldnmny = new UFDouble(0.0);

//		String sourceBillID[];

		String SourceBillTypeID;

		String SQLUpdate = "update so_saleexecute set ntotalinvoicemny=isnull(ntotalinvoicemny,0) - ? + ? where csale_bid = ?  and csaleid = ?";

		Connection con = null;
		PreparedStatement stmt = null;

		String errMsg = null;

		try {

			for (int i = 0; i < bill.getChildrenVO().length; i++) {
				// ���۷�Ʊ���ӱ�VO
				CircularlyAccessibleValueObject bodyVO = bill.getChildrenVO()[i];

				if (bodyVO.getAttributeValue("cupreceipttype") != null) {
					// ��Դ����ID csourcebillhid
					csourcebillhid = bodyVO
							.getAttributeValue("cupsourcebillid").toString();
					// ��Դ���ݸ���ID csourcebiibid
					csourcebiibid = bodyVO.getAttributeValue(
							"cupsourcebillbodyid").toString();

					if (bodyVO.getStatus() != VOStatus.DELETED) {
						// nmny = (UFDouble)bodyVO.getAttributeValue("nmny");
						nmny = (UFDouble) bodyVO.getAttributeValue("nsummny");
						if (nmny == null)
							nmny = new UFDouble(0.0);
					}
					if (bodyVO.getStatus() != VOStatus.NEW
							&& bodyVO.getStatus() != VOStatus.UNCHANGED) {
						oldnmny = getOldMoney(SaleBillType.SaleInvoice, bodyVO
								.getPrimaryKey());
						if (oldnmny == null)
							oldnmny = new UFDouble(0.0);
					}

					// �����Դ�����۶���/�ڳ����۶��� ֱ�ӻ�д���۶����Ŀ�Ʊ����
					if (bodyVO.getAttributeValue("cupreceipttype").equals(
							nc.ui.scm.so.SaleBillType.SaleOrder)) {

						con = getConnection();
						stmt = con.prepareStatement(SQLUpdate);
						stmt.setBigDecimal(1, oldnmny.toBigDecimal());
						stmt.setBigDecimal(2, nmny.toBigDecimal());
						// csale_bid
						stmt.setString(3, csourcebiibid);
						// csaleid
						stmt.setString(4, csourcebillhid);
						stmt.executeUpdate();
						// �ر�����
						stmt.close();
						con.close();

					} else {
						// �����ԴΪ���۳��ⵥ�����ܰ������۷������������۶���

						if (bodyVO.getAttributeValue("cupreceipttype").equals(
								"4C")) {

							// ��һ�� ���۳��ⵥ�ӱ�
							// �ۼƿ�Ʊ����
							// SQLUpdate =
							// "update ic_general_bb3 set
							// nsignnum=isnull(nsignnum,0) + ? where cgeneralbid
							// = ? and cgeneralhid = ?";

							// con = getConnection();
							// stmt = con.prepareStatement(SQLUpdate);
							// stmt.setBigDecimal(1, nmny.toBigDecimal());
							// csale_bid
							// stmt.setString(2, csourcebiibid);
							// csaleid
							// stmt.setString(3, csourcebillhid);
							// stmt.executeUpdate();
							// �ر�����
							// stmt.close();
							// con.close();

							// nc.vo.scm.pub.SCMEnv.out("��д���۳��ⵥ���ۼƿ�Ʊ����");
							// �ڶ��� �������۷�Ʊ�����ϲ���Դ

							SourceBillTypeID = getSourceBillTypeID(bodyVO
									.getAttributeValue("cinvoice_bid")
									.toString());

							if (SourceBillTypeID == null) {
								errMsg = nc.bs.ml.NCLangResOnserver
										.getInstance().getStrByID("sopub",
												"UPPsopub-000082")/*
																	 * @res
																	 * "�������۳��ⵥ�ķ�Ʊ�����۳��ⵥ��Դû�д��ݸ���Ʊ"
																	 */;
								BusinessException e = new BusinessException(
										errMsg);
								throw e;

							}

							SQLUpdate = "update so_saleexecute set ntotalinvoicemny=isnull(ntotalinvoicemny,0) - ? + ? where csale_bid = ?  and csaleid = ?";

							// ����������۷�����
							if (SourceBillTypeID
									.equals(nc.ui.scm.so.SaleBillType.SaleReceive)) {
								/**v5.5������ԭ���߼�������*/
								/*
								// �ȸ������۷��������ۼ�ִ������
								// �ӷ�Ʊ�еõ����ϲ�ID

								con = getConnection();
								stmt = con.prepareStatement(SQLUpdate);
								stmt.setBigDecimal(1, oldnmny.toBigDecimal());
								stmt.setBigDecimal(2, nmny.toBigDecimal());
								// csale_bid
								stmt.setString(3, bodyVO.getAttributeValue(
										"csourcebillbodyid").toString());
								// csaleid
								stmt.setString(4, bodyVO.getAttributeValue(
										"csourcebillid").toString());
								stmt.executeUpdate();
								// �ر�����
								stmt.close();
								con.close();
								nc.vo.scm.pub.SCMEnv
										.out("��д���۳��ⵥ���ϲ㷢�������ۼƿ�Ʊ����");
								// �ٸ������۶������ۼƿ�Ʊ����

								sourceBillID = getSourceID(
										nc.ui.scm.so.SaleBillType.SaleReceipt,
										csourcebiibid);
								con = getConnection();
								stmt = con.prepareStatement(SQLUpdate);
								stmt.setBigDecimal(1, oldnmny.toBigDecimal());
								stmt.setBigDecimal(2, nmny.toBigDecimal());
								// csale_bid
								stmt.setString(3, sourceBillID[1]);
								// csaleid
								stmt.setString(4, sourceBillID[2]);
								nc.vo.scm.pub.SCMEnv
										.out("��д���۳��ⵥ���ϲ㷢���������۶������ۼƿ�Ʊ����");
							*/} else {
								// �������۶������ۼ�ִ������
								if (SourceBillTypeID
										.equals(nc.ui.scm.so.SaleBillType.SaleOrder)) {
									// �ȸ������۷��������ۼ�ִ������
									// �ӷ�Ʊ�еõ����ϲ�ID

									con = getConnection();
									stmt = con.prepareStatement(SQLUpdate);
									stmt.setBigDecimal(1, oldnmny
											.toBigDecimal());
									stmt.setBigDecimal(2, nmny.toBigDecimal());
									// csale_bid
									stmt.setString(3, bodyVO.getAttributeValue(
											"csourcebillbodyid").toString());
									// csaleid
									stmt.setString(4, bodyVO.getAttributeValue(
											"csourcebillid").toString());
									stmt.executeUpdate();
									// �ر�����
									stmt.close();
									con.close();
									nc.vo.scm.pub.SCMEnv
											.out("��д���۳��ⵥ���ϲ����۶������ۼƿ�Ʊ����");
								}
							}

						} else {
							// �����Դ�ڳ����۳��ⵥ ���ڳ����۶���

							if (bodyVO
									.getAttributeValue("cupreceipttype")
									.equals(
											nc.ui.scm.so.SaleBillType.SaleInitOutStore)) {

								// //��һ�� ���۳��ⵥ�ӱ�
								// //�ۼƿ�Ʊ����
								// SQLUpdate =
								// "update ic_general_bb3 set
								// nsignnum=isnull(nsignnum,0) + ? where
								// cgeneralbid = ? and cgeneralhid = ?";

								// con = getConnection();
								// stmt = con.prepareStatement(SQLUpdate);
								// stmt.setBigDecimal(1, nmny.toBigDecimal());
								// //csale_bid
								// stmt.setString(2, csourcebiibid);
								// //csaleid
								// stmt.setString(3, csourcebillhid);
								// stmt.executeUpdate();
								// //�ر�����
								// stmt.close();
								// con.close();

								// nc.vo.scm.pub.SCMEnv.out("��д�ڳ����۳��ⵥ���ۼƿ�Ʊ����");

								SourceBillTypeID = getSourceBillTypeID(bodyVO
										.getAttributeValue("cinvoice_bid")
										.toString());

								if (SourceBillTypeID == null) {
									errMsg = nc.bs.ml.NCLangResOnserver
											.getInstance().getStrByID("sopub",
													"UPPsopub-000083")/*
																		 * @res
																		 * "�����ڳ����۳��ⵥ���ڳ���Ʊ���ڳ����۳��ⵥ��Դû�д��ݸ���Ʊ"
																		 */;
									BusinessException e = new BusinessException(
											errMsg);
									throw e;

								}

								SQLUpdate = "update so_saleexecute set ntotalinvoicemny=isnull(ntotalinvoicemny,0) - ? + ? where csale_bid = ?  and csaleid = ?";

							}

						}

					}

				}
			}

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		/** ********************************************************** */
		// ������ϵͳ����ӿڣ�
		afterCallMethod("nc.bs.so.pub.DataControlDMO", "setTotalInvoiceMoney",
				new Object[] { bill });
		/** ********************************************************** */
	}

	/**
	 * �ۼ�(��д)��Ʊ�������� �������ڣ�(2001-6-21 14:00:35)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param strDepartmentID
	 *            java.lang.String3 �޸����ڣ�2003-11-22 �޸��ˣ����� �޸����ݣ���д����Ϊ��˰�ϼ�
	 */
	public void setTotalInvoiceNum(AggregatedValueObject bill)
			throws SQLException, BusinessException{

		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
		CircularlyAccessibleValueObject billhead = bill.getParentVO();
		if (billhead == null || billbody == null || billbody.length <= 0)
			return;

		//2007-12-11 xhq
		Vector vTemp = new Vector();
		for(int i = 0; i < billbody.length; i++){
			if(billbody[i].getStatus() == VOStatus.DELETED)
        vTemp.addElement(billbody[i]);
		}
		for(int i = 0; i < billbody.length; i++){
			if(billbody[i].getStatus() == VOStatus.UPDATED) 
        vTemp.addElement(billbody[i]);
		}
		for(int i = 0; i < billbody.length; i++){
			if(billbody[i].getStatus() != VOStatus.UPDATED && billbody[i].getStatus() != VOStatus.DELETED) 
        vTemp.addElement(billbody[i]);
		}

		billbody = new CircularlyAccessibleValueObject[vTemp.size()];
		vTemp.copyInto(billbody);
		//2007-12-11 xhq
		
		ArrayList alistids = new ArrayList();
		String[] corder_bids = null;

		// �����ԴΪ���۶���ֱ�ӻ�д
		// �����ԴΪ���۳��ⵥ�����ܰ������۷������������۶���
		// �����Դ�ڳ����۳��ⵥ ���ڳ����۶���

		// Դ����ID csourcebillhid
		String csourcebillhid = null;
		// Դ���ݸ���ID csourcebiibid
		String csourcebiibid = null;
		// ��Դ����ID csourcebillhid
		String cupsourcebillhid = null;
		// ��Դ���ݸ���ID csourcebiibid
		String cupsourcebiibid = null;

		String creceipttype = null;

		for (int i = 0, loop = billbody.length; i < loop; i++) {
			creceipttype = billbody[i].getAttributeValue("creceipttype") == null ? ""
					: billbody[i].getAttributeValue("creceipttype").toString();
			if (!"30".equals(creceipttype))
				continue;
			Object id = billbody[i].getAttributeValue("csourcebillbodyid");
			if (id != null && !alistids.contains(id)) {
				alistids.add(id);
			}
		}

		// ����ǰ������Ʊ����������Ʊ״̬

		HashMap hsntotalinvoicenumber = null;
		if (alistids.size() > 0) {
			corder_bids = (String[]) alistids.toArray(new String[alistids
					.size()]);
			hsntotalinvoicenumber = SOToolsDMO.getAnyValueUFDouble(
					"so_saleexecute", "ntotalinvoicenumber", "csale_bid",
					corder_bids, " creceipttype = '30' ");

		}

		// ������
		UFDouble nnumber = new UFDouble(0.0);
		// ԭ����
		UFDouble oldnnumber = new UFDouble(0.0);
		// ������
		UFDouble nmny = new UFDouble(0.0);
		// ԭ����
		UFDouble oldnmny = new UFDouble(0.0);
		// ������
		UFDouble nsubmny = new UFDouble(0.0);
		// ԭ����
		UFDouble oldnsubmny = new UFDouble(0.0);

		Hashtable[] hasdata = getOldData(SaleBillType.SaleInvoice, billhead
				.getPrimaryKey());

		Hashtable hasNumber = hasdata[0];
		Hashtable hasNmny = hasdata[1];
		Hashtable hasSubmny = hasdata[2];
    
    HashSet<String> hssourcebid = new HashSet<String>();
		String SQLUpdate = "update so_saleexecute set ntotalinvoicenumber=isnull(ntotalinvoicenumber,0) - ? + ? ,ntotalinvoicemny=isnull(ntotalinvoicemny,0) - ? + ? ,narsubinvmny=isnull(narsubinvmny,0) - ? + ? where csale_bid = ?  and csaleid = ? and creceipttype = '30' ";

		Connection con = null;
		PreparedStatement stmtSQLUpdate = null;

		String SQLOutUpdate = "update ic_general_bb3 set nsignnum=isnull(nsignnum,0) - ? + ? where cgeneralbid = ?  and cgeneralhid = ?";

		PreparedStatement stmtSQLOutUpdate = null;

		try {

			con = getConnection();

			SaleinvoiceDMO invDMO = new SaleinvoiceDMO();

			for (int i = 0; i < billbody.length; i++) {
				// ���۷�Ʊ���ӱ�VO

				CircularlyAccessibleValueObject bodyVO = billbody[i];

				if (bodyVO.getAttributeValue("cupreceipttype") != null
						&& bodyVO.getAttributeValue("csourcebillid") != null) {
					// ��Դ����type creceipttype
					creceipttype = (String) bodyVO
							.getAttributeValue("creceipttype");
					// ��Դ����ID csourcebillhid
					cupsourcebillhid = (String) bodyVO
							.getAttributeValue("cupsourcebillid");
					// ��Դ���ݸ���ID csourcebiibid
					cupsourcebiibid = (String) bodyVO
							.getAttributeValue("cupsourcebillbodyid");
					// Դͷ����ID
					csourcebillhid = (String) bodyVO
							.getAttributeValue("csourcebillid");
					// Դͷ���ݸ���ID
					csourcebiibid = (String) bodyVO
							.getAttributeValue("csourcebillbodyid");

					String cupreceipttype = (String) bodyVO
							.getAttributeValue("cupreceipttype");
					if (cupreceipttype.equals("32")) {
						SaleinvoiceBVO[] upvos = invDMO
								.queryBodyDataBybid(cupsourcebiibid);
						cupsourcebillhid = (String) upvos[0]
								.getAttributeValue("cupsourcebillid");
						cupsourcebiibid = (String) upvos[0]
								.getAttributeValue("cupsourcebillbodyid");
						cupreceipttype = (String) upvos[0]
								.getAttributeValue("cupreceipttype");
					}

					oldnnumber = new UFDouble(0.0);
					oldnmny = new UFDouble(0.0);
					nnumber = new UFDouble(0.0);
					nmny = new UFDouble(0.0);
					if (csourcebillhid == null || csourcebiibid == null
							|| cupsourcebillhid == null
							|| cupsourcebiibid == null) {
						throw new BusinessException(
								"������ԴID���ڿ�ֵ�����ܸ����ۼƿ�Ʊ���������飡");
					}

					if (bodyVO.getStatus() != VOStatus.DELETED) {
						nnumber = bodyVO.getAttributeValue("nnumber") == null ? new UFDouble(
								0)
								: (UFDouble) bodyVO
										.getAttributeValue("nnumber");

						// nmny
						nmny = bodyVO.getAttributeValue("nsummny") == null ? new UFDouble(
								0)
								: (UFDouble) bodyVO
										.getAttributeValue("nsummny");

						// nsubmny
						UFDouble nsubcursummny = bodyVO
								.getAttributeValue("nsubcursummny") == null ? new UFDouble(
								0)
								: (UFDouble) bodyVO
										.getAttributeValue("nsubcursummny");
						nsubmny = nsubcursummny.sub(nmny);
					}
					if (bodyVO.getStatus() != VOStatus.NEW
							&& bodyVO.getStatus() != VOStatus.UNCHANGED) {
						if (hasNumber.containsKey(bodyVO.getPrimaryKey()))
							oldnnumber = (UFDouble) hasNumber.get(bodyVO
									.getPrimaryKey());
						else
							oldnnumber = new UFDouble(0.0);
						if (hasNmny.containsKey(bodyVO.getPrimaryKey()))
							oldnmny = (UFDouble) hasNmny.get(bodyVO
									.getPrimaryKey());
						else
							oldnmny = new UFDouble(0.0);
						if (hasSubmny.containsKey(bodyVO.getPrimaryKey()))
							oldnsubmny = (UFDouble) hasSubmny.get(bodyVO
									.getPrimaryKey());
						else
							oldnsubmny = new UFDouble(0.0);
					}

					// �����Դ�����۶���/�ڳ����۶��� ֱ�ӻ�д���۶����Ŀ�Ʊ����
					if (cupreceipttype
							.equals(nc.ui.scm.so.SaleBillType.SaleOrder)) {

						//2007-11-20 xhq
//						IICToSO icTOso = (IICToSO) NCLocator.getInstance().lookup(IICToSO.class.getName());
//						nc.bs.pub.para.SysInitDMO initdmo = new nc.bs.pub.para.SysInitDMO();
//						UFDouble so26 = initdmo.getParaDbl((String) billhead.getAttributeValue("pk_corp"),"SO26");
//						UFBoolean so25 = initdmo.getParaBoolean((String) billhead.getAttributeValue("pk_corp"),"SO25");
//						if (!so25.booleanValue())
//              so26 = new UFDouble(0);
//						Hashtable<String, UFDouble> hs = new Hashtable<String, UFDouble>();
//						hs.put(csourcebiibid, nnumber.sub(oldnnumber));
//						reWrite30InvoiceNum(hs, so26);
              hssourcebid.add(csourcebiibid);
//						2007-11-20 xhq

						if (stmtSQLUpdate == null)
							stmtSQLUpdate = prepareStatement(con, SQLUpdate);

						// old
						stmtSQLUpdate.setBigDecimal(1, oldnnumber
								.toBigDecimal());
						// new
						stmtSQLUpdate.setBigDecimal(2, nnumber.toBigDecimal());
						// old
						stmtSQLUpdate.setBigDecimal(3, oldnmny.toBigDecimal());
						// new
						stmtSQLUpdate.setBigDecimal(4, nmny.toBigDecimal());
						// oldsubmny
						stmtSQLUpdate.setBigDecimal(5, oldnsubmny
								.toBigDecimal());
						// nsubmny
						stmtSQLUpdate.setBigDecimal(6, nsubmny.toBigDecimal());
						// csale_bid
						stmtSQLUpdate.setString(7, cupsourcebiibid);
						// csaleid
						stmtSQLUpdate.setString(8, cupsourcebillhid);

						executeUpdate(stmtSQLUpdate);

						// nc.vo.scm.pub.SCMEnv.out("��д����----------------ֱ����Դ���۶���-------------");

					} else {
						// �����ԴΪ���۳��ⵥ�����ܰ������۷������������۶���

						if (cupreceipttype.equals("4C")) {

							IICToSO icTOso = (IICToSO) NCLocator.getInstance().lookup(IICToSO.class.getName());
							nc.bs.pub.para.SysInitDMO initdmo = new nc.bs.pub.para.SysInitDMO();
							UFDouble so65 = initdmo.getParaDbl((String) billhead.getAttributeValue("pk_corp"),"SO65");
							UFBoolean so64 = initdmo.getParaBoolean((String) billhead.getAttributeValue("pk_corp"),"SO64");
							UFDouble so26 = initdmo.getParaDbl((String) billhead.getAttributeValue("pk_corp"),"SO26");
							UFBoolean so25 = initdmo.getParaBoolean((String) billhead.getAttributeValue("pk_corp"),"SO25");

							// V5���������ⵥ��Ʊ
							if (!so64.booleanValue()) so65 = new UFDouble(0);
							if (!so25.booleanValue()) so26 = new UFDouble(0);
							Hashtable<String, UFDouble> hs = new Hashtable<String, UFDouble>();
							hs.put(cupsourcebiibid, nnumber.sub(oldnnumber));
							icTOso.reWrite4CInvoiceNum(hs, so65);

							// ��һ�� ���۳��ⵥ�ӱ�

							// �ڶ��� �������۷�Ʊ�����ϲ���Դ
							// �ٸ������۶������ۼƿ�Ʊ����
							if (nc.ui.scm.so.SaleBillType.SaleOrder
									.equals(creceipttype)) {
//								2007-11-20 xhq
//								hs = new Hashtable<String, UFDouble>();
//								hs.put(csourcebiibid, nnumber.sub(oldnnumber));
//								reWrite30InvoiceNum(hs, so26);
                  hssourcebid.add(csourcebiibid);
//								2007-11-20 xhq
								if (stmtSQLUpdate == null)
									stmtSQLUpdate = prepareStatement(con,
											SQLUpdate);

								stmtSQLUpdate.setBigDecimal(1, oldnnumber
										.toBigDecimal());
								stmtSQLUpdate.setBigDecimal(2, nnumber
										.toBigDecimal());
								// old
								stmtSQLUpdate.setBigDecimal(3, oldnmny
										.toBigDecimal());
								// new
								stmtSQLUpdate.setBigDecimal(4, nmny
										.toBigDecimal());
								// oldsubmny
								stmtSQLUpdate.setBigDecimal(5, oldnsubmny
										.toBigDecimal());
								// nsubmny
								stmtSQLUpdate.setBigDecimal(6, nsubmny
										.toBigDecimal());
								// csale_bid
								stmtSQLUpdate.setString(7, csourcebiibid);
								// csaleid
								stmtSQLUpdate.setString(8, csourcebillhid);

								executeUpdate(stmtSQLUpdate);

							}

						} else {
							// �����Դ�ڳ����۳��ⵥ ���ڳ����۶���

							if (cupreceipttype
									.equals(nc.ui.scm.so.SaleBillType.SaleInitOutStore)) {

								// ��һ�� ���۳��ⵥ�ӱ�

								if (stmtSQLOutUpdate == null)
									stmtSQLOutUpdate = prepareStatement(con,
											SQLOutUpdate);

								stmtSQLOutUpdate.setBigDecimal(1, oldnnumber
										.toBigDecimal());
								stmtSQLOutUpdate.setBigDecimal(2, nnumber
										.toBigDecimal());
								// csale_bid
								stmtSQLOutUpdate.setString(3, cupsourcebiibid);
								// csaleid
								stmtSQLOutUpdate.setString(4, cupsourcebillhid);

								executeUpdate(stmtSQLOutUpdate);

								if (nc.ui.scm.so.SaleBillType.SaleOrder
										.equals(creceipttype)) {

									if (stmtSQLUpdate == null)
										stmtSQLUpdate = prepareStatement(con,
												SQLUpdate);

									stmtSQLUpdate.setBigDecimal(1, oldnnumber
											.toBigDecimal());
									stmtSQLUpdate.setBigDecimal(2, nnumber
											.toBigDecimal());
									// old
									stmtSQLUpdate.setBigDecimal(3, oldnmny
											.toBigDecimal());
									// new
									stmtSQLUpdate.setBigDecimal(4, nmny
											.toBigDecimal());
									// oldsubmny
									stmtSQLUpdate.setBigDecimal(5, oldnsubmny
											.toBigDecimal());
									// nsubmny
									stmtSQLUpdate.setBigDecimal(6, nsubmny
											.toBigDecimal());
									// csale_bid
									stmtSQLUpdate.setString(7, csourcebiibid);
									// csaleid
									stmtSQLUpdate.setString(8, csourcebillhid);

									executeUpdate(stmtSQLUpdate);

								}

							}

						}

					}

				}
			}

			if (stmtSQLUpdate != null)
				executeBatch(stmtSQLUpdate);

			if (stmtSQLOutUpdate != null)
				executeBatch(stmtSQLOutUpdate);
      //��鿪Ʊ�����Ͷ���������ϵ�������ˣ���update����ύ��ͳһ����У��
      String pk_corp = (String) billhead.getAttributeValue("pk_corp");
      UFDouble so26 = SysinitAccessor.getInstance().getParaDbl(pk_corp,"SO26");
      UFBoolean so25 =SysinitAccessor.getInstance().getParaBoolean(pk_corp,"SO25");
      
      if (null == so25 || !so25.booleanValue())
        so26 = new UFDouble(0);
      
      reWrite30InvoiceNum(hssourcebid, so26);


		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException("Remote Call", e);
		} finally {
			try {
				if (stmtSQLUpdate != null) {
					stmtSQLUpdate.close();
				}
			} catch (Exception e) {
			}
			try {
				if (stmtSQLOutUpdate != null) {
					stmtSQLOutUpdate.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		// ����Ʊ״̬
		if (corder_bids != null && corder_bids.length > 0) {

			try {
				// ��ѯ��������Ϣ
				nc.impl.scm.so.so001.SaleOrderDMO saledmo = new nc.impl.scm.so.so001.SaleOrderDMO();
				nc.vo.so.so001.SaleorderBVO[] oldordbvos = (nc.vo.so.so001.SaleorderBVO[]) saledmo
						.queryBodyDataForUpdateStatus(corder_bids);
				if (oldordbvos == null || oldordbvos.length <= 0)
					return;

				if (hsntotalinvoicenumber != null) {
					// �����޸�ǰ�ĳ�������
					for (int i = 0, loop = oldordbvos.length; i < loop; i++) {
						oldordbvos[i]
								.setNtotalinvoicenumber_old((UFDouble) hsntotalinvoicenumber
										.get(oldordbvos[i].getCorder_bid()));
					}
				}

				// modify by river for 2012-08-08
				// �޸Ŀ�Ʊ�ر�״̬������Ʊ��������붩��������ͬʱ��Ʊ�رգ�����ƱΪ����״̬
				for(SaleorderBVO ordb : oldordbvos) {
					if(ordb.getNnumber().sub(ordb.getNtotalinvoicenumber() == null ? new UFDouble("0") : ordb.getNtotalinvoicenumber()).doubleValue() <= 0) {
				
						// ����Ʊ״̬�߼�
						// river
						saledmo.processInvoicendState(new SaleorderBVO[]{ordb});
		
						// �������һ�ο�Ʊ����
						saledmo.setOrdLastDate("dlastinvoicedt", new SaleorderBVO[]{ordb},
								(UFDate) billhead.getAttributeValue("dbilldate"));
					} else {
						try { UAPServQueryBS.iUAPQueryBS.executeQuery("update so_saleexecute set bifinvoicefinish = 'N' where csale_bid = '"+ordb.getCorder_bid()+"'", null); } catch(Exception e) { Logger.error(DataControlDMO.class.getName() + " - 2279 - ���ʱ ����ִ�б��¼�Ŀ�Ʊ�ر�״̬"); } ;
					}
				}
				
			} catch (BusinessException e) {
				throw e;
			} catch (Exception e) {
				throw new BusinessException(e);
			}

		}

		/** ********************************************************** */
		// ������ϵͳ����ӿڣ�
		afterCallMethod("nc.bs.so.pub.DataControlDMO", "setTotalInvoiceNumber",
				new Object[] { bill });
		/** ********************************************************** */
	}

	 /**
	  * ��д���۶����ۼƿ�Ʊ����
	  * 
	  * @param hsNum��
	  * key--������۶�������id String
	  * value--��ű����޸ĵĲ�ֵ UFDouble
	  *           
	  * @param soparam ��������Ʊ������so������Ϊ20��10
	  * @throws java.lang.Exception
	  * 2007-11-20 xhq
	  */
	  public void reWrite30InvoiceNum(HashSet<String> hssourcebid,UFDouble soparam)
	    throws BusinessException{
		  
	    if(null == hssourcebid || hssourcebid.size() <= 0) 
        return ;
      //���㳬������Ʊ����
	    if(null == soparam)
	      soparam = new UFDouble(1);
	    else
	      soparam = soparam.div(new UFDouble(100)).add(new UFDouble(1));
	    
	    Connection con = null;
	    PreparedStatement stmt = null;	    
	    try{
        //��Դ������ID
	      String[] bids = hssourcebid.toArray(new String[0]);
	      PKLock lock = PKLock.getInstance();
	      boolean isLockSucc = false;
	      isLockSucc = lock.addBatchDynamicLock(bids);
		  if (!isLockSucc) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000080")/* @res* "���ڲ������������Ժ�����"*/);
		  }

	      String sql = "select csale_bid,nnumber,nrushnum,ntotalinvoicenumber,invcode,invname ";
	      sql += "from so_saleexecute A, so_saleorder_b B, bd_invbasdoc C where A.dr = 0 and B.dr = 0 and A.csale_bid = B.corder_bid ";
	      sql += "and cinvbasdocid = pk_invbasdoc "+ GeneralSqlString.formInSQL("A.csale_bid", bids);
	      
	      con = getConnection();
	      stmt = con.prepareStatement(sql);
	      ResultSet rs = stmt.executeQuery();
	      HashMap hmnum = new HashMap();
	      String id = null, invcode = null, invname = null;
	      Object oTemp = null;
	      UFDouble nNumber = null, nRushNum = null, nSignNum = null;
	      while(rs.next()){
	    	  id = rs.getString(1);
	    	  
          //��������
	    	  oTemp = rs.getObject(2);
	    	  if(null !=oTemp) 
            nNumber = new UFDouble(oTemp.toString());
	    	  else 
            nNumber = new UFDouble(0);
          //�ۼƳ���Գ�����
	    	  oTemp = rs.getObject(3);
	    	  if(null !=oTemp) 
            nRushNum = new UFDouble(oTemp.toString());
	    	  else 
            nRushNum = new UFDouble(0);
          //�ۼƿ�Ʊ����
	    	  oTemp = rs.getObject(4);
	    	  if(null !=oTemp) 
            nSignNum = new UFDouble(oTemp.toString());
	    	  else 
            nSignNum = new UFDouble(0);
          //�������
	    	  invcode = rs.getString(5);
	    	  //�������
          invname = rs.getString(6);
	    	  
	    	  if(null != id) 
            hmnum.put(id, new Object[]{nNumber,nRushNum,nSignNum,invcode,invname});
	      }
        
	      if(null !=rs) 
          rs.close();
	      
	      boolean haveError = false;
	      StringBuilder sb_throwmsg = new StringBuilder(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub","UPPsopub-000064")/*@res "��Ʊ�������ڶ�������"*/).append("\n");
	      
	      Object o[] = null;
	      UFDouble dTemp = null;
	      for(int i = 0,iloop = bids.length; i <iloop; i++){
	        haveError = false;
	  
	        oTemp = hmnum.get(bids[i]);
	        if(null == oTemp) 
            continue;	  
          
	        o = (Object[])oTemp;
	        nNumber = (UFDouble)o[0];
	        nRushNum = (UFDouble)o[1];
	        nSignNum = (UFDouble)o[2];
	        invcode = (String)o[3];
	        invname = (String)o[4];
	        	                
	        dTemp = nNumber.multiply(soparam).sub(nRushNum).sub(nSignNum);
	        if(nNumber.doubleValue() >= 0){
	          if(dTemp.doubleValue() < 0) haveError = true;
	        }else{
	          if(dTemp.doubleValue() > 0) haveError = true;
	        }
	        
	        if(haveError){
	        	sb_throwmsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub","UPPsopub-000060")/*@res "�������"*/ + ":")
	                  .append(invcode).append(";");
	            sb_throwmsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub","UPPsopub-000061")/*@res "�������"*/  + ":")
	                  .append(invname).append(";");
	            throw new BusinessException (sb_throwmsg.toString());       
	        }
	      }
	      	      	      
	    }catch(Exception e){
	      throw new BusinessException(e.getMessage());
	    }finally{
	    	try{
	    		if(stmt != null) stmt.close();
	    		if(con != null) con.close();
	    	}catch(Exception ex){}
	    }
	    
	  }
	  
	/*
	 * �������ⵥ�Գ�ʱ���¶�����Ʊ״̬,�����¶����ۼƿ�Ʊ���� 2007-08-30 xhq
	 */
	public void setTotalInvoiceNumForRush(AggregatedValueObject bill)
			throws SQLException, BusinessException{

		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
		CircularlyAccessibleValueObject billhead = bill.getParentVO();
		if (billhead == null || billbody == null || billbody.length <= 0)
			return;

		ArrayList alistids = new ArrayList();
		String[] corder_bids = null;

		String creceipttype = null;
		HashMap hRushNum = new HashMap();// �����ж�Ӧ���ۼƶԳ�����
		UFDouble dTemp = null;

		for (int i = 0, loop = billbody.length; i < loop; i++) {
			creceipttype = billbody[i].getAttributeValue("creceipttype") == null ? ""
					: billbody[i].getAttributeValue("creceipttype").toString();
			if (!"30".equals(creceipttype))
				continue;
			Object id = billbody[i].getAttributeValue("csourcebillbodyid");
			if (id != null && !alistids.contains(id)) {
				alistids.add(id);
			}
			if (id != null) {
				if (hRushNum.get(id) == null) {
					hRushNum.put(id, (UFDouble) billbody[i]
							.getAttributeValue("nnumber"));
				} else {
					dTemp = (UFDouble) hRushNum.get(id);
					dTemp = dTemp.add((UFDouble) billbody[i]
							.getAttributeValue("nnumber"));
					hRushNum.put(id, dTemp);
				}
			}
		}

		// ����ǰ������Ʊ����������Ʊ״̬
		HashMap hsntotalinvoicenumber = null;
		if (alistids.size() > 0) {
			corder_bids = (String[]) alistids.toArray(new String[alistids
					.size()]);
			hsntotalinvoicenumber = SOToolsDMO.getAnyValueUFDouble(
					"so_saleexecute", "ntotalinvoicenumber", "csale_bid",
					corder_bids, " creceipttype = '30' ");

		}

		// ����Ʊ״̬
		if (corder_bids != null && corder_bids.length > 0) {

			try {
				// ��ѯ��������Ϣ
				nc.impl.scm.so.so001.SaleOrderDMO saledmo = new nc.impl.scm.so.so001.SaleOrderDMO();
				nc.vo.so.so001.SaleorderBVO[] oldordbvos = (nc.vo.so.so001.SaleorderBVO[]) saledmo
						.queryBodyDataForUpdateStatus(corder_bids);
				if (oldordbvos == null || oldordbvos.length <= 0)
					return;

				UFDouble d = new UFDouble(0);
				if (hsntotalinvoicenumber != null) {
					// �����޸�ǰ�ĳ�������
					for (int i = 0, loop = oldordbvos.length; i < loop; i++) {
						dTemp = (UFDouble) hsntotalinvoicenumber
								.get(oldordbvos[i].getCorder_bid());
						if (dTemp == null)
							dTemp = new UFDouble(0);
						if (hRushNum.get(oldordbvos[i].getCorder_bid()) != null)
							d = (UFDouble) hRushNum.get(oldordbvos[i]
									.getCorder_bid());
						oldordbvos[i].setNtotalinvoicenumber_old(dTemp.sub(d));
						oldordbvos[i].setNtotalinvoicenumber(dTemp);
					}
				}

				// ����Ʊ״̬�߼�
				saledmo.processInvoicendState(oldordbvos);

			} catch (BusinessException e) {
				throw e;
			} catch (Exception e) {
				throw new BusinessException("Remote Call", e);
			}

		}

	}

	/**
	 * ���ԭʼ������ �������ڣ�(2001-9-3 9:30:24)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * 
	 * �޸����ڣ�2003-12-16 �޸����ݣ�����˰���nmny->��Ϊ��˰�ϼ�nsummny
	 */
	private Hashtable[] getOldData(String strBillType, String strID)
			throws SQLException {

		Hashtable[] hasreturn = new Hashtable[3];

		UFDouble nnumber = new UFDouble(0.0);
		UFDouble nmny = new UFDouble(0.0);
		UFDouble nsubmny = new UFDouble(0);
		// String strSQL = "Select nnumber,nmny, ";
		String strSQL = "select nnumber,nsummny, ";
		String strField = null;
		String strTable = null;

		// ����
		if (strBillType.equals(SaleBillType.SaleOrder)) {
			strTable = " so_saleorder_b ";
			strField = "corder_bid";
		}
		// ��Ʊ
		if (strBillType.equals(SaleBillType.SaleInvoice)) {

			strTable = " so_saleinvoice_b ";
			strField = "cinvoice_bid, nsubcursummny - nsummny as submny ";
		}

		strSQL = strSQL + strField + " from " + strTable
				+ " where csaleid =  ? ";

		Connection con = null;
		PreparedStatement stmt = null;
		Hashtable hNumber = new Hashtable();
		Hashtable hNmny = new Hashtable();
		Hashtable hSubmny = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			// pk
			stmt.setString(1, strID);

			ResultSet rs = stmt.executeQuery();
			//
			BigDecimal n = null;
			String id = null;

			while (rs.next()) {
				n = (BigDecimal) rs.getObject(1);
				nnumber = (n == null ? new UFDouble(0) : new UFDouble(n));
				n = (BigDecimal) rs.getObject(2);
				nmny = (n == null ? new UFDouble(0) : new UFDouble(n));
				id = rs.getString(3);
				if (strBillType.equals(SaleBillType.SaleInvoice)) {
					BigDecimal dTemp = rs.getBigDecimal(4);
					nsubmny = (dTemp == null ? new UFDouble(0) : new UFDouble(
							dTemp));
				}

				hNumber.put(id, nnumber);
				hNmny.put(id, nmny);

				if (hSubmny == null)
					hSubmny = new Hashtable();
				hSubmny.put(id, nsubmny);

			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		hasreturn[0] = hNumber;
		hasreturn[1] = hNmny;
		hasreturn[2] = hSubmny;

		return hasreturn;
	}

	/**
	 * HANWEI ���ܣ���üӹ�����OperatelogVO ������ ���أ� ���⣺ ���ڣ�(2004-6-24 15:46:23)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.vo.sm.log.OperatelogVO
	 * @param sMsg
	 *            java.lang.String
	 * @param sEnterbutton
	 *            java.lang.String
	 * @param sMessgageType
	 *            java.lang.String
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	public nc.vo.sm.log.OperatelogVO getOperLogVO(String sMsg,
			String sEnterbutton, String sMessgageType, String cuserid,
			String scudate, nc.vo.pub.AggregatedValueObject vo) {

		if (vo == null || vo.getParentVO() == null)
			return null;

		if ("ERROR".equalsIgnoreCase(sMessgageType)) {
			// �쳣��Ϣ
			sMsg = nc.vo.scm.funcs.Businesslog.MSGERROR + sMsg;
		} else {
			if (sMsg == null || sMsg.trim().length() == 0) {
				sMsg = nc.vo.scm.funcs.Businesslog.MSGMESSAGE;
			} else {
				// ������Ϣ
				sMsg = nc.vo.scm.funcs.Businesslog.MSGWARNING + sMsg;
			}
		}

		try {

			FetchValueDMO fetchdmo = new FetchValueDMO();

			if (vo.getClass() == SaleOrderVO.class) {

				SaleOrderVO ordvo = (SaleOrderVO) vo;
				nc.vo.sm.log.OperatelogVO operatelogvo = new nc.vo.sm.log.OperatelogVO();

				SaleorderHVO ordhvo = ordvo.getHeadVO();

				String cusername = ordvo.getCusername();

				String ccorpname = ordvo.getCcorpname();

				if (ccorpname == null || ccorpname.trim().length() <= 0) {
					ccorpname = fetchdmo.fetchValue("bd_corp", "unitname",
							" pk_corp ='" + ordhvo.getPk_corp() + "' ");
				}

				if (cuserid == null || cuserid.trim().length() == 0)
					cuserid = ordhvo.getCoperatorid();

				if ((cusername == null || cusername.trim().length() <= 0)
						&& cuserid != null) {
					cusername = fetchdmo.fetchValue("sm_user", "user_name",
							" cuserid ='" + cuserid + "' ");
				}

				operatelogvo.setCompanyname(ccorpname);

				operatelogvo.setOpratorname(cusername);

				operatelogvo
						.setEnterfunction((ordvo.getCnodename() == null || ordvo
								.getCnodename().trim().length() <= 0) ? nc.bs.ml.NCLangResOnserver
								.getInstance().getStrByID("40060301",
										"UPT40060301-000021")
								/* @res "���۶���" */
								: ordvo.getCnodename());
				operatelogvo.setEnterbutton(sEnterbutton);

				if (ordvo.getIAction() == ISaleOrderAction.A_ADD) {
					operatelogvo.setEnterbutton(nc.bs.ml.NCLangResOnserver
							.getInstance().getStrByID("SCMCOMMON",
									"UPPSCMCommon-000288")
					/* @res "����" */);
				} else if (ordvo.getIAction() == ISaleOrderAction.A_EDIT) {
					operatelogvo
							.setEnterbutton(nc.bs.ml.NCLangResOnserver
									.getInstance().getStrByID("common",
											"UC001-0000045")/* @res "�޸�" */);
				} else if (ordvo.getIAction() == ISaleOrderAction.A_MODIFY) {
					operatelogvo.setEnterbutton(nc.bs.ml.NCLangResOnserver
							.getInstance().getStrByID("SCMCOMMON",
									"UPPSCMCommon-000290")/* @res "�޶�" */);
				} else if (ordvo.getIAction() == ISaleOrderAction.A_AUDIT) {
					operatelogvo
							.setEnterbutton(nc.bs.ml.NCLangResOnserver
									.getInstance().getStrByID("common",
											"UC001-0000027")/* @res "����" */);
				} else if (ordvo.getIAction() == ISaleOrderAction.A_UNAUDIT) {
					operatelogvo
							.setEnterbutton(nc.bs.ml.NCLangResOnserver
									.getInstance().getStrByID("common",
											"UC001-0000028")/* @res "����" */);
				} else if (ordvo.getIAction() == ISaleOrderAction.A_BLANKOUT) {
					operatelogvo
							.setEnterbutton(nc.bs.ml.NCLangResOnserver
									.getInstance().getStrByID("common",
											"UC001-0000005")/* @res "����" */);
				} else if (ordvo.getIAction() == ISaleOrderAction.A_FREEZE) {
					operatelogvo
							.setEnterbutton(nc.bs.ml.NCLangResOnserver
									.getInstance().getStrByID("common",
											"UC001-0000030")/* @res "����" */);
				} else if (ordvo.getIAction() == ISaleOrderAction.A_UNFREEZE) {
					operatelogvo
							.setEnterbutton(nc.bs.ml.NCLangResOnserver
									.getInstance().getStrByID("common",
											"UC001-0000031")/* @res "�ⶳ" */);
				} else if (ordvo.getIAction() == ISaleOrderAction.A_CLOSE) {
					operatelogvo.setEnterbutton(nc.bs.ml.NCLangResOnserver
							.getInstance().getStrByID("SCMCOMMON",
									"UPPSCMCommon-000128")/* @res "����" */);
				}
				operatelogvo.setBillcode(ordhvo.getVreceiptcode());
				operatelogvo.setBillType("30");
				operatelogvo.setBillId(ordhvo.getCsaleid());
				operatelogvo.setBusiType(ordhvo.getCbiztype());
				operatelogvo.setPKCorp(ordhvo.getPk_corp());
				operatelogvo.setOperatorId(cuserid);
				IServiceProviderSerivce isps = (IServiceProviderSerivce) NCLocator
						.getInstance().lookup(
								IServiceProviderSerivce.class.getName());
				UFDateTime logtime = isps.getServerTime();
				if (logtime == null && ordvo.getDcurdate() != null) {
					logtime = new UFDateTime(ordvo.getDcurdate().toString());
				}
				operatelogvo.setLoginTime(logtime == null ? "" : logtime
						.toString());
				operatelogvo.setEnterip(ordvo.getLogip());
				operatelogvo
						.setOperatetype(nc.vo.sm.log.OperatelogVO.BUSINESS_LOG);

				if (sMsg != null && sMsg.length() > 3999) {
					sMsg = sMsg.substring(0, 3999);
				}
				operatelogvo.setBusinessLog(sMsg);

				return operatelogvo;

			}

		} catch (Exception e) {
			/** �������ܣ����������쳣 */
			nc.vo.scm.pub.SCMEnv.out(nc.bs.ml.NCLangResOnserver.getInstance()
					.getStrByID("4008check", "UPP4008check-000069")/*
																	 * @res
																	 * "nc.bs.ic.pub.check.CheckDMO.insertBusinesslog����ҵ����־���ִ���"
																	 */);
			SCMEnv.out(e);
		}

		return null;
	}

	/**
	 * ����������ҵ����־
	 * @param vo -- ������vo
	 * @param action -- ������
	 */
	public void insertBusinesslog(nc.vo.pub.AggregatedValueObject vo,String action){
		Operlog operlog=new Operlog();
		SaleReceiveVO srvo = (SaleReceiveVO)vo;
		srvo.getOperatelogVO().setOpratorname(getOpratorname(srvo.getCl().getUser()));
		srvo.getOperatelogVO().setCompanyname(getCompanyname(srvo.getCl().getCorp()));
		srvo.getOperatelogVO().setOperatorId(srvo.getCl().getUser());
	    operlog.insertBusinessExceptionlog(vo, action, null, nc.vo.scm.funcs.Businesslog.MSGMESSAGE, SaleBillType.SaleReceive);
	}
	
	public String getCompanyname(String pk_corp){
		String ccorpname = null;
		try {
		FetchValueDMO fetchdmo = new FetchValueDMO();
		ccorpname = fetchdmo.fetchValue("bd_corp", "unitname",
					" pk_corp ='" + pk_corp + "' ");
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(nc.bs.ml.NCLangResOnserver.getInstance()
					.getStrByID("4008check", "UPP4008check-000069")/*
																	 * @res
																	 * "nc.bs.ic.pub.check.CheckDMO.insertBusinesslog����ҵ����־���ִ���"
																	 */);
			SCMEnv.out(e);
		}
		return ccorpname;
	}
	
    public String getOpratorname(String cuserid){
    	String cusername = null;
		try {
		FetchValueDMO fetchdmo = new FetchValueDMO();
		cusername = fetchdmo.fetchValue("sm_user", "user_name",
				" cuserid ='" + cuserid + "' ");
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(nc.bs.ml.NCLangResOnserver.getInstance()
					.getStrByID("4008check", "UPP4008check-000069")/*
																	 * @res
																	 * "nc.bs.ic.pub.check.CheckDMO.insertBusinesslog����ҵ����־���ִ���"
																	 */);
			SCMEnv.out(e);
		}
		return cusername;
	}
	
	/**
	 * �˴����뷽��˵���� ����ҵ����־ by hanwei �������ڣ�(2004-4-2 11:21:59)
	 * 
	 * @param voBill
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * @param sMsg
	 *            java.lang.String ��������Ϣ sEnterbutton������������ƣ��������棬�޸ı��棬��ˣ�����
	 */
	public void insertBusinesslog(nc.vo.pub.AggregatedValueObject vo,
			String sMessgageType, String sMsg, String sEnterbutton,
			String cuserid, String sdate) {

		if (vo == null || vo.getParentVO() == null)
			return;

		nc.vo.sm.log.OperatelogVO operatelogvo = getOperLogVO(sMsg,
				sEnterbutton, sMessgageType, cuserid, sdate, vo);

		insertBusinesslog(new nc.vo.sm.log.OperatelogVO[] { operatelogvo });

	}

	/**
	 * �˴����뷽��˵���� ����ҵ����־ by hanwei �������ڣ�(2004-4-2 11:21:59)
	 * 
	 * @param voBill
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * @param sMsg
	 *            java.lang.String ��������Ϣ sEnterbutton������������ƣ��������棬�޸ı��棬��ˣ�����
	 */
	public void insertBusinesslog(nc.vo.sm.log.OperatelogVO[] logvos) {

		try {

			if (logvos == null || logvos.length <= 0)
				return;

			IOperateLogService log = (IOperateLogService) NCLocator
					.getInstance().lookup(IOperateLogService.class.getName());
			log.insertArray(logvos);

		} catch (Exception e) {
			/** �������ܣ����������쳣 */
			nc.vo.scm.pub.SCMEnv.out(nc.bs.ml.NCLangResOnserver.getInstance()
					.getStrByID("4008check", "UPP4008check-000069")/*
																	 * @res
																	 * "nc.bs.ic.pub.check.CheckDMO.insertBusinesslog����ҵ����־���ִ���"
																	 */);
			SCMEnv.out(e);
		}

	}

	/**
	 * �˴����뷽��˵���� ����ҵ����־ by hanwei �������ڣ�(2004-4-2 11:21:59)
	 * 
	 * @param voBill
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * @param sMsg
	 *            java.lang.String ��������Ϣ sEnterbutton������������ƣ��������棬�޸ı��棬��ˣ�����
	 */
	public void insertBusinesslog(nc.vo.pub.AggregatedValueObject vo,
			String sMsg, String sEnterbutton, String cuserid, String sdate) {

		insertBusinesslog(vo, "WARNING", sMsg, sEnterbutton, cuserid, sdate);

	}

	/**
	 * �˴����뷽��˵���� �����쳣ҵ����־ by hanwei �������ڣ�(2004-4-2 11:21:59)
	 * 
	 * @param voBill
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * @param sMsg
	 *            java.lang.String
	 */
	public void insertBusinessExceptionlog(nc.vo.pub.AggregatedValueObject vo,
			String sMsg, String sEnterbutton, String cuserid, String sdate) {

		if (vo == null || vo.getParentVO() == null)
			return;

		nc.vo.sm.log.OperatelogVO operatelogvo = getOperLogVO(sMsg,
				sEnterbutton, "ERROR", cuserid, sdate, vo);

		insertBusinessExceptionlog(new nc.vo.sm.log.OperatelogVO[] { operatelogvo });

	}

	/**
	 * �˴����뷽��˵���� �����쳣ҵ����־ by hanwei �������ڣ�(2004-4-2 11:21:59)
	 * 
	 * @param voBill
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * @param sMsg
	 *            java.lang.String
	 */
	public void insertBusinessExceptionlog(nc.vo.sm.log.OperatelogVO[] logvos) {

		if (logvos == null || logvos.length <= 0)
			return;

		// OperatelogBO log = null;
		try {
			/** �������ܣ����������쳣 */
			// nc.bs.sm.log.OperatelogHome lh = (nc.bs.sm.log.OperatelogHome)
			// getBeanHome(
			// nc.bs.sm.log.OperatelogHome.class,
			// "nc.bs.sm.log.OperatelogBO");
			IOperateLogService log = (IOperateLogService) NCLocator
					.getInstance().lookup(IOperateLogService.class.getName());

			log.insertArray(logvos);

		} catch (Exception e) {
			/** �������ܣ����������쳣 */
			nc.vo.scm.pub.SCMEnv.out(nc.bs.ml.NCLangResOnserver.getInstance()
					.getStrByID("4008check", "UPP4008check-000011")/*
																	 * @res
																	 * "nc.bs.ic.pub.check.CheckBO.insertBusinessExceptionlog����ҵ����־���ִ���"
																	 */);
			SCMEnv.out(e);
		} 

	}

	/**
	 * from v50 ���ݵ�����ۿ� �Ƚϸ�������� �����ۿ� *����ĵ�Ʒ�ۿ� ������Сֵ
	 * 
	 * @param billVO
	 * @return
	 */
	public UFDouble getMaxInvDiscountrate(AggregatedValueObject billVO) {
		UFDouble result = new UFDouble(100.0);
		if (billVO == null || billVO.getChildrenVO() == null
				|| billVO.getChildrenVO().length < 1
				|| !(billVO.getChildrenVO()[0] instanceof ISOInventoryDiscount))
			return result;
		UFDouble dDiscont = null;
		UFDouble dItemDiscont = null;
		UFDouble allDiscont = null;
		for (int i = 0; i < billVO.getChildrenVO().length; i++) {
			dDiscont = ((ISOInventoryDiscount) billVO.getChildrenVO()[i])
					.getDiscount();
			dItemDiscont = ((ISOInventoryDiscount) billVO.getChildrenVO()[i])
					.getItemDiscount();
			dDiscont = dDiscont == null ? new UFDouble(0.0) : dDiscont;
			dItemDiscont = dItemDiscont == null ? new UFDouble(0.0)
					: dItemDiscont;
			allDiscont = dDiscont.multiply(dItemDiscont).div(100.0);
			if (result.compareTo(allDiscont) > 0)
				result = allDiscont;
		}
		return result;
	}

	/**
	 * from v51 ���ݵ���С�ۿ� �Ƚϸ�������� �����ۿ� *����ĵ�Ʒ�ۿ� �������ֵ
	 * 
	 * @param billVO
	 * @return
	 */
	public UFDouble getMinInvDiscountrate(AggregatedValueObject billVO) {
		UFDouble result = new UFDouble(0.0);
		if (billVO == null || billVO.getChildrenVO() == null
				|| billVO.getChildrenVO().length < 1
				|| !(billVO.getChildrenVO()[0] instanceof ISOInventoryDiscount))
			return result;
		UFDouble dDiscont = null;
		UFDouble dItemDiscont = null;
		UFDouble allDiscont = null;
		for (int i = 0; i < billVO.getChildrenVO().length; i++) {
			dDiscont = ((ISOInventoryDiscount) billVO.getChildrenVO()[i])
					.getDiscount();
			dItemDiscont = ((ISOInventoryDiscount) billVO.getChildrenVO()[i])
					.getItemDiscount();
			dDiscont = dDiscont == null ? new UFDouble(0.0) : dDiscont;
			dItemDiscont = dItemDiscont == null ? new UFDouble(0.0)
					: dItemDiscont;
			allDiscont = dDiscont.multiply(dItemDiscont).div(100.0);
			if (result.compareTo(allDiscont) < 0)
				result = allDiscont;
		}
		return result;
	}

	/**
	 * from v50 ���ۼ۸���С�ڴ������ۼ� ȡ�����б���Ĵ�� ȡ�ô��������ۼ�
	 * �������Ƿ���С�ڴ������ۼۣ�����з���true;����false 20060613
	 * �ڿͻ��Ĺ�����������һ�����ԡ��������ۼ۱�������Ϊ�ٷ�����Ĭ��Ϊ100��������Ϊ��80����ʾ������Ϊ����ۼ۲��ܵ��ڡ����������������ۼۡ�*80%��
	 * ���ô���ĺ�˰�����롰���������������ۼۡ�*���������ۼ۱�����%ȥ���бȽϡ�
	 * 
	 * @param billVO
	 * @return
	 */
	public UFBoolean lessSaleMinPrice(AggregatedValueObject billVO) {
		UFBoolean result = new UFBoolean(true);
		if (billVO == null || billVO.getChildrenVO() == null
				|| billVO.getChildrenVO().length < 1
				|| !(billVO.getChildrenVO()[0] instanceof ISOInventoryDiscount))
			return result;
		// ȡ�����б���Ĵ��
		// ȡ�ô��������ۼ�
		// �������Ƿ���С�ڴ������ۼۣ�����з���true;����false.
		// ȡ�ÿͻ�������������ۼ۱���
		SmartDMO dmo = null;
		UFDouble udLowRate = null;
		try {
			dmo = new SmartDMO();
			String smanSql = " select stockpriceratio from bd_cumandoc where pk_cumandoc='"
					+ billVO.getParentVO().getAttributeValue("ccustomerid")
					+ "' and dr=0";
			Object[] o = dmo.selectBy2(smanSql);
			if (o == null || o.length == 0)
				udLowRate = new UFDouble(1);
			else {
				Object[] o1 = (Object[]) o[0];
				if (o1 == null || o1.length == 0 || o1[0] == null)
					udLowRate = new UFDouble(1);
				else {
					udLowRate = new UFDouble(o1[0].toString());
					udLowRate = udLowRate.div(100);
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}

		String[] invs = new String[billVO.getChildrenVO().length];
		HashMap map = new HashMap(billVO.getChildrenVO().length);
		for (int i = 0; i < billVO.getChildrenVO().length; i++) {

			invs[i] = ((ISOInventoryDiscount) billVO.getChildrenVO()[i])
					.getInventory();
			Object blargessflag = billVO.getChildrenVO()[i]
					.getAttributeValue("blargessflag");
			if (blargessflag != null
					&& new UFBoolean(blargessflag.toString()).booleanValue())
				continue;
			if (map.containsKey(invs[i])) {
				UFDouble price2 = ((ISOInventoryDiscount) billVO
						.getChildrenVO()[i]).getTaxNetPrice();
				price2 = price2 == null ? new UFDouble(0.0) : price2;
				UFDouble price = (UFDouble) map.get(invs[i]);
				price = price == null ? new UFDouble(0.0) : price;
				if (price2.compareTo(price) < 0) {
					map.put(invs[i], price2);
				}
			} else {
				UFDouble price2 = ((ISOInventoryDiscount) billVO
						.getChildrenVO()[i]).getTaxNetPrice();
				price2 = price2 == null ? new UFDouble(0.0) : price2;
				map.put(invs[i], price2);
			}
		}

		String sql = "select pk_invmandoc,lowestprice from bd_invmandoc where 1=1 "
				+ GeneralSqlString.formInSQL("pk_invmandoc", invs);
		String sHint;
		String ss="";
		
		try {
			Object[] o = dmo.selectBy2(sql);
			Object[] onerow = null;
			UFDouble minPrice = null;
			UFDouble price;
			if (o != null) {
				for (int i = 0; i < o.length; i++) {
					/** for each inventory* */
					onerow = (Object[]) o[i];
					if (onerow != null && onerow.length > 1) {
						minPrice = (UFDouble) map.get(onerow[0]);
						price = (new UFDouble(onerow[1].toString()))
								.multiply(udLowRate);
						/** compare price* */
						if (minPrice != null && onerow[1] != null
								&& minPrice.compareTo(price) < 0) {
							sHint = "";
							ss += nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("SCMCOMMON",
											"UPPSCMCommon-000289");
							for (int j = 0; j < billVO.getChildrenVO().length; j++) {
								// �����ͬ���۸�����Ҫ��
								if ((((ISOInventoryDiscount) billVO
										.getChildrenVO()[j]).getInventory()
										.equals(onerow[0]))
										&& (((ISOInventoryDiscount) billVO
												.getChildrenVO()[j])
												.getTaxNetPrice()
												.compareTo(
														(new UFDouble(onerow[1]
																.toString()))
																.multiply(udLowRate)) < 0)) {
									sHint += billVO.getChildrenVO()[j]
											.getAttributeValue("crowno")
											+ " ";
								}// end if
							}// end for
							if (sHint.length() > 0) {
								ss += sHint + " ����ۼ�:" + price + "\n";
								// nc.bs.logging.Logger.setMappedThreadState(IPFConfigInfo.FUNCTION_OUT,ss);
							}
							// return new UFBoolean(false);
						}// end if price

					}
				}// end for each o
			}// end if o

			if (ss != null && ss.length() > 0) {
				nc.bs.logging.Logger.setMappedThreadState(
						IPFConfigInfo.FUNCTION_OUT, ss);
				return UFBoolean.FALSE;
			}

		} catch (Exception e) {
			SCMEnv.out(e);
		}

		return result;
	}

	/**
	 * from v50 ������˰�۸���С�ڴ������ۼ� ȡ�����б���Ĵ�� ȡ�ô��������ۼ�
	 * �������Ƿ���С�ڴ������ۼۣ�����з���true;����false 20060613
	 * �ڿͻ��Ĺ�����������һ�����ԡ��������ۼ۱�������Ϊ�ٷ�����Ĭ��Ϊ100��������Ϊ��80����ʾ������Ϊ����ۼ۲��ܵ��ڡ����������������ۼۡ�*80%��
	 * ���ô���ĺ�˰�����롰���������������ۼۡ�*���������ۼ۱�����%ȥ���бȽϡ�
	 * 
	 * @param billVO
	 * @return
	 */
	public UFBoolean lessSaleMinPriceNew(AggregatedValueObject billVO) {
		UFBoolean result = new UFBoolean(true);
		if (billVO == null || billVO.getChildrenVO() == null
				|| billVO.getChildrenVO().length < 1
				|| !(billVO.getChildrenVO()[0] instanceof ISOInventoryDiscount))
			return result;
		// ȡ�����б���Ĵ��
		// ȡ�ô��������ۼ�
		// �������Ƿ���С�ڴ������ۼۣ�����з���true;����false.
		// ȡ�ÿͻ�������������ۼ۱���
		SmartDMO dmo = null;
		UFDouble udLowRate = null;
		try {
			dmo = new SmartDMO();
			String smanSql = " select stockpriceratio from bd_cumandoc where pk_cumandoc='"
					+ billVO.getParentVO().getAttributeValue("ccustomerid")
					+ "' and dr=0";
			Object[] o = dmo.selectBy2(smanSql);
			if (o == null || o.length == 0)
				udLowRate = new UFDouble(1);
			else {
				Object[] o1 = (Object[]) o[0];
				if (o1 == null || o1.length == 0 || o1[0] == null)
					udLowRate = new UFDouble(1);
				else {
					udLowRate = new UFDouble(o1[0].toString());
					udLowRate = udLowRate.div(100);
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}

		String[] invs = new String[billVO.getChildrenVO().length];
		HashMap map = new HashMap(billVO.getChildrenVO().length);
		for (int i = 0; i < billVO.getChildrenVO().length; i++) {

			invs[i] = ((ISOInventoryDiscount) billVO.getChildrenVO()[i])
					.getInventory();
			Object blargessflag = billVO.getChildrenVO()[i]
					.getAttributeValue("blargessflag");
			if (blargessflag != null
					&& new UFBoolean(blargessflag.toString()).booleanValue())
				continue;
			if (map.containsKey(invs[i])) {
				UFDouble price2 = ((ISOInventoryDiscount) billVO
						.getChildrenVO()[i]).getNnetprice();
				price2 = price2 == null ? new UFDouble(0.0) : price2;
				UFDouble price = (UFDouble) map.get(invs[i]);
				price = price == null ? new UFDouble(0.0) : price;
				if (price2.compareTo(price) < 0) {
					map.put(invs[i], price2);
				}
			} else {
				UFDouble price2 = ((ISOInventoryDiscount) billVO
						.getChildrenVO()[i]).getNnetprice();
				price2 = price2 == null ? new UFDouble(0.0) : price2;
				map.put(invs[i], price2);
			}
		}

		String sql = "select pk_invmandoc,lowestprice from bd_invmandoc where 1=1 "
				+ GeneralSqlString.formInSQL("pk_invmandoc", invs);
		String ss = "";
		try {
			Object[] o = dmo.selectBy2(sql);
			Object[] onerow = null;
			UFDouble minPrice = null;
			UFDouble price;
			if (o != null) {
				for (int i = 0; i < o.length; i++) {
					onerow = (Object[]) o[i];
					if (onerow != null && onerow.length > 1) {
						minPrice = (UFDouble) map.get(onerow[0]);
						price = (new UFDouble(onerow[1].toString()))
								.multiply(udLowRate);
						if (minPrice != null && onerow[1] != null
								&& minPrice.compareTo(price) < 0) {
							String sHint = "";
							ss += nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("SCMCOMMON",
											"UPPSCMCommon-000289");
							for (int j = 0; j < billVO.getChildrenVO().length; j++) {
								// �����ͬ���۸�����Ҫ��
								if ((((ISOInventoryDiscount) billVO
										.getChildrenVO()[j]).getInventory()
										.equals(onerow[0]))
										&& (((ISOInventoryDiscount) billVO
												.getChildrenVO()[j])
												.getNnetprice()
												.compareTo(
														(new UFDouble(onerow[1]
																.toString()))
																.multiply(udLowRate)) < 0)) {

									sHint += billVO.getChildrenVO()[j]
											.getAttributeValue("crowno")
											+ " ";
								}// end if
							}// end for
							if (sHint.length() > 0) {
								ss += sHint + " ����ۼ�:" + price + "\n";
							}
						}
					}
				}
			}

			if (ss != null && ss.length() > 0) {
				nc.bs.logging.Logger.setMappedThreadState(
						IPFConfigInfo.FUNCTION_OUT, ss);
				return UFBoolean.FALSE;
			}

		} catch (Exception e) {
			SCMEnv.out(e);
		}

		return result;
	}

}
