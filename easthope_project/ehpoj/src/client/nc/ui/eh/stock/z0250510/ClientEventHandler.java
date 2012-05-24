package nc.ui.eh.stock.z0250510;

import java.util.List;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.dlg.CADlg;
import nc.ui.eh.dlg.ValidDlg;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.businessaction.IPFACTION;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.stock.z0250505.StockInBVO;
import nc.vo.eh.stock.z0250505.StockInVO;
import nc.vo.framework.rsa.Encode;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.sm.cmenu.GlorgbookExtVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.uap.rbac.UserRoleVO;

public class ClientEventHandler extends AbstractSPEventHandler {

	public static ClientUI clientui = null;
	
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	@Override 
    protected void onBoCard() throws Exception {

 		super.onBoCard();
   	 
	   	System.out.println("onBoCard !");
	   	
	   	AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
		if(modelVo != null) {
			StockInVO sivo = (StockInVO)modelVo.getParentVO();
			String vapproveid = sivo.getVapproveid();
	    	if(vapproveid == null)
	    		vapproveid = "";
	    	
	    	String pk_user = ClientUI.getCE().getUser().getPrimaryKey();
	    	
	    	if((sivo.getVbillstatus() == 1 || sivo.getVbillstatus() == 0) && vapproveid.equals(pk_user)) {
	    		getButtonManager().getButton(IBillButton.CancelAudit).setEnabled(true);
	    	} else {
	    		getButtonManager().getButton(IBillButton.CancelAudit).setEnabled(false);
	    	}
		}
		
		getBillUI().updateButtonUI();
    }
	
	
	
	@Override
    protected void onBoRefresh() throws Exception {

		super.onBoRefresh();

		System.out.println("onBoRefresh !");

		AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
		if (modelVo != null) {
			StockInVO sivo = (StockInVO) modelVo
					.getParentVO();
			String vapproveid = sivo.getVapproveid();
			if (vapproveid == null)
				vapproveid = "";

			String pk_user = ClientUI.getCE().getUser()
					.getPrimaryKey();

			if ((sivo.getVbillstatus() == 1 || sivo.getVbillstatus() == 0)
					&& vapproveid.equals(pk_user)) {
				getButtonManager().getButton(IBillButton.CancelAudit)
						.setEnabled(true);
			} else {
				getButtonManager().getButton(IBillButton.CancelAudit)
						.setEnabled(false);
			}
		}

		getBillUI().updateButtonUI();
	}
    
   @Override
   protected void onBoCancelAudit() throws Exception {
   	System.out.println("CancelAudit !"); 
   	
	IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
	
   	AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
   	setCheckManAndDate(modelVo);
   	
   	String rowids = "";
   	StockInBVO[] cavo = (StockInBVO[])modelVo.getChildrenVO();
	
	if(cavo.length > 0) {
    	for(StockInBVO bvo : cavo) {
    		rowids += "'" + bvo.getPk_in_b() + "',";
    	}
	}
	
	if(rowids.length() > 0)
		rowids = rowids.substring(0 , rowids.length() - 1);
   	
   	GlorgbookExtVO extVO = (GlorgbookExtVO)ClientUI.getCE().getValue("pk_glorgbook");
   	String pk_user = ClientUI.getCE().getUser().getPrimaryKey();
   	StockInVO sivo = (StockInVO)modelVo.getParentVO();
   	String vapproveid = sivo.getVapproveid();
   	if(vapproveid == null)
   		vapproveid = "";
   	
   	if((sivo.getVbillstatus() == 1 || sivo.getVbillstatus() == 0) && vapproveid.equals(pk_user)) {
	    	String billno = sivo.getBillno();
	    	
	    	int fkrq_M = sivo.getIndate().getMonth();
	    	int fkrq_Y = sivo.getIndate().getYear();
	    	String pk_corp = sivo.getPk_corp();
	    	
	    	
	    	
	    	StringBuffer sb = new StringBuffer();
	    	sb.append("select jz_flag from eh_period where pk_corp = '"+pk_corp+"' and nyear = '"+fkrq_Y+"' and nmonth = '"+fkrq_M+"'");
	    	String jz_flag = iUAPQueryBS.executeQuery(sb.toString(), new ColumnProcessor("jz_flag")) == null ? "" : iUAPQueryBS.executeQuery(sb.toString(),new ColumnProcessor("jz_flag")).toString();
			if(jz_flag != null)
				if(jz_flag.equals("Y"))
					throw new Exception ("�����ѽ��ʣ���ֹ����! ");
			
			
			sb = new StringBuffer();
			
			// vsourcebillid ͨ����ⵥ����PK�ж�
			sb.append(" select count(h.pk_corp) counts from eh_arap_stockinvoices_b b left join eh_arap_stockinvoice h on b.pk_stockinvoice = h.pk_stockinvoice ");
			sb.append(" where h.pk_corp = '"+pk_corp+"' and b.vsourcebillid = '"+sivo.getPk_in()+"' and h.dmakedate >= '"+sivo.getDmakedate().toString()+"' and  nvl(h.dr,0)=0  and nvl(b.dr,0)=0");
			String rkdcount = iUAPQueryBS.executeQuery(sb.toString(), new ColumnProcessor("counts")) == null ? "" : iUAPQueryBS.executeQuery(sb.toString(),new ColumnProcessor("counts")).toString();
			if(rkdcount != null) 
				if(Integer.valueOf(rkdcount) > 0)
					throw new Exception ("�˵��������ɷ�Ʊ����ֹ����! ");
	    	
			sb = new StringBuffer();
			
			// vsourcebillrowid ͨ����ⵥ�ӱ���PK�ж�
			sb.append(" select count(fk.pk_corp) counts from eh_arap_fk_b fk_b left join eh_arap_fk fk on fk_b.pk_fk = fk.pk_fk ");
			sb.append(" where vsourcebillrowid in ("+rowids+") and fk.pk_corp = '"+pk_corp+"' and fk.dmakedate >= '"+sivo.getDmakedate().toString()+"'  and nvl(fk.dr,0)=0 and nvl(fk_b.dr,0)=0");
			
			String rkbcount = iUAPQueryBS.executeQuery(sb.toString(), new ColumnProcessor("counts")) == null ? "" : iUAPQueryBS.executeQuery(sb.toString(),new ColumnProcessor("counts")).toString();
			if(rkbcount != null) 
				if(Integer.valueOf(rkbcount) > 0)
					throw new Exception ("�˵������������ֹ����! ");
			
	    	sb = new StringBuffer();
	    	
	    	sb.append(" select count(fin.pk_finindex) counts from dap_finindex fin left join dap_rtvouch rt ");
	    	sb.append(" on fin.pk_corp = rt.pk_corp and fin.pk_rtvouch = rt.pk_voucher ");
	    	sb.append(" where rt.year >= " + fkrq_Y + " and rt.period >= " + fkrq_M + " and fin.pk_corp = '"+pk_corp+"' and fin.billcode = '"+billno+"'  and nvl(fin.dr,0)=0 and nvl(rt.dr,0)=0");
	    	
	    	
			String count = iUAPQueryBS.executeQuery(sb.toString(), new ColumnProcessor("counts")) == null ? "" : iUAPQueryBS.executeQuery(sb.toString(),new ColumnProcessor("counts")).toString();
			if(count != null) 
				if(Integer.valueOf(count) > 0)
					throw new Exception ("����������ƾ֤����ֹ����! ");
			
			sb = new StringBuffer();
			sb.append(" select count(explanation) counts from gl_voucher ");
			sb.append(" where pk_corp = '"+pk_corp+"' and year= "+fkrq_Y+" and period = "+fkrq_M+" ");
			sb.append(" and explanation in ('���º���ԭ����','���º��ð�װ','���·��乤��','���º���ȼ��','���º��õ��','���·����������') ");
			sb.append(" and nvl(dr,0)=0  and pk_system='XX' ");
			
			
			count = iUAPQueryBS.executeQuery(sb.toString(),
					new ColumnProcessor("counts")) == null ? "" : iUAPQueryBS
					.executeQuery(sb.toString(), new ColumnProcessor("counts"))
					.toString();
//			if (count != null)
//				if (Integer.valueOf(count) > 0)
//					throw new Exception("���������ɳɱ�ƾ֤����ֹ����! ");
			
			Object counts = iUAPQueryBS.executeQuery("select count(1) from eh_validoperate where pk_button = (select pk_button from eh_buttons where buttonname = '����') and nvl(dr,0) = 0 ", new ColumnProcessor());
			if(Integer.valueOf(counts.toString()) > 0) {
				counts = iUAPQueryBS.executeQuery("select count(1) from eh_validoperate where pk_button = (select pk_button from eh_buttons where buttonname = '����') and nvl(dr,0) = 0 and isenable = 'N'", new ColumnProcessor());
				if(Integer.valueOf(counts.toString()) == 0) {
					// ���DIALOG add by river for 2011-12-22
					ValidDlg validdlg = new ValidDlg((ClientUI)getBillUI());
					if(validdlg.showModal() == UIDialog.ID_OK) {
						// ��Ӷ�DIALOG�е���Ϣ����֤
						String txtUser = validdlg.getTxtUser().getText();
						String txtPass = new String(validdlg.getTxtPass().getPassword());
						
						
						Encode encode = new Encode();
						txtPass = encode.encode(txtPass);
						
						Object obj = iUAPQueryBS.executeQuery(" select count(1) from sm_user where user_code = '"+txtUser+"'", new ColumnProcessor());
						if(obj == null || Integer.valueOf(obj.toString()) == 0) {
							try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark , validuser , validpass) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+sivo.getPk_in()+"' , '"+sivo.getBillno()+"' , '��֤ʧ�ܣ���¼�������ڣ�' , '"+txtUser+"' , '"+txtPass+"')", null); } catch (Exception e) {};
							
							throw new Exception("��֤ʧ�ܣ���¼�������ڣ� ");
						
						}
						obj = iUAPQueryBS.executeQuery(" select authen_type from sm_user where user_code = '"+txtUser+"' and user_password = '"+txtPass+"'", new ColumnProcessor());
						Object cuserid = iUAPQueryBS.executeQuery(" select cuserid from sm_user where user_code = '"+txtUser+"'", new ColumnProcessor());
						
						if(cuserid == null)
							cuserid = "";
						
						if(obj == null) {
							try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark  , validuser , validpass , validcuserid) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+sivo.getPk_in()+"' , '"+sivo.getBillno()+"' , '��֤ʧ�ܣ��������' , '"+txtUser+"' , '"+txtPass+"' , '"+cuserid+"')", null); } catch (Exception e) {e.printStackTrace();};
							throw new Exception("��֤ʧ�ܣ�������� ");
						
						}
						
						if(obj != null)
							if("ncca".equals(obj.toString().trim())) {
								CADlg cadlg = new CADlg((ClientUI)getBillUI());
								if(cadlg.showModal() == UIDialog.ID_OK) {
									String txtValid = new String(cadlg.getTxtValid().getPassword()); // ��ȡ�����������CA��֤��
									
									/**
									 *   ���������CA��֤�Ĵ���
									 */
									
									txtValid = encode.encode(txtPass);
								} else {
									try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark , validuser , validpass , validcuserid) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+sivo.getPk_in()+"' , '"+sivo.getBillno()+"' , '������ȡ��' , '"+txtUser+"' , '"+txtPass+"' , '"+cuserid+"')", null); } catch (Exception e) {};
									return ;
								}
							}
						
						obj = iUAPQueryBS.executeQuery("select pk_role from eh_validoperate where pk_button = (select pk_button from eh_buttons where buttonname = '����') and nvl(dr,0) = 0 and isenable = 'Y'", new ColumnProcessor());
						obj = iUAPQueryBS.executeQuery("select count(1) from sm_user_role where cuserid = ( select cuserid from sm_user where user_code = '"+txtUser+"' and user_password = '"+txtPass+"' )and pk_role in ("+obj+") and pk_corp = '"+pk_corp+"'", new ColumnProcessor());
						if(obj == null || Integer.valueOf(obj.toString()) == 0) {
							
							try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark , validuser , validpass , validcuserid) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+sivo.getPk_in()+"' , '"+sivo.getBillno()+"' , 'ע�⣺���û�û�����ð�ťȨ�ޣ� ' , '"+txtUser+"' , '"+txtPass+"' , '"+cuserid+"')", null); } catch (Exception e) {};
							throw new Exception("��֤ʧ�ܣ��û��޴�Ȩ�ޣ� ");
						
						}
						
						try {iUAPQueryBS.executeQuery("update eh_validoperate set opeatefield = 'Y' where  pk_button = (select pk_button from eh_buttons where buttonname = '����') and dr = 0", null); } catch (Exception e) {}
						
						try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark  , validuser , validpass , validcuserid) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+sivo.getPk_in()+"' , '"+sivo.getBillno()+"' , 'TRUE' , '"+txtUser+"' , '"+txtPass+"' , '"+cuserid+"')", null); } catch (Exception e) {};
						
					} else {
						try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark  , validuser , validpass ) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+sivo.getPk_in()+"' , '"+sivo.getBillno()+"' , '������ȡ��')", null); } catch (Exception e) {};
						return ;
	   	
					}
					}
				}
				if(JOptionPane.showConfirmDialog((ClientUI)getBillUI(), "ȷ��Ҫִ����������� ", "��ʾ", JOptionPane.YES_NO_OPTION) == 0) {
			    	// �����������
			    	super.onBoCancelAudit();
			    	
		   	
			    	// ���������Զ������Ƶ��˵Ĳ���
			    	// �������
			    	modelVo = getBufferData().getCurrentVOClone();
			    	setCheckManAndDate(modelVo);
					
					// ���״̬һ�����˳�
					if (checkVOStatus(modelVo, new int[] { IBillStatus.CHECKPASS })) {
						System.out.println("��Ч����괦�����");
						return;
					}
					beforeOnBoAction(IBillButton.Audit, modelVo);
					
					StringBuffer sql = new StringBuffer();
					sql.append(" insert into eh_unapprove (pk_corp , vbillno , pk_bill , ts,billtablename , billname , OPERATER , REMARK) ");
					sql.append(" values('"+sivo.getPk_corp()+"' , '"+sivo.getBillno()+"' , '"+sivo.getPk_in()+"' , to_char(sysdate , 'yyyy-mm-dd HH24-mi-ss') , 'eh_stock_in' , '�ɹ���ⵥ' , '"+pk_user+"' , 'TRUE')");
					try {
						iUAPQueryBS.executeQuery(sql.toString(), null);
					} catch(Exception e) {}
					
					// *******************
					// AggregatedValueObject retVo = (AggregatedValueObject) getBusinessAction().approve(modelVo, getUIController().getBillType(),getBillUI()._getDate().toString(),getBillUI().getUserObject());
					
					AggregatedValueObject retVo = (AggregatedValueObject) nc.ui.pub.pf.PfUtilClient_EH.runAction(null, IPFACTION.APPROVE, getUIController().getBillType(), getBillUI()._getDate().toString(), modelVo, getBillUI().getUserObject(), null, null, null);
			
					if (PfUtilClient.isSuccess()) {
			
						afterOnBoAction(IBillButton.Audit, retVo);
						CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
						if (childVos == null)
							modelVo.setParentVO(retVo.getParentVO());
						else
							modelVo = retVo;
						// �����б�
						getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
						getBufferData().setCurrentRow(getBufferData().getCurrentRow());
					}
			    	
					super.onBoRefresh();
				} else {
					StringBuffer sql = new StringBuffer();
					sql.append(" insert into eh_unapprove (pk_corp , vbillno , pk_bill , ts,billtablename , billname,  OPERATER , REMARK) ");
					sql.append(" values('"+sivo.getPk_corp()+"' , '"+sivo.getBillno()+"' , '"+sivo.getPk_in()+"' , to_char(sysdate , 'yyyy-mm-dd HH24-mi-ss') , 'eh_stock_in' , '�ɹ���ⵥ' , '"+pk_user+"' , 'FALSE')");
					try {
						iUAPQueryBS.executeQuery(sql.toString(), null);
					} catch(Exception e) {}
			}
   	} else {
   		throw new Exception ("��ǰ��Ա�޿�����Ĳ���!");
   	}
   	
   	getBillUI().updateButtonUI();
   	
   }
   
   @Override
	public void onBoAudit() throws Exception {
		super.onBoAudit();
		
		AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
		if (modelVo != null) {
			StockInVO sivo = (StockInVO) modelVo
					.getParentVO();
			String vapproveid = sivo.getVapproveid();
			if (vapproveid == null)
				vapproveid = "";
	
			String pk_user = ClientUI.getCE().getUser()
					.getPrimaryKey();
	
			if ((sivo.getVbillstatus() == 1 || sivo.getVbillstatus() == 0)
					&& vapproveid.equals(pk_user)) {
				getButtonManager().getButton(IBillButton.CancelAudit)
						.setEnabled(true);
			} else {
				getButtonManager().getButton(IBillButton.CancelAudit)
						.setEnabled(false);
			}
		}
	
		getBillUI().updateButtonUI();
	}
    
    private void setCheckManAndDate(AggregatedValueObject vo) throws Exception {
		// �����������ڡ�������
		vo.getParentVO().setAttributeValue(getBillField().getField_CheckDate(),
				getBillUI()._getDate());
		vo.getParentVO().setAttributeValue(getBillField().getField_CheckMan(),
				getBillUI()._getOperator());
	}
    
    private CircularlyAccessibleValueObject[] getChildVO(
			AggregatedValueObject retVo) {
		CircularlyAccessibleValueObject[] childVos = null;
		if (retVo instanceof IExAggVO)
			childVos = ((IExAggVO) retVo).getAllChildrenVO();
		else
			childVos = retVo.getChildrenVO();
		return childVos;
	}

}
