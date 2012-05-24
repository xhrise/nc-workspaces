package nc.ui.eh.sc.h0451005;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.eh.sc.h0450705.ScMrpBVO;
import nc.vo.eh.sc.h0450705.ScMrpBillVO;
import nc.vo.eh.sc.h0450705.ScMrpCVO;
import nc.vo.eh.sc.h0450705.ScMrpVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * 说明:上下游单据界面 MRP->生产任务单
 * @author WB 
 * 2009-2-7 14:11:59
 */
public class ZB32TOZA44DLG extends AbstractBTOBDLG {
	
      
//    private IUAPQueryBS iUAPQueryBS;
    public ZB32TOZA44DLG(String pkField, String pkCorp, String operator, String funNode,
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
        		ScMrpBillVO.class.getName(),
        		ScMrpVO.class.getName(),
        		ScMrpBVO.class.getName(),
        		ScMrpCVO.class.getName()
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0450705","MRP运算");
            queryCondition.hideNormal();
        }
        return queryCondition;
    }

    @Override
	public String getHeadCondition() {
    	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    	String sDate = ce.getDate().toString();
    	StringBuffer strWherePart = new StringBuffer();
        //根据业务类型，公司编码，单据状态查询
        strWherePart.append(" pk_corp = '");
        strWherePart.append(getPkCorp());
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+"  and (isnull(sc_flag,'N')='N' or sc_flag='') " +
                "and calcdate >= '"+sDate+"' " +
                "and isnull(dr,0)=0 and (isnull(lock_flag,'N')='N' or lock_flag='') ");//增加关闭状态标记
        String strwp = strWherePart.toString();
        return strwp;
    }
    
    @Override
	public void loadBodyData2(String tmpWhere) throws Exception{
		//回去参照单据中表体VO
        String BillVos2 = getBillVos()[3];			//原料明细
        //返回根据条件查询的表体
        SuperVO[] childvo = HYPubBO_Client.queryByCondition(Class.forName(BillVos2), tmpWhere);
        getbillListPanel().getBodyBillModel("eh_sc_mrp_c").setBodyDataVO(childvo);
        getbillListPanel().getBodyBillModel("eh_sc_mrp_c").execLoadFormula();
	}
    
   @Override
   //表体判断
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
            //多表头的处理
            if(retBillVo.getChildrenVO()==null){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            HashMap hmsame=new HashMap();
            ArrayList arr = new ArrayList();
            ClientEnvironment ce = ClientEnvironment.getInstance();
            for(int i=0;i<retBillVos.length;i++){
            	 String pk_mrps = ((ScMrpVO)retBillVos[i].getParentVO()).getPk_mrp();
            	 arr.add(pk_mrps);
            	 ScMrpBVO[] bvos = (ScMrpBVO[]) retBillVos[i].getChildrenVO();
                 for(int j=0;j<bvos.length;j++){
                	ScMrpBVO bvo=bvos[j];
                 	String pk_mrp_b = bvos[j].getPk_mrp_b()==null?"":bvos[j].getPk_mrp_b().toString();						//MRP子表pk
                 	String pk_mrp = bvos[j].getPk_mrp()==null?"":bvos[j].getPk_mrp().toString();							//MRP主表pk
                 	bvo.setPk_mrp_b("'"+pk_mrp_b+"'");
                 	bvo.setPk_mrp("'"+pk_mrp+"'");
                 	String  billno = null;
                 	String pk_invbas = bvos[j].getPk_invbasdoc()==null?"":bvos[j].getPk_invbasdoc().toString();
                 	UFDouble amout= new UFDouble(bvos[j].getBcamount()==null?"0":bvos[j].getBcamount().toString());
                	UFDouble yzrwamount = new UFDouble(bvos[j].getYzrwamount()==null?"0":bvos[j].getYzrwamount().toString());
                	
                	bvo.setYzrwamount(yzrwamount);					//已做生产任务量
                	bvo.setYscamount(amout.sub(yzrwamount));		//未生产量
                	
                	// BOM 版本  edit by wb at 
            		String sql="select ver from eh_bom where isnull(dr,0)=0 and sc_flag ='Y'  and pk_corp='"+getPkCorp()+"' and pk_invbasdoc='"+pk_invbas+"'";
            		String billnosql = "select billno from eh_sc_mrp where pk_mrp = '"+pk_mrp+"'";
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
                 		ScMrpBVO oldbvo =(ScMrpBVO) hmsame.get(pk_invbas);
                 		UFDouble number = oldbvo.getBcamount();
                 		amout=amout.add(number);				//MRP数量累加
                 		oldbvo.setBcamount(amout);
                 		yzrwamount = yzrwamount.add(oldbvo.getYzrwamount()==null?new UFDouble(0):oldbvo.getYzrwamount());		//已经生产数量累加
                 		oldbvo.setYzrwamount(yzrwamount);
                 		String pk_mrp_bs = "'"+pk_mrp_b+"',"+oldbvo.getPk_mrp_b();
                 		String pk_mrpss = "'"+pk_mrp+"',"+oldbvo.getPk_mrp();
                 		String billnos = billno+","+oldbvo.getVsourcebillid();
                 		oldbvo.setPk_mrp_b(pk_mrp_bs);
                 		oldbvo.setPk_mrp(pk_mrpss);
                 		oldbvo.setVsourcebillid(billnos);
                 		oldbvo.setYscamount(amout.sub(yzrwamount));	//未生产量
                 		hmsame.put(pk_invbas, oldbvo);
                 	}else{
                 		hmsame.put(pk_invbas, bvo);
                 	}
                 }
            }
         
            Object[] keyset=hmsame.keySet().toArray();
            ScMrpBVO[] newmrp = new ScMrpBVO[hmsame.size()];
            for(int i=0;i<newmrp.length;i++){
            	newmrp[i]=(ScMrpBVO) hmsame.get(keyset[i].toString());
            	String pk_invbasdoc = newmrp[i].getPk_invbasdoc();
				try {
					HashMap hmkc = new PubTools().getDateinvKC(null, pk_invbasdoc, ce.getDate(), "1", ce.getCorporation().getPk_corp());
    				UFDouble kcamount = new UFDouble(hmkc.get(pk_invbasdoc)==null?"":hmkc.get(pk_invbasdoc).toString());
    				newmrp[i].setKcamount(kcamount);
				}catch (Exception e) {
					e.printStackTrace();
				}
            	
            }
            String[] pk_mrps = (String[])arr.toArray(new String[arr.size()]);
            String pk_mrpss = PubTools.combinArrayToString2(pk_mrps);
            if(pk_mrpss!=null&&pk_mrpss.length()>2500){
            	JOptionPane.showMessageDialog(null, "所选MRP记录太多,不允许超过100条记录!", "提示",JOptionPane.INFORMATION_MESSAGE);
				return;
            }
            retBillVo.getParentVO().setAttributeValue("pk_mrp", pk_mrpss);
            retBillVo.setChildrenVO(newmrp); 
        } 
        this.getAlignmentX();
        this.closeOK();
    } 
	

	 

	 
}