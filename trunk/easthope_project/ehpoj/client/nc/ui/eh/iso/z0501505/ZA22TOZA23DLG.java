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
import nc.vo.eh.stock.z0250505.StockInBVO;
import nc.vo.eh.stock.z0250505.StockInVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/** 
 * 功能：上下游单据参照（不合格的入库单到抽样单） 
 * Edit: 王明
 * Date:2008年11月28日13:40:02
 */
public class ZA22TOZA23DLG extends AbstractBTOBDLG {
	
	static IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
      
    public ZA22TOZA23DLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, String nodeKey, Object userObj, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, nodeKey, userObj, parent);
        
        BillItem billItem = getbillListPanel().getBillListData().getHeadItem("vbillstatus");
        initComboBox(billItem, IBillStatus.strStateRemark,true);
    }

    @Override
	public String[] getBillVos(){
        return new String[] {
        		 PubBillVO.class.getName(),
                 StockInVO.class.getName(),
                 StockInBVO.class.getName()  
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0250505","原料入库单");
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
        strWherePart.append("' and (NVL(th_flag,'N')='N' or th_flag='') and vbillstatus=1 ");
        String strwp = strWherePart.toString();
        return strwp;
    }
    /**
     * 功能:判断上游多行明细是否来自与同一个客户
     * @author 王明
     * 2008-4-9
     */
    @Override
	@SuppressWarnings("unchecked")
	public void onOk() {
    	//当没有上游单据可以参照时,弹出提示
        if (getbillListPanel().getHeadBillModel().getRowCount() == 0){
            super.close();
            JOptionPane.showMessageDialog(null,"没有可以参照的单据!","提示",JOptionPane.INFORMATION_MESSAGE); 
            return;
        }
     
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
        	         	
            retBillVo = getbillListPanel().getSelectedVO("nc.vo.eh.pub.PubBillVO", m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs("nc.vo.eh.pub.PubBillVO", m_billHeadVo, m_billBodyVo);
            CircularlyAccessibleValueObject[] childs = retBillVo.getChildrenVO();
            if(retBillVos==null || retBillVo ==null || retBillVos.length==0 ){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            if(retBillVos.length>=2){
            	JOptionPane.showMessageDialog(null,"不能同时选两条入库单!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            if(childs.length>=2){
            	JOptionPane.showMessageDialog(null,"不能同时选两个物料!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            ArrayList<StockInBVO> list=new ArrayList<StockInBVO>();
            StockInBVO[] bvos=(StockInBVO[]) retBillVo.getChildrenVO();
            StockInVO vo=(StockInVO) retBillVo.getParentVO();
            String pk_in = vo.getPk_in()==null?"":vo.getPk_in().toString();
            
            StockInBVO bvoone=bvos[0];
            UFDouble amount=bvoone.getInamount()==null?new UFDouble(0):bvoone.getInamount();
            String pk_invbasdoc=bvoone.getPk_invbasdoc()==null?"":bvoone.getPk_invbasdoc().toString();
            String sql = "select b.* from eh_iso a,eh_iso_b b where a.pk_iso=b.pk_iso and a.pk_invbasdoc='"+
            pk_invbasdoc+"' and NVL(a.dr,0)=0 and NVL(b.dr,0)=0 " +
            "and vbillstatus="+IBillStatus.CHECKPASS+" and NVL(a.def_1,'N')='Y' " +
                    "and (NVL(lock_flag,'N')='N' or lock_flag='') "; //def_1为最新标记和增加关闭状态标记
            ArrayList al = new ArrayList();
            try {
				al = (ArrayList)iUAPQueryBS.executeQuery(sql, new BeanListProcessor(IsoBVO.class));
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			if(al!=null && al.size()>0){
				//把取出来的数据放到上游表头单据相应的字段中
				for(int i=0;i<al.size();i++){
					StockInBVO bvo = new StockInBVO();
					bvo.setDef_1(((IsoBVO)al.get(i)).getPk_project()==null?"":((IsoBVO)al.get(i)).getPk_project().toString());
					bvo.setDef_6(new UFDouble(((IsoBVO)al.get(i)).getLl_ceil()==null?"":((IsoBVO)al.get(i)).getLl_ceil().toString()));
					bvo.setDef_7(new UFDouble(((IsoBVO)al.get(i)).getLl_limit()==null?"0":((IsoBVO)al.get(i)).getLl_limit().toString()));
					bvo.setDef_8(new UFDouble(((IsoBVO)al.get(i)).getRece_ceil()==null?"0":((IsoBVO)al.get(i)).getRece_ceil().toString()));
					bvo.setDef_9(new UFDouble(((IsoBVO)al.get(i)).getRece_limit()==null?"":((IsoBVO)al.get(i)).getRece_limit().toString()));
			        bvo.setDef_2(((IsoBVO)al.get(i)).getAnaimethod()==null?"":((IsoBVO)al.get(i)).getAnaimethod().toString());
			        bvo.setDr(new Integer(((IsoBVO)al.get(i)).getTreatype()==null?"3":((IsoBVO)al.get(i)).getTreatype().toString()));
					list.add(bvo);							
				}
			}
			vo.setDef_1(pk_invbasdoc);
			vo.setDef_6(amount);
			UFDouble amount2=new UFDouble(0);
			String sql2="select sum(weight) amount  from eh_stock_back a, eh_stock_back_b b where a.pk_back=b.pk_back and " +
					" NVL(a.dr,0)=0 and NVL(b.dr,0)=0 and a.pk_in='"+pk_in+"' and b.pk_invbasdoc='"+pk_invbasdoc+"' group by a.pk_in";
			 ArrayList al2 = new ArrayList();
	            try {
					al2 = (ArrayList)iUAPQueryBS.executeQuery(sql2,new MapListProcessor());
				} catch (BusinessException e) {
					e.printStackTrace();
				}
			if(al2!=null && al2.size()>0){
				HashMap hm=(HashMap) al2.get(0);
				amount2= amount.sub(new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString()));
				
			}else{
				amount2=amount;
			}
			vo.setDef_6(amount2);
			
			SuperVO[] child=new SuperVO[list.size()] ;
			child = list.toArray(child);
			
			retBillVo.setChildrenVO(child);
			retBillVo.setParentVO(vo);         
            
        }
        this.getAlignmentX();
        this.closeOK();
    }   
}