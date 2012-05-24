package nc.ui.eh.iso.z0501505;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.iso.z0501005.IsoBVO;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z06005.SbbillBVO;
import nc.vo.eh.stock.z06005.SbbillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/** 
 * ���ܣ������ε��ݲ��գ�ԭ��˾�������������� 
 * Edit: ����Դ 
 * Date:2008-04-14
 */ 
public class ZA18TOZA23DLG extends AbstractBTOBDLG {
	
	static IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
      
    public ZA18TOZA23DLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, String nodeKey, Object userObj, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, nodeKey, userObj, parent);
        
        BillItem billItem = getbillListPanel().getBillListData().getHeadItem("vbillstatus");
        initComboBox(billItem, IBillStatus.strStateRemark,true);
        
        BillItem sbtype = getbillListPanel().getBillListData().getHeadItem("sbtype");
        initComboBox(sbtype, ICombobox.STR_SBBILLTYPE,true);
        
        
        getbillListPanel().getBodyUIPanel().setVisible(false); // ���ر���
    }

    @Override
	public String[] getBillVos(){
        return new String[] {
        		PubBillVO.class.getName(),
        		SbbillVO.class.getName(),
                SbbillVO.class.getName()
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H06005","ԭ��˾����");
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
        strWherePart.append("' and NVL(ycy_flag,'N')='N' and (NVL(close_flag,'N')='N' or close_flag='') and  NVL(dr,0)=0 and sbtype=1 and ischeck='Y' "); //���˵��Ѿ�������˾����
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
    	//��û�����ε��ݿ��Բ���ʱ,������ʾ
        if (getbillListPanel().getHeadBillModel().getRowCount() == 0){
            super.close();
            JOptionPane.showMessageDialog(null,"û�п��Բ��յĵ���!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
            return;
        }
     
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
        	        	
            retBillVo = getbillListPanel().getSelectedVO("nc.vo.eh.pub.PubBillVO", m_billHeadVo, m_billHeadVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs("nc.vo.eh.pub.PubBillVO", m_billHeadVo, m_billHeadVo);
            
            if(retBillVos==null || retBillVo ==null || retBillVos.length==0 ){
                JOptionPane.showMessageDialog(null,"������ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }

            SbbillVO sbvo = (SbbillVO) retBillVo.getParentVO();
            
            //�����α�ͷ������,����ȡ����
            String pk_invbasdoc = retBillVo.getParentVO().getAttributeValue("pk_invbasdoc")==null?"":
            					retBillVo.getParentVO().getAttributeValue("pk_invbasdoc").toString();
            String pk_cubasdoc = retBillVo.getParentVO().getAttributeValue("pk_cubasdoc")==null?"":
            					retBillVo.getParentVO().getAttributeValue("pk_cubasdoc").toString();
            String pk_receipt_b = retBillVo.getParentVO().getAttributeValue("vsourcebillrowid")==null?"":
								retBillVo.getParentVO().getAttributeValue("vsourcebillrowid").toString(); //�ջ�֪ͨ���ӱ�����
            
            sbvo.setDef_11(pk_cubasdoc);
            
            String shname = retBillVo.getParentVO().getAttributeValue("shname")==null?"":
                retBillVo.getParentVO().getAttributeValue("shname").toString();
            if(shname!=null){
                sbvo.setDef_12(shname);//��ɢ����Ϣ����ȥ
            }
   
            String carnumber = retBillVo.getParentVO().getAttributeValue("carnumber")==null?"":
                retBillVo.getParentVO().getAttributeValue("carnumber").toString(); //�������ϵĳ���
            String tranno = retBillVo.getParentVO().getAttributeValue("tranno")==null?"":
                retBillVo.getParentVO().getAttributeValue("tranno").toString(); //�������ϵĳ�Ƥ��
            
            sbvo.setDef_13(carnumber);
            sbvo.setDef_14(tranno);
            
            
            //��������ͬһ�ͻ���ͬһ���Ͻ��г���     add by wb at 2008-6-6 13:15:12
            if(retBillVos.length>0){
           	 	int length = retBillVos.length;
           	 	SbbillVO sbVO = new SbbillVO();
           	 	String pk_sbbill = null;
           	 	String pk_cubasdoc2 = null;
           	 	String pk_invbasdoc2 = null;
           	 	String pk_receipt_b2 = null;                          //�ջ�֪ͨ���ӱ�����
           	 	String[] pk_sbbills = new String[length];             // ����˾����������
            	for(int i=0;i<length;i++){
            		sbVO = (SbbillVO)retBillVos[i].getParentVO();
            		pk_cubasdoc2 = sbVO.getPk_cubasdoc()==null?"":sbVO.getPk_cubasdoc().toString();
            		pk_invbasdoc2 = sbVO.getPk_invbasdoc()==null?"":sbVO.getPk_invbasdoc().toString();
            		pk_receipt_b2 = sbVO.getVsourcebillrowid()==null?"":sbVO.getVsourcebillrowid();
            		if(pk_receipt_b.equals("")||pk_receipt_b2.equals("")){
            			if(!pk_invbasdoc2.equals(pk_invbasdoc)||!pk_cubasdoc2.equals(pk_cubasdoc)){
            				JOptionPane.showMessageDialog(null,"��������ͬһ�ͻ���ͬһ����!","����",JOptionPane.INFORMATION_MESSAGE); 
                            return;
            			}
            		}
            		if(!pk_receipt_b2.equals(pk_receipt_b)){
            			JOptionPane.showMessageDialog(null,"��������ͬһ�ջ�֪ͨ����ͬһ����!","����",JOptionPane.INFORMATION_MESSAGE); 
                        return;
            		}
            		pk_sbbill = sbVO.getPk_sbbill();
            		pk_sbbills[i] = pk_sbbill;
            	}
            	pk_sbbill = PubTools.combinArrayToString2(pk_sbbills);
            	//����˾����������ѯ˾������ (�������γ����� Դ���� �ֶ���)
            	String billnosql = "select billno from eh_sbbill where pk_sbbill in ("+pk_sbbill+")";
            	ArrayList list=new ArrayList();
            	try {
            	ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(billnosql,new MapListProcessor());
            	if(arr!=null && arr.size()>0){
					String[] billnos = new String[arr.size()]; 
            		for(int i=0;i<arr.size();i++){
						HashMap hm = (HashMap)arr.get(i);
            			String billno = hm.get("billno")==null?"":hm.get("billno").toString();
            			billnos[i] = billno;
					}
            		String billno = PubTools.combinArrayToString3(billnos);
            		sbvo.setDef_1(billno);
            	}
            	retBillVo.getParentVO().setAttributeValue("carnumber", null);
	            retBillVo.getParentVO().setAttributeValue("def_2", pk_sbbill);        // ͨ��˾������def_1��˾�����齨�������������
	            
	            //�������α�ͷ������PKȥ��ѯԭ��������׼���е�����
	            String sql = "select b.* from eh_iso a,eh_iso_b b where a.pk_iso=b.pk_iso and a.pk_invbasdoc='"+
	        					pk_invbasdoc+"' and NVL(a.dr,0)=0 and NVL(b.dr,0)=0 " +
                                "and vbillstatus="+IBillStatus.CHECKPASS+" and NVL(a.def_1,'N')='Y' " +
                                        "and (NVL(lock_flag,'N')='N' or lock_flag='') "; //def_1Ϊ���±�Ǻ����ӹر�״̬���
	            ArrayList al = new ArrayList();
	            
					al = (ArrayList)iUAPQueryBS.executeQuery(sql, new BeanListProcessor(IsoBVO.class));
					if(al!=null && al.size()>0){
						//��ȡ���������ݷŵ����α�ͷ������Ӧ���ֶ���
						for(int i=0;i<al.size();i++){
							SbbillBVO bvo = new SbbillBVO();
							bvo.setDef_2(((IsoBVO)al.get(i)).getPk_project()==null?"":((IsoBVO)al.get(i)).getPk_project().toString());
							bvo.setDef_3(((IsoBVO)al.get(i)).getLl_ceil()==null?"":((IsoBVO)al.get(i)).getLl_ceil().toString());
							bvo.setDef_4(new UFDouble(((IsoBVO)al.get(i)).getLl_limit()==null?"":((IsoBVO)al.get(i)).getLl_limit().toString()));
							bvo.setDef_5(((IsoBVO)al.get(i)).getRece_ceil()==null?"":((IsoBVO)al.get(i)).getRece_ceil().toString());
							bvo.setDef_6(((IsoBVO)al.get(i)).getRece_limit()==null?"":((IsoBVO)al.get(i)).getRece_limit().toString());
	                        bvo.setDef_7(((IsoBVO)al.get(i)).getAnaimethod()==null?"":((IsoBVO)al.get(i)).getAnaimethod().toString());
	                        bvo.setDr(new Integer(((IsoBVO)al.get(i)).getTreatype()==null?"3":((IsoBVO)al.get(i)).getTreatype().toString()));
							list.add(bvo);							
						}
					}else{
                        JOptionPane.showMessageDialog(null,"������û����ԭ�ϱ�׼����ά��������ϵ����Ѿ��رգ����ʵ!","��ʾ",JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
				} catch (BusinessException e) {
					e.printStackTrace();
				}
	            
				SuperVO[] child=new SuperVO[list.size()] ;
	            child = (SuperVO[])list.toArray(child);
	            retBillVo.setChildrenVO(child);
         }
        }
        this.getAlignmentX();
        this.closeOK();
    }   
 
    @Override
	public void loadBodyData(int row) {   	
    }
}