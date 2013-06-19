package nc.ui.yto.blacklist;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.yto.blacklist.IblkQueryFunc;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.field.BillField;
import nc.vo.trade.pub.IBillStatus;
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

	private final IUAPQueryBS iUAPQueryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
	
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
			if(psndoc.length > 0) 
				pk_psnbasdoc = psndoc[0].getPk_psnbasdoc();
			
			if (pk_psnbasdoc != null) {
				

				nc.vo.yto.business.PsnbasdocVO psnbas = blkQuery.getPsnbasByPK(pk_psnbasdoc.toString(), this
						.getClientEnvironment().getConfigAccount()
						.getDataSourceName());
				
				PsndocVO[] psn = blkQuery.getPsnByPK(pk_psnbasdoc.toString(), this
						.getClientEnvironment().getConfigAccount()
						.getDataSourceName());
				
				if(psn.length > 0) {
					showErrorMessage("该人员未离职，不能加入黑名单");
					
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
				
				Object result = iUAPQueryBS.executeQuery("select count(1) count from hi_psndoc_bad where id = '"+psnbas.getId()+"'", new ColumnProcessor());
				if(Integer.valueOf(String.valueOf(result)) > 0) {
					
					showErrorMessage("该人员已经存在于黑名单中，无需重复添加！");
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
				
				panel.setBodyValueAt(psnbas.getPsnname(), event.getRow(), "psnname");
				
				String sex = psnbas.getAttributeValue("sex").toString();
				if(sex.equals("男")) {
					panel.setBodyValueAt("1", event.getRow(), "sex");
				}
				else if(sex.equals("女")) {
					panel.setBodyValueAt("2", event.getRow(), "sex");
				}
				
				panel.setBodyValueAt(psnbas.getBirthdate(), event.getRow(), "birthday");
				
				panel.setBodyValueAt(psnbas.getId(), event.getRow(), "id");
				
				Object docname = iUAPQueryBS.executeQuery("select docname from bd_defdoc where pk_defdoc = '"+psnbas.getPermanreside()+"' ", new ColumnProcessor());
				panel.setBodyValueAt(docname, event.getRow(), "permanentres");
				
				panel.setBodyValueAt(this.getClientEnvironment().getUser().getPrimaryKey(), event.getRow(), "cuserid");
				
				panel.setBodyValueAt(this.getClientEnvironment().getUser().getUserName(), event.getRow(), "def1");
				
				panel.setBodyValueAt(this.getClientEnvironment().getDate(), event.getRow(), "indate");
				
				panel.setBodyValueAt(this.getClientEnvironment().getCorporation().getPk_corp(), event.getRow(), "pk_corp");
				panel.setBodyValueAt(this.getClientEnvironment().getCorporation().getUnitname(), event.getRow(), "def2");

			}

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			
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
