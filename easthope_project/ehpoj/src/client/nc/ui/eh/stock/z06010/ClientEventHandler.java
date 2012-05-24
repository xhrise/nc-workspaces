package nc.ui.eh.stock.z06010;

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
import nc.vo.eh.stock.z06005.SbbillVO;
import nc.vo.framework.rsa.Encode;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.sm.cmenu.GlorgbookExtVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.pub.IExAggVO;

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
		if (modelVo != null) {
			SbbillVO sbvo = (SbbillVO) modelVo
					.getParentVO();
			String vapproveid = sbvo.getVapproveid();
			if (vapproveid == null)
				vapproveid = "";

			String pk_user = ClientUI.getCE().getUser()
					.getPrimaryKey();

			if ((sbvo.getVbillstatus() == 1 || sbvo.getVbillstatus() == 0)
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
	protected void onBoRefresh() throws Exception {

		super.onBoRefresh();

		System.out.println("onBoRefresh !");

		AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
		if (modelVo != null) {
			SbbillVO sbvo = (SbbillVO) modelVo
					.getParentVO();
			String vapproveid = sbvo.getVapproveid();
			if (vapproveid == null)
				vapproveid = "";

			String pk_user = ClientUI.getCE().getUser()
					.getPrimaryKey();

			if ((sbvo.getVbillstatus() == 1 || sbvo.getVbillstatus() == 0)
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

		GlorgbookExtVO extVO = (GlorgbookExtVO) nc.ui.eh.stock.z0250510.ClientUI
				.getCE().getValue("pk_glorgbook");
		String pk_user = ClientUI.getCE().getUser()
				.getPrimaryKey();
		SbbillVO sbvo = (SbbillVO) modelVo
				.getParentVO();
		String vapproveid = sbvo.getVapproveid();
		if (vapproveid == null)
			vapproveid = "";

		if ((sbvo.getVbillstatus() == 1 || sbvo.getVbillstatus() == 0)
				&& vapproveid.equals(pk_user)) {
			int fkrq_M = sbvo.getDmakedate().getMonth();
			int fkrq_Y = sbvo.getDmakedate().getYear();
			String pk_corp = sbvo.getPk_corp();
			

			StringBuffer sb = new StringBuffer();
			sb.append("select jz_flag from eh_period where pk_corp = '"
					+ pk_corp + "' and nyear = '" + fkrq_Y + "' and nmonth = '"
					+ fkrq_M + "'");
			String jz_flag = iUAPQueryBS.executeQuery(sb.toString(),
					new ColumnProcessor("jz_flag")) == null ? ""
					: iUAPQueryBS.executeQuery(sb.toString(),
							new ColumnProcessor("jz_flag")).toString();
			if (jz_flag != null)
				if (jz_flag.equals("Y"))
					throw new Exception("当月已结帐，禁止操作! ");
			
			
			sb = new StringBuffer();
			sb.append("select count(dmakedate) counts from eh_stock_in where dmakedate >= '"+sbvo.getDmakedate().toString()+"'  and vsourcebillid = '"+sbvo.getPk_sbbill()+"' and pk_corp = '"+pk_corp+"' and nvl(dr,0)=0");
			String vsbcount = iUAPQueryBS.executeQuery(sb.toString(),new ColumnProcessor("counts")) == null ? "": iUAPQueryBS.executeQuery(sb.toString(),new ColumnProcessor("counts")).toString();
			if(vsbcount != null) 
				if(Integer.valueOf(vsbcount) > 0)
					throw new Exception ("此单据已做入库单，禁止操作! ");
			
			sb = new StringBuffer();
			sb.append("select count(pk_sbbills) counts from eh_stock_sample ");
			sb.append(" where pk_sbbills like '%"+sbvo.getPk_sbbill()+"%' and pk_corp = '"+pk_corp+"' and dmakedate >= '"+sbvo.getDmakedate().toString()+"'  and nvl(dr,0)=0" );
			String sbcount = iUAPQueryBS.executeQuery(sb.toString(),new ColumnProcessor("counts")) == null ? "": iUAPQueryBS.executeQuery(sb.toString(),new ColumnProcessor("counts")).toString();
			if(sbcount != null) 
				if(Integer.valueOf(sbcount) > 0)
					throw new Exception ("此单据已生成检测申请单，禁止操作! ");
			
			Object counts = iUAPQueryBS.executeQuery("select count(1) from eh_validoperate where pk_button = (select pk_button from eh_buttons where buttonname = '弃审') and nvl(dr,0) = 0 ", new ColumnProcessor());
			if(Integer.valueOf(counts.toString()) > 0) {
				counts = iUAPQueryBS.executeQuery("select count(1) from eh_validoperate where pk_button = (select pk_button from eh_buttons where buttonname = '弃审') and nvl(dr,0) = 0 and isenable = 'N'", new ColumnProcessor());
				if(Integer.valueOf(counts.toString()) == 0) {
			// 添加DIALOG add by river for 2011-12-22
					ValidDlg validdlg = new ValidDlg((ClientUI)getBillUI());
					if(validdlg.showModal() == UIDialog.ID_OK) {
						// 添加对DIALOG中的信息的验证
						String txtUser = validdlg.getTxtUser().getText();
						String txtPass = new String(validdlg.getTxtPass().getPassword());
						
						
						Encode encode = new Encode();
						txtPass = encode.encode(txtPass);
						
						Object obj = iUAPQueryBS.executeQuery(" select count(1) from sm_user where user_code = '"+txtUser+"'", new ColumnProcessor());
						if(obj == null || Integer.valueOf(obj.toString()) == 0) {
							try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark , validuser , validpass) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+sbvo.getPk_sbbill()+"' , '"+sbvo.getBillno()+"' , '认证失败，登录名不存在！' , '"+txtUser+"' , '"+txtPass+"')", null); } catch (Exception e) {};
							
							throw new Exception("认证失败，登录名不存在！ ");
						
						}
						obj = iUAPQueryBS.executeQuery(" select authen_type from sm_user where user_code = '"+txtUser+"' and user_password = '"+txtPass+"'", new ColumnProcessor());
						Object cuserid = iUAPQueryBS.executeQuery(" select cuserid from sm_user where user_code = '"+txtUser+"'", new ColumnProcessor());
						
						if(cuserid == null)
							cuserid = "";
						
						if(obj == null) {
							try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark  , validuser , validpass , validcuserid) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+sbvo.getPk_sbbill()+"' , '"+sbvo.getBillno()+"' , '认证失败，密码错误！' , '"+txtUser+"' , '"+txtPass+"' , '"+cuserid+"')", null); } catch (Exception e) {e.printStackTrace();};
							throw new Exception("认证失败，密码错误！ ");
						
						}
						if(obj != null)
							if("ncca".equals(obj.toString().trim())) {
								CADlg cadlg = new CADlg((ClientUI)getBillUI());
								if(cadlg.showModal() == UIDialog.ID_OK) {
									String txtValid = new String(cadlg.getTxtValid().getPassword());  // 获取界面上输入的CA认证码
									
									/**
									 *   在这里添加CA认证的代码
									 */
									
									txtValid = encode.encode(txtPass);
								} else {
									try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark , validuser , validpass , validcuserid) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+sbvo.getPk_sbbill()+"' , '"+sbvo.getBillno()+"' , '操作被取消' , '"+txtUser+"' , '"+txtPass+"' , '"+cuserid+"')", null); } catch (Exception e) {};
									return ;
								}
							}
						
						obj = iUAPQueryBS.executeQuery("select pk_role from eh_validoperate where pk_button = (select pk_button from eh_buttons where buttonname = '弃审') and nvl(dr,0) = 0 and isenable = 'Y'", new ColumnProcessor());
						obj = iUAPQueryBS.executeQuery("select count(1) from sm_user_role where cuserid = ( select cuserid from sm_user where user_code = '"+txtUser+"' and user_password = '"+txtPass+"' )and pk_role in ("+obj+") and pk_corp = '"+pk_corp+"'", new ColumnProcessor());
						if(obj == null || Integer.valueOf(obj.toString()) == 0) {
							
							try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark , validuser , validpass , validcuserid) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+sbvo.getPk_sbbill()+"' , '"+sbvo.getBillno()+"' , '注意：该用户没有配置按钮权限！ ' , '"+txtUser+"' , '"+txtPass+"' , '"+cuserid+"')", null); } catch (Exception e) {};
							throw new Exception("认证失败，用户无此权限！ ");
						
						}
						
						try {iUAPQueryBS.executeQuery("update eh_validoperate set opeatefield = 'Y' where pk_button = (select pk_button from eh_buttons where buttonname = '弃审') and dr = 0", null); } catch (Exception e) {}
						
						try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark  , validuser , validpass , validcuserid) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+sbvo.getPk_sbbill()+"' , '"+sbvo.getBillno()+"' , 'TRUE' , '"+txtUser+"' , '"+txtPass+"' , '"+cuserid+"')", null); } catch (Exception e) {};
						
					} else {
						try { iUAPQueryBS.executeQuery("insert into eh_validoperate_log( pk_log , cuserid , pk_corp , operatedate , type , pk_bill , billno , remark  , validuser , validpass ) values (generatepk('"+pk_corp+"') , '"+pk_user+"' , '"+pk_corp+"' , '"+ClientUI.getCE().getDate().toString()+"' , 'CancelAudit' , '"+sbvo.getPk_sbbill()+"' , '"+sbvo.getBillno()+"' , '操作被取消')", null); } catch (Exception e) {};
						return ;
			
					}
					
				}
			}
			if(JOptionPane.showConfirmDialog((ClientUI)getBillUI(), "确定要执行弃审操作吗？ ", "提示", JOptionPane.YES_NO_OPTION) == 0) {
				// 进行弃审操作
				super.onBoCancelAudit();
	
				// 弃审后进行自动驳回制单人的操作
				// 获得数据
				modelVo = getBufferData().getCurrentVOClone();
				setCheckManAndDate(modelVo);
	
				// 如果状态一致则退出
				if (checkVOStatus(modelVo, new int[] { IBillStatus.CHECKPASS })) {
					System.out.println("无效的鼠标处理机制");
					return;
				}
				beforeOnBoAction(IBillButton.Audit, modelVo);
	
				StringBuffer sql = new StringBuffer();
				sql.append(" insert into eh_unapprove (pk_corp , vbillno , pk_bill , ts,billtablename , billname , OPERATER , REMARK) ");
				sql.append(" values('" + sbvo.getPk_corp() + "' , '"+ sbvo.getBillno() + "' , '" + sbvo.getPk_sbbill()+ "' , to_char(sysdate , 'yyyy-mm-dd HH24-mi-ss') , 'eh_sbbill' , '司磅单', '"+pk_user+"' , 'TRUE')");
				try {
					iUAPQueryBS.executeQuery(sql.toString(), null);
				} catch (Exception e) {
				}
	
				// *******************
				// AggregatedValueObject retVo = (AggregatedValueObject)
				// getBusinessAction().approve(modelVo,
				// getUIController().getBillType(),getBillUI()._getDate().toString(),getBillUI().getUserObject());
	
				AggregatedValueObject retVo = (AggregatedValueObject) nc.ui.pub.pf.PfUtilClient_EH
						.runAction(null, IPFACTION.APPROVE, getUIController()
								.getBillType(), getBillUI()._getDate().toString(),
								modelVo, getBillUI().getUserObject(), null, null,
								null);
	
				if (PfUtilClient.isSuccess()) {
	
					afterOnBoAction(IBillButton.Audit, retVo);
					CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
					if (childVos == null)
						modelVo.setParentVO(retVo.getParentVO());
					else
						modelVo = retVo;
					// 更新列表
					getBufferData().setVOAt(getBufferData().getCurrentRow(),
							modelVo);
					getBufferData().setCurrentRow(getBufferData().getCurrentRow());
				}
	
				super.onBoRefresh();
			} else {
				StringBuffer sql = new StringBuffer();
				sql.append(" insert into eh_unapprove (pk_corp , vbillno , pk_bill , ts,billtablename , billname , OPERATER , REMARK) ");
				sql.append(" values('" + sbvo.getPk_corp() + "' , '"+ sbvo.getBillno() + "' , '" + sbvo.getPk_sbbill()+ "' , to_char(sysdate , 'yyyy-mm-dd HH24-mi-ss') , 'eh_sbbill' , '司磅单', '"+pk_user+"' , 'FALSE')");
				try {
					iUAPQueryBS.executeQuery(sql.toString(), null);
				} catch (Exception e) {
				}
			}
		} else {
			throw new Exception("当前人员无可弃审的操作!");
		}
		
		getBillUI().updateButtonUI();

	}
	
	@Override
	public void onBoAudit() throws Exception {
		super.onBoAudit();
		
		AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
		if (modelVo != null) {
			SbbillVO sbvo = (SbbillVO) modelVo
					.getParentVO();
			String vapproveid = sbvo.getVapproveid();
			if (vapproveid == null)
				vapproveid = "";

			String pk_user = ClientUI.getCE().getUser()
					.getPrimaryKey();

			if ((sbvo.getVbillstatus() == 1 || sbvo.getVbillstatus() == 0)
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
		// 放入审批日期、审批人
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
