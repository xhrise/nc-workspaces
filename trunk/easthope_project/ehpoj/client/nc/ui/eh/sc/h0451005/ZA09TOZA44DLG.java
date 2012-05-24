package nc.ui.eh.sc.h0451005;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0206005.OrderBVO;
import nc.vo.eh.trade.z0206005.OrderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * ˵��:�����ε��ݽ���
 * @author ����
 * 2007-9-29 ����04:02:28
 */
public class ZA09TOZA44DLG extends AbstractBTOBDLG {
	
      
//    private IUAPQueryBS iUAPQueryBS;
    public ZA09TOZA44DLG(String pkField, String pkCorp, String operator, String funNode,
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
        		OrderVO.class.getName(),
        		OrderBVO.class.getName()
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0206005","���۶���");
            queryCondition.hideNormal();
        }
        return queryCondition;
    }

    @Override
	public String getHeadCondition() {
    	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    	String sDate = ce.getDate().toString();
    	StringBuffer strWherePart = new StringBuffer();
        //����ҵ�����ͣ���˾���룬����״̬��ѯ
        strWherePart.append(" pk_corp = '");
        strWherePart.append(getPkCorp());
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+"  and (isnull(scrw_flag,'N')='N' or scrw_flag='') " +
                "and enddate >= '"+sDate+"' " +
                "and isnull(dr,0)=0 and (isnull(lock_flag,'N')='N' or lock_flag='') ");//���ӹر�״̬���
        String strwp = strWherePart.toString();
        return strwp;
    }
   @Override
   //�����ж�
	public String getBodyCondition() {
	   		m_whereStr=null;
		   StringBuffer strWherePart = new StringBuffer();
		   strWherePart.append(" (isnull(sc_flag,'N')='N' or sc_flag='') ");
		   String strwp = strWherePart.toString();
		   return strwp;
	}
	@Override
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void onOk()  {
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            getbillListPanel().setBodyModelDataCopy(getbillListPanel().getHeadBillModel().convertIntoModelRow(getbillListPanel().getHeadTable().getSelectedRow()));        
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            //���ͷ�Ĵ���
            if(retBillVo.getChildrenVO()==null){
                JOptionPane.showMessageDialog(null,"������ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            HashMap hmsame=new HashMap();
            ArrayList arr = new ArrayList();
            for(int i=0;i<retBillVos.length;i++){
            	 String pk_ord = ((OrderVO)retBillVos[i].getParentVO()).getPk_order();
            	 arr.add(pk_ord);
            	 OrderBVO[] orders=(OrderBVO[]) retBillVos[i].getChildrenVO();
                 for(int j=0;j<orders.length;j++){
                 	OrderBVO bvo=orders[j];
                 	String pk_order_b = orders[j].getPk_order_b()==null?"":orders[j].getPk_order_b().toString();			//�����ӱ�pk
                 	String pk_order = orders[j].getPk_order()==null?"":orders[j].getPk_order().toString();					//��������pk
                 	bvo.setPk_order_b("'"+pk_order_b+"'");
                 	bvo.setPk_order("'"+pk_order+"'");
                 	String  billno = null;
                 	String pk_invbas = orders[j].getPk_invbasdoc()==null?"":orders[j].getPk_invbasdoc().toString();
                 	UFDouble amout= new UFDouble(orders[j].getFzamount()==null?"0":orders[j].getFzamount().toString());
                	UFDouble scamount = new UFDouble(orders[j].getScamount()==null?"0":orders[j].getScamount().toString());
                	
                	bvo.setDef_8(scamount);				//��������
                	bvo.setDef_6(amout.sub(scamount));	//δ������
                	
                	// BOM �汾  edit by wb at 
            		String sql="select ver from eh_bom where isnull(dr,0)=0 and new_flag='Y' and pk_corp='"+getPkCorp()+"' and pk_invbasdoc='"+pk_invbas+"'";
            		String billnosql = "select billno from eh_order where pk_order = '"+pk_order+"'";
            		try {
    					Object verObj = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
    					Object billnoObj = iUAPQueryBS.executeQuery(billnosql, new ColumnProcessor());
    					if(verObj!=null){
    				    	Integer ver = Integer.parseInt(verObj.toString());
    				    	bvo.setDr(ver);	
    				    }else{
    				    	bvo.setDr(null);
    				    }
    					if(billnoObj!=null){
    				    	billno = billnoObj.toString();
    				    	bvo.setVsourcebillid(billno);
    				    }
					} catch (BusinessException e) {
							e.printStackTrace();
					}
                 	if(hmsame.containsKey(pk_invbas)){
                 		OrderBVO oldbvo=(OrderBVO) hmsame.get(pk_invbas);
                 		UFDouble number=oldbvo.getFzamount();
                 		amout=amout.add(number);				//���������ۼ�
                 		oldbvo.setFzamount(amout);
                 		scamount = scamount.add(oldbvo.getScamount()==null?new UFDouble(0):oldbvo.getScamount());		//�Ѿ����������ۼ�
                 		oldbvo.setScamount(scamount);
                 		String pk_order_bs = "'"+pk_order_b+"',"+oldbvo.getPk_order_b();
                 		String pk_orders = "'"+pk_order+"',"+oldbvo.getPk_order();
                 		String billnos = billno+","+oldbvo.getVsourcebillid();
                 		oldbvo.setPk_order_b(pk_order_bs);
                 		oldbvo.setPk_order(pk_orders);
                 		oldbvo.setVsourcebillid(billnos);
                 		oldbvo.setDef_8(scamount);				//��������
                 		oldbvo.setDef_6(amout.sub(scamount));	//δ������
                 		hmsame.put(pk_invbas, oldbvo);
                 	}else{
                 		hmsame.put(pk_invbas, bvo);
                 	}
                 }
            }
         
            Object[] keyset=hmsame.keySet().toArray();
            OrderBVO[] neworder=new OrderBVO[hmsame.size()];
            for(int i=0;i<neworder.length;i++){
            	neworder[i]=(OrderBVO) hmsame.get(keyset[i].toString());
            	String pk_invbasdoc = neworder[i].getPk_invbasdoc();
				try {
					HashMap hm = getKCamount(pk_invbasdoc);
					UFDouble kcamount = new UFDouble(hm==null?"0":hm.get(pk_invbasdoc)==null?"0":hm.get(pk_invbasdoc).toString());
					neworder[i].setDef_9(kcamount);
				}catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            }
            String[] pk_orders = (String[])arr.toArray(new String[arr.size()]);
            String pk_orderss = PubTools.combinArrayToString2(pk_orders);
            if(pk_orderss!=null&&pk_orderss.length()>2500){
            	JOptionPane.showMessageDialog(null, "��ѡ������¼̫��,��������100����¼!", "��ʾ",JOptionPane.INFORMATION_MESSAGE);
				return;
            }
            retBillVo.getParentVO().setAttributeValue("pk_order", pk_orderss);
            retBillVo.setChildrenVO(neworder); 
        } 
        this.getAlignmentX();
        this.closeOK();
    } 
	
	//�õ����ε����ݵ�����
	 @SuppressWarnings("unchecked")
	public HashMap calamount(String tablename,String amountname,String vsourcebillids){
	        StringBuffer sql=new StringBuffer("")
	        .append(" select vsourcebillrowid,sum(isnull(").append(amountname).append(",0)) amount from ").append(tablename)
	        .append(" where vsourcebillrowid in ").append(vsourcebillids).append(" and isnull(dr,0)=0 group by vsourcebillrowid");
	        
	        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	        HashMap hm=new HashMap();
	        try {
	            ArrayList arr=(ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
	            if(!arr.isEmpty()){
	                for(int i=0;i<arr.size();i++){
	                    HashMap rs=(HashMap)arr.get(i);
	                    String vsourcebillid = rs.get("vsourcebillrowid").toString();
	                    UFDouble amount=new UFDouble(rs.get("amount").toString());
	                    hm.put(vsourcebillid, amount);
	                }
	            }
	        } catch (BusinessException e) {
	            e.printStackTrace();
	        }
	        return hm;
	    }
	 
	 //�õ������е����϶�Ӧ������
	 @SuppressWarnings("unchecked")
	private HashMap getKCamount(String pk_invbasdocs) throws BusinessException{
		 IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 String sql="select sum(amount)  amount ,pk_invbasdoc pk from eh_kc  where pk_invbasdoc = '"+pk_invbasdocs+"' and isnull(dr,0)=0 group by pk_invbasdoc ";
		 HashMap hm=new HashMap();
		 ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		 for(int i=0;i<al.size();i++){
			 HashMap hmone=(HashMap) al.get(i);
			 String pk_invbasdoc=hmone.get("pk")==null?"":hmone.get("pk").toString();
			 UFDouble amount = new UFDouble(hmone.get("amount")==null?"0":hmone.get("amount").toString());
			 hm.put(pk_invbasdoc, amount);
		 }
		 return hm;
	 }
	 
	 
}