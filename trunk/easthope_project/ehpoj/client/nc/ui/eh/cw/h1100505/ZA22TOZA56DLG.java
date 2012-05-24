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
 * ���ܣ������ε��ݲ���
 * Edit: ���� 
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
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0250505","��ⵥ");
            queryCondition.hideNormal();
        }
        return queryCondition;
    }

    @Override
	public String getHeadCondition() {
        StringBuffer strWherePart = new StringBuffer();
        //����ҵ�����ͣ���˾���룬����״̬��ѯ
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
     * ����:�ж����ζ�����ϸ�Ƿ�������ͬһ���ͻ�
     * @author ����Դ
     * 2008-4-9
     */
    @Override
	@SuppressWarnings("unchecked")
    public void onOk() {
    	//��û�����ε��ݿ��Բ���ʱ ������ʾ
        if (getbillListPanel().getHeadBillModel().getRowCount() == 0){
            super.close();
            JOptionPane.showMessageDialog(null,"û�п��Բ��յĵ���!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
            return;
        }
     
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            
            if(retBillVos.length>1||retBillVos.length==0){
                JOptionPane.showMessageDialog(null,"��ͷֻ��ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;             
           }
            
            if(retBillVos==null || retBillVo ==null || retBillVos.length==0 || retBillVos.length==0){
                JOptionPane.showMessageDialog(null,"������ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            pk_cubasdoc = retBillVo.getParentVO().getAttributeValue("pk_cubasdoc")==null?"":retBillVo.getParentVO()
                    .getAttributeValue("pk_cubasdoc").toString();
            
//            HashMap hmpk_contract=new HashMap();//��ź�ͬPK
//            for(int i=0;i<retBillVos.length;i++){
//            	StockInVO invos=(StockInVO) retBillVos[i].getParentVO();        	
//            	String pk_contract=invos.getPk_contract()==null?"":invos.getPk_contract().toString();//���κ�ͬPK    
//                
//            	if(!(hmpk_contract.containsKey(pk_contract)) && hmpk_contract.size()!=0 ){
//            		JOptionPane.showMessageDialog(null,"��ѡ��ͬһ�ź�ͬ!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
//                    return;
//            	}
//            	else{
//            		hmpk_contract.put(pk_contract, pk_contract);
//            	}
//            }
//            
//            String pk_contract=((StockInVO) retBillVos[0].getParentVO()).getPk_contract()==null?"":
//            	((StockInVO) retBillVos[0].getParentVO()).getPk_contract().toString();//���κ�ͬ��PK
//            HashMap hmpk_invbasdoc=new HashMap();          //��Ÿ������κ�ͬ��PKȥ�ɹ���ͬ���Һ�ͬ����
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
//                        String pk_invbasdoc=sbvo[j].getPk_invbasdoc()==null?"":sbvo[j].getPk_invbasdoc().toString();//���α�������pk
//                        UFDouble inamount=new UFDouble(sbvo[j].getInamount()==null?"0":sbvo[j].getInamount().toString());//�������
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
//            String pk_in = null;//��ⵥ��pk
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
//                retBillVo.getParentVO().setAttributeValue("pk_ins", pk_in);//����ⵥ�ж���PK�ŵ�����pk_ins�У��������ε�Vsourbillid��
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
//            HashMap hmPk_in=new HashMap();//�Լ����кͽ����ϵ�ֵ�ú� 
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
//                    UFDouble inamount=new UFDouble(sbvo[j].getInamount()==null?"0":sbvo[j].getInamount().toString());//�����������
//                	if(!pk_contract.equals("")){
//                		String pk_invabsdoc = sbvo[j].getPk_invbasdoc()==null?"":sbvo[j].getPk_invbasdoc().toString();
//                        //���ε��ݺ�ͬ����
//                		UFDouble amount=hmpk_invbasdoc.get(pk_invabsdoc)==null?new UFDouble():new UFDouble(hmpk_invbasdoc.get(pk_invabsdoc).toString());
//                        sbvo[j].setTaxinprice(new UFDouble(0));
//                        sbvo[j].setTaxinprice(amount);  //��ͬ����
//                        UFDouble ztsl=inamount.sub(amount);//��;����
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