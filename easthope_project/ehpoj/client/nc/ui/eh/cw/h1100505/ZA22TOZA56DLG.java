package nc.ui.eh.cw.h1100505;

import java.awt.Container;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z0250505.StockInBVO;
import nc.vo.eh.stock.z0250505.StockInVO;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBillStatus;

/** 
 * 功能：上下游单据参照
 * Edit: 王明 
 * Date:2008-05-30
 */

public class ZA22TOZA56DLG extends AbstractBTOBDLG {
      
    public static String pk_cubasdoc = null;
    
    public ZA22TOZA56DLG(String pkField, String pkCorp, String operator, String funNode,
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
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0250505","入库单");
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
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+" and (isnull(lock_flag,'N')='N' or lock_flag='' ) " +
                " and isnull(dr,0)=0 and pk_contract!='' ");
        String strwp = strWherePart.toString();
        return strwp;
    }
    
    @Override
    public String getBodyCondition() {
    	m_whereStr = null;
    	return null;
    }
    /**
     * 功能:判断上游多行明细是否来自与同一个客户
     * @author 张起源
     * 2008-4-9
     */
    @Override
	@SuppressWarnings("unchecked")
    public void onOk() {
    	//当没有上游单据可以参照时 弹出提示
        if (getbillListPanel().getHeadBillModel().getRowCount() == 0){
            super.close();
            JOptionPane.showMessageDialog(null,"没有可以参照的单据!","提示",JOptionPane.INFORMATION_MESSAGE); 
            return;
        }
     
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            
            if(retBillVos.length>1||retBillVos.length==0){
                JOptionPane.showMessageDialog(null,"表头只能选择一条数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;             
           }
            
            if(retBillVos==null || retBillVo ==null || retBillVos.length==0 || retBillVos.length==0){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            pk_cubasdoc = retBillVo.getParentVO().getAttributeValue("pk_cubasdoc")==null?"":retBillVo.getParentVO()
                    .getAttributeValue("pk_cubasdoc").toString();
            
//            HashMap hmpk_contract=new HashMap();//存放合同PK
//            for(int i=0;i<retBillVos.length;i++){
//            	StockInVO invos=(StockInVO) retBillVos[i].getParentVO();        	
//            	String pk_contract=invos.getPk_contract()==null?"":invos.getPk_contract().toString();//上游合同PK    
//                
//            	if(!(hmpk_contract.containsKey(pk_contract)) && hmpk_contract.size()!=0 ){
//            		JOptionPane.showMessageDialog(null,"请选择同一张合同!","提示",JOptionPane.INFORMATION_MESSAGE); 
//                    return;
//            	}
//            	else{
//            		hmpk_contract.put(pk_contract, pk_contract);
//            	}
//            }
//            
//            String pk_contract=((StockInVO) retBillVos[0].getParentVO()).getPk_contract()==null?"":
//            	((StockInVO) retBillVos[0].getParentVO()).getPk_contract().toString();//上游合同的PK
//            HashMap hmpk_invbasdoc=new HashMap();          //存放根据上游合同的PK去采购合同中找合同数量
//            if(pk_contract!=null && pk_contract.length()>0){
//            	String sql="select pk_invbasdoc,amount from eh_stock_contract_b where pk_contract= '"+pk_contract+"' " +
//                        " and isnull(dr,0)=0 ";
//            	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//            	try {
//					ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
//					for(int i=0;i<al.size();i++){
//						HashMap hmamount=(HashMap) al.get(i);
//						String pk_invabsdoc=hmamount.get("pk_invbasdoc")==null?"":hmamount.get("pk_invbasdoc").toString();
//						UFDouble amount= hmamount.get("amount")==null?new UFDouble():new UFDouble( hmamount.get("amount").toString());
//						hmpk_invbasdoc.put(pk_invabsdoc, amount);
//					}
//				} catch (BusinessException e) {
//					e.printStackTrace();
//				}
//            }
//            
            ArrayList list=new ArrayList();
//            if(pk_contract!=null && pk_contract.length()>0){
//                for(int i=0;i<retBillVos.length;i++){
//                    StockInBVO[] sbvo=(StockInBVO[]) retBillVos[i].getChildrenVO();
//                    for(int j=0;j<sbvo.length;j++){
//                        String pk_invbasdoc=sbvo[j].getPk_invbasdoc()==null?"":sbvo[j].getPk_invbasdoc().toString();//上游表体物料pk
//                        UFDouble inamount=new UFDouble(sbvo[j].getInamount()==null?"0":sbvo[j].getInamount().toString());//入库数量
//                        UFDouble amount=new UFDouble(hmpk_invbasdoc.get(pk_invbasdoc)==null?"0":hmpk_invbasdoc.get(pk_invbasdoc).toString());
//                        UFDouble ztsl=inamount.sub(amount);
//                        
//                        sbvo[j].setDef_6(inamount);
//                        sbvo[j].setDef_7(amount);
//                        sbvo[j].setDef_8(ztsl);
//                        
//                        list.add(sbvo[j]);
//                    }                  
//                }         
//            }
//     
//            
//            String pk_in = null;//入库单的pk
//            if(retBillVos.length>0){
//                int length=retBillVos.length;
//                String[] pk_ins = new String[length];
//                StockInVO svo = new StockInVO();
//                for(int i=0;i<length;i++){
//                    svo = (StockInVO) retBillVos[i].getParentVO();
//                    pk_in = svo.getPk_in();
//                    pk_ins[i]=pk_in;
//                }
//                pk_in=PubTools.combinArrayToString2(pk_ins);
//                retBillVo.getParentVO().setAttributeValue("pk_ins", pk_in);//将入库单中多条PK放到上游pk_ins中，带到下游的Vsourbillid中
//            }
//            
            
//            ArrayList alarr = new ArrayList();
//            for(int i=0;i<retBillVos.length;i++){
//            	StockInBVO[] childs=(StockInBVO[]) retBillVos[i].getChildrenVO();
//                for (int j=0; j<childs.length; j++) {
//                		String pk_in_b=childs[j].getPk_in_b()==null?"":childs[j].getPk_in_b().toString();
//                		alarr.add(pk_in_b);
//                	}
//                }
//            String[] pk_in_b=(String[]) alarr.toArray(new String[alarr.size()]);
//         
//            String df = PubTool.combinArrayToString(pk_in_b);
//            String sql="select vsourcebillrowid,sum(ztmount) amount from eh_arap_fkqs_b where vsourcebillrowid " +
//            		"in"+df+"  and isnull(dr,0)=0 group by vsourcebillrowid";
//            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
//            
//            HashMap hmPk_in=new HashMap();//自己库中和界面上的值得和 
//    		try {
//    			ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
//    			for(int i=0;i<arr.size();i++){
//                    HashMap hm=(HashMap)arr.get(i);     
//                    String vsourcebillrowid=hm.get("vsourcebillrowid")==null?"":hm.get("vsourcebillrowid").toString();
//                    UFDouble amount=new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
//                    hmPk_in.put(vsourcebillrowid, amount);
//    			 }
//    		} catch (BusinessException e) {
//    			e.printStackTrace();
//    		}        
//        
          
////            String [] pk_in_b=new String[retBillVos.length];
//            for(int i=0;i<retBillVos.length;i++){
//            	StockInBVO[] sbvo=(StockInBVO[]) retBillVos[i].getChildrenVO();
//                for (int j = 0; j < sbvo.length; j++) {
//                	String pk_in_bs = sbvo[j].getPk_in_b()==null?"":sbvo[j].getPk_in_b().toString();
//            		UFDouble def_8=new UFDouble(hmPk_in.get(pk_in_bs)==null?"0":hmPk_in.get(pk_in_bs).toString());
//                    UFDouble inamount=new UFDouble(sbvo[j].getInamount()==null?"0":sbvo[j].getInamount().toString());//上游入库数量
//                	if(!pk_contract.equals("")){
//                		String pk_invabsdoc = sbvo[j].getPk_invbasdoc()==null?"":sbvo[j].getPk_invbasdoc().toString();
//                        //上游单据合同数量
//                		UFDouble amount=hmpk_invbasdoc.get(pk_invabsdoc)==null?new UFDouble():new UFDouble(hmpk_invbasdoc.get(pk_invabsdoc).toString());
//                        sbvo[j].setTaxinprice(new UFDouble(0));
//                        sbvo[j].setTaxinprice(amount);  //合同数量
//                        UFDouble ztsl=inamount.sub(amount);//在途数量
//                        sbvo[j].setDef_9(ztsl);
//                        
//                	}
//                    sbvo[j].setDef_8(def_8);
//                    list.add(sbvo[j]);
//                }
//            }   
//            if(pk_contract.equals("")){
//                for(int i=0;i<retBillVos.length;i++){
//                    CircularlyAccessibleValueObject[] childs=retBillVos[i].getChildrenVO();
//                    for (int j = 0; j < childs.length; j++) {
//                        list.add(childs[j]);
//                    }
//                }   
//            }            
            
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            retBillVo.setChildrenVO(child);
            
        }
        this.getAlignmentX();
        this.closeOK();

    }   
}