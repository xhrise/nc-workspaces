package nc.ui.yto.blacklist;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.yto.blacklist.IblkQueryFunc;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.field.BillField;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.yto.business.PsnbasdocVO;
import nc.vo.yto.business.PsndocVO;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class ClientUI extends AbstractClientUI {

	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void initSelfData() {
	}

	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();

		String[] itemkeys = new String[] { fileDef.getField_Corp(),
				fileDef.getField_Operator(), fileDef.getField_Billtype(),
				fileDef.getField_BillStatus() };
		Object[] values = new Object[] { pkCorp,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				billtype, new Integer(IBillStatus.FREE).toString() };

		for (int i = 0; i < itemkeys.length; i++) {
			BillItem item = null;
			item = getBillCardPanel().getHeadItem(itemkeys[i]);
			if (item == null)
				item = getBillCardPanel().getTailItem(itemkeys[i]);
			if (item != null)
				item.setValue(values[i]);
		}
	}

	// @Override
	public void afterEdit(BillEditEvent event) {
		// super.afterEdit(event);
		BillCardPanel panel = this.getBillCardPanel();
		getBillCardPanel().setAutoExecHeadEditFormula(true);
		IblkQueryFunc blkQuery = (IblkQueryFunc) NCLocator.getInstance().lookup(IblkQueryFunc.class.getName());
		try {
			

			Object psncode = panel.getBodyValueAt(event.getRow() , "psncode");
			PsndocVO[] psndoc = null;
			if(psncode != null)
				psndoc = blkQuery.getPsnByPsncode(psncode.toString(), this
						.getClientEnvironment().getConfigAccount()
						.getDataSourceName());
			
			String pk_psnbasdoc = null;
			if(psndoc != null) 
				pk_psnbasdoc = psndoc[0].getPk_psnbasdoc();
			
			if (pk_psnbasdoc != null) {
				

				PsnbasdocVO psnbas = blkQuery.getPsnbasByPK(pk_psnbasdoc.toString(), this
						.getClientEnvironment().getConfigAccount()
						.getDataSourceName());
				
				PsndocVO[] psn = blkQuery.getPsnByPK(pk_psnbasdoc.toString(), this
						.getClientEnvironment().getConfigAccount()
						.getDataSourceName());
				
				if(psn.length > 0) {
					JOptionPane.showMessageDialog(null, "该人员未离职，不能加入黑名单" , "提示" , JOptionPane.OK_OPTION);
					
					panel.setBodyValueAt(null, event.getRow(), "psncode");
					panel.setBodyValueAt(null, event.getRow(), "psnname");
					panel.setBodyValueAt(null, event.getRow(), "sex");
					panel.setBodyValueAt(null, event.getRow(), "birthday");
					panel.setBodyValueAt(null, event.getRow(), "id");
					panel.setBodyValueAt(null, event.getRow(), "permanentres");
					panel.setBodyValueAt(null, event.getRow(), "cuserid");
					panel.setBodyValueAt(null, event.getRow(), "def1");
					panel.setBodyValueAt(null, event.getRow(), "indate");
					panel.setBodyValueAt(null, event.getRow(), "pk_corp");
					
					return;
				}
					
				
//				panel.getBodyItem("psnname").setValue(psnbas.getPsnname());
				panel.setBodyValueAt(psnbas.getPsnname(), event.getRow(), "psnname");
				
				String sex = psnbas.getAttributeValue("sex").toString();
				if(sex.equals("男")) {
//					panel.getBodyItem("sex").setValue("1");
					panel.setBodyValueAt("1", event.getRow(), "sex");
				}
				else if(sex.equals("女")) {
//					panel.getBodyItem("sex").setValue("2");
					panel.setBodyValueAt("2", event.getRow(), "sex");
				}
				
//				panel.getBodyItem("birthday").setValue(psnbas.getBirthdate());
				panel.setBodyValueAt(psnbas.getBirthdate(), event.getRow(), "birthday");
				
//				panel.getBodyItem("id").setValue(psnbas.getId());
				panel.setBodyValueAt(psnbas.getId(), event.getRow(), "id");
				
//				panel.getBodyItem("permanentres").setValue(psnbas.getBasgroupdef18());
				panel.setBodyValueAt(psnbas.getBasgroupdef18(), event.getRow(), "permanentres");
				
//				panel.getBodyItem("cuserid").setValue(this.getClientEnvironment().getUser().getPrimaryKey());
				panel.setBodyValueAt(this.getClientEnvironment().getUser().getPrimaryKey(), event.getRow(), "cuserid");
				
				panel.setBodyValueAt(this.getClientEnvironment().getUser().getUserName(), event.getRow(), "def1");
				
//				panel.getBodyItem("indate").setValue(this.getClientEnvironment().getDate());
				panel.setBodyValueAt(this.getClientEnvironment().getDate(), event.getRow(), "indate");
				
//				panel.getBodyItem("pk_corp").setValue(this.getClientEnvironment().getCorporation().getPk_corp());
				panel.setBodyValueAt(this.getClientEnvironment().getCorporation().getPk_corp(), event.getRow(), "pk_corp");
				panel.setBodyValueAt(this.getClientEnvironment().getCorporation().getUnitname(), event.getRow(), "def2");
				
				
				
//				
//				panel.updateValue();
//				panel.updateUI();
				

			}

		} catch (Exception e) {
			e.printStackTrace();
			
			panel.setBodyValueAt(null, event.getRow(), "psncode");
			panel.setBodyValueAt(null, event.getRow(), "psnname");
			panel.setBodyValueAt(null, event.getRow(), "sex");
			panel.setBodyValueAt(null, event.getRow(), "birthday");
			panel.setBodyValueAt(null, event.getRow(), "id");
			panel.setBodyValueAt(null, event.getRow(), "permanentres");
			panel.setBodyValueAt(null, event.getRow(), "cuserid");
			panel.setBodyValueAt(null, event.getRow(), "def1");
			panel.setBodyValueAt(null, event.getRow(), "indate");
			panel.setBodyValueAt(null, event.getRow(), "pk_corp");
		}
	}

}
