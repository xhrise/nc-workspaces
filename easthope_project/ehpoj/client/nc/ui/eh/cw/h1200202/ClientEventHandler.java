package nc.ui.eh.cw.h1200202;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.cw.h1200202.HjlhsBVO;
import nc.vo.eh.cw.h1200202.HjlhsVO;
import nc.vo.eh.ipub.ISQLChange;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;
/**
 * 回机料维护
 * @author houcq
 * 2011-06-16 10:41:21 
 */
public class ClientEventHandler extends AbstractEventHandler {
	
	String pk_icout_b = null;
	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}
	
	 public void onBoSave() throws Exception {
         //保存时不允许为空
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();  
         super.onBoSave();
         HjlhsBVO[] bvo = (HjlhsBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
         StringBuilder pk_icout_bs = new StringBuilder("('");
         for (int i=0;i<bvo.length;i++)
         {
        	 pk_icout_bs.append(bvo[i].getPk_icout_b()+"','");
         }
         pk_icout_bs.append("')");
         String updateSql = "update eh_icout_b set hj_flag='Y' where pk_icout_b in "+pk_icout_bs.toString();
         PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
 		 pubitf.updateSQL(updateSql);
	 }
	 
	 
	/**
	 * 功能：回机料价格的自动变更，取成本计算生成中的成本价格
	 * 时间：2009-11-21
	 * 作者：张志远
	 */
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
	}

     
     
     @Override
    protected void onBoRefresh() throws Exception {
    	super.onBoRefresh();
    }
     
    /**
     * 在保存回机出库单根据金额数量计算出回机料单价并回写到采购入库单中
     * wb
     * 2008-12-8 17:49:27
     * @throws Exception
     */
    public void updateHJrk() throws Exception{
    	String startdate = _getDate().toString().substring(0,8)+"01"; 	//indate between '"+startdate+"' and '"+_getDate()+"' and 
    	String enddate = _getDate().toString().substring(0,8)+_getDate().getDaysMonth();
    	String pk_corp = _getCorp().getPk_corp();
    	//取回机出库的平均单价
    	StringBuffer sql = new StringBuffer()
    	.append(" select cast(round(sum(isnull(b.def_7,0))/sum(isnull(outamount,0)),4) as decimal(18,4))  hjdj")
    	.append(" from eh_icout a,eh_icout_b b")
    	.append(" where a.pk_icout = b.pk_icout")
    	.append(" and a.pk_outtype = '"+ISQLChange.OUTTYPE_HJ+"' and a.pk_corp = '"+pk_corp+"'")
    	.append(" and a.outdate between '"+startdate+"' and '"+enddate+"' AND ISNULL(a.dr,0)=0 AND ISNULL(b.dr,0)=0");
    	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	Object hjobj = (Object) iUAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
    	UFDouble hjdj = hjobj==null?new UFDouble("0"):new UFDouble(hjobj.toString());   //回机单价
    	//更新到入库单中回机入库单中
    	StringBuffer updateSql = new StringBuffer()
    	.append(" update eh_stock_in_b set inprice = "+hjdj+" ,def_6 = inamount*"+hjdj+" where pk_in_b in ")
    	.append(" ( select b.pk_in_b")
    	.append(" from eh_stock_in a,eh_stock_in_b b")
    	.append(" where a.pk_in = b.pk_in")
    	.append(" and a.pk_intype = '"+ISQLChange.INTYPE_HJ+"' and a.pk_corp = '"+pk_corp+"'")
    	.append(" and a.indate between '"+startdate+"' and '"+enddate+"' AND ISNULL(a.dr,0)=0 AND ISNULL(b.dr,0)=0")
    	.append(" )");
    	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		pubitf.updateSQL(updateSql.toString());
    }
    
    //取成本计算中的物料单价
    @SuppressWarnings("unchecked")
	public HashMap getPrice(){
    	HashMap priceHm = new HashMap();
    	String cudate = PubTools.getLastDate(_getDate().toString());
        String year = cudate.substring(0,4);
        String month = cudate.substring(5,7);
        StringBuffer sql = new StringBuffer()
        .append(" SELECT arcob.pk_costobj_b,CASE arcob.def_6 WHEN 0 THEN 0 ELSE arcob.sumfy/arcob.def_6 END price ")
        .append(" FROM eh_arap_cosths arco, eh_arap_cosths_b arcob WHERE arco.pk_cosths = arcob.pk_cosths  ")
        .append(" AND arco.pk_corp = '"+_getCorp().getPk_corp()+"' AND NVL(arco.dr,0)=0 AND arco.Nyear = '"+year+"' AND arco.Nmonth = '"+month+"' ");
        IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	try {
			ArrayList ar  = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(ar!=null&&ar.size()>0){
				for(int i=0;i<ar.size();i++){
					HashMap m = (HashMap) ar.get(i);
					String pk_invbasdoc =  m.get("pk_costobj_b")==null?"":m.get("pk_costobj_b").toString();
					UFDouble price = new UFDouble(m.get("price")==null?"0":m.get("price").toString(),2);
					priceHm.put(pk_invbasdoc, price);
				}
			}
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
    	return priceHm;
    }
    /**
     * 功能：
     * <p>根据出库单生成明细</p>
     */    
    @SuppressWarnings("unchecked")
    protected void onBoGENRENDETAIL() throws Exception{

    	String startdate = _getDate().toString().substring(0,8)+"01";
    	String enddate = _getDate().toString().substring(0,8)+_getDate().getDaysMonth();
        String pk_corp = _getCorp().getPrimaryKey(); 
        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
        StringBuffer sql=new StringBuffer()
        .append(" select a.billno,a.pk_icout,a.pk_icout_b,a.pk_invbasdoc,a.outamount," )
        .append("b.invcode,b.invname,b.invspec, b.invtype, b.def1,d.brandname,e.measname from (")
        .append(" select a.billno,a.pk_icout,b.pk_icout_b,b.pk_invbasdoc,b.outamount")
        .append(" from eh_icout a, eh_icout_b b")
        .append(" where a.pk_icout = b.pk_icout")
        .append("  and a.pk_corp = '"+pk_corp+"'")        
        .append(" and a.dmakedate between '"+startdate+"' and '"+enddate+"'")
        .append(" and nvl(a.dr, 0) = 0  and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
        .append(" and nvl(b.hj_flag,'N') ='N'")
        .append(" and a.pk_outtype = '"+ISQLChange.OUTTYPE_HJ+"'")
        .append(" ) a,bd_invbasdoc b,bd_invmandoc c,eh_brand d, bd_measdoc e")
        .append(" where a.pk_invbasdoc = c.pk_invmandoc")
        .append(" and c.pk_invbasdoc = b.pk_invbasdoc")
        .append(" and e.pk_measdoc = b.pk_measdoc")
        .append(" and d.pk_brand = b.invpinpai");
        ArrayList<HashMap> arr=(ArrayList<HashMap>)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
        int rowcount=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
        int[] rows=new int[rowcount];
        for(int i=rowcount - 1;i>=0;i--){
                rows[i]=i;
        }
        getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(rows);
        getBillUI().updateUI();
    	getBillCardPanelWrapper().getBillCardPanel().setTailItem("dmakedate", _getDate());
    	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);
    	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vbilltype", IBillType.eh_h1200202);
    	getBillCardPanelWrapper().getBillCardPanel().setTailItem("coperatorid", _getOperator());
    	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
        HashMap priceHm =getPrice();
        if (arr.size()==0)
        {
        	getBillUI().showErrorMessage("本期没有回机料!");
        	return;
        }
        for(int i=0;i<arr.size();i++){
            onBoLineAdd();
            HashMap hm=(HashMap)arr.get(i);
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString(), i, "pk_invbasdoc");
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("billno"), i, "ckdbillno");
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("pk_icout"), i, "pk_icout");
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("pk_icout_b"), i, "pk_icout_b");
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invcode"), i, "invcode");
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invname"), i, "invname");
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invspec"), i, "gg");
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invtype"), i, "invtype");
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("brandname"), i, "brand");
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("def1"), i, "color");
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("measname"), i, "dw");
            UFDouble outamount = new UFDouble(hm.get("outamount")==null?"0":hm.get("outamount").toString());
            UFDouble unitprice = new UFDouble(priceHm.get(hm.get("pk_invbasdoc"))==null?"0":priceHm.get(hm.get("pk_invbasdoc")).toString());
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("outamount"), i, "outamount");
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(priceHm.get(hm.get("pk_invbasdoc")), i, "unitprice");
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(outamount.multiply(unitprice), i, "je");
        }
        getBillUI().updateUI();
        getButtonManager().getButton(IBillButton.Save).setEnabled(true);
        getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
		getBillUI().updateButtonUI();
        
    } 
    /* （非 Javadoc）
     * @see nc.ui.trade.bill.BillEventHandler#onBoElse(int)
     */
    protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.GENRENDETAIL:    //生成明细
                onBoGENRENDETAIL();
                break;
        }  
        super.onBoElse(intBtn);
    }
	protected void onBoDelete() throws Exception {
		int res = onBoDeleteN(); 
    	if(res==0){
    		return;
    	}
    	HjlhsVO hvo = (HjlhsVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
    	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
    	String updateSql="update eh_icout_b set hj_flag='N' where pk_icout_b in (select pk_icout_b from eh_hjlhs_b where pk_hjlhs='"+hvo.getPk_hjlhs()+"')";
    	pubitf.updateSQL(updateSql);
		super.onBoTrueDelete();
	}
	
	/**
	 * 功能：删行时取消标记
	 * 时间：201年6月16日
	 */
	protected void onBoLineDel() throws Exception {
		PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		int[] rowids = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRows();
		for (int i=0;i<rowids.length;i++)
		{
			String pk_icout_b = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(rowids[i],"pk_icout_b")==null?"":
				getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(rowids[i], "pk_icout_b").toString();
			String sql = "update eh_icout_b set hj_flag='N' where pk_icout_b = '"+pk_icout_b+"' ";
			pubitf.updateSQL(sql);
		}	   	
		super.onBoLineDel();
	}
}
