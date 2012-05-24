package nc.ui.eh.cw.h1101010;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.dlg.CADlg;
import nc.ui.eh.dlg.ValidDlg;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.businessaction.IPFACTION;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.cw.h1101005.ArapFkVO;
import nc.vo.framework.rsa.Encode;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.sm.cmenu.GlorgbookExtVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.pub.IExAggVO;

/**
 * ����˵�����������
 * @author ����
 * 2008-05-28 ����02:03:18
 */

public class ClientEventHandler extends AbstractSPEventHandler{
	
	public static ClientUI clientui = null;

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}
    
     public String addCondtion() {
     	return  " vbilltype = '" + IBillType.eh_h1101005 + "' ";
     }
     
     @Override 
     protected void onBoCard() throws Exception {

  		super.onBoCard();
    	 
    	System.out.println("onBoCard !");
    	
    	AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
 		if(modelVo != null) {
 			ArapFkVO fkvo = (ArapFkVO)modelVo.getParentVO();
 			String vapproveid = fkvo.getVapproveid();
 	    	if(vapproveid == null)
 	    		vapproveid = "";
 	    	
 	    	String pk_user = ClientUI.getCE().getUser().getPrimaryKey();
 	    	
 	    	if((fkvo.getVbillstatus() == 1 || fkvo.getVbillstatus() == 0) && vapproveid.equals(pk_user)) {
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
			ArapFkVO fkvo = (ArapFkVO) modelVo
					.getParentVO();
			String vapproveid = fkvo.getVapproveid();
			if (vapproveid == null)
				vapproveid = "";

			String pk_user = ClientUI.getCE().getUser()
					.getPrimaryKey();

			if ((fkvo.getVbillstatus() == 1 || fkvo.getVbillstatus() == 0)
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
     
     /**
		 * ����-��� �����������ʱ�����������Ƶ��·��Ƿ���ʡ��ɱ�ƾ֤�Ƿ����� modify by river for 2011-11-24
		 */
    @Override
    protected void onBoCancelAudit() throws Exception {
    	System.out.println("CancelAudit !"); 
    	
    	IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
    	AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
    	setCheckManAndDate(modelVo);
    	
    	GlorgbookExtVO extVO = (GlorgbookExtVO)ClientUI.getCE().getValue("pk_glorgbook");
    	String pk_user = ClientUI.getCE().getUser().getPrimaryKey();
    	ArapFkVO fkvo = (ArapFkVO)modelVo.getParentVO();
    	String vapproveid = fkvo.getVapproveid();
    	if(vapproveid == null)
    		vapproveid = "";
    	
    	if((fkvo.getVbillstatus() == 1 || fkvo.getVbillstatus() == 0) && vapproveid.equals(pk_user)) {
	    	String billno = fkvo.getBillno();
	    	int fkrq_M = fkvo.getFkrq().getMonth();
	    	int fkrq_Y = fkvo.getFkrq().getYear();
	    	String pk_corp = fkvo.getPk_corp();
	    	
	    	StringBuffer sb = new StringBuffer();
	    	sb.append("select jz_flag from eh_period where pk_corp = '"+pk_corp+"' and nyear = '"+fkrq_Y+"' and nmonth = '"+fkrq_M+"'");
	    	String jz_flag = iUAPQueryBS.executeQuery(sb.toString(), new ColumnProcessor("jz_flag")) == null ? "" : iUAPQueryBS.executeQuery(sb.toString(),new ColumnProcessor("jz_flag")).toString();
			if(jz_flag != null)
				if(jz_flag.equals("Y"))
					throw new Exception ("�����ѽ��ʣ���ֹ����! ");
	    	
	    	
	    	sb = new StringBuffer();
	    	
	    	sb.append(" select count(fin.pk_finindex) counts from dap_finindex fin left join dap_rtvouch rt ");
	    	sb.append(" on fin.pk_corp = rt.pk_corp and fin.pk_rtvouch = rt.pk_voucher ");
	    	sb.append(" where rt.year >= " + fkrq_Y + " and rt.period >= " + fkrq_M + " and fin.pk_corp = '"+pk_corp+"' and fin.billcode = '"+billno+"' and nvl(fin.dr , 0) = 0 and nvl(rt.dr , 0) = 0");
	    	
			
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
			if (count != null)
				if (Integer.valueOf(count) > 0)
					throw new Exception("���������ɳɱ�ƾ֤����ֹ����! ");
			
			
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
							try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark , validuser , validpass) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+fkvo.getPk_fk()+"' , '"+fkvo.getBillno()+"' , '��֤ʧ�ܣ���¼�������ڣ�' , '"+txtUser+"' , '"+txtPass+"')", null); } catch (Exception e) {};
							
							throw new Exception("��֤ʧ�ܣ���¼�������ڣ� ");
						
						}
						obj = iUAPQueryBS.executeQuery(" select authen_type from sm_user where user_code = '"+txtUser+"' and user_password = '"+txtPass+"'", new ColumnProcessor());
						Object cuserid = iUAPQueryBS.executeQuery(" select cuserid from sm_user where user_code = '"+txtUser+"'", new ColumnProcessor());
						
						if(cuserid == null)
							cuserid = "";
						
						if(obj == null) {
							try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark  , validuser , validpass , validcuserid) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+fkvo.getPk_fk()+"' , '"+fkvo.getBillno()+"' , '��֤ʧ�ܣ��������' , '"+txtUser+"' , '"+txtPass+"' , '"+cuserid+"')", null); } catch (Exception e) {e.printStackTrace();};
							throw new Exception("��֤ʧ�ܣ�������� ");
						
						}
						if(obj != null)
							if("ncca".equals(obj.toString().trim())) {
								CADlg cadlg = new CADlg((ClientUI)getBillUI());
								if(cadlg.showModal() == UIDialog.ID_OK) {
									String txtValid = new String(cadlg.getTxtValid().getPassword());  // ��ȡ�����������CA��֤��
									
									/**
									 *   ���������CA��֤�Ĵ���
									 */
									
									txtValid = encode.encode(txtPass);
								} else {
									try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark , validuser , validpass , validcuserid) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+fkvo.getPk_fk()+"' , '"+fkvo.getBillno()+"' , '������ȡ��' , '"+txtUser+"' , '"+txtPass+"' , '"+cuserid+"')", null); } catch (Exception e) {};
									return ;
								}
							}
						
						obj = iUAPQueryBS.executeQuery("select pk_role from eh_validoperate where pk_button = (select pk_button from eh_buttons where buttonname = '����') and nvl(dr,0) = 0 and isenable = 'Y'", new ColumnProcessor());
						obj = iUAPQueryBS.executeQuery("select count(1) from sm_user_role where cuserid = ( select cuserid from sm_user where user_code = '"+txtUser+"' and user_password = '"+txtPass+"' )and pk_role in ("+obj+") and pk_corp = '"+pk_corp+"'", new ColumnProcessor());
						if(obj == null || Integer.valueOf(obj.toString()) == 0) {
							
							try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark , validuser , validpass , validcuserid) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+fkvo.getPk_fk()+"' , '"+fkvo.getBillno()+"' , 'ע�⣺���û�û�����ð�ťȨ�ޣ� ' , '"+txtUser+"' , '"+txtPass+"' , '"+cuserid+"')", null); } catch (Exception e) {};
							throw new Exception("��֤ʧ�ܣ��û��޴�Ȩ�ޣ� ");
						
						}
						
						try {iUAPQueryBS.executeQuery("update eh_validoperate set opeatefield = 'Y' where  pk_button = (select pk_button from eh_buttons where buttonname = '����') and dr = 0", null); } catch (Exception e) {}
						
						
						try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark  , validuser , validpass , validcuserid) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+fkvo.getPk_fk()+"' , '"+fkvo.getBillno()+"' , 'TRUE' , '"+txtUser+"' , '"+txtPass+"' , '"+cuserid+"')", null); } catch (Exception e) {};
						
					} else {
						try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark  , validuser , validpass ) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+fkvo.getPk_fk()+"' , '"+fkvo.getBillno()+"' , '������ȡ��')", null); } catch (Exception e) {};
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
				sql.append(" values('"+fkvo.getPk_corp()+"' , '"+fkvo.getBillno()+"' , '"+fkvo.getPk_fk()+"' , to_char(sysdate , 'yyyy-mm-dd HH24-mi-ss') , 'eh_arap_fk' , '���' , '"+pk_user+"' , 'TRUE')");
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
				sql.append(" insert into eh_unapprove (pk_corp , vbillno , pk_bill , ts,billtablename , billname , OPERATER , REMARK) ");
				sql.append(" values('"+fkvo.getPk_corp()+"' , '"+fkvo.getBillno()+"' , '"+fkvo.getPk_fk()+"' , to_char(sysdate , 'yyyy-mm-dd HH24-mi-ss') , 'eh_arap_fk' , '���' , '"+pk_user+"' , 'FALSE')");
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
			ArapFkVO fkvo = (ArapFkVO) modelVo
					.getParentVO();
			String vapproveid = fkvo.getVapproveid();
			if (vapproveid == null)
				vapproveid = "";

			String pk_user = ClientUI.getCE().getUser()
					.getPrimaryKey();

			if ((fkvo.getVbillstatus() == 1 || fkvo.getVbillstatus() == 0)
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
