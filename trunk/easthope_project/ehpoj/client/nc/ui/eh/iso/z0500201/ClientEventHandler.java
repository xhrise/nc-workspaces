
package nc.ui.eh.iso.z0500201;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.iso.z0500201.BdCheckitemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * 功能：检测项目单
 * @author 张起源
 * 2008-04-11 
 */
public class ClientEventHandler extends CardEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	} 
	 
	 @Override
	protected void onBoSave() throws Exception {
         //非空的有效性判断
         int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();        
         for (int i = 0; i < row; i++) {
             //设置公司编码
             getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");             
         }
         //保存时不允许为空
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate(); 
         super.onBoSave();
         onBoQuery();
         ((ClientUI)getBillUI()).setDefaultData();
	 }
	 
	 @Override
	protected void onBoQuery() throws Exception {
         SuperVO[] vos = getBusiDelegator().queryByCondition(BdCheckitemVO.class,  "pk_corp='" + _getCorp().getPk_corp() + "' order by itemcode");
         //需要先清空
         getBufferData().clear();

         if (vos != null) {
             HYBillVO billVO = new HYBillVO();
             //加载数据到单据
             billVO.setChildrenVO(vos);
             //加载数据到缓冲
             if (getBufferData().isVOBufferEmpty()) {
                 getBufferData().addVOToBuffer(billVO);
             } else {
                 getBufferData().setCurrentVO(billVO);
             }

             //设置当前行
             getBufferData().setCurrentRow(0);
         } else {
             getBufferData().setCurrentRow(-1);
         }
	}

	/**
	 * 功能：如果检测项目已被引用则不允许删除
	 * 时间：2010-03-01
	 * 作者：张志远
	 * */
	protected void onBoLineDel() throws Exception {
		if(this.usedCheckitem()){
			this.getBillUI().showErrorMessage("该检测项目已被引用不允许删除！");
			return;
		}
		super.onBoLineDel();
	}

	protected void onBoDelete() throws Exception {
		if(this.usedCheckitem()){
			this.getBillUI().showErrorMessage("该检测项目已被引用不允许删除！");
			return;
		}
		super.onBoDelete();
	}
	
	/**
	 * 功能：判断检测项目已被引用则不允许删除
	 * 时间：2010-03-01
	 * 作者：张志远
	 * */
	public boolean usedCheckitem(){
		boolean value = false;
		int row = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		String pk_checkitem = this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "pk_checkitem")==null?"":
								 this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "pk_checkitem").toString();
		StringBuffer strSql = new StringBuffer()
		.append(" select pk_project from( ")
		.append(" select pk_project from eh_iso_b where pk_project = '"+pk_checkitem+"' ")
		.append(" union all ")
		.append(" select pk_project From eh_stock_checkreport_b where pk_project = '"+pk_checkitem+"') ");
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		ArrayList arr = null;
		try {
			arr = (ArrayList) iUAPQueryBS.executeQuery(strSql.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if(arr.size()>0){
			value = true;
		}
		return value;
	}
	 
	


}
