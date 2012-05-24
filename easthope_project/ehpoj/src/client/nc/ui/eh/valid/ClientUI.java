package nc.ui.eh.valid;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.body.AbstractClientUI;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.vo.eh.valid.MyBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

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

public class ClientUI extends AbstractClientUI implements ILinkQuery {

	private ClientEventHandler event = null;
	
	protected ClientEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	public String getRefBillType() {
		return null;
	}

	public ClientUI() {
		initilize();
	}
	
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		if("buttons".equals(((BillItem)e.getSource()).getKey())) {
			
			CircularlyAccessibleValueObject[] selectedVOs = getBillCardWrapper().getSelectedBodyVOs();
			
			if(selectedVOs.length > 0) {
				Object operateFlag = selectedVOs[0].getAttributeValue("opeatefield");
				Object enableFlag = selectedVOs[0].getAttributeValue("isenable");
			
				if(operateFlag == null)
					operateFlag = new UFBoolean(false);
				
				if(enableFlag == null) 
					enableFlag = new UFBoolean(false);
				
				if(((UFBoolean)operateFlag).booleanValue() || ((UFBoolean)enableFlag).booleanValue()) {
					JOptionPane.showMessageDialog(this, "该按钮已经被启用或被关联 , 不能修改按钮信息" , "提示" , JOptionPane.OK_OPTION);
					return false;
				}
			
			}
				
		}
		return super.beforeEdit(e);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		
		try {
			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
			
			if(e.getKey().equals("buttons")) {
				String pk = ((UIRefPane)getBillCardPanel().getBodyItem(e.getKey()).getComponent()).getRefPK();
				
				Object obj = iUAPQueryBS.executeQuery("select count(1) from eh_validoperate where pk_button = '"+pk+"' and dr = 0", new ColumnProcessor());
				if(Integer.valueOf(obj.toString()) > 0) {
					getBillCardPanel().setBodyValueAt(null , e.getRow() , "pk_button");
					getBillCardPanel().setBodyValueAt(null , e.getRow() , e.getKey());
					
					JOptionPane.showMessageDialog(this, "该按钮已被引用！" , "提示" , JOptionPane.OK_OPTION);
					return ;
				}
			} else if(e.getKey().equals("show_roler")) {
				String[] refpks = ((UIRefPane)getBillCardPanel().getBodyItem(e.getKey()).getComponent()).getRefPKs();
				
				if(refpks == null || refpks.length == 0)
					return ;
				
				String pk = "";
				for(String str : refpks) {
					pk += "'" + str + "',";
				}
				
				String role_name = "";
				for (int r = 0 ; r < refpks.length ; r++) {
					Object obj = iUAPQueryBS.executeQuery(" select role_name from sm_role where pk_role = '"+refpks[r]+"' " , new ColumnProcessor());
					role_name += obj + ",";
				}
				
				if(pk.length() > 0) {
					pk = pk.substring(0 , pk.length() - 1);
					role_name = role_name.substring(0 , role_name.length() - 1);
				}
				
				getBillCardPanel().setBodyValueAt(role_name , e.getRow() , e.getKey());
				getBillCardPanel().setBodyValueAt(pk, e.getRow(), "pk_role");
				getBillCardPanel().setBodyValueAt(role_name , e.getRow() , "role_name");
				
				getBillCardWrapper().getSelectedBodyVOs()[0].setAttributeValue("pk_role", pk);
				getBillCardWrapper().getSelectedBodyVOs()[0].setAttributeValue("role_name", role_name);
				
			}
		} catch (Exception ex) {
			
		}
		
		
	}

	/**
	 * 修改此方法初始化模板控件数据
	 */
	protected void initSelfData() {
		((UIRefPane)getBillCardPanel().getBodyItem("show_roler").getComponent()).setMultiSelectedEnabled(true);
	}

	private void initilize() {
		try {
			setDefaultData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setDefaultData() throws Exception {
		try {

			Class c = Class.forName(getUIControl().getBillVoName()[1]);
			SuperVO[] vos = getBusiDelegator().queryByCondition(c,
					getBodyWherePart());
			// 需要先清空
			getBufferData().clear();

			if (vos != null) {
				MyBillVO billVO = new MyBillVO();
				// 加载数据到单据
				billVO.setChildrenVO(vos);
				// 加载数据到缓冲
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
				} else {
					getBufferData().setCurrentVO(billVO);
				}

				// 设置当前行
				getBufferData().setCurrentRow(0);
				
			} else {
				getBufferData().setCurrentRow(-1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改此方法增加后台校验
	 */
	public Object getUserObject() {
		return new ClientUICheckRuleGetter();
	}

	protected String getBodyWherePart() {
		return " 1 = 1  order by createdate asc";
	}

	@Override
	protected ICardController createController() {
		return new nc.ui.eh.valid.ClientUICtrl();
	}

	public void doQueryAction(ILinkQueryData querydata) {
		String billId = querydata.getBillID();
		if (billId != null) {
			try {
				AggregatedValueObject vo = loadHeadData(billId);
				getBufferData().addVOToBuffer(vo);
				setListHeadData(new CircularlyAccessibleValueObject[] { vo
						.getParentVO() });
				getBufferData().setCurrentRow(getBufferData().getCurrentRow());
				setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static ClientEnvironment getCE() {

		ClientEnvironment ceVO = ClientEnvironment.getInstance();

		return ceVO;
	}

}
