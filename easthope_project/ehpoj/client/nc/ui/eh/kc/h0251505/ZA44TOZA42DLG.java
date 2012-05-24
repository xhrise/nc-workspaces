package nc.ui.eh.kc.h0251505;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0451005.ScPosmBVO;
import nc.vo.eh.sc.h0451005.ScPosmVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/** 
 * 功能：上下游单据参照（生产任务单到生产备料单） 
 * Edit: 张起源 
 * Date:2008-05-07
 */
public class ZA44TOZA42DLG extends AbstractBTOBDLG {
    
    static IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
      
    public ZA44TOZA42DLG(String pkField, String pkCorp, String operator, String funNode,
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
        		ScPosmVO.class.getName(),
        		ScPosmBVO.class.getName() 
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"h0451005","生产任务单");
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
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+" and (isnull(bl_flag,'N')='N' or bl_flag='') " +
                " and (isnull(lock_flag,'N')='N' or lock_flag='') and isnull(dr,0)=0 ");
        String strwp = strWherePart.toString();
        return strwp;
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
            
            if(retBillVos==null || retBillVo ==null || retBillVos.length==0 || retBillVos.length==0){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            if(retBillVos.length>1||retBillVos.length==0){
           	 	JOptionPane.showMessageDialog(null,"表头只能选择一条数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;          	
           }
            
            ArrayList al = new ArrayList();
            String pk_posm = retBillVo.getParentVO().getAttributeValue("pk_posm").toString(); //取得上游生产任务单的PK
            
//            String pk_posm =  null;
//            if(retBillVos.length>0){
//                int length = retBillVos.length;                
//                String[] pk_posms = new String[length];
//                ScPosmVO svo = new ScPosmVO();
//                for(int i=0;i<length;i++){
//                    svo = (ScPosmVO) retBillVos[i].getParentVO();
//                    pk_posm = svo.getPk_posm();
//                    pk_posms[i] = pk_posm;
//                }
//                pk_posm = PubTools.combinArrayToString2(pk_posms);
//                retBillVo.getParentVO().setAttributeValue("pk_posms", pk_posm);
//            }
            
            //根据生产任务单的PK去BOM档案中找相应的最新版本的物料 add by zqy 2008-6-18 19:15:59
            StringBuffer sql = new StringBuffer()
            .append(" select a.pk_invbasdoc,a.pk_measdoc vunit ,a.zamount,a.altflag,c.scmount,c.pk_unit,c.pk_invbasdoc x ")
            .append(" from eh_bom_b a,eh_bom b,eh_sc_posm_b c,eh_sc_posm d ")
            .append(" where a.pk_bom=b.pk_bom and c.pk_posm=d.pk_posm and c.pk_invbasdoc=b.pk_invbasdoc ")
            .append(" and d.pk_posm ='"+pk_posm+"' and b.new_flag='Y' and b.pk_corp = d.pk_corp ")
            .append(" and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 and isnull(c.dr,0)=0 and isnull(d.dr,0)=0 ");
            
            try {
                HashMap hm = new HashMap();
                ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
                UFDouble zamount = null;
                UFDouble scmount =  null;
//                UFDouble changerate = null;
//                UFDouble changmount = null;
                UFDouble summount = null;
                
                if(all!=null && all.size()>0){
                    for(int i=0;i<all.size();i++){
                        HashMap hm1 = (HashMap) all.get(i);
                        String pk_invbasdoc = hm1.get("pk_invbasdoc")==null?"":hm1.get("pk_invbasdoc").toString(); //BOM子表物料PK
//                        String pk_unit1 = hm1.get("vunit")==null?"":hm1.get("vunit").toString();  //BOM子表物料单位
                        zamount = new UFDouble(hm1.get("zamount")==null?"0":hm1.get("zamount").toString()); //BOM中用料数量
//                        String altflag = hm1.get("altflag")==null?"":hm1.get("altflag").toString(); //是否替代物料的标记
                        scmount = new UFDouble(hm1.get("scmount")==null?"0":hm1.get("scmount").toString());//生产任务单表体的生产数量
//                        String pk_unit = hm1.get("pk_unit")==null?"":hm1.get("pk_unit").toString();   //生产任务单子表单位
                        
                        String pk_measdoc = hm1.get("vunit")==null?"":hm1.get("vunit").toString(); //BOM子表主计量单位
                        
//                        String pk_invbasdoc1 = hm1.get("x")==null?"":hm1.get("x").toString();  //生产任务单子表物料
//                        HashMap hmRate = new TzzkDAO().getInvRate(pk_invbasdoc1); //进行单位转换
//                        changerate = new UFDouble(hmRate.get(pk_unit)==null?"1":hmRate.get(pk_unit).toString());//转换的单位数量
                        
//                        StringBuffer sql2 = new StringBuffer()
//                        .append(" select b.changerate from eh_invbasdoc a,eh_invbasdoc_b b ")
//                        .append(" where a.pk_invbasdoc=b.pk_invbasdoc and b.pk_measdoc='"+pk_unit+"' and b.pk_invbasdoc='"+pk_invbasdoc1+"'")
//                        .append("and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 ");
//                        
//                        ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql2.toString(), new MapListProcessor());
//                        UFDouble changer =null;
//                        if(arr!=null && arr.size()>0){//子表中有辅计量单位的
//                            for(int j=0;j<arr.size();j++){
//                                HashMap hm2 = (HashMap) arr.get(j);
//                                changer = new UFDouble(hm2.get("changerate")==null?"0":hm2.get("changerate").toString());
//                            }
//                            changmount = scmount.div(changer); //转换率
//                            summount = changmount.multiply(zamount);//转换后的数量
//                        }else{ //子表中没有辅计量单位的                  
//                            summount = scmount.multiply(zamount);//转换后的数量
//                        }
                        summount = scmount.multiply(zamount);
                        
                        if(hm.containsKey(pk_invbasdoc)){
                            ScPosmBVO sbvo = (ScPosmBVO)hm.get(pk_invbasdoc);
                            summount=summount.add(sbvo.getScmount());
                            sbvo.setScmount(summount);
                            hm.put(pk_invbasdoc, sbvo);
                        }else{
                            ScPosmBVO sbvo = new ScPosmBVO();
                            sbvo.setPk_invbasdoc(pk_invbasdoc);
                            sbvo.setScmount(summount);
                            sbvo.setPk_unit(pk_measdoc);
                            hm.put(pk_invbasdoc, sbvo);
                        }
                    }
                    
                    Object[] set = hm.keySet().toArray();
                    for(int i=0;i<set.length;i++){
                        ScPosmBVO sbvo = (ScPosmBVO) hm.get(set[i]);
                        al.add(sbvo);
                    }
                    ScPosmBVO[] bvo = (ScPosmBVO[]) al.toArray(new ScPosmBVO[al.size()]);
                    retBillVo.setChildrenVO(bvo);
                }
                else{
                    JOptionPane.showMessageDialog(null,"所选物料没有在BOM中维护或已经删除，请核实!","提示",JOptionPane.INFORMATION_MESSAGE); 
                    return;
                }
    
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        this.getAlignmentX();
        this.closeOK();

    }   
}