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
 * ���ܣ������ε��ݲ��գ��������񵥵��������ϵ��� 
 * Edit: ����Դ 
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
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"h0451005","��������");
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
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+" and (isnull(bl_flag,'N')='N' or bl_flag='') " +
                " and (isnull(lock_flag,'N')='N' or lock_flag='') and isnull(dr,0)=0 ");
        String strwp = strWherePart.toString();
        return strwp;
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
            
            if(retBillVos==null || retBillVo ==null || retBillVos.length==0 || retBillVos.length==0){
                JOptionPane.showMessageDialog(null,"������ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            if(retBillVos.length>1||retBillVos.length==0){
           	 	JOptionPane.showMessageDialog(null,"��ͷֻ��ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;          	
           }
            
            ArrayList al = new ArrayList();
            String pk_posm = retBillVo.getParentVO().getAttributeValue("pk_posm").toString(); //ȡ�������������񵥵�PK
            
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
            
            //�����������񵥵�PKȥBOM����������Ӧ�����°汾������ add by zqy 2008-6-18 19:15:59
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
                        String pk_invbasdoc = hm1.get("pk_invbasdoc")==null?"":hm1.get("pk_invbasdoc").toString(); //BOM�ӱ�����PK
//                        String pk_unit1 = hm1.get("vunit")==null?"":hm1.get("vunit").toString();  //BOM�ӱ����ϵ�λ
                        zamount = new UFDouble(hm1.get("zamount")==null?"0":hm1.get("zamount").toString()); //BOM����������
//                        String altflag = hm1.get("altflag")==null?"":hm1.get("altflag").toString(); //�Ƿ�������ϵı��
                        scmount = new UFDouble(hm1.get("scmount")==null?"0":hm1.get("scmount").toString());//�������񵥱������������
//                        String pk_unit = hm1.get("pk_unit")==null?"":hm1.get("pk_unit").toString();   //���������ӱ�λ
                        
                        String pk_measdoc = hm1.get("vunit")==null?"":hm1.get("vunit").toString(); //BOM�ӱ���������λ
                        
//                        String pk_invbasdoc1 = hm1.get("x")==null?"":hm1.get("x").toString();  //���������ӱ�����
//                        HashMap hmRate = new TzzkDAO().getInvRate(pk_invbasdoc1); //���е�λת��
//                        changerate = new UFDouble(hmRate.get(pk_unit)==null?"1":hmRate.get(pk_unit).toString());//ת���ĵ�λ����
                        
//                        StringBuffer sql2 = new StringBuffer()
//                        .append(" select b.changerate from eh_invbasdoc a,eh_invbasdoc_b b ")
//                        .append(" where a.pk_invbasdoc=b.pk_invbasdoc and b.pk_measdoc='"+pk_unit+"' and b.pk_invbasdoc='"+pk_invbasdoc1+"'")
//                        .append("and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 ");
//                        
//                        ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql2.toString(), new MapListProcessor());
//                        UFDouble changer =null;
//                        if(arr!=null && arr.size()>0){//�ӱ����и�������λ��
//                            for(int j=0;j<arr.size();j++){
//                                HashMap hm2 = (HashMap) arr.get(j);
//                                changer = new UFDouble(hm2.get("changerate")==null?"0":hm2.get("changerate").toString());
//                            }
//                            changmount = scmount.div(changer); //ת����
//                            summount = changmount.multiply(zamount);//ת���������
//                        }else{ //�ӱ���û�и�������λ��                  
//                            summount = scmount.multiply(zamount);//ת���������
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
                    JOptionPane.showMessageDialog(null,"��ѡ����û����BOM��ά�����Ѿ�ɾ�������ʵ!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
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