package nc.ui.eh.stock.h0150210;

import java.util.ArrayList;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.stock.h0150206.StockQuerypriceVO;
import nc.vo.eh.stock.z00140.SwVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;



/**
 * 功能说明：询价单
 * @author 王明
 * 2008年12月11日15:39:14
 */
public class ClientUI extends AbstractClientUI {

	
	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl());
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("querydate", _getDate());
		getBillCardPanel().setHeadItem("yxdate", _getDate());
		super.setDefaultData_withNObillno();
//		super.setDefaultData();
	}
	
	@Override
	protected void initSelfData() {
	     getBillCardWrapper().initHeadComboBox("method", ICombobox.CG_XJFS,true);
	     getBillListWrapper().initHeadComboBox("method", ICombobox.CG_XJFS,true);
	     super.initSelfData();
	}
	

	@Override
	public void afterEdit(BillEditEvent e) {
		String strKey = e.getKey();
		if(strKey.equals("vsw")||strKey.equals("areaname")||strKey.equals("invcode")){
			int row = e.getRow();
			getBillCardPanel().setBodyValueAt(null, row, "carriage");
			getBillCardPanel().setBodyValueAt(null, row, "memo");
			getBillCardPanel().setBodyValueAt(null, row, "dcj");	
			String pk_invbasdoc = getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?null:getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
			String pk_areacl = getBillCardPanel().getBodyValueAt(row, "pk_areacl")==null?null:getBillCardPanel().getBodyValueAt(row, "pk_areacl").toString();
			String pk_sw = getBillCardPanel().getBodyValueAt(row, "pk_sw")==null?null:getBillCardPanel().getBodyValueAt(row, "pk_sw").toString();
			String yscode = "";
			if(pk_invbasdoc!=null&&pk_areacl!=null&&pk_sw!=null){
				try {
					IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					SwVO swvo = (SwVO)iUAPQueryBS.retrieveByPK(SwVO.class, pk_sw);
					yscode = swvo!=null?swvo.getSwcode():"";					//运输方式编码
				} catch (BusinessException e1) {
					e1.printStackTrace();
				}
				UFDouble tpprice = new UFDouble(getBillCardPanel().getBodyValueAt(row, "tpprice")==null?"0":getBillCardPanel().getBodyValueAt(row, "tpprice").toString());
				ArrayList arr = new YfTools().getYf(pk_invbasdoc, pk_areacl, pk_sw,_getCorp().getPk_corp(),tpprice);
				if(arr!=null&&arr.size()==2){
					UFDouble sumyf = new UFDouble(arr.get(0)==null?"0":arr.get(0).toString());			//合计运费
					String memo = arr.get(1)==null?null:arr.get(1).toString();							//运费明细
					
					UFDouble tprice = new UFDouble(getBillCardPanel().getBodyValueAt(row, "tpprice")==null?"0":
													getBillCardPanel().getBodyValueAt(row, "tpprice").toString());				//谈判价格
					UFDouble dcj = tprice.add(sumyf);				//到厂价
					if(yscode.equals(YfTools.gjyscode)){			//国际货运时 到厂价=运费
						dcj = sumyf;
						sumyf = new UFDouble(0);
					}
					getBillCardPanel().setBodyValueAt(sumyf, row, "carriage");
					getBillCardPanel().setBodyValueAt(memo, row, "memo");
					getBillCardPanel().setBodyValueAt(dcj, row, "dcj");					//到厂价 = 谈判价+运费
				}
			}
		}
		if(strKey.equals("method")){
			//当询价方式有改变时，将表体的数据全部删除。时间：2010-01-12作者：张志远
			int rows = this.getBillCardPanel().getBillModel().getRowCount();
			int [] arr = new int[rows];
			this.getBillCardPanel().getBillModel().delLine(arr);
		}
		super.afterEdit(e);
	}
	
	
}
