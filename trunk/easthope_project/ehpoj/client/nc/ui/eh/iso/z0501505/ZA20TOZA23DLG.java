package nc.ui.eh.iso.z0501505;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.iso.z0501005.IsoBVO;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z0151001.StockReceiptBVO;
import nc.vo.eh.stock.z0151001.StockReceiptVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/** 
 * 功能：上下游单据参照（收货通知单到抽样单） 
 * Edit: 张起源 
 * Date:2008-04-14
 */

public class ZA20TOZA23DLG extends AbstractBTOBDLG {
	//getDatebase Link
	static IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
      
    public ZA20TOZA23DLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, String nodeKey, Object userObj, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, nodeKey, userObj, parent);
        
        BillItem billItem = getbillListPanel().getBillListData().getHeadItem("vbillstatus");
        initComboBox(billItem, IBillStatus.strStateRemark,true);
    }

    @Override
	public String[] getBillVos(){
        return new String[] { PubBillVO.class.getName(),
        		StockReceiptVO.class.getName(),
                StockReceiptBVO.class.getName()
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0151001","收货通知单");
            queryCondition.hideNormal();
        }
        return queryCondition;
    }

    @Override
	public String getHeadCondition() {
        StringBuffer strWherePart = new StringBuffer();
        //根据业务类型，公司编码，单据状态查询
        strWherePart.append(" pk_corp = '");
        strWherePart.append(getPkCorp());
        strWherePart.append("'and vbillstatus="+IBillStatus.CHECKPASS+" and (NVL(yjcy_flag,'N')='N' or yjcy_flag='') " +
                            " and ( yjsb_flag=1 or yjsb_flag=2 ) " +
                            " and NVL(dr,0)=0 and (NVL(lock_flag,'N')='N' or lock_flag='') " +
                            " and (allcheck=0 or allcheck=1 ) and NVL(isallx,'N')='N'  "); //增加没有经过司磅的条件和关闭标记
        String strwp = strWherePart.toString();
        return strwp;
        
        
    }
    
    @Override
	public String getBodyCondition() {
    	m_whereStr=null;
        StringBuffer strWherePart = new StringBuffer();
        strWherePart.append(" (NVL(issb,'N')='N' or issb='') and (NVL(sfcy_flag,'N')='N' or sfcy_flag ='') " +
                "  and NVL(dr,0)=0 and allcheck='Y' and  (NVL(rk_flag,'N')='N' or rk_flag='') ");  //没有经过司磅的单据和allcheck为是否免检标记
        String strwp = strWherePart.toString();
        return strwp;
    }
    
    /**
     * 功能:判断上游多行明细是否来自与同一个客户
     * @author 张起源
     * 2008-4-9
     */
    @Override
	public void onOk() {
    	//当没有上游单据可以参照时 弹出提示
        if (getbillListPanel().getHeadBillModel().getRowCount() == 0){
            super.close();
            JOptionPane.showMessageDialog(null,"没有可以参照的单据!","提示",JOptionPane.INFORMATION_MESSAGE); 
            return;
        }
    
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
        	
        	@SuppressWarnings("unused")
			String ss=m_billVo;
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            
            if(retBillVos.length>1||retBillVos.length==0){
           	 	JOptionPane.showMessageDialog(null,"表头只能选择一条数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;          	
           }
            
            StockReceiptBVO[] StockReceiptBvo = (StockReceiptBVO[]) retBillVo.getChildrenVO();
                if(StockReceiptBvo==null || StockReceiptBvo.length ==0 || StockReceiptBvo.length >1){
                	JOptionPane.showMessageDialog(null,"表体只能选择一条数据或至少选择一条数据！","提示",JOptionPane.INFORMATION_MESSAGE); 
                    return;          	
                }
            
            if(retBillVos==null || retBillVo ==null || retBillVos.length==0 || retBillVos.length==0){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            
            ArrayList list=new ArrayList();
            //1。把上游单据子表的物料主键取出来
            String pk_invbasdoc = retBillVo.getChildrenVO()[0].getAttributeValue("pk_invbasdoc")==null?"":
            					retBillVo.getChildrenVO()[0].getAttributeValue("pk_invbasdoc").toString();
            String pk_receipt_b = retBillVo.getChildrenVO()[0].getAttributeValue("pk_receipt_b")==null?"":
				retBillVo.getChildrenVO()[0].getAttributeValue("pk_receipt_b").toString(); //收货通知单子表主键

            StockReceiptVO stockReceiptVO = (StockReceiptVO) retBillVo.getParentVO();
            
            UFDouble inamount=new UFDouble(retBillVo.getChildrenVO()[0].getAttributeValue("inamount")==null?"":
				retBillVo.getChildrenVO()[0].getAttributeValue("inamount").toString());//本次收获数量
            
            UFDouble packageamount=new UFDouble(retBillVo.getChildrenVO()[0].getAttributeValue("sumpackagweight")==null?"":
				retBillVo.getChildrenVO()[0].getAttributeValue("sumpackagweight").toString()).div(new UFDouble(1000));//包装袋重量
            
            UFDouble amount =inamount.sub(packageamount);
//				UFDouble amount =inamount;
            stockReceiptVO.setDef_6(amount);
            
            String pk_cubasdoc = retBillVo.getParentVO().getAttributeValue("pk_cubasdoc")==null?"":
                retBillVo.getParentVO().getAttributeValue("pk_cubasdoc").toString(); //上游表头的客户pk
            
            stockReceiptVO.setDef_5(pk_cubasdoc);
            
            UFBoolean retail_flag = new UFBoolean(retBillVo.getParentVO().getAttributeValue("retail_flag")==null?"":
                retBillVo.getParentVO().getAttributeValue("retail_flag").toString());
            
            String billno = retBillVo.getParentVO().getAttributeValue("billno")==null?"":
                retBillVo.getParentVO().getAttributeValue("billno").toString(); //上游表头单据号
            
            String carnumber = retBillVo.getParentVO().getAttributeValue("carnumber")==null?"":
                retBillVo.getParentVO().getAttributeValue("carnumber").toString(); //上游表头的车牌号
            
            String tranno = retBillVo.getParentVO().getAttributeValue("tranno")==null?"":
                retBillVo.getParentVO().getAttributeValue("tranno").toString(); //上游表头的车皮号
            stockReceiptVO.setCarnumber(carnumber);
            stockReceiptVO.setDef_3(tranno);
//            .setBillno(tranno);
            
            
            String retailinfo = null;
            if(retail_flag.toString().equals("Y")){
                //String sql = " select retailinfo from eh_stock_receipt where billno='"+billno+"' and NVL(dr,0)=0 ";//modify by houcq 2011-03-14
                String sql = " select retailinfo from eh_stock_receipt where pk_receipt='"+stockReceiptVO.getPk_receipt()+"' and NVL(dr,0)=0 ";
                try {
                    ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
                    if(all!=null && all.size()>0){
                        for(int i=0;i<all.size();i++){
                            HashMap hm = (HashMap) all.get(i);
                            retailinfo = hm.get("retailinfo")==null?"":hm.get("retailinfo").toString();
                        }
                    }
                } catch (BusinessException e) {
                    e.printStackTrace();
                }   
            }
            stockReceiptVO.setDef_4(retailinfo);
            
            stockReceiptVO.setDef_1(pk_invbasdoc);
            stockReceiptVO.setDef_2(pk_receipt_b);
            //2.根据物料去查询该物料的质量标准单
            String sql = "select b.* from eh_iso a,eh_iso_b b where a.pk_iso=b.pk_iso and a.pk_invbasdoc='"+
            	pk_invbasdoc+"' and NVL(a.dr,0)=0 and NVL(b.dr,0)=0 " +
                        "and vbillstatus="+IBillStatus.CHECKPASS+" and NVL(a.def_1,'N')='Y' " +
                                " and (NVL(lock_flag,'N')='N' or lock_flag='' ) "; //def_1为最新标记 add by wb 增加关闭状态标记
            
            ArrayList al = new ArrayList();
            try {
				al = (ArrayList)iUAPQueryBS.executeQuery(sql, new BeanListProcessor(IsoBVO.class));
				if(al!=null && al.size()>0){
					//把取出来的数据放到下游单据相应的字段中
					for(int i=0;i<al.size();i++){
						StockReceiptBVO bvo = new StockReceiptBVO();
						bvo.setGg(((IsoBVO)al.get(i)).getPk_project()==null?"":((IsoBVO)al.get(i)).getPk_project().toString());
						bvo.setDef_1(((IsoBVO)al.get(i)).getLl_ceil()==null?"":((IsoBVO)al.get(i)).getLl_ceil().toString());
						bvo.setDef_2(((IsoBVO)al.get(i)).getLl_limit()==null?"":((IsoBVO)al.get(i)).getLl_limit().toString());
						bvo.setDef_3(((IsoBVO)al.get(i)).getRece_ceil()==null?"":((IsoBVO)al.get(i)).getRece_ceil().toString());
						bvo.setDef_4(((IsoBVO)al.get(i)).getRece_limit()==null?"":((IsoBVO)al.get(i)).getRece_limit().toString());
                        bvo.setDef_5(((IsoBVO)al.get(i)).getAnaimethod()==null?"":((IsoBVO)al.get(i)).getAnaimethod().toString());
                        bvo.setDr(new Integer(((IsoBVO)al.get(i)).getTreatype()==null?"3":((IsoBVO)al.get(i)).getTreatype().toString()));
						list.add(bvo);
					}                  
				}else{
                    JOptionPane.showMessageDialog(null,"此物料没有在原料标准单中维护或此物料单据已经关闭，请核实!","提示",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            retBillVo.setChildrenVO(child);
        }
        this.getAlignmentX();
        this.closeOK();

    }   
}